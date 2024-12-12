package services.implement;

import dtos.ClassDto;
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

import java.util.ArrayList;
import java.util.List;

public class ClassService implements IServices<Classroom> {

    @Override
    public void Insert(Classroom classroom) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try{
                session.persist(classroom);
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
    public void Update(Classroom classroom) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try {
                session.merge(classroom);
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
    public void Delete(int t) {}

    @Override
    public ResultList<Classroom> selectAll(int start, int elementsPerPage) {
        List<Classroom> classrooms;
        Long noOfRecord;
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try{
                CriteriaBuilder cb = session.getCriteriaBuilder();

                // Lâý số lượng trang kết quả trả về
                CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
                Root<Classroom> countRoot = countQuery.from(Classroom.class);
                countQuery.select(cb.count(countRoot));
                noOfRecord = (session.createQuery(countQuery).getSingleResult() + elementsPerPage - 1) / elementsPerPage;

                // Lấy kết quả trả về
                CriteriaQuery<Classroom> cq = cb.createQuery(Classroom.class);
                Root<Classroom> root = cq.from(Classroom.class);
                cq.select(root);
                classrooms = session.createQuery(cq).setFirstResult(start).setMaxResults(elementsPerPage).list();

                for (Classroom classroom : classrooms) {
                    Hibernate.initialize(classroom.getCrs());
                    Hibernate.initialize(classroom.getCreateBy());
                    Hibernate.initialize(classroom.getLec());
                }
                transaction.commit();
                return new ResultList<>(noOfRecord, classrooms) ;
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Classroom selectedById(int classID) {
        Classroom classroom ;
        try(Session session = HibernateUtils.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try{
                CriteriaBuilder cb = session.getCriteriaBuilder();

                // Query lấy ClassRoom dựa trên ClassID
                CriteriaQuery<Classroom> cr2 = cb.createQuery(Classroom.class);
                Root<Classroom> root = cr2.from(Classroom.class);
                cr2.select(root).where(cb.equal(root.get("id"),classID));

                classroom = session.createQuery(cr2).getSingleResult();
                Hibernate.initialize(classroom.getCrs());
                Hibernate.initialize(classroom.getCreateBy());
                Hibernate.initialize(classroom.getLec());

                transaction.commit();
                return classroom;
            }catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
        return null;
    }

    public ResultList<Student> getStudentList(Classroom classroom, int start, int elementsPerPage) {
    Long noOfRecord ;
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try {
                CriteriaBuilder cb = session.getCriteriaBuilder();

                CriteriaQuery<Student> cr3 = cb.createQuery(Student.class);
                Root<Enrollment> enrollmentRoot = cr3.from(Enrollment.class);
                Join<Enrollment, Student> classJoin = enrollmentRoot.join("student");

                CriteriaQuery<Long> cr4 = cb.createQuery(Long.class);
                Root<Enrollment> countRoot = cr4.from(Enrollment.class);
                Join<Enrollment, Student> countStudentJoin = countRoot.join("student");

                cr4.select(cb.count(countStudentJoin))
                        .where(cb.equal(countRoot.get("classField").get("id"), classroom.getId()));

                noOfRecord =  session.createQuery(cr4).getSingleResult();


                cr3.select(classJoin)
                        .where(cb.equal(enrollmentRoot.get("classField").get("id"), classroom.getId()));

                List<Student> studentList = session.createQuery(cr3).setFirstResult(start).setMaxResults(elementsPerPage).list();
                for(Student student : studentList){
                    Hibernate.initialize(student.getUser());
                    Hibernate.initialize(student.getCreateBy());
                }
                transaction.commit();
                return  new ResultList<>(noOfRecord, studentList) ;
            }catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        }
        return null;
    }


    public ResultList<Student> findByStudentCondition(int classID, StudentDto studentDto, int start, int elementsPerPage) {
        Long count = 0L;
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try {
                CriteriaBuilder cb = session.getCriteriaBuilder();

                // Query để tính tổng số lượng
                CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
                Root<Enrollment> countRoot = countQuery.from(Enrollment.class);
                Join<Enrollment, Student> studentCount = countRoot.join("student");

                // Query để lấy List
                CriteriaQuery<Student> cr3 = cb.createQuery(Student.class);
                Root<Enrollment> enrollmentRoot = cr3.from(Enrollment.class);
                Join<Enrollment, Student> classJoin = enrollmentRoot.join("student");

                List<Predicate> predicates = new ArrayList<>();
                List<Predicate> predicates2 = new ArrayList<>();

                if (studentDto.getStudentGender().equalsIgnoreCase("male") || studentDto.getStudentGender().equalsIgnoreCase("female")) {
                    predicates.add(cb.equal(classJoin.get("studentGender"), studentDto.getStudentGender().equalsIgnoreCase("male")));
                    predicates2.add(cb.equal(studentCount.get("studentGender"), studentDto.getStudentGender().equalsIgnoreCase("female")));
                }

                Expression<String> fullName = cb.concat(classJoin.get("studentFname"), cb.literal(" "));
                fullName = cb.concat(fullName, classJoin.get("studentLname"));

                Expression<String> fullName2 = cb.concat(studentCount.get("studentFname"), cb.literal(" "));
                fullName2 = cb.concat(fullName2, studentCount.get("studentLname"));

                predicates.add(cb.like(cb.lower(fullName), "%" + studentDto.getStudentLastName().toLowerCase().trim() + "%"));
                predicates2.add(cb.like(cb.lower(fullName2), "%" + studentDto.getStudentLastName().toLowerCase().trim() + "%"));

                predicates.add(cb.equal(enrollmentRoot.get("classField").get("id"), classID));
                predicates2.add(cb.equal(countRoot.get("classField").get("id"), classID));

                Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
                Predicate[] predicateArray2 = predicates2.toArray(new Predicate[0]);


                countQuery.select(cb.count(countRoot)).where(predicateArray2);
                cr3.select(classJoin).where(predicateArray);

                count = (session.createQuery(countQuery).getSingleResult() + elementsPerPage - 1) / elementsPerPage;

                List<Student> studentList = session.createQuery(cr3).setFirstResult(start).setMaxResults(elementsPerPage).list();
                for (Student student : studentList) {
                    Hibernate.initialize(student.getUser());
                    Hibernate.initialize(student.getCreateBy());
                    Hibernate.initialize(student.getMajor());
                }
                transaction.commit();
                return new ResultList<>(count,studentList);
            }catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }

        }
        return null;
    }
    public ResultList<Classroom> findByClassCondition(ClassDto classDto, int start, int elementsPerPage) {
        Long count = 0L;
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            try {
                CriteriaBuilder cb = session.getCriteriaBuilder();

                // Query để tính tổng số lượng
                CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
                Root<Classroom> countRoot = countQuery.from(Classroom.class);

                // Query để lấy List
                CriteriaQuery<Classroom> cr3 = cb.createQuery(Classroom.class);
                Root<Classroom> enrollmentRoot = cr3.from(Classroom.class);

                List<Predicate> predicates = new ArrayList<>();
                List<Predicate> predicates2 = new ArrayList<>();

                Expression<String> yearExpression = cb.function("YEAR", String.class, enrollmentRoot.get("startDate"));
                Expression<String> yearExpression2 = cb.function("YEAR", String.class, countRoot.get("startDate"));

                if (classDto.getStartDate() != null && !classDto.getStartDate().isEmpty()) {
                    predicates.add(cb.equal(yearExpression, classDto.getStartDate()));
                    predicates2.add(cb.equal(yearExpression2, classDto.getStartDate()));
                }

                Expression<String> fullName = cb.concat(enrollmentRoot.get("lec").get("lecFname"), cb.literal(" "));
                fullName = cb.concat(fullName, enrollmentRoot.get("lec").get("lecLname"));

                Expression<String> fullName2 = cb.concat(countRoot.get("lec").get("lecFname"), cb.literal(" "));
                fullName2 = cb.concat(fullName2, countRoot.get("lec").get("lecLname"));

                predicates.add(cb.like(cb.lower(fullName), "%" + classDto.getLecLName().toLowerCase().trim() + "%"));
                predicates2.add(cb.like(cb.lower(fullName2), "%" + classDto.getLecLName().toLowerCase().trim() + "%"));

                if(classDto.getCourseID()!=0) {
                    predicates.add(cb.equal(enrollmentRoot.get("crs").get("id"), classDto.getCourseID()));
                    predicates2.add(cb.equal(countRoot.get("crs").get("id"), classDto.getCourseID()));
                }

                predicates.add(cb.like(enrollmentRoot.get("crs").get("majorIn").get("majorName"), "%" +classDto.getSelectedMajor()+"%"));
                predicates2.add(cb.like(countRoot.get("crs").get("majorIn").get("majorName"), "%" +classDto.getSelectedMajor() + "%"));

                Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
                Predicate[] predicateArray2 = predicates2.toArray(new Predicate[0]);

                countQuery.select(cb.count(countRoot)).where(predicateArray2);
                cr3.select(enrollmentRoot).where(predicateArray);


                List<Classroom> classroomList = session.createQuery(cr3).setFirstResult(start).setMaxResults(elementsPerPage).list();
                count = (session.createQuery(countQuery).getSingleResult() + elementsPerPage - 1) / elementsPerPage;

                for(Classroom classroom : classroomList) {
                    Hibernate.initialize(classroom.getCrs());
                    Hibernate.initialize(classroom.getLec());
                    Hibernate.initialize(classroom.getCreateBy());
                }

                transaction.commit();
                return new ResultList<>(count,classroomList);
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
            Root<Classroom> root = cq.from(Classroom.class);

            // Sử dụng hàm YEAR trong CriteriaBuilder
            cq.select(cb.function("YEAR", Integer.class, root.get("startDate"))).distinct(true);

            return session.createQuery(cq).getResultList();
        }
    }
}
