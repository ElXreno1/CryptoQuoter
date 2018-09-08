package com.finplant.cryptoquoter.service;

import org.jasypt.util.text.StrongTextEncryptor;

/**
 * Class for encrypt password from config file
 * implemets singleton
 */
public class Encoder {

    public static final Encoder INSTANCE = new Encoder();

    private byte[] pass = "Password for encoding".getBytes();
    private StrongTextEncryptor textEncryptor = new StrongTextEncryptor();

    private Encoder() {
        textEncryptor.setPassword(new String(pass));
    }

    public String Encode(String pass) {
        return textEncryptor.encrypt(pass);
    }

    public String Decode(String endodedPass) {
        return textEncryptor.decrypt(endodedPass);
    }

}
