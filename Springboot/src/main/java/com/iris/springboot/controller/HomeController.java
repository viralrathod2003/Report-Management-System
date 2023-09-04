package com.iris.springboot.controller;

import com.google.gson.Gson;
import com.iris.springboot.dao.DepartmentDAO;
import com.iris.springboot.dao.ReportDAO;
import com.iris.springboot.dao.RequestDAO;
import com.iris.springboot.pojo.Department;
import com.iris.springboot.pojo.Report;
import com.iris.springboot.pojo.RequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class HomeController {
    @Autowired
    ReportDAO reportDAO;
    @Autowired
    DepartmentDAO departmentDAO;

    @Autowired
    RequestDAO requestDAO;

    @GetMapping("request/details/{requestId}")
    public ResponseEntity<String> getAllRequestDetailsById(@PathVariable("requestId") int requestId,
            HttpServletRequest request) {
        RequestDetails requestDetails = null;
        try {
            requestDetails = requestDAO.getAllRequestDetailsById(requestId);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(requestDetails), HttpStatus.OK);
    }

    @GetMapping("/reports")
    public ResponseEntity<String> getAllReports(HttpServletRequest request) throws Exception {
        List<Report> reports = new ArrayList<>();
        try {
            reports = reportDAO.getAllReports();
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(reports), HttpStatus.OK);
    }

    @GetMapping("/departments")
    public ResponseEntity<String> getAllDepartments(HttpServletRequest request) throws Exception {
        List<Department> departments = new ArrayList<>();
        try {
            departments = departmentDAO.getAllDepartment();
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(departments), HttpStatus.OK);
    }
}
