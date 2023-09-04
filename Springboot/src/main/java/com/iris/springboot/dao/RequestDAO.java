package com.iris.springboot.dao;

import com.iris.springboot.entity.Users;
import com.iris.springboot.pojo.Action;
import com.iris.springboot.pojo.Request;
import com.iris.springboot.pojo.RequestDetails;

import java.util.List;
import java.util.Map;

public interface RequestDAO {
    List<Request> getRequestsByUsername() throws Exception;

    Request getRequestById(Integer requestId) throws Exception;

    void createNewRequest(Request newRequest) throws Exception;

    List<Action> actionsList(Integer requestId) throws Exception;

    Map<Integer, List<Users>> getApproversById(int requestId) throws Exception;

    RequestDetails getAllRequestDetailsById(Integer requestId) throws Exception;

    void actionPerform(Action actionDetails) throws Exception;

    List<Request> getPendingRequest() throws Exception;

    List<Request> getAllRequests() throws Exception;
}
