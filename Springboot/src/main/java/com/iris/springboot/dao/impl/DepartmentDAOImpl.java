package com.iris.springboot.dao.impl;

import com.iris.springboot.dao.DepartmentDAO;
import com.iris.springboot.pojo.Department;
import com.iris.springboot.utils.Commons;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class DepartmentDAOImpl implements DepartmentDAO {
    @Autowired
    ComboPooledDataSource dataSource;
    private static final Logger logger = LogManager.getLogger(ApproversDAOImpl.class);

    @Override
    public Department getDepartmentByDepartmentName(String departmentName) throws Exception {
        var connection = dataSource.getConnection();
        final String query = "SELECT * FROM DEPARTMENTS WHERE DEPARTMENT_NAME = ?";
        Department department = null;
        ResultSet resultSet = null;
        PreparedStatement getReport = connection.prepareStatement(query);
        try {
            getReport.setString(1, departmentName);
            resultSet = getReport.executeQuery();
            if (resultSet.next()) {
                department = new Department();
                department.setDepartmentId(resultSet.getInt("DEPARTMENT_ID"));
                department.setDepartmentName(resultSet.getString("DEPARTMENT_NAME"));
                department.setDepartmentDescription(resultSet.getString("DEPARTMENT_DESCRIPTION"));
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getReport, connection, resultSet)), logger);
        }
        return department;
    }

    @Override
    public List<Department> getAllDepartment() throws Exception {
        var connection = dataSource.getConnection();
        final String query = "SELECT * FROM DEPARTMENTS";
        List<Department> departments = new ArrayList<>();
        ResultSet resultSet = null;
        PreparedStatement getReport = connection.prepareStatement(query);
        try {
            resultSet = getReport.executeQuery();
            if (resultSet.next()) {
                do {
                    Department department = new Department();
                    department.setDepartmentId(resultSet.getInt("DEPARTMENT_ID"));
                    department.setDepartmentName(resultSet.getString("DEPARTMENT_NAME"));
                    department.setDepartmentDescription(resultSet.getString("DEPARTMENT_DESCRIPTION"));
                    departments.add(department);
                } while (resultSet.next());
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getReport, connection, resultSet)), logger);
        }
        return departments;
    }

    @Override
    public void addDepartment(Department department) throws Exception {
        var connection = dataSource.getConnection();
        String query = "INSERT INTO DEPARTMENTS(DEPARTMENT_ID, DEPARTMENT_NAME,DEPARTMENT_DESCRIPTION) VALUES (DEFAULT,?,?)";
        PreparedStatement addDepartment = connection.prepareStatement(query);
        try {
            addDepartment.setString(1, department.getDepartmentName());
            addDepartment.setString(2, department.getDepartmentDescription());
            addDepartment.executeUpdate();
            connection.close();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(addDepartment, connection, null)), logger);
        }
    }
}
