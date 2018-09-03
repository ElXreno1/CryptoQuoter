package com.finplant.cryptoquoter.Model.Configuration;
//import com.fasterxml.jackson.*;


public class Db {
    public String url;
    public String database;
    public String user;
    public String password;  //encrypted password

    @Override
    public String toString() {
        //Server=myServerAddress;Database=myDataBase;Uid=myUsername;Pwd=myPassword;
        return "Server=" + url + ";Database=" + database + ";Uid=" + user + "Pwd=" + password;
    }
}
