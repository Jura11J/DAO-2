package dao.hibernate;

import dao.StudentDAO;
import model.Course;
import model.Grade;
import model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentHibernate implements StudentDAO {

    SessionFactory factory;
    Session session;
    Transaction transaction;

    public StudentHibernate() {
        factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        session = factory.openSession();
        transaction = session.beginTransaction();
    }

    public void commit() {
        transaction.commit();
    }

    public void rollback() {
        transaction.rollback();
    }

    public void close() {
        session.close();
        factory.close();
    }

    @Override
    public Student addStudent(String firstName, String lastName, String email) {

        if (transaction == null || !transaction.isActive()) {
            transaction = session.beginTransaction();
        }

        Student student = new Student(firstName, lastName, email);
        session.persist(student);
        session.flush();   // teraz już jest na pewno w ramach aktywnej transakcji

        return student;
    }



    @Override
    public Student getStudentById(int id) {
        return session.get(Student.class, id);
    }

    @Override
    public Student getStudentByEmail(String email) {
        Query<Student> query = session.createQuery(
                "FROM Student s WHERE s.email = :email", Student.class);
        query.setParameter("email", email);

        return query.uniqueResult();

    }

    @Override
    public List<Student> getAllStudents() {
        return session.createQuery("FROM Student", Student.class)
                .getResultList();
    }

    @Override
    public void updateStudent(Student student) {

        session.merge(student);
    }

    @Override
    public void deleteStudent(Student student) {
        Student managed = student;
        if (!session.contains(student)) {
            managed = session.get(Student.class, student.getId());
        }
        if (managed != null) {
            session.remove(managed);
        }
    }

    @Override
    public List<Course> getEnrolledCourses(Student student) {

        Student managed = student;
        if (!session.contains(student)) {
            managed = session.get(Student.class, student.getId());
        }
        if (managed == null) {
            return new ArrayList<>();
        }

        Set<Course> coursesSet = new HashSet<>();
        Set<Grade> grades = managed.getGrades();

        for (Grade g : grades) {
            Course c = g.getCourse();
            if (c != null) {
                coursesSet.add(c);
            }
        }

        return new ArrayList<>(coursesSet);
    }
}
