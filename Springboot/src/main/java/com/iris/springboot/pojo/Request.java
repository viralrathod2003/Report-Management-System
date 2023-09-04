package com.iris.springboot.pojo;

import lombok.Data;

@Data
public class Request {
    private Integer requestId;
    private String username;
    private Integer userId;
    private String reportName;
    private String requestDate;
    private String requestDescription;
    private Integer requestStatus;
    private Integer reportId;
}
