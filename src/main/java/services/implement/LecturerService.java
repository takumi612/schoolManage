package services.implement;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import models.Course;
import models.Lecturer;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.interfaces.IServices;
import utils.HibernateUtils;
import utils.ResultList;

public class LecturerService implements IServices<Lecturer> {
    @Override
    public void Insert(Lecturer lecturer) {

    }

    @Override
    public void Update(Lecturer lecturer) {

    }

    @Override
    public void Delete(int t) {

    }

    @Override
    public ResultList<Lecturer> selectAll(int start, int elementPerPages) {
        return null;
    }

    @Override
    public Lecturer selectedById(int lecID) {
        Lecturer lecturer ;
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                CriteriaBuilder cb = session.getCriteriaBuilder();

                // Query lấy ClassRoom dựa trên ClassID
                CriteriaQuery<Lecturer> cr2 = cb.createQuery(Lecturer.class);
                Root<Lecturer> root = cr2.from(Lecturer.class);
                cr2.select(root).where(cb.equal(root.get("id"),lecID));

                lecturer = session.createQuery(cr2).getSingleResult();
                Hibernate.initialize(lecturer.getMajorIn());

                transaction.commit();
                return lecturer;
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
