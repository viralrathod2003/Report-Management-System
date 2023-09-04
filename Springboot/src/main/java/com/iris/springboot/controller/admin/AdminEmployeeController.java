package com.iris.springboot.controller.admin;

import com.google.gson.Gson;
import com.iris.springboot.dao.EmployeeDAO;
import com.iris.springboot.entity.Users;
import com.iris.springboot.pojo.Request;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminEmployeeController {
    /*
     * function added
     * 1. getAllEmployees
     * 2. getEmployeeByUsername
     * 3. getEmployeeById
     * 4. getEmployeeHistoryById
     */
    @Autowired
    EmployeeDAO employeeDAO;

    @GetMapping("/employees")
    public ResponseEntity<String> getAllEmployees(HttpServletRequest request) throws Exception {
        List<Users> employees = new ArrayList<>();
        try {
            employees = employeeDAO.getAllEmployees();
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(employees), HttpStatus.OK);
    }

    @GetMapping("employee/{username}")
    public ResponseEntity<String> getEmployeeByUsername(@PathVariable("username") String username,
            HttpServletRequest request) {
        Users employee = null;
        try {
            employee = employeeDAO.getEmployeeByUsername(username);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(employee), HttpStatus.OK);
    }

    @GetMapping("/employee/history/{username}")
    public ResponseEntity<String> getEmployeeHistoryById(@PathVariable("username") String username,
            HttpServletRequest request) throws Exception {
        List<Request> requests = null;
        try {
            requests = employeeDAO.getEmployeeHistoryByUsername(username);
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(requests), HttpStatus.OK);
    }

    @PostMapping("/employee")
    public ResponseEntity<String> addEmployee(@RequestBody String employeeDetails, HttpServletRequest request) {
        Users newEmployeeDetails = new Gson().fromJson(employeeDetails, Users.class);
        try {
            employeeDAO.addEmployee(newEmployeeDetails);
            newEmployeeDetails = new Gson().fromJson(employeeDetails, Users.class);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(newEmployeeDetails), HttpStatus.OK);
    }
}
