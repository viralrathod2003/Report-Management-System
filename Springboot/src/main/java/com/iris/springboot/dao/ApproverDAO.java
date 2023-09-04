package com.iris.springboot.dao;

import com.iris.springboot.entity.Users;
import com.iris.springboot.pojo.Action;

import java.util.List;
import java.util.Map;

public interface ApproverDAO {
    Users getApproverByUsername(String username) throws Exception;

    List<Users> getAllApprovers() throws Exception;

    Map<Integer, List<Users>> getApproversByDepartmentName(String departmentName) throws Exception;

    List<Action> getApproverHistoryByApproverUsername(String username) throws Exception;

    void addNewApprover(Users newApproverDetails) throws Exception;

    void updateApproverDetails(Users updateApproverDetails) throws Exception;
}
