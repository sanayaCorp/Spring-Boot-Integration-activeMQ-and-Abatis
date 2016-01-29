package com.example.domain;

public interface IPersistable<ID> {
    ID getId();
    void setId(ID id);
}
