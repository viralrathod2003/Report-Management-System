package com.iris.springboot.utils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class Commons {

    public static void writeErrorToLogFile(Exception e, Logger logger, HttpServletRequest request) {
        logger.error("towerDigitalTwin | SourceIP: " + request.getHeader("X-Real-IP") + " | sessionId: " + request.getSession().getId() + " | Username: " + request.getRemoteUser() + " | " + e.getMessage(), e);
    }

    public static void safeClose(List<AutoCloseable> closeableList, Logger logger) {
        for (AutoCloseable closeable : closeableList) {
            safeClose(closeable, logger);
        }
    }

    public static void safeClose(AutoCloseable closeable, Logger logger) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (Exception e) {
            logger.warn("Error while closing", e);
        }
    }
}
