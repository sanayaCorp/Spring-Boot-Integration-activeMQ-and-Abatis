package com.example.service;

import com.example.domain.Customer;
import com.example.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService extends AbstractService<Customer, Long> {

    private CustomerRepository repository;


    @Autowired
    public CustomerService(CustomerRepository repository) {
        super(repository);
        this.repository=repository;
    }

    public List<Customer> findByName(String name) {
        return repository.findByName(name);
    }
}
