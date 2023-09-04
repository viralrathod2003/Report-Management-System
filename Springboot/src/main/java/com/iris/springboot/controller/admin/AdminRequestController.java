package com.iris.springboot.controller.admin;

import com.google.gson.Gson;
import com.iris.springboot.dao.RequestDAO;
import com.iris.springboot.pojo.Request;
import com.iris.springboot.pojo.RequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRequestController {
    /*
     * 1. getRequestById
     * 2. getAllRequests
     * 3. getAllRequestDetailsById
     */
    @Autowired
    RequestDAO requestDAO;

    @GetMapping("request/{requestId}")
    public ResponseEntity<String> getRequestById(@PathVariable("requestId") int requestId, HttpServletRequest request) {
        Request requestDetail = null;
        try {
            requestDetail = requestDAO.getRequestById(requestId);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(requestDetail), HttpStatus.OK);
    }

    @GetMapping("requests")
    public ResponseEntity<String> getAllRequests() {
        List<Request> requests = null;
        try {
            requests = requestDAO.getAllRequests();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(requests), HttpStatus.OK);
    }

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

}
