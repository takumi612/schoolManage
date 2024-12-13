package services.implement;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import models.Major;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.query.Query;
import services.interfaces.IServices;
import utils.HibernateUtils;
import utils.ResultList;

import java.util.List;

public class MajorService implements IServices<Major> {

    @Override
    public void Insert(Major major) {}

    @Override
    public void Update(Major major) {}

    @Override
    public void Delete(int t) {}

    @Override
    public ResultList<Major> selectAll(int start, int elementPerPages) {
        return null;
    }

    public List<Major> selectAll() {
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Major> criteria = builder.createQuery(Major.class);
            Root<Major> root = criteria.from(Major.class);
            criteria.select(root);
            criteria.orderBy(builder.asc(root.get("majorName")));
            Query<Major> query = session.createQuery(criteria);
            List<Major> majorList = query.getResultList();
            for(Major major : majorList) {
                Hibernate.initialize(major.getFacilityIn());
            }
            return majorList;
        }
    }

    @Override
    public Major selectedById(int t) {
        return null;
    }

    public Major selectedbyName(String name) {
        Major major = new Major();
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            try{
                CriteriaBuilder builder = session.getCriteriaBuilder();
                CriteriaQuery<Major> criteria = builder.createQuery(Major.class);
                Root<Major> root = criteria.from(Major.class);
                criteria.select(root).where(builder.equal(root.get("majorName"), name));
                major = session.createQuery(criteria).getSingleResult();
                Hibernate.initialize(major.getFacilityIn());
                return major;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
