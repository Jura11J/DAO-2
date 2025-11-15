package dao.hibernate;

import dao.CourseDAO;
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

public class CourseHibernate implements CourseDAO {

    SessionFactory factory;
    Session session;
    Transaction transaction;

    public CourseHibernate() {
        factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        session = factory.openSession();
        transaction = session.beginTransaction();
    }

    @Override
    public void rollback() {
        transaction.rollback();
    }

    @Override
    public void commit() {
        transaction.commit();
    }

    @Override
    public Course addCourse(String courseName) {

        if (transaction == null || !transaction.isActive()) {
            transaction = session.beginTransaction();
        }

        Course course = new Course(courseName);
        session.persist(course);
        session.flush();

        return course;
    }



    @Override
    public Course getCourseById(int id) {
        return session.get(Course.class, id);
    }

    @Override
    public Course getCourseByName(String name) {
        Query<Course> query = session.createQuery(
                "FROM Course c WHERE c.courseName = :name", Course.class);
        query.setParameter("name", name);


        return query.uniqueResult();

    }

    @Override
    public List<Course> getAllCourses() {
        return session.createQuery("FROM Course", Course.class)
                .getResultList();
    }

    @Override
    public void updateCourse(Course course) {

        session.merge(course);
    }

    @Override
    public void deleteCourse(Course course) {

        if (transaction == null || !transaction.isActive()) {
            transaction = session.beginTransaction();
        }

        if (course == null) {
            return;
        }


        Course managed = course;
        if (!session.contains(course)) {
            managed = session.get(Course.class, course.getId());
        }


        if (managed != null) {
            session.remove(managed);
            session.flush();
        }
    }


    @Override
    public List<Student> getEnrolledStudents(Course course) {

        Course managed = course;
        if (!session.contains(course)) {
            managed = session.get(Course.class, course.getId());
        }
        if (managed == null) {
            return new ArrayList<>();
        }

        Set<Student> studentsSet = new HashSet<>();
        Set<Grade> grades = managed.getGrades();

        for (Grade g : grades) {
            Student s = g.getStudent();
            if (s != null) {
                studentsSet.add(s);
            }
        }

        return new ArrayList<>(studentsSet);
    }
}
