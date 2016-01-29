package com.example;

import com.example.DemoApplication;
import com.example.DBConfig;
import com.example.domain.IPersistable;
import com.example.rest.AbstractController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;


import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {DemoApplication.class, DBConfig.class})
@WebAppConfiguration
public abstract class AbstractIT<T extends IPersistable<ID>, ID extends Serializable> {

    private AbstractController<T,ID> controller;
    private JpaRepository<T,ID> repository;
    private String urlPrefix;
    private Class<T> cl;

    protected MockMvc mockMvc;

    private HttpMessageConverter jsonConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));


    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

//        this.jsonConverter = Arrays.asList(converters).stream().filter(
//                hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

        Assert.assertNotNull("the JSON message converter must not be null",
                this.jsonConverter);
    }

    public void setController(AbstractController<T,ID> controller) {
        this.controller=controller;
    }


    public void setRepository(JpaRepository<T,ID> repository) {
        this.repository=repository;
    }


    public AbstractIT(String urlPrefix, Class<T> cl) {
        this.urlPrefix=urlPrefix;
        this.cl=cl;
    }


    protected String jsonToObject(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.jsonConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    protected Object jsonToObject(String json) throws IOException {
        Object result = null;
        if (json!=null && json.substring(0,1).equals("[")) {
            result=new ArrayList<T>();
            String jsonTrimmed=json.substring(2,json.length()-2);
            String[] valArray=jsonTrimmed.split("\\},\\{");
            for (String val:valArray) {
                T element=(T)jsonToObject("{"+val+"}");
                ((List)result).add(element);
            }


        } else {
            MockHttpInputMessage mockHttpInputMessage = new MockHttpInputMessage(json.getBytes());

            try {
                result = this.jsonConverter.read(cl, mockHttpInputMessage);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result == null) {
                mockHttpInputMessage = new MockHttpInputMessage(json.getBytes());
                result = this.jsonConverter.read(ArrayList.class, mockHttpInputMessage);
            }
        }
        return result;
    }

    protected T getTestEntity() {
        T result= null;
//        try {
//            result = cl.newInstance();
//            ObjectPopulator.populate(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return result;
    }

    @Before
    public void init() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    public String templateGetRequest(String url) throws Exception {
        String result=mockMvc.perform(get(url)
                        .contentType(contentType))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString()
                ;
        return result;
    }

    public String templatePostRequest(String url, String json) throws Exception {
        String result=mockMvc.perform(post(url)
                .content(json)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString()
                ;
        return result;
    }

    public T callSave(T entity) throws Exception {
        String json= templatePostRequest("/" + urlPrefix + "/save/", jsonToObject(entity));
        return (T)jsonToObject(json);
    }

    public T callFindOne(ID entityId) throws Exception {
        String json= templateGetRequest("/" + urlPrefix + "/findOne/" + entityId.toString());
        return (T)jsonToObject(json);
    }

    public List<T> callFindAll() throws Exception {
        String json= templateGetRequest("/" + urlPrefix + "/findAll/");
        return (List<T>)jsonToObject(json);
    }

    public void callDelete(T entity) throws Exception {
        templatePostRequest("/" + urlPrefix + "/delete/", jsonToObject(entity));
    }

    @Test
    public void saveTestForNewElement() throws Exception {
        T testEntity=getTestEntity();

        int countBefore=repository.findAll().size();
        testEntity= callSave(testEntity);
        int countAfter=repository.findAll().size();

        Assert.assertEquals("count AFTER ADD should be count BEFORE +1 ", countAfter, countBefore+1);
        Assert.assertTrue("test element should be present in repository", repository.findAll().contains(testEntity));

    }

    @Test
    public void saveTestForExistingElement() throws Exception {
        T testEntity=getTestEntity();
        testEntity=repository.save(testEntity);


        T updatedEntity=getTestEntity();
        updatedEntity.setId(testEntity.getId());

        updatedEntity=callSave(updatedEntity);


        Assert.assertTrue("old element should not be present in repository", !repository.findAll().contains(testEntity));
        Assert.assertTrue("updated element should be present in repository", repository.findAll().contains(updatedEntity));

    }

    @Test
    public void findAllTest() throws Exception {
        T testEntity=getTestEntity();
        testEntity=repository.save(testEntity);
        List<T> all= callFindAll();
        Assert.assertTrue("test element should be present in result list", all.contains(testEntity));

    }


    @Test
    public void getByIdTest() throws Exception {
        T testEntity=getTestEntity();
        testEntity=repository.save(testEntity);
        ID id=testEntity.getId();

        T returnEntity=callFindOne(id);
        Assert.assertTrue("return element should be the same as original element", testEntity.equals(returnEntity));
    }

    @Test
    public void deleteTest() throws Exception {
        T testEntity=getTestEntity();
        testEntity=repository.save(testEntity);
        List<T> all= repository.findAll();
        Assert.assertTrue("repository should contain SAVED element", all.contains(testEntity));
        callDelete(testEntity);
        all= repository.findAll();
        Assert.assertTrue("repository should NOT contain DELETED element", !all.contains(testEntity));

    }

}
