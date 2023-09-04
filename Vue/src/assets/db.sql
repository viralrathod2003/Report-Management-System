CREATE DATABASE DEMO;
USE DEMO;

CREATE TABLE DEPARTMENTS(
    DEPARTMENT_ID INT PRIMARY KEY AUTO_INCREMENT,
    DEPARTMENT_NAME VARCHAR(500) UNIQUE,
    DEPARTMENT_DESCRIPTION VARCHAR(1000)
);
CREATE TABLE USERS (
    USER_ID INT PRIMARY KEY AUTO_INCREMENT,
    USERNAME VARCHAR(255) UNIQUE,
    NAME VARCHAR(255),
    PASSWORD VARCHAR(500),
    USER_LEVEL SMALLINT,
    EMAIL VARCHAR(255),
    ENABLED TINYINT,
    DEPARTMENT_ID INT,
    FOREIGN KEY (DEPARTMENT_ID) REFERENCES DEPARTMENTS(DEPARTMENT_ID)
);
CREATE TABLE AUTHORITIES (
    USER_ID INT,
    AUTHORITY VARCHAR(225),
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);
CREATE TABLE REPORTS(
    REPORT_ID INT PRIMARY KEY AUTO_INCREMENT,
    REPORT_NAME VARCHAR(500) UNIQUE,
    REPORT_PATH VARCHAR(500),
    DEPARTMENT_ID INT,
    FOREIGN KEY (DEPARTMENT_ID) REFERENCES DEPARTMENTS(DEPARTMENT_ID)
);
CREATE TABLE REQUESTS(
    REQUEST_ID INT AUTO_INCREMENT PRIMARY KEY,
    USER_ID INT,
    REPORT_ID INT,
    REQUEST_DATE DATETIME,
    REQUEST_DESCRIPTION VARCHAR(1000),
    REQUEST_STATUS INT,
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID),
    FOREIGN KEY (REPORT_ID) REFERENCES REPORTS(REPORT_ID)
);
CREATE TABLE ACTIONS(
    ACTION_ID INT AUTO_INCREMENT PRIMARY KEY,
    USER_ID INT,
    ACTION_TAKEN BOOLEAN,
    ACTION_DESCRIPTION VARCHAR(1000),
    ACTION_DATE DATETIME,
    ACTION_LEVEL INT,
    REQUEST_ID INT,
    FOREIGN KEY (REQUEST_ID) REFERENCES REQUESTS(REQUEST_ID),
    FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID)
);

INSERT INTO DEPARTMENTS (
        DEPARTMENT_ID,
        DEPARTMENT_NAME,
        DEPARTMENT_DESCRIPTION
    )
VALUES (
        1,
        'Sales',
        'Responsible for generating revenue and sales growth.'
    ),
    (
        2,
        'HR',
        'Responsible for managing human resources and employee relations.'
    ),
    (
        3,
        'Finance',
        'Responsible for financial management and budgeting.'
    ),
    (
        4,
        'Marketing',
        'Responsible for promoting and advertising products or services.'
    ),
    (
        5,
        'Operations',
        'Responsible for overseeing day-to-day business operations.'
    ),
    (
        6,
        'IT',
        'Responsible for managing information technology and computer systems.'
    ),
    (
        7,
        'Research and Development',
        'Responsible for innovation and product development.'
    ),
    (
        8,
        'Customer Service',
        'Responsible for providing support and assistance to customers.'
    ),
    (
        9,
        'Logistics',
        'Responsible for managing the flow of goods and services.'
    ),
    (
        10,
        'Quality Assurance',
        'Responsible for ensuring product or service quality meets standards.'
    );
INSERT INTO USERS (
        USER_ID,
        USERNAME,
        NAME,
        PASSWORD,
        USER_LEVEL,
        EMAIL,
        ENABLED,
        DEPARTMENT_ID
    )
VALUES (
        1,
        'admin',
        'John Doe',
        '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a',
        200,
        'john.doe@example.com',
        1,
        1
    );
INSERT INTO AUTHORITIES (USER_ID, AUTHORITY)
VALUES (1, 'ROLE_ADMIN');

INSERT INTO REPORTS (REPORT_NAME, REPORT_PATH, DEPARTMENT_ID)
VALUES (
    'Sales Report', '/reports/sales_report.pdf', 1),
    ('HR Report', '/reports/hr_report.pdf', 2),
    (
        'Financial Report',
        '/reports/financial_report.pdf',
        3
    ),
    (
        'Marketing Report',
        '/reports/marketing_report.pdf',
        4
    ),
    (
        'Operations Report',
        '/reports/operations_report.pdf',
        5
    ),
    ('IT Report', '/reports/it_report.pdf', 6),
    ('R&D Report', '/reports/rd_report.pdf', 7),
    (
        'Customer Service Report',
        '/reports/customer_service_report.pdf',
        8
    ),
    (
        'Logistics Report',
        '/reports/logistics_report.pdf',
        9
    ),
    (
        'Quality Assurance Report',
        '/reports/qa_report.pdf',
        10
    ),
    (
        'Sales Analysis Report',
        '/reports/sales_analysis_report.pdf',
        1
    ),
    ('Budget Report', '/reports/budget_report.pdf', 3),
    (
        'Product Marketing Report',
        '/reports/product_marketing_report.pdf',
        4
    ),
    (
        'Supply Chain Report',
        '/reports/supply_chain_report.pdf',
        9
    ),
    (
        'Quality Control Report',
        '/reports/quality_control_report.pdf',
        10
    ),
    (
        'Inventory Report',
        '/reports/inventory_report.pdf',
        5
    ),
    (
        'Training Report',
        '/reports/training_report.pdf',
        2
    ),
    (
        'System Security Report',
        '/reports/system_security_report.pdf',
        6
    ),
    (
        'Research Findings Report',
        '/reports/research_findings_report.pdf',
        7
    ),
    (
        'Customer Feedback Report',
        '/reports/customer_feedback_report.pdf',
        8
    );
    
--  showing data  
select *
from departments;
select *
from users;
select *
from reports;
select *
from actions;
select *
from requests;
select *
from authorities;