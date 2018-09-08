package com.finplant.cryptoquoter;

import com.finplant.cryptoquoter.model.configuration.Yamlconfig;
import com.finplant.cryptoquoter.service.Encoder;
import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import javax.persistence.metamodel.EntityType;
import java.io.*;
import java.util.TimeZone;

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

    public static void main( String[] args )   {

        if (args.length == 1) {
            if (args[0].equals("-p"))
                pass();
            else if (args[0].equals("-h"))
                help();
            return;
        }

        final Session session = getSession();
        try {
            System.out.println("querying all the managed entities...");
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                final Query query = session.createQuery("from " + entityName);
                System.out.println("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    System.out.println("  " + o);
                }
            }
        } finally {
            session.close();
        }

        Yaml yaml = new Yaml();
        Yamlconfig yamlConfig;

        try
        {
            InputStream file = new FileInputStream(fileName);
            yamlConfig = yaml.loadAs(file,Yamlconfig.class);
        }
        catch (Exception ex)
        {
            System.out.println("Error reading settings file " + fileName);
            System.out.println(ex.getMessage());
            ex.printStackTrace(System.out);
            return;
        }

        System.out.println( "Connection string: " + yamlConfig.db );
    }

    private static void pass() {
        System.out.print( "Please, input password to get encoded password for database: ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String rawPass = null;
        try
        {
            rawPass = br.readLine();
        }
        catch (IOException ex)
        {
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
