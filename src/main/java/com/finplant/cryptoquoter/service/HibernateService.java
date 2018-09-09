package com.finplant.cryptoquoter.service;

import com.finplant.cryptoquoter.model.configuration.Yamlconfig;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.persistence.metamodel.EntityType;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class HibernateService {

    private static final SessionFactory ourSessionFactory;
    private static final Logger logger = LogManager.getLogger(HibernateService.class);

    static {
        try {
            Configuration cfg = new Configuration();
            cfg.configure();

            Yamlconfig yamlCfg = Yamlconfig.INSTANCE;
            Properties props = cfg.getProperties();
            props.setProperty("hibernate.connection.url", formUrl(yamlCfg.db.url, yamlCfg.db.database));
            props.setProperty("hibernate.connection.password", Encoder.INSTANCE.Decode(yamlCfg.db.password));
            props.setProperty("hibernate.connection.username", yamlCfg.db.user);

            ourSessionFactory = cfg.buildSessionFactory();
        } catch (Throwable ex) {
            logger.error("Can't initialize connection. Please, check config file");
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static String formUrl(String server, String db) {
        return "jdbc:mysql://"+ server+":3306/" + db + "?seTimezone=true&serverTimezone=UTC&useSSL=false";
    }

    public static SessionFactory getSessionFactory() {
        return ourSessionFactory;
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    /**
     * initializion of database by create.sql script
     */
    public static void initDatabase() {

        Session session = getSession();
        try {
            ClassLoader classloader = HibernateService.class.getClassLoader();

            InputStream is = classloader.getResourceAsStream("create.sql");
            String result = CharStreams.toString(new InputStreamReader(is, Charsets.UTF_8));

            session.beginTransaction();
            session.createSQLQuery(result).executeUpdate();
            session.getTransaction().commit();

        }  catch (IOException ex) {
            MainService.handleError("Error reading creating database file", ex);
            return;
        }
        finally {
            session.close();
        }
    }

    /**
     * print all entities from database using hibernate
     */
    public static void getAllEntities() {

        Session session = getSession();
        try {
            logger.info("querying all the managed entities...");
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                final Query query = session.createQuery("from " + entityName);

                logger.info("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    logger.info("  " + o);
                }
            }
        }
        finally {
            session.close();
        }
    }
}
