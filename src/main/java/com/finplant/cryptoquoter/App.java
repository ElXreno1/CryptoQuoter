package com.finplant.cryptoquoter;

import com.finplant.cryptoquoter.model.configuration.Yamlconfig;
import com.finplant.cryptoquoter.service.Encoder;
import com.finplant.cryptoquoter.service.HibernateService;
import com.finplant.cryptoquoter.service.MainService;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.metamodel.EntityType;
import java.io.*;


public class App {

    private static String fileName = "settings-cryptoquoter.yml";
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main( String[] args )  {
        init();

        if (args.length == 1) {
            if (args[0].equals("-p"))
                pass();
            else if (args[0].equals("-h"))
                help();
            return;
        }

        try {
            logger.info("Connection string: " + Yamlconfig.INSTANCE.db);
        }
        catch (Exception ex) {
            logger.error("Yaml config not initialized");
            return;
        }

        try {
            HibernateService.initDatabase();
            HibernateService.getAllEntities();
        }
        finally {
            HibernateService.getSessionFactory().close();
        }

        logger.info("quit");
    }

    private static void init() {
        System.setProperty( "com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");
        DOMConfigurator.configure("log4j.xml");
    }

    private static void pass() {
        System.out.print( "Please, input password to get encoded password for database: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String rawPass = null;

        try   {
            rawPass = br.readLine();
        }
        catch (IOException ex)   {
            System.out.println( "Input is incorrect. " + ex.getMessage());
        }

        String encPass = Encoder.INSTANCE.Encode(rawPass);
        System.out.println("Encoded password: " + encPass);
    }

    private  static void help() {
        System.out.println( "Usage of cryptoquoter:\n");
        System.out.println( "\tcryptoquoter.jar\n\t\tgetting quotes for exchanges\n");
        System.out.println( "\tcryptoquoter.jar -p\n\t\tmaking password for config\n");
        System.out.println( "\tcryptoquoter.jar -h\n\t\tshowing that help\n");
    }
}
