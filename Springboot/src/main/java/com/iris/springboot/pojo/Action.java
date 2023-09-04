package com.iris.springboot.pojo;

import lombok.Data;

@Data
public class Action {
    private Integer actionId;
    private String userId;
    private String username;
    private Integer requestId;
    private String actionDescription;
    private Boolean actionTaken;
    private String actionDate;
    private Integer actionLevel;
}
