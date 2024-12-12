package services.implement;

import dtos.StudentDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import models.Classroom;
import models.Enrollment;
import models.Student;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.interfaces.IServices;
import utils.HibernateUtils;
import utils.ResultList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentService implements IServices<Student> {
    @Override
    public void Insert(Student student) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try{
                session.persist(student);
                transaction.commit();
            }catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
    }
    @Override
    public void Update(Student student) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(student);
                transaction.commit();
            }catch (Exception e) {
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
    public ResultList<Student> selectAll(int start, int elementsPerPage) {
        ResultList<Student> resultList;
        Long noOfRecord;
        List<Student> studentList;
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try{
                CriteriaBuilder cb = session.getCriteriaBuilder();

                CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
                Root<Student> countRoot = countQuery.from(Student.class);
                countQuery.select(cb.count(countRoot));

                noOfRecord = (session.createQuery(countQuery).getSingleResult() + elementsPerPage - 1) / elementsPerPage;

                // Querry lấy tất cả student
                CriteriaQuery<Student> cq = cb.createQuery(Student.class);
                Root<Student> root = cq.from(Student.class);
                cq.select(root);
                studentList = session.createQuery(cq).setFirstResult(start).setMaxResults(elementsPerPage).list();

                for(Student student : studentList){
                    Hibernate.initialize(student.getUser());
                    Hibernate.initialize(student.getCreateBy());
                    Hibernate.initialize(student.getMajor());
                }
                transaction.commit();
                return new ResultList<Student>(noOfRecord,studentList);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Student selectedByCode(String studentCode) {
        Student student ;
        Long noOfRecord;
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                CriteriaBuilder cb = session.getCriteriaBuilder();

                // Query lấy Student dựa trên userName hay StudenCode
                CriteriaQuery<Student> cr2 = cb.createQuery(Student.class);
                Root<Student> root2 = cr2.from(Student.class);
                cr2.select(root2).where(cb.equal(root2.get("user").get("userName"), studentCode));

                student = session.createQuery(cr2).uniqueResult();

                Hibernate.initialize(student.getUser());
                Hibernate.initialize(student.getCreateBy());
                Hibernate.initialize(student.getMajor());

                transaction.commit();
                return student;
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
            return null;
        }
    }

    public ResultList<Classroom> getClassList(Student student, int start, int elementsPerPage) {
        List<Classroom> classList;
        Long noOfRecord;
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                CriteriaBuilder cb = session.getCriteriaBuilder();

                CriteriaQuery<Classroom> cr3 = cb.createQuery(Classroom.class);
                Root<Enrollment> enrollmentRoot = cr3.from(Enrollment.class);
                Join<Enrollment, Classroom> classJoin = enrollmentRoot.join("classField");

                CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
                Root<Enrollment> countRoot = countQuery.from(Enrollment.class);
                Join<Enrollment, Classroom> countClassJoin = countRoot.join("classField");

                countQuery.select(cb.count(countClassJoin))
                        .where(cb.equal(countRoot.get("student").get("id"), student.getId()));

                noOfRecord = (session.createQuery(countQuery).getSingleResult() + elementsPerPage - 1) / elementsPerPage;

                cr3.select(classJoin)
                        .where(cb.equal(enrollmentRoot.get("student").get("id"), student.getId()));
                classList = session.createQuery(cr3).setFirstResult(start).setMaxResults(elementsPerPage).list();
                for(Classroom classroom : classList) {
                    Hibernate.initialize(classroom.getCrs());
                    Hibernate.initialize(classroom.getLec());
                    Hibernate.initialize(classroom.getCreateBy());
                }
                return new ResultList<>(noOfRecord,classList);

            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Student selectedById(int t) {
        Student student;
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try {
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery<Student> cr = cb.createQuery(Student.class);
                Root<Student> root = cr.from(Student.class);
                cr.select(root).where(cb.equal(root.get("id"), t));
                student = session.createQuery(cr).uniqueResult();
                Hibernate.initialize(student.getUser());
                Hibernate.initialize(student.getCreateBy());
                Hibernate.initialize(student.getMajor());
                transaction.commit();
                return student;
            }catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
        return null;
    }

    public ResultList<Classroom> searchByYear(Student student, String year, int start, int elementsPerPage) {
        Long count ;
        String[] years = year.split("-");
        LocalDate startDate = LocalDate.of(Integer.parseInt(years[0]), 1, 1);
        LocalDate endDate = LocalDate.of(Integer.parseInt(years[1]), 12, 31);

        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();

            // Query để tính tổng số lượng
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Enrollment> countRoot = countQuery.from(Enrollment.class);
            Join<Enrollment, Classroom> classCount = countRoot.join("classField");

            //Query để lấy List Class đc chọn
            CriteriaQuery<Classroom> cr2 = cb.createQuery(Classroom.class);
            Root<Enrollment> enrollmentRoot = cr2.from(Enrollment.class);
            Join<Enrollment, Classroom> classJoin = enrollmentRoot.join("classField");


            Predicate datePredicate1 = cb.between(
                    classJoin.get("startDate"),
                    startDate,
                    endDate
            );
            Predicate datePredicate2 = cb.between(
                    classCount.get("startDate"),
                    startDate,
                    endDate
            );

            cr2.select(classJoin).where(cb.and(datePredicate1 ,cb.equal(enrollmentRoot.get("student").get("id"), student.getId())));

            List<Classroom> classList = session.createQuery(cr2).setFirstResult(start).setMaxResults(elementsPerPage).list();

            countQuery.select(cb.count(countRoot)).where(cb.and(datePredicate2, cb.equal(countRoot.get("student").get("id"), student.getId())));
            count = (session.createQuery(countQuery).getSingleResult() + elementsPerPage - 1) / elementsPerPage;

            for(Classroom classroom : classList) {
                Hibernate.initialize(classroom.getCrs());
                Hibernate.initialize(classroom.getLec());
                Hibernate.initialize(classroom.getCreateBy());
            }
            return new ResultList<>(count, classList);
        }
    }

    public ResultList<Student> findByCondition(StudentDto studentDto, int start, int elementsPerPage) {
        Long countResult;
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();

            try {
                CriteriaBuilder builder = session.getCriteriaBuilder();

                // Query để tính tổng số lượng
                CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
                Root<Student> countRoot = countQuery.from(Student.class);

                // Query để lấy List
                CriteriaQuery<Student> dataQuery = builder.createQuery(Student.class);
                Root<Student> dataRoot = dataQuery.from(Student.class);

                List<Predicate> predicates = new ArrayList<>();
                List<Predicate> predicates2 = new ArrayList<>();

                Expression<String> yearExpression = builder.function("YEAR", String.class, dataRoot.get("entryYear"));
                Expression<String> yearExpression2 = builder.function("YEAR", String.class, countRoot.get("entryYear"));

                if (studentDto.getStudentEntryYear() != null && !studentDto.getStudentEntryYear().isEmpty()) {
                    predicates.add(builder.equal(yearExpression, studentDto.getStudentEntryYear()));
                    predicates2.add(builder.equal(yearExpression2, studentDto.getStudentEntryYear()));
                }
                if (studentDto.getMajor() != null && !studentDto.getMajor().isEmpty()) {
                    predicates.add(builder.like(dataRoot.get("major").get("majorName"), studentDto.getMajor()));
                    predicates2.add(builder.like(countRoot.get("major").get("majorName"), studentDto.getMajor()));
                }
                if (studentDto.getStudentCode() != null && !studentDto.getStudentCode().isEmpty()) {
                    predicates.add(builder.equal(dataRoot.get("user").get("userName"), studentDto.getStudentCode()));
                    predicates2.add(builder.equal(countRoot.get("user").get("userName"), studentDto.getStudentCode()));
                }

                Predicate[] predicatesArray = predicates.toArray(new Predicate[0]);
                Predicate[] predicatesArray2 = predicates2.toArray(new Predicate[0]);

                countQuery.select(builder.count(countRoot)).where(predicatesArray2);
                dataQuery.where(predicatesArray);

                countResult = (session.createQuery(countQuery).uniqueResult() + elementsPerPage - 1) / elementsPerPage;

                List<Student> studentList = session.createQuery(dataQuery).setFirstResult(start).setMaxResults(elementsPerPage).getResultList();
                for (Student student : studentList) {
                    Hibernate.initialize(student.getUser());
                    Hibernate.initialize(student.getCreateBy());
                    Hibernate.initialize(student.getMajor());
                }
                transaction.commit();
                return new ResultList<>(countResult,studentList);
            }catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
        return null;
    }

    public List<Integer> listYear() {
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Integer> cq = cb.createQuery(Integer.class);
            Root<Student> root = cq.from(Student.class);

            // Sử dụng hàm YEAR trong CriteriaBuilder
            cq.select(cb.function("YEAR", Integer.class, root.get("entryYear"))).distinct(true);

            return session.createQuery(cq).getResultList();
        }
    }

}
