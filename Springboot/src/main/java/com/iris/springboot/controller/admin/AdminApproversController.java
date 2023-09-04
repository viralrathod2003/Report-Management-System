package com.iris.springboot.controller.admin;

import com.google.gson.Gson;
import com.iris.springboot.dao.ApproverDAO;
import com.iris.springboot.entity.Users;
import com.iris.springboot.pojo.Action;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminApproversController {
    /*
     * function added
     * 1. getAllApprovers
     * 2. getApproverByUsername
     * 3. getApproverHistoryByApproverUsername
     * 4. getApproversByDepartmentId
     * 5. getApproverHistoryByApproverId
     * 6. addNewApprover
     * 7. updateApprover
     * 8. deleteApprover
     */

    @Autowired
    ApproverDAO approverDAO;

    @GetMapping("/approvers")
    public ResponseEntity<String> getAllApprovers(HttpServletRequest request) throws Exception {
        List<Users> approvers = new ArrayList<>();
        try {
            approvers = approverDAO.getAllApprovers();
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(approvers), HttpStatus.OK);
    }

    @GetMapping("approver/{username}")
    public ResponseEntity<String> getApproverByUsername(@PathVariable("username") String username,
            HttpServletRequest request) {
        Users approver = null;
        try {
            approver = approverDAO.getApproverByUsername(username);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(approver), HttpStatus.OK);
    }

    @GetMapping("/approver/history/{username}")
    public ResponseEntity<String> getApproverHistoryByApproverUsername(@PathVariable("username") String username,
            HttpServletRequest request) throws Exception {
        List<Action> actions = null;
        try {
            actions = approverDAO.getApproverHistoryByApproverUsername(username);
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(actions), HttpStatus.OK);
    }

    @GetMapping("/departments/{departmentName}")
    public ResponseEntity<String> getApproversByDepartmentId(@PathVariable("departmentName") String departmentName,
            HttpServletRequest request) throws Exception {
        Map<Integer, List<Users>> approvers = new HashMap<>();
        try {
            approvers = approverDAO.getApproversByDepartmentName(departmentName);
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(approvers), HttpStatus.OK);
    }

    @PostMapping("/approver")
    public ResponseEntity<String> addNewApprover(@RequestBody String approverDetails, HttpServletRequest request) {
        Users newApproverDetails = new Gson().fromJson(approverDetails, Users.class);
        try {
            approverDAO.addNewApprover(newApproverDetails);
            newApproverDetails = new Gson().fromJson(approverDetails, Users.class);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(newApproverDetails), HttpStatus.OK);
    }

    @PutMapping("/approver")
    public ResponseEntity<String> updateApprover(@RequestBody String approverDetails, HttpServletRequest request) {
        Users updateApproverDetails = new Gson().fromJson(approverDetails, Users.class);
        try {
            approverDAO.updateApproverDetails(updateApproverDetails);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Approvers Details added successfully Updated Successfully", HttpStatus.OK);

    }
}
