package com.iris.springboot.pojo;

import lombok.Data;

@Data
public class Report {
    private Integer reportId;
    private String reportName;
    private String reportPath;
    private String departmentName;
    private Integer departmentId;
}