package com.finplant.cryptoquoter.service;

import com.finplant.cryptoquoter.App;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class MainService {
    private static final Logger logger = LogManager.getLogger(MainService.class);

    public static void handleError(String errorMsg, Exception ex) {
        logger.error(errorMsg);
        logger.error(ex.getMessage());
        for (StackTraceElement str: ex.getStackTrace()) {
            logger.error(str.toString());
        }
    }

}
