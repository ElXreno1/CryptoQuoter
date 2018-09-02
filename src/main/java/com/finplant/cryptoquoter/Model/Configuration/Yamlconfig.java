package com.finplant.cryptoquoter.Model.Configuration;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class Yamlconfig {
    public Map<String,Map<String,String>> environment;
    public Db db;
    public Long flush_period_s;
    public List<Instrument> instruments;
    //static Map<String, Instrument> instruments;

    /**
     * init configuration from config file, e.g. settings-cryptoquoter.yml
     * @throws FileNotFoundException
     * @throws Exception
     */
    public static void init() throws FileNotFoundException, Exception {
        //YamlReader reader = new YamlReader(new FileReader("settings-cryptoquoter.yml"));
        //YamlConfig asd = new com.esotericsoftware.yamlbeans.YamlConfig();
        //YamlEntry sd = new com.esotericsoftware.yamlbeans.document.YamlEntry();
        //reader.
        //Object db = reader.get("db");
       // System.out.println(db.toString());
        /*while (true) {
            Map obj = (Map) reader.read(Db.class);
            if (obj == null) break;
            if (obj.toString() == "db") {
                Db =

            }
            for (Object key: instruments.keySet()) {
                System.out.println(key.toString());

            }

        }
        */

    }
/*
    public static String getUrl() {
        return url;
    }
    public static String getUser() {
        return user;
    }
    public static String getPassword() {
        return password;
    }
    */
    //public static Long getFlushPeriod() {
     //   return flushPeriod;
 //   }

}
