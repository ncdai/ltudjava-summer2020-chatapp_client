package vn.name.chanhdai.chatapp.client.utils;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.Serializable;

/**
 * vn.name.chanhdai.chatapp.client.utils
 *
 * @created by ncdai3651408 - StudentID : 18120113
 * @date 7/25/20 - 3:00 PM
 * @description
 */
public class HibernateUtils {
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory Creation Failed!");
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static <T> boolean insertRow(T row) {
        Transaction transaction = null;

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(row);
            transaction.commit();
            session.refresh(row);

            session.close();
            return true;

        } catch (HibernateException ex) {
            ex.printStackTrace();

            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }

    public static <T> T getRow(Class<T> classT, Serializable primaryKey) {
        T row = null;

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            row = session.get(classT, primaryKey);
            session.close();
        } catch (HibernateException ex) {
            ex.printStackTrace();
        }

        return row;
    }

    public static <T> boolean updateRow(T row) {
        Transaction transaction = null;

        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(row);
            transaction.commit();
            session.refresh(row);

            session.close();
            return true;

        } catch (HibernateException ex) {
            ex.printStackTrace();

            if (transaction != null) {
                transaction.rollback();
            }
            return false;
        }
    }
}
