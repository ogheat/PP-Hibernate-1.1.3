package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private SessionFactory sessionfactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        Transaction tx = null;
        try (Session session = sessionfactory.getCurrentSession()) {
            tx = session.beginTransaction();
            Query query = session.createNativeQuery("CREATE TABLE IF NOT EXISTS users(id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                    "    name VARCHAR(255),\n" +
                    "    lastName VARCHAR(255),\n" +
                    "    age TINYINT)");
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {

        }
    }

    @Override
    public void dropUsersTable() {
        Transaction tx = null;
        try (Session session = sessionfactory.getCurrentSession()) {
            tx = session.beginTransaction();
            Query query = session.createNativeQuery("DROP TABLE IF EXISTS users");
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {

        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction tx = null;
        try (Session session = sessionfactory.getCurrentSession()) {
            tx = session.beginTransaction();
            User user = new User();
            user.setName(name);
            user.setLastName(lastName);
            user.setAge(age);
            session.save(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction tx = null;
        try (Session session = sessionfactory.getCurrentSession()) {
            tx = session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction tx = null;
        try (Session session = sessionfactory.getCurrentSession()) {
            tx = session.beginTransaction();
            Query<User> query = session.createQuery("FROM User", User.class);
            List<User> users = query.list();
            tx.commit();
            return users;
        } catch (HibernateException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            return null;
        }
    }

    @Override
    public void cleanUsersTable() {
        Transaction tx = null;
        try (Session session = sessionfactory.getCurrentSession()) {
            tx = session.beginTransaction();
            Query query = session.createNativeQuery("TRUNCATE TABLE users;");
            query.executeUpdate();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
        }
    }
}
