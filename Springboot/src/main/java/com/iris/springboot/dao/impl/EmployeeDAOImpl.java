package com.iris.springboot.dao.impl;

import com.iris.springboot.dao.EmployeeDAO;
import com.iris.springboot.entity.Users;
import com.iris.springboot.pojo.Request;
import com.iris.springboot.utils.Commons;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class EmployeeDAOImpl implements EmployeeDAO {
    @Autowired
    ComboPooledDataSource dataSource;
    private static final Logger logger = LogManager.getLogger(ApproversDAOImpl.class);

    @Override
    public Users getEmployeeByUsername(String username) throws Exception {
        var connection = dataSource.getConnection();
        final String query = """
                SELECT
                    U.USER_ID,
                    U.USERNAME,
                    U.NAME,
                    U.PASSWORD,
                    U.USER_LEVEL,
                    U.EMAIL,
                    U.DEPARTMENT_ID,
                    D.DEPARTMENT_NAME
                FROM
                    USERS U
                JOIN
                    AUTHORITIES A ON U.USER_ID = A.USER_ID
                JOIN
                    DEPARTMENTS D ON U.DEPARTMENT_ID = D.DEPARTMENT_ID
                WHERE
                    A.AUTHORITY = 'ROLE_USER'
                    AND D.DEPARTMENT_NAME = ?
                """;
        Users employee = null;
        ResultSet requestRs = null;
        PreparedStatement getReport = connection.prepareStatement(query);
        try {
            getReport.setString(1, username);
            requestRs = getReport.executeQuery();
            if (requestRs.next()) {
                employee = new Users();
                employee.setUserId(requestRs.getInt("USER_ID"));
                employee.setUsername(requestRs.getString("USERNAME"));
                employee.setName(requestRs.getString("NAME"));
                employee.setUserLevel(requestRs.getInt("USER_LEVEL"));
                employee.setEmail(requestRs.getString("EMAIL"));
                employee.setDepartmentId(requestRs.getInt("DEPARTMENT_ID"));
                employee.setDepartmentName(requestRs.getString("DEPARTMENT_NAME"));
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getReport, connection, requestRs)), logger);
        }
        return employee;
    }

    @Override
    public List<Users> getAllEmployees() throws Exception {
        var connection = dataSource.getConnection();
        final String query = """
                SELECT
                    U.USER_ID,
                    U.USERNAME,
                    U.NAME,
                    U.PASSWORD,
                    U.USER_LEVEL,
                    U.EMAIL,
                    U.DEPARTMENT_ID,
                    D.DEPARTMENT_NAME
                FROM
                    USERS U
                JOIN
                    AUTHORITIES A ON U.USER_ID = A.USER_ID
                JOIN
                    DEPARTMENTS D ON U.DEPARTMENT_ID = D.DEPARTMENT_ID
                WHERE
                    A.AUTHORITY = 'ROLE_USER'
                """;
        List<Users> employees = new ArrayList<>();
        PreparedStatement getReport = connection.prepareStatement(query);
        ResultSet resultSet = null;
        try {
            resultSet = getReport.executeQuery();
            if (resultSet.next()) {
                do {
                    Users employee = new Users();
                    employee.setUserId(resultSet.getInt("USER_ID"));
                    employee.setUsername(resultSet.getString("USERNAME"));
                    employee.setName(resultSet.getString("NAME"));
                    employee.setUserLevel(resultSet.getInt("USER_LEVEL"));
                    employee.setEmail(resultSet.getString("EMAIL"));
                    employee.setDepartmentId(resultSet.getInt("DEPARTMENT_ID"));
                    employee.setDepartmentName(resultSet.getString("DEPARTMENT_NAME"));
                    employees.add(employee);
                } while (resultSet.next());
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getReport, connection, resultSet)), logger);
        }
        return employees;
    }

    @Override
    public List<Request> getEmployeeHistoryByUsername(String username) throws Exception {
        var connection = dataSource.getConnection();
        final String query = """
                SELECT
                    R.REQUEST_ID,
                    U.USERNAME,
                    RP.REPORT_NAME,
                    RP.REPORT_ID,
                    R.REQUEST_DATE,
                    R.REQUEST_DESCRIPTION,
                    R.REQUEST_STATUS
                FROM
                    REQUESTS R
                JOIN
                    USERS U ON R.USER_ID = U.USER_ID
                JOIN
                    REPORTS RP ON R.REPORT_ID = RP.REPORT_ID
                WHERE
                    U.USERNAME = ?
                """;
        List<Request> requests = new ArrayList<>();
        PreparedStatement getReport = connection.prepareStatement(query);
        ResultSet resultSet = null;
        try {
            getReport.setString(1, username);
            resultSet = getReport.executeQuery();
            if (resultSet.next()) {
                do {
                    Request request = new Request();
                    request.setRequestId(resultSet.getInt("REQUEST_ID"));
                    request.setUsername(resultSet.getString("USERNAME"));
                    request.setReportId(resultSet.getInt("REPORT_ID"));
                    request.setReportName(resultSet.getString("REPORT_NAME"));
                    request.setRequestDate(resultSet.getString("REQUEST_DATE"));
                    request.setRequestDescription(resultSet.getString("REQUEST_DESCRIPTION"));
                    request.setRequestStatus(resultSet.getInt("REQUEST_STATUS"));
                    requests.add(request);
                } while (resultSet.next());
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getReport, connection, resultSet)), logger);
        }
        return requests;
    }

    @Override
    public void addEmployee(Users employee) throws Exception {
        var connection = dataSource.getConnection();
        String insertUserQuery = """
                INSERT INTO USERS (USERNAME, NAME, PASSWORD, USER_LEVEL, EMAIL,ENABLED, DEPARTMENT_ID)
                VALUES (?, ?, ?, 1, ?, 1 ,?);
                """;
        String insertAuthorityQuery = """
                INSERT INTO AUTHORITIES (USER_ID, AUTHORITY)
                VALUES (?, 'ROLE_USER');
                """;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(employee.getPassword());
        PreparedStatement addUser = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
        try {
            addUser.setString(1, employee.getUsername());
            addUser.setString(2, employee.getName());
            addUser.setString(3, encodedPassword); // Store the encoded password
            addUser.setString(4, employee.getEmail());
            addUser.setInt(5, employee.getDepartmentId());
            addUser.executeUpdate();
            ResultSet generatedKeys = addUser.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);
                PreparedStatement addAuthority = connection.prepareStatement(insertAuthorityQuery);
                addAuthority.setInt(1, userId);
                addAuthority.executeUpdate();
            } else {
                throw new SQLException("Failed to retrieve generated user ID.");
            }
            connection.close();
        } catch (Exception e) {
            throw new Exception(e);
        }finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(addUser, connection, null)), logger);
        }
    }
}
