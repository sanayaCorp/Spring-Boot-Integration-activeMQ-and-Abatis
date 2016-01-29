/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example.processor;

import com.example.domain.Customer;
import org.springframework.batch.item.ItemProcessor;

/**
 *
 * @author afes
 */
public class CustomerJobProcessor implements ItemProcessor<Customer, Customer>{
    
    @Override
    public Customer process(final Customer customer) throws Exception {
        customer.setName(customer.getName());
        return process(customer);
    }
}
