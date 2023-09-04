package com.iris.springboot.dao.impl;

import com.iris.springboot.dao.EmployeeDAO;
import com.iris.springboot.dao.ReportDAO;
import com.iris.springboot.dao.RequestDAO;
import com.iris.springboot.entity.Users;
import com.iris.springboot.pojo.Action;
import com.iris.springboot.pojo.Report;
import com.iris.springboot.pojo.Request;
import com.iris.springboot.pojo.RequestDetails;
import com.iris.springboot.utils.Commons;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.*;

@Repository
public class RequestDAOImpl implements RequestDAO {
    /*
     * 1. getRequest
     * 2. createNewRequest
     * 3. actionsList
     * 4. getApproversById
     * 5. getAllRequests
     */
    @Autowired
    private final ComboPooledDataSource dataSource = new ComboPooledDataSource();
    @Autowired
    ReportDAO reportDAO;
    @Autowired
    EmployeeDAO employeeDAO;
    private static final Logger logger = LogManager.getLogger(ApproversDAOImpl.class);

    @Override
    public List<Request> getRequestsByUsername() throws Exception {
        List<Request> requests = new ArrayList<>();
        var connection = dataSource.getConnection();
        final String query = """
                SELECT
                    R.REQUEST_ID,
                    U.USERNAME,
                    RP.REPORT_NAME,
                    R.REQUEST_DATE,
                    R.REQUEST_DESCRIPTION,
                    R.REQUEST_STATUS,
                    R.REPORT_ID
                FROM
                    REQUESTS R
                    JOIN USERS U ON R.USER_ID = U.USER_ID
                    JOIN REPORTS RP ON R.REPORT_ID = RP.REPORT_ID
                WHERE
                    U.USERNAME = ?;
                """;
        ResultSet resultSet = null;
        PreparedStatement getRequest = connection.prepareStatement(query);
        try {
            getRequest.setString(1, SecurityContextHolder.getContext().getAuthentication().getName());
            resultSet = getRequest.executeQuery();
            if (resultSet.next()) {
                do {
                    Request request = new Request();
                    request.setRequestId(resultSet.getInt("REQUEST_ID"));
                    request.setUsername(resultSet.getString("username"));
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
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getRequest, connection, resultSet)), logger);
        }
        return requests;
    }

    @Override
    public Request getRequestById(Integer requestId) throws Exception {
        var connection = dataSource.getConnection();
        final String query = """
                SELECT
                    R.REQUEST_ID,
                    U.USERNAME,
                    RP.REPORT_NAME,
                    R.REQUEST_DATE,
                    R.REQUEST_DESCRIPTION,
                    R.REQUEST_STATUS,
                    R.REPORT_ID
                FROM
                    REQUESTS R
                    JOIN USERS U ON R.USER_ID = U.USER_ID
                    JOIN REPORTS RP ON R.REPORT_ID = RP.REPORT_ID
                WHERE
                    R.REQUEST_ID = ?;
                """;
        Request request = new Request();
        ResultSet resultSet = null;
        PreparedStatement getRequest = connection.prepareStatement(query);
        try {
            getRequest.setInt(1, requestId);
            resultSet = getRequest.executeQuery();
            if (resultSet.next()) {
                request.setRequestId(resultSet.getInt("REQUEST_ID"));
                request.setUsername(resultSet.getString("USERNAME"));
                request.setReportName(resultSet.getString("REPORT_NAME"));
                request.setRequestDate(resultSet.getString("REQUEST_DATE"));
                request.setRequestDescription(resultSet.getString("REQUEST_DESCRIPTION"));
                request.setRequestStatus(resultSet.getInt("REQUEST_STATUS"));
                request.setReportId(resultSet.getInt("REPORT_ID"));
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getRequest, connection, resultSet)), logger);
        }
        return request;
    }

    @Override
    public void createNewRequest(Request newRequest) throws Exception {
        var connection = dataSource.getConnection();
        String insertQuery = """
                INSERT INTO REQUESTS (USER_ID, REPORT_ID, REQUEST_DATE, REQUEST_DESCRIPTION, REQUEST_STATUS)
                SELECT
                    U.USER_ID,
                    ?,
                    NOW(),
                    ?,
                    1
                FROM
                    USERS U
                WHERE
                    U.USERNAME = ?
                    AND NOT EXISTS (
                        SELECT 1
                        FROM REQUESTS R
                        WHERE R.USER_ID = U.USER_ID
                            AND R.REPORT_ID = ?
                            AND R.REQUEST_STATUS != 100
                    );
                """;
        PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
        try {
            insertStatement.setInt(1, newRequest.getReportId());
            insertStatement.setString(2, newRequest.getRequestDescription());
            insertStatement.setString(3, SecurityContextHolder.getContext().getAuthentication().getName());
            insertStatement.setInt(4, newRequest.getReportId());
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted == 0) {
                throw new Exception("User already has a request with a status other than 100 for the same report.");
            }
        } catch (SQLException e) {
            throw new Exception("Failed to create a new request.", e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(insertStatement, connection, null)), logger);
        }
    }

    @Override
    public List<Action> actionsList(Integer requestId) throws Exception {
        var connection = dataSource.getConnection();
        List<Action> actions = new ArrayList<>();
        ResultSet actionRs = null;
        String query = """
                SELECT
                    A.ACTION_ID,
                    U.USERNAME,
                    A.ACTION_TAKEN,
                    A.ACTION_DESCRIPTION,
                    A.ACTION_DATE,
                    A.ACTION_LEVEL,
                    A.REQUEST_ID
                FROM
                    ACTIONS A
                JOIN
                    USERS U ON A.USER_ID = U.USER_ID
                WHERE
                    A.REQUEST_ID = ?
                """;
        PreparedStatement getAction = connection.prepareStatement(query);
        try {
            getAction.setInt(1, requestId);
            actionRs = getAction.executeQuery();
            if (actionRs.next()) {
                do {
                    Action action = new Action();
                    action.setActionId(actionRs.getInt("ACTION_ID"));
                    action.setActionLevel(actionRs.getInt("ACTION_LEVEL"));
                    action.setRequestId(actionRs.getInt("REQUEST_ID"));
                    action.setUsername(actionRs.getString("USERNAME"));
                    action.setActionId(actionRs.getInt("ACTION_ID"));
                    action.setActionTaken(actionRs.getBoolean("ACTION_TAKEN"));
                    action.setActionDescription(actionRs.getString("ACTION_DESCRIPTION"));
                    action.setActionDate(actionRs.getString("ACTION_DATE"));
                    actions.add(action);
                } while (actionRs.next());
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getAction, connection, actionRs)), logger);
        }
        return actions;
    }

    @Override
    public Map<Integer, List<Users>> getApproversById(int requestId) throws Exception {
        Map<Integer, List<Users>> approvers = new HashMap<>();
        var connection = dataSource.getConnection();
        ResultSet resultSet = null;
        String query = """
                SELECT
                    *
                FROM
                    USERS U
                JOIN
                    REQUESTS R ON U.DEPARTMENT_ID = (
                        SELECT DEPARTMENT_ID
                        FROM REPORTS
                        WHERE REPORT_ID = R.REPORT_ID
                    )
                JOIN
                    AUTHORITIES AT ON U.USER_ID = AT.USER_ID
                WHERE
                    R.REQUEST_ID = ? AND
                    U.USER_LEVEL > R.REQUEST_STATUS AND
                    AT.AUTHORITY = "ROLE_APPROVER" ;
                    """;
        Users approver = null;
        PreparedStatement getAction = connection.prepareStatement(query);
        try {
            getAction.setInt(1, requestId);
            resultSet = getAction.executeQuery();
            if (resultSet.next()) {
                do {
                    approver = new Users();
                    approver.setUsername(resultSet.getString("USERNAME"));
                    approver.setName(resultSet.getString("NAME"));
                    approver.setUserLevel(resultSet.getInt("USER_LEVEL"));
                    approver.setDepartmentId(resultSet.getInt("DEPARTMENT_ID"));
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
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getAction, connection, resultSet)), logger);
        }
        return approvers;
    }

    @Override
    public RequestDetails getAllRequestDetailsById(Integer requestId) throws Exception {
        RequestDetails requestDetails = new RequestDetails();
        Request request = getRequestById(requestId);
        try {
            requestDetails.setRequest(request);
            Report report = reportDAO.getReport(request.getReportName());
            requestDetails.setReport(report);
            List<Action> actions = actionsList(requestId);
            requestDetails.setActions(actions);
            Map<Integer, List<Users>> approvers = getApproversById(requestId);
            requestDetails.setApproversByLevel(approvers);
        }catch (Exception e){
            throw new Exception("error during finding request details",e);
        }
        return requestDetails;
    }

    @Override
    public void actionPerform(Action actionDetails) throws Exception {
        var connection = dataSource.getConnection();
        String insertQuery = """
                INSERT INTO ACTIONS (USER_ID, REQUEST_ID, ACTION_DATE, ACTION_DESCRIPTION, ACTION_TAKEN, ACTION_LEVEL)
                SELECT
                    U.USER_ID,
                    ?,
                    NOW(),
                    ?,
                    ?,
                    U.USER_LEVEL
                FROM
                    USERS U
                WHERE
                    U.USERNAME = ?;
                """;
        String updateQuery = """
                UPDATE REQUESTS R
                JOIN USERS U1 ON U1.USERNAME = ?
                JOIN USERS U2 ON U2.DEPARTMENT_ID = U1.DEPARTMENT_ID
                SET R.REQUEST_STATUS = CASE
                    WHEN ? = 1 THEN (
                        CASE
                            WHEN U1.USER_LEVEL = (
                                SELECT MAX(U3.USER_LEVEL)
                                FROM USERS U3
                                WHERE U3.DEPARTMENT_ID = U1.DEPARTMENT_ID
                            ) THEN 200
                            ELSE U1.USER_LEVEL
                        END
                    )
                    ELSE 100
                END
                WHERE R.REQUEST_ID = ?;
                """;
        Savepoint save = null;
        try {
            connection.setAutoCommit(false);
            /* Check if user_level + 1 = request_status */
            String conditionQuery = """
                    SELECT U.USER_LEVEL = 1 + R.REQUEST_STATUS
                    FROM USERS U
                    JOIN REQUESTS R ON R.REQUEST_ID = ?
                    WHERE U.USERNAME = ?;
                    """;
            PreparedStatement checkCondition = connection.prepareStatement(conditionQuery);
            checkCondition.setInt(1, actionDetails.getRequestId());
            checkCondition.setString(2, SecurityContextHolder.getContext().getAuthentication().getName());
            ResultSet conditionResult = checkCondition.executeQuery();
            Collection<? extends GrantedAuthority> roles = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
            boolean hasAdminRole = false;
            for (GrantedAuthority authority : roles) {
                String authorityName = authority.getAuthority();
                if (authorityName.equals("ROLE_ADMIN")) {
                    hasAdminRole = true;
                    break;
                }
            }
            if (conditionResult.next() || hasAdminRole) {
                boolean conditionSatisfied = conditionResult.getBoolean(1);
                if (conditionSatisfied || hasAdminRole) {
                    save = connection.setSavepoint();
                    PreparedStatement addAction1 = connection.prepareStatement(insertQuery);
                    addAction1.setInt(1, actionDetails.getRequestId());
                    addAction1.setString(2, actionDetails.getActionDescription());
                    addAction1.setBoolean(3, actionDetails.getActionTaken());
                    addAction1.setString(4, SecurityContextHolder.getContext().getAuthentication().getName());
                    int executed = addAction1.executeUpdate();
                    if (executed != 1) {
                        throw new Exception("Issue while approving");
                    }

                    /* execute the UPDATE query */
                    PreparedStatement addAction2 = connection.prepareStatement(updateQuery);
                    addAction2.setString(1, SecurityContextHolder.getContext().getAuthentication().getName());
                    addAction2.setBoolean(2, actionDetails.getActionTaken());
                    addAction2.setInt(3, actionDetails.getRequestId());
                    executed = addAction2.executeUpdate();
                    if (executed != 1) {
                        connection.rollback(save);
                        throw new Exception("Issue in updating request");
                    }
                    connection.commit();
                } else {
                    throw new Exception("Condition not satisfied");
                }
            } else {
                throw new Exception("Condition not found");
            }
            connection.setAutoCommit(true);
        } catch (Exception e) {
            if (save != null) {
                connection.rollback(save);
            }
            throw new Exception(e);
        } finally {
            if (save != null) {
                connection.releaseSavepoint(save);
            }
            connection.close();
        }
    }

    @Override
    public List<Request> getPendingRequest() throws Exception {
        var connection = dataSource.getConnection();
        final String query = """
                SELECT R.*
                FROM REQUESTS AS R
                JOIN
                    USERS AS A1 ON R.REQUEST_STATUS+1 = A1.USER_LEVEL
                JOIN
                    REPORTS AS RP ON RP.REPORT_ID = R.REPORT_ID
                JOIN
                    USERS AS A2 ON RP.DEPARTMENT_ID = A2.DEPARTMENT_ID
                JOIN
                    AUTHORITIES AS AT ON AT.USER_ID = A1.USER_ID
                WHERE
                    A1.USERNAME = ? AND A2.USERNAME = ? AND AT.AUTHORITY = "ROLE_APPROVER"
                """;
        ResultSet resultSet=null;
        PreparedStatement result = connection.prepareStatement(query);
        List<Request> requests = new ArrayList<>();
        try {
            result.setString(1, SecurityContextHolder.getContext().getAuthentication().getName());
            result.setString(2, SecurityContextHolder.getContext().getAuthentication().getName());
            resultSet = result.executeQuery();
            if (resultSet.next()) {
                do {
                    Request request = new Request();
                    request.setRequestId(resultSet.getInt("REQUEST_ID"));
                    request.setUserId(resultSet.getInt("USER_ID"));
                    request.setReportId(resultSet.getInt("REPORT_ID"));
                    request.setRequestDate(resultSet.getString("REQUEST_DATE"));
                    request.setRequestDescription(resultSet.getString("REQUEST_DESCRIPTION"));
                    request.setRequestStatus(resultSet.getInt("REQUEST_STATUS"));
                    requests.add(request);
                } while (resultSet.next());
            }
            connection.close();
        } catch (Exception e) {
            throw new Exception(e);
        }finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(result, connection, resultSet)), logger);
        }
        return requests;
    }

    @Override
    public List<Request> getAllRequests() throws Exception {
        var connection = dataSource.getConnection();
        final String query = """
                SELECT
                    R.REQUEST_ID,
                    U.USERNAME,
                    RP.REPORT_NAME,
                    R.REQUEST_DATE,
                    R.REQUEST_DESCRIPTION,
                    R.REQUEST_STATUS
                FROM
                    REQUESTS R
                    JOIN USERS U ON R.USER_ID = U.USER_ID
                    JOIN REPORTS RP ON R.REPORT_ID = RP.REPORT_ID;
                """;
        ResultSet resultSet=null;
        List<Request> requests = new ArrayList<>();
        PreparedStatement result = connection.prepareStatement(query);
        try {
            resultSet = result.executeQuery();
            if (resultSet.next()) {
                do {
                    Request request = new Request();
                    request.setRequestId(resultSet.getInt("REQUEST_ID"));
                    request.setUsername(resultSet.getString("username"));
                    request.setReportName(resultSet.getString("REPORT_NAME"));
                    request.setRequestDate(resultSet.getString("REQUEST_DATE"));
                    request.setRequestDescription(resultSet.getString("REQUEST_DESCRIPTION"));
                    request.setRequestStatus(resultSet.getInt("REQUEST_STATUS"));
                    requests.add(request);
                } while (resultSet.next());
            }
            connection.close();
        } catch (Exception e) {
            throw new Exception(e);
        }finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(result, connection, resultSet)), logger);
        }
        return requests;
    }
}
