package com.finplant.cryptoquoter;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.document.YamlEntry;
import com.finplant.cryptoquoter.Model.Db;
import com.finplant.cryptoquoter.Model.Instrument;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

/**
 *
 */
public class Configuration {
    static Db db;
    static Long flushPeriod;
    static Map<String, Instrument> instruments;

    /**
     * init configuration from config file, e.g. settings-cryptoquoter.yml
     * @throws FileNotFoundException
     * @throws Exception
     */
    public static void init() throws FileNotFoundException, Exception {
        YamlReader reader = new YamlReader(new FileReader("settings-cryptoquoter.yml"));
        //YamlConfig asd = new com.esotericsoftware.yamlbeans.YamlConfig();
        //YamlEntry sd = new com.esotericsoftware.yamlbeans.document.YamlEntry();
        //reader.
        Object db = reader.get("db");
        System.out.println(db.toString());
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
    public static Long getFlushPeriod() {
        return flushPeriod;
    }

}
