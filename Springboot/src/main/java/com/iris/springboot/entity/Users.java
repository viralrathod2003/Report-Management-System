package com.iris.springboot.entity;

import lombok.Data;
@Data
public class Users {
    private Integer userId;
    private String username;
    private String name;
    private String password;
    private Integer userLevel;
    private String email;
    private Integer departmentId;
    private String departmentName;
    private Integer enable;
}
