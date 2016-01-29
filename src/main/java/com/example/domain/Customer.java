/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sanaya
 */
@XmlRootElement(name = "customer")
@Entity(name = "CUSTOMER")
@Table(name = "customer")
public abstract class Customer implements IPersistable<Long>, Serializable{
    
    private static long serialVersionUID = -8886199720000049703L;

    /**
     * @return the serialVersionUID
     */
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    /**
     * @param aSerialVersionUID the serialVersionUID to set
     */
    public static void setSerialVersionUID(long aSerialVersionUID) {
        serialVersionUID = aSerialVersionUID;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "No")
    private Long no;
    
    @Column(name = "Name", nullable = false, columnDefinition = "varchar(30)")
    private String name;
    
    @Column(name = "Address", nullable = true, columnDefinition = "varchar(50)")
    private String address;
    
    @Column(name = "Gender", nullable = true, columnDefinition = "char(1)")
    private String gender;
    
    @Column(name = "DOB", nullable = true, columnDefinition = "varchar(10)")
    private String dob;
    
    @Column(name = "Currency", nullable = true, columnDefinition = "varchar(5)")
    private String currency;
    
    @Column(name = "Balance", nullable = true, columnDefinition = "decimal(18.8)")
    private Double balance;

    @Override
    public String toString() {
        return "Customer{" +
                "no=" + getNo() +
                ", name='" + getName() +
                ", address='" + getAddress() +
                ", gender='" + getGender() +
                ", dob='" + getDob() +
                ", currency='" + getCurrency() +
                ", balance='" + getBalance() +'\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (!no.equals(customer.no)) return false;
        if (!name.equals(customer.name)) return false;
        if (!address.equals(customer.address)) return false;
        if (!gender.equals(customer.gender)) return false;
        if (!dob.equals(customer.dob)) return false;
        if (!currency.equals(customer.currency)) return false;
        if (!balance.equals(customer.balance)) return false;
        
        return !(no != null ? !no.equals(customer.no) : customer.no != null);
    }

    /**
     * @return the no
     */
    public Long getNo() {
        return no;
    }

    /**
     * @param no the no to set
     */
    public void setNo(Long no) {
        this.no = no;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender the gender to set
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return the dob
     */
    public String getDob() {
        return dob;
    }

    /**
     * @param dob the dob to set
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * @return the currency
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * @return the balance
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * @param balance the balance to set
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }
  

}
