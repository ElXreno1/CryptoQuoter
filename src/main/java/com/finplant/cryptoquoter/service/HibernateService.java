package com.finplant.cryptoquoter.service;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
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

public class HibernateService {

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

    public static void initDatabase()  {
        Session session = getSession();
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();

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

    public static void getAllEntities() {
        Session session = getSession();
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
        }
        finally {
            session.close();
        }
    }
}
