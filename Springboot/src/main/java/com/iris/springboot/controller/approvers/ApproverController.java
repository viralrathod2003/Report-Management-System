package com.iris.springboot.controller.approvers;

import com.google.gson.Gson;
import com.iris.springboot.dao.ApproverDAO;
import com.iris.springboot.dao.RequestDAO;
import com.iris.springboot.pojo.Action;
import com.iris.springboot.pojo.Request;
import com.iris.springboot.pojo.RequestDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("approver")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ApproverController {
    /*
     * 1. getRequestById
     * 2. getPendingRequest
     * 3. getAllRequestDetailsById
     * 4. actionPerform
     */

    @Autowired
    RequestDAO requestDAO;

    @Autowired
    ApproverDAO approverDAO;

    @GetMapping("requests")
    public ResponseEntity<String> getRequestById(HttpServletRequest request) {
        List<Request> requestDetail = null;
        try {
            requestDetail = requestDAO.getRequestsByUsername();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(requestDetail), HttpStatus.OK);
    }

    @GetMapping("requests/pending")
    public ResponseEntity<String> getPendingRequest(HttpServletRequest request) {
        List<Request> requests = null;
        try {
            requests = requestDAO.getPendingRequest();
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

    @PutMapping("request")
    public ResponseEntity<String> actionPerform(@RequestBody String actionDetails, HttpServletRequest request) {
        Action action = new Gson().fromJson(actionDetails, Action.class);
        try {
            requestDAO.actionPerform(action);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Request Updated Successfully", HttpStatus.OK);
    }

    @GetMapping("history")
    public ResponseEntity<String> getApproverHistoryByApproverUsername() throws Exception {
        List<Action> actions = null;
        try {
            actions = approverDAO.getApproverHistoryByApproverUsername(
                    SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new Gson().toJson(actions), HttpStatus.OK);
    }

}
