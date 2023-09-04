package com.iris.springboot.dao.impl;

import com.iris.springboot.dao.ApproverDAO;
import com.iris.springboot.entity.Users;
import com.iris.springboot.pojo.Action;
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
import java.util.*;

@Repository
public class ApproversDAOImpl implements ApproverDAO {
    @Autowired
    private ComboPooledDataSource dataSource;
    private static final Logger logger = LogManager.getLogger(ApproversDAOImpl.class);

    @Override
    public Users getApproverByUsername(String username) throws Exception {
        var connection = dataSource.getConnection();
        final String query = """
                SELECT
                    U.USER_ID,
                    U.USERNAME,
                    U.NAME,
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
                    A.AUTHORITY = 'ROLE_APPROVER'
                    AND U.USERNAME = ?
                """;
        Users approver = null;
        ResultSet approverRs = null;
        PreparedStatement getReport = connection.prepareStatement(query);
        try {
            getReport.setString(1, username);
            approverRs = getReport.executeQuery();
            if (approverRs.next()) {
                approver = new Users();
                approver.setUserId(approverRs.getInt("USER_ID"));
                approver.setUsername(approverRs.getString("USERNAME"));
                approver.setName(approverRs.getString("NAME"));
                approver.setUserLevel(approverRs.getInt("USER_LEVEL"));
                approver.setEmail(approverRs.getString("EMAIL"));
                approver.setDepartmentId(approverRs.getInt("DEPARTMENT_ID"));
                approver.setDepartmentName(approverRs.getString("DEPARTMENT_NAME"));
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getReport, connection, approverRs)), logger);
        }
        connection.close();
        return approver;
    }

    @Override
    public Map<Integer, List<Users>> getApproversByDepartmentName(String departmentName) throws Exception {
        Map<Integer, List<Users>> approvers = new HashMap<>();
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
                    A.AUTHORITY = 'ROLE_APPROVER'
                    AND D.DEPARTMENT_NAME = ?
                """;
        Users approver = null;
        ResultSet resultSet = null;
        PreparedStatement getApprover = connection.prepareStatement(query);
        try {
            getApprover.setString(1, departmentName);
            resultSet = getApprover.executeQuery();
            if (resultSet.next()) {
                do {
                    approver = new Users();
                    approver.setUserId(resultSet.getInt("USER_ID"));
                    approver.setUsername(resultSet.getString("USERNAME"));
                    approver.setName(resultSet.getString("NAME"));
                    approver.setUserLevel(resultSet.getInt("USER_LEVEL"));
                    approver.setEmail(resultSet.getString("EMAIL"));
                    approver.setDepartmentId(resultSet.getInt("DEPARTMENT_ID"));
                    approver.setDepartmentName(resultSet.getString("DEPARTMENT_NAME"));
                    if (!approvers.containsKey(approver.getUserLevel())) {
                        List<Users> approverList = new ArrayList<>();
                        approverList.add(approver);
                        approvers.put(approver.getUserLevel(), approverList);
                    } else {
                        approvers.get(approver.getUserLevel()).add(approver);
                    }
                } while (resultSet.next());
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getApprover, connection, resultSet)), logger);
        }
        return approvers;
    }

    @Override
    public List<Action> getApproverHistoryByApproverUsername(String username) throws Exception {
        List<Action> actions = new ArrayList<>();
        var connection = dataSource.getConnection();
        final String query = """
                SELECT
                    A.ACTION_ID,
                    U.USERNAME,
                    A.ACTION_TAKEN,
                    A.ACTION_DESCRIPTION,
                    A.ACTION_DATE,
                    A.ACTION_LEVEL,
                    A.REQUEST_ID
                FROM
                    USERS U
                JOIN
                    ACTIONS A ON U.USER_ID = A.USER_ID
                WHERE
                    U.USERNAME = ?
                """;
        ResultSet resultSet = null;
        PreparedStatement getActions = connection.prepareStatement(query);
        try {
            getActions.setString(1, username);
            resultSet = getActions.executeQuery();
            if (resultSet.next()) {
                do {
                    Action action = new Action();
                    action.setActionId(resultSet.getInt("ACTION_ID"));
                    action.setUsername(resultSet.getString("USERNAME"));
                    action.setActionTaken(resultSet.getBoolean("ACTION_TAKEN"));
                    action.setActionDescription(resultSet.getString("ACTION_DESCRIPTION"));
                    action.setActionDate(resultSet.getString("ACTION_DATE"));
                    action.setActionLevel(resultSet.getInt("ACTION_LEVEL"));
                    action.setRequestId(resultSet.getInt("REQUEST_ID"));
                    actions.add(action);
                } while (resultSet.next());
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getActions, connection, resultSet)), logger);
        }
        return actions;
    }

    @Override
    public void addNewApprover(Users newApproverDetails) throws Exception {
        var connection = dataSource.getConnection();
        String insertUserQuery = """
                INSERT INTO USERS (USERNAME, NAME, PASSWORD, USER_LEVEL, EMAIL,ENABLED, DEPARTMENT_ID)
                VALUES (?, ?, ?, ?, ?,1,?);
                """;
        String insertAuthorityQuery = """
                INSERT INTO AUTHORITIES (USER_ID, AUTHORITY)
                VALUES (?, 'ROLE_APPROVER');
                """;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newApproverDetails.getPassword());
        ResultSet generatedKeys = null;
        PreparedStatement addUser = connection.prepareStatement(insertUserQuery, Statement.RETURN_GENERATED_KEYS);
        try {
            addUser.setString(1, newApproverDetails.getUsername());
            addUser.setString(2, newApproverDetails.getName());
            addUser.setString(3, encodedPassword); // Store the encoded password
            addUser.setInt(4, newApproverDetails.getUserLevel());
            addUser.setString(5, newApproverDetails.getEmail());
            addUser.setInt(6, newApproverDetails.getDepartmentId());
            addUser.executeUpdate();
            generatedKeys = addUser.getGeneratedKeys();
            if (generatedKeys.next()) {
                int userId = generatedKeys.getInt(1);
                PreparedStatement addAuthority = connection.prepareStatement(insertAuthorityQuery);
                addAuthority.setInt(1, userId);
                addAuthority.executeUpdate();
            } else {
                throw new SQLException("Failed to retrieve generated user ID.");
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(addUser, connection, generatedKeys)), logger);
        }
    }

    @Override
    public void updateApproverDetails(Users updateApproverDetails) throws Exception {
        var connection = dataSource.getConnection();
        String query = "UPDATE USERS SET  USER_LEVEL = ? , DEPARTMENT_ID = ? WHERE USER_ID=?";
        PreparedStatement addApprover = connection.prepareStatement(query);
        try {
            addApprover.setInt(1, updateApproverDetails.getUserLevel());
            addApprover.setInt(2, updateApproverDetails.getDepartmentId());
            addApprover.setInt(3, updateApproverDetails.getUserId());
            addApprover.executeUpdate();
            connection.close();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(addApprover, connection, null)), logger);
        }
    }

    public List<Users> getAllApprovers() throws Exception {
        List<Users> approvers = new ArrayList<>();
        var connection = dataSource.getConnection();
        final String query = """
                SELECT
                    U.USER_ID,
                    U.USERNAME,
                    U.NAME,
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
                    A.AUTHORITY = 'ROLE_APPROVER'
                """;
        Users approver = null;
        ResultSet resultSet = null;
        PreparedStatement getApprover = connection.prepareStatement(query);
        try {
            resultSet = getApprover.executeQuery();
            if (resultSet.next()) {
                do {
                    approver = new Users();
                    approver.setUserId(resultSet.getInt("USER_ID"));
                    approver.setUsername(resultSet.getString("USERNAME"));
                    approver.setName(resultSet.getString("NAME"));
                    approver.setUserLevel(resultSet.getInt("USER_LEVEL"));
                    approver.setEmail(resultSet.getString("EMAIL"));
                    approver.setDepartmentId(resultSet.getInt("DEPARTMENT_ID"));
                    approver.setDepartmentName(resultSet.getString("DEPARTMENT_NAME"));
                    approvers.add(approver);
                } while (resultSet.next());
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getApprover, connection, resultSet)), logger);
        }
        return approvers;
    }
}
