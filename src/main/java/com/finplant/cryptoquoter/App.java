package com.finplant.cryptoquoter;

import com.finplant.cryptoquoter.model.configuration.Yamlconfig;
import com.finplant.cryptoquoter.model.entity.QuotesEntity;
import com.finplant.cryptoquoter.service.Encoder;
import com.finplant.cryptoquoter.service.HibernateService;
import com.finplant.cryptoquoter.service.MainService;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.yaml.snakeyaml.Yaml;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import java.io.*;
import java.util.List;


public class App {

    private static String fileName = "settings-cryptoquoter.yml";
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main( String[] args ) throws IOException   {

        System.out.println("constructor");
        if (args.length == 1) {
            if (args[0].equals("-p"))
                pass();
            else if (args[0].equals("-h"))
                help();
            return;
        }

        final Session session = getSession();
        HibernateService.initDatabase();
        HibernateService.getAllEntities();

        try {
            System.out.println("Connection string: " + Yamlconfig.INSTANCE.db);
        }
        catch (Exception ex) {
            System.out.println("Yaml config not initialized");
        }
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
