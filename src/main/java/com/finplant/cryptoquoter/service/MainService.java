package com.finplant.cryptoquoter.service;

public class MainService {
    public static void handleError(String errorMsg, Exception ex) {
        System.out.println(errorMsg);
        System.out.println(ex.getMessage());
        ex.printStackTrace(System.out);
    }

}
