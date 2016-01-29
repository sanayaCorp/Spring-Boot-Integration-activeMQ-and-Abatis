package com.example.rest;

import com.example.domain.Customer;
import com.example.service.AbstractService;
import com.example.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController extends AbstractController<Customer, Long> {

    private CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        super(service);
        this.service=service;
    }

    @RequestMapping(value="findByName/{name}", method = RequestMethod.GET)
    public List<Customer> findByName(@PathVariable String name) {
        return service.findByName(name);
    }
}
