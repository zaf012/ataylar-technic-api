package com.ay_za.ataylar_technic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_type")
public class UserType {

    @Id
    @Column(name = "id", length = 200)
    private Integer id;

    @Column(name = "user_type_name", nullable = false, length = 100)
    private String userTypeName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserTypeName() {
        return userTypeName;
    }

    public void setUserTypeName(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public UserType() {
    }

    public UserType(Integer id, String userTypeName) {
        this.id = id;
        this.userTypeName = userTypeName;
    }
}
