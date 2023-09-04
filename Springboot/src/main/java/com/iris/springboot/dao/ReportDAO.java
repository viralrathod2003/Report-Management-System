package com.iris.springboot.dao;

import com.iris.springboot.entity.Users;
import com.iris.springboot.pojo.Report;

import java.util.List;

public interface ReportDAO {
    List<Users> getUsersWithReportAccess(int reportId) throws Exception;

    List<Report> getAllReports() throws Exception;

    Report getReport(String reportName) throws Exception;

    void addReport(Report newReportDetails) throws Exception;

    void updateReport(Report updatedReportDetails) throws Exception;
}