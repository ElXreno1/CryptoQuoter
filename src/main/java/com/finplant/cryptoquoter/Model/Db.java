package com.finplant.cryptoquoter.Model;
//import com.fasterxml.jackson.*;


public class Db {
    //@JsonProperty
    public String url;
    //@JsonProperty
    public String database;
    //@JsonProperty
    public String user;
    //@JsonProperty
    public String password;

    @Override
    public String toString() {
        //Server=myServerAddress;Database=myDataBase;Uid=myUsername;Pwd=myPassword;
        return "Server=" + url + ";Database=" + database + ";Uid=" + user + "Pwd=" + password;
    }
}
