/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.repository;

import com.example.domain.Customer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sanaya
 */
@Repository(value = "CustomerRepository")
public interface CustomerRepository extends JpaRepository<Customer, Long>{
    
    @Query("FROM CUSTOMER where Name like %?1% ")
    List<Customer> findByName(String name);
}
