package com.iris.springboot.dao.impl;

import com.iris.springboot.dao.ReportDAO;
import com.iris.springboot.entity.Users;
import com.iris.springboot.pojo.Report;
import com.iris.springboot.utils.Commons;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class ReportDAOImpl implements ReportDAO {
    @Autowired
    private final ComboPooledDataSource dataSource = new ComboPooledDataSource();
    private static final Logger logger = LogManager.getLogger(ApproversDAOImpl.class);

    @Override
    public List<Users> getUsersWithReportAccess(int reportId) throws Exception {
        List<Users> users = new ArrayList<>();
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
                    REQUESTS R ON U.USER_ID = R.USER_ID
                JOIN
                    DEPARTMENTS D ON U.DEPARTMENT_ID = D.DEPARTMENT_ID
                WHERE
                    R.REPORT_ID = ?
                    AND R.REQUEST_STATUS = 200;
                """;
        ResultSet resultSet = null;
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        try {
            statement.setInt(1, reportId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Users user = new Users();
                user.setUserId(resultSet.getInt("USER_ID"));
                user.setUsername(resultSet.getString("USERNAME"));
                user.setName(resultSet.getString("NAME"));
                user.setUserLevel(resultSet.getInt("USER_LEVEL"));
                user.setEmail(resultSet.getString("EMAIL"));
                user.setDepartmentId(resultSet.getInt("DEPARTMENT_ID"));
                user.setDepartmentName(resultSet.getString("DEPARTMENT_NAME"));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new Exception("Failed to retrieve users with report access.", e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(statement, connection, resultSet)), logger);
        }
        return users;
    }

    @Override
    public List<Report> getAllReports() throws Exception {
        var connection = dataSource.getConnection();
        final String query = """
                SELECT
                    RP.REPORT_ID,
                    RP.REPORT_NAME,
                    RP.REPORT_PATH,
                    D.DEPARTMENT_ID,
                    D.DEPARTMENT_NAME
                FROM
                    REPORTS RP
                JOIN
                    DEPARTMENTS D ON RP.DEPARTMENT_ID = D.DEPARTMENT_ID
                """;
        List<Report> reports = new ArrayList<Report>();
        ResultSet reportRs = null;
        PreparedStatement getAllDetails = null;
        try {
            getAllDetails = connection.prepareStatement(query);
            reportRs = getAllDetails.executeQuery();
            if (reportRs.next()) {
                do {
                    Report report = new Report();
                    report.setReportId(reportRs.getInt("REPORT_ID"));
                    report.setReportName(reportRs.getString("REPORT_NAME"));
                    report.setReportPath(reportRs.getString("REPORT_PATH"));
                    report.setDepartmentId(reportRs.getInt("DEPARTMENT_ID"));
                    report.setDepartmentName(reportRs.getString("DEPARTMENT_NAME"));
                    reports.add(report);
                } while (reportRs.next());
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getAllDetails, connection, reportRs)), logger);
        }
        return reports;
    }

    @Override
    public Report getReport(String reportName) throws Exception {
        var connection = dataSource.getConnection();
        final String query = """
                SELECT
                    RP.REPORT_ID,
                    RP.REPORT_NAME,
                    RP.REPORT_PATH,
                    D.DEPARTMENT_ID,
                    D.DEPARTMENT_NAME
                FROM
                    REPORTS RP
                JOIN
                    DEPARTMENTS D ON RP.DEPARTMENT_ID = D.DEPARTMENT_ID
                WHERE
                    RP.REPORT_NAME = ?
                """;
        Report report = new Report();
        ResultSet reportRs = null;
        PreparedStatement getReport = connection.prepareStatement(query);
        try {
            getReport.setString(1, reportName);
            reportRs = getReport.executeQuery();
            if (reportRs.next()) {
                report.setReportId(reportRs.getInt("REPORT_ID"));
                report.setReportName(reportRs.getString("REPORT_NAME"));
                report.setReportPath(reportRs.getString("REPORT_PATH"));
                report.setDepartmentId(reportRs.getInt("DEPARTMENT_ID"));
                report.setDepartmentName(reportRs.getString("DEPARTMENT_NAME"));
            }
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(getReport, connection, reportRs)), logger);
        }
        connection.close();
        return report;
    }

    @Override
    public void addReport(Report newReportDetails) throws Exception {
        var connection = dataSource.getConnection();
        String query = "INSERT INTO REPORTS VALUES (DEFAULT,?,?,?)";
        PreparedStatement addReport = connection.prepareStatement(query);
        try {
            addReport.setString(1, newReportDetails.getReportName());
            addReport.setString(2, newReportDetails.getReportPath());
            addReport.setInt(3, newReportDetails.getDepartmentId());
            addReport.executeUpdate();
            connection.close();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(addReport, connection, null)), logger);
        }
    }

    @Override
    public void updateReport(Report updatedReportDetails) throws Exception {
        var connection = dataSource.getConnection();
        String query = "UPDATE REPORTS SET REPORT_PATH = ?,REPORT_NAME=?, DEPARTMENT_ID = ? WHERE REPORT_ID=?";
        PreparedStatement addReport = connection.prepareStatement(query);
        try {
            addReport.setString(1, updatedReportDetails.getReportPath());
            addReport.setString(2, updatedReportDetails.getDepartmentName());
            addReport.setInt(3, updatedReportDetails.getDepartmentId());
            addReport.setInt(4, updatedReportDetails.getReportId());
            System.out.println(updatedReportDetails.getReportPath());
            addReport.executeUpdate();
            connection.close();
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            Commons.safeClose(new ArrayList<AutoCloseable>(Arrays.asList(addReport, connection, null)), logger);
        }
    }
}
