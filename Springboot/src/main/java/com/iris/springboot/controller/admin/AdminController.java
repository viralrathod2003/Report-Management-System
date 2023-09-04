package com.iris.springboot.controller.admin;

import com.google.gson.Gson;
import com.iris.springboot.dao.DepartmentDAO;
import com.iris.springboot.dao.ReportDAO;
import com.iris.springboot.entity.Users;
import com.iris.springboot.pojo.Department;
import com.iris.springboot.pojo.Report;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    DepartmentDAO departmentDAO;
    @Autowired
    ReportDAO reportDAO;

    @GetMapping("report/hasAccess/{reportId}")
    public ResponseEntity<String> getUsersWithReportAccess(@PathVariable("reportId") Integer reportId,
            HttpServletRequest request) {
        List<Users> users;
        try {
            users = reportDAO.getUsersWithReportAccess(reportId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve users with report access.", e);
        }
        return new ResponseEntity<>(new Gson().toJson(users), HttpStatus.OK);
    }

    @PostMapping("/department")
    public ResponseEntity<String> addDepartment(@RequestBody String approverDetails, HttpServletRequest request) {
        Department department = new Gson().fromJson(approverDetails, Department.class);
        try {
            departmentDAO.addDepartment(department);
            department = new Gson().fromJson(approverDetails, Department.class);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(department), HttpStatus.OK);
    }

    @PostMapping("/report")
    public ResponseEntity<String> addNewReport(@RequestBody String reportDetails, HttpServletRequest request) {
        Report newReportDetails = new Gson().fromJson(reportDetails, Report.class);
        try {
            reportDAO.addReport(newReportDetails);
            newReportDetails = new Gson().fromJson(reportDetails, Report.class);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(newReportDetails), HttpStatus.OK);
    }

    @PutMapping("/report")
    public ResponseEntity<String> updateReport(@RequestBody String reportDetails, HttpServletRequest request) {
        Report updatedReportDetails = new Gson().fromJson(reportDetails, Report.class);
        try {
            reportDAO.updateReport(updatedReportDetails);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Report Updated Successfully", HttpStatus.OK);
    }

}
