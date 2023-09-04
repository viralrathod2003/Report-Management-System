package com.iris.springboot.dao;

import com.iris.springboot.entity.Users;
import com.iris.springboot.pojo.Request;
import java.util.List;

public interface EmployeeDAO {
    Users getEmployeeByUsername(String username) throws Exception;

    List<Users> getAllEmployees() throws Exception;

    List<Request> getEmployeeHistoryByUsername(String username) throws Exception;

    void addEmployee(Users employee) throws Exception;
}
