package com.iris.springboot.dao;

import com.iris.springboot.pojo.Department;
import java.util.List;

public interface DepartmentDAO {

    Department getDepartmentByDepartmentName(String departmentName) throws Exception;

    List<Department> getAllDepartment() throws Exception;

    void addDepartment(Department department) throws Exception;
}
