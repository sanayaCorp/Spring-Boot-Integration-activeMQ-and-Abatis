package com.example.rest;

import com.example.service.AbstractService;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractController<T, ID extends Serializable> {

    private AbstractService<T,ID> service;

    public AbstractController(AbstractService<T,ID> service) {
        this.service=service;
    }

    @RequestMapping("/hello")
    public String sayHello() {
        return "Hello from controller:"+ this.getClass();
    }

    @RequestMapping(value = "save",  method = RequestMethod.POST)
    public @ResponseBody
    T save(@RequestBody T entity) {
        T result=service.save(entity);
        return result;
    }

    @RequestMapping(value="findOne/{entityId}", method = RequestMethod.GET)
    public T findOne(@PathVariable ID entityId) {
        return service.findOne(entityId);
    }

    @RequestMapping(value="findAll", method = RequestMethod.GET)
    public List<T> findAll() {
        return service.findAll();
    }

    @RequestMapping(value = "delete",  method = RequestMethod.POST)
    public @ResponseBody
    void delete(@RequestBody T entity) {
        service.delete(entity);
    }



}

