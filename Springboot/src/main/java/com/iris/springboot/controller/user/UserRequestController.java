package com.iris.springboot.controller.user;

import com.google.gson.Gson;
import com.iris.springboot.dao.RequestDAO;
import com.iris.springboot.pojo.Request;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserRequestController {
    /*
     * 1. getReportById
     * 2. getAllRequestDetailsById
     * 3. createNewRequest
     */
    @Autowired
    RequestDAO requestDAO;

    @GetMapping("requests")
    public ResponseEntity<String> getRequestsByUsername(HttpServletRequest request) {
        List<Request> requestDetail = null;
        try {
            requestDetail = requestDAO.getRequestsByUsername();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(requestDetail), HttpStatus.OK);
    }

    @PostMapping("/request")
    public ResponseEntity<String> createNewRequest(@RequestBody String requestDetails, HttpServletRequest request) {
        Request newRequestDetails = new Gson().fromJson(requestDetails, Request.class);
        try {
            requestDAO.createNewRequest(newRequestDetails);
            newRequestDetails = new Gson().fromJson(requestDetails, Request.class);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(newRequestDetails), HttpStatus.OK);
    }

}
