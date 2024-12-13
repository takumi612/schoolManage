package services.implement;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.interfaces.IServices;
import utils.HibernateUtils;
import utils.ResultList;

import java.util.List;

public class UserService implements IServices<User> {
    @Override
    public void Insert(User user) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(user);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }

    @Override
    public void Update(User user) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(user);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }

    @Override
    public void Delete(int t) {

    }

    @Override
    public ResultList<User> selectAll(int start, int elementPerPages) {
        return null;
    }

    @Override
    public User selectedById(int t) {
        return null;
    }

    public User selectByUserName(String userName) {
        try (Session sessionFactory = HibernateUtils.getSessionFactory().openSession()) {
            CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();

            // Query lấy User dựa trên userName
            CriteriaQuery<User> cr = cb.createQuery(User.class);
            Root<User> root = cr.from(User.class);
            cr.select(root).where(cb.equal(root.get("userName"), userName));

            User user = sessionFactory.createQuery(cr).uniqueResult();
            return user;
        }
    }
}
