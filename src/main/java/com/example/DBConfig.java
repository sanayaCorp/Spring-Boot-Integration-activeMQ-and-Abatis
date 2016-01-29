/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.hibernate.ejb.HibernatePersistence;
import org.springframework.batch.support.DatabaseType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author sanaya
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("com.example")
@PropertySource("classpath:environment/application.properties")
@EnableJpaRepositories("com.example.repository")
public class DBConfig {
    
    private static final String PROP_DATABASE_DRIVER = "db.driver";
    private static final String PROP_DATABASE_PASSWORD = "db.password";
    private static final String PROP_DATABASE_URL = "db.url";
    private static final String PROP_DATABASE_USERNAME = "db.username";
    private static final String PROP_HIBERNATE_DIALECT = "db.hibernate.dialect";
    private static final String PROP_HIBERNATE_SHOW_SQL = "db.hibernate.show_sql";
    private static final String PROP_ENTITYMANAGER_PACKAGES_TO_SCAN = "db.entitymanager.packages.to.scan";
    private static final String PROP_HIBERNATE_HBM2DDL_AUTO = "db.hibernate.hbm2ddl.auto";
    private static final String PROP_HIBERNATE_FORMAT_SQL = "db.hibernate.format_sql";
    
    private static final String DEFAULT_SCHEMA_LOCATION = "classpath:org/springframework/"	+ "batch/core/schema-@@platform@@.sql";

    private static final String DEFAULT_DROP_SCHEMA_LOCATION = "classpath:org/springframework/" + "batch/core/schema-drop-@@platform@@.sql";

    @Resource
    private Environment env;
    
    @Inject
    private ResourceLoader resourceLoader;
    
    @PostConstruct
    protected void initialize() throws Exception {
            String platform = DatabaseType.fromMetaData(this.dataSource()).toString().toLowerCase();
            String schemaCreateLocation = this.env.getProperty("schema",	  DEFAULT_SCHEMA_LOCATION);             
            schemaCreateLocation = schemaCreateLocation.replace("@@platform@@", platform);
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.addScript(this.resourceLoader.getResource(schemaCreateLocation));
            populator.setContinueOnError(true);
            DatabasePopulatorUtils.execute(populator, dataSource());
    }
    
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(env.getRequiredProperty(PROP_DATABASE_DRIVER));
        dataSource.setUrl(env.getRequiredProperty(PROP_DATABASE_URL));
        dataSource.setUsername(env.getRequiredProperty(PROP_DATABASE_USERNAME));
        dataSource.setPassword(env.getRequiredProperty(PROP_DATABASE_PASSWORD));

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistence.class);
        entityManagerFactoryBean.setPackagesToScan(env.getRequiredProperty(PROP_ENTITYMANAGER_PACKAGES_TO_SCAN));

        entityManagerFactoryBean.setJpaProperties(getHibernateProperties());

        return entityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    private Properties getHibernateProperties() {
        Properties properties = new Properties();
        // Generated SQL
        properties.setProperty("hibernate.connection.driver_class", env.getRequiredProperty("db.driver"));
        // Logging
        properties.put("hibernate.dialect", env.getRequiredProperty(PROP_HIBERNATE_DIALECT));
        properties.put("hibernate.show_sql", env.getRequiredProperty(PROP_HIBERNATE_SHOW_SQL));
        properties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty(PROP_HIBERNATE_HBM2DDL_AUTO));
        properties.setProperty("hibernate.format_sql", env.getRequiredProperty(PROP_HIBERNATE_FORMAT_SQL));
        
        return properties;
    }
}
