package com.example.service;

import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractService<Customer,ID extends Serializable> {

    private JpaRepository<Customer,ID> repository;

    public AbstractService(JpaRepository<Customer,ID> repository) {
        this.repository=repository;

    }

    public Customer save(Customer entity) {
        Customer result=repository.save(entity);
        return result;
    }

    public void delete(Customer entity) {
        repository.delete(entity);
    }

    public Customer findOne(ID entityId) {
        Customer result=repository.findOne(entityId);
        return result;
    }

    public List<Customer> findAll() {
        List<Customer> result= repository.findAll();
        return result;
    }


}
