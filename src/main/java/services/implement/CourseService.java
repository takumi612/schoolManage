package services.implement;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import models.Classroom;
import models.Course;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.interfaces.IServices;
import utils.HibernateUtils;
import utils.ResultList;

public class CourseService implements IServices<Course> {

    @Override
    public void Insert(Course course) {
    }

    @Override
    public void Update(Course course) {
    }

    @Override
    public void Delete(int t) {
    }

    @Override
    public ResultList<Course> selectAll(int start, int elementPerPages) {
        return null;
    }

    @Override
    public Course selectedById(int courseID) {
        Course course ;
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                CriteriaBuilder cb = session.getCriteriaBuilder();

                CriteriaQuery<Course> cr2 = cb.createQuery(Course.class);
                Root<Course> root = cr2.from(Course.class);
                cr2.select(root).where(cb.equal(root.get("id"),courseID));

                course = session.createQuery(cr2).getSingleResult();
                Hibernate.initialize(course.getMajorIn());

                transaction.commit();
                return course;
            }catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
        return null;
    }
    public Course selectedByName(String courseName) {
        Course course ;
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                CriteriaBuilder cb = session.getCriteriaBuilder();

                CriteriaQuery<Course> cr2 = cb.createQuery(Course.class);
                Root<Course> root = cr2.from(Course.class);
                cr2.select(root).where(cb.equal(root.get("courseName"),courseName));

                course = session.createQuery(cr2).getSingleResult();
                Hibernate.initialize(course.getMajorIn());

                transaction.commit();
                return course;
            }catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
        return null;
    }
}
