package dao.hibernate;

import dao.GradeDAO;
import model.Course;
import model.Grade;
import model.GradeId;
import model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class GradeHibernate implements GradeDAO {

    SessionFactory factory;
    Session session;
    Transaction transaction;

    public GradeHibernate() {
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
    public Grade enrollStudent(Student student, Course course) {
        // upewniamy się, że student i course są „managed”
        if (!session.contains(student)) {
            student = session.get(Student.class, student.getId());
        }
        if (!session.contains(course)) {
            course = session.get(Course.class, course.getId());
        }

        // tworzymy Grade z nullem jako oceną (samo zapisanie zapisuje „zapisanie na kurs”)
        Grade grade = new Grade(student, course, null);
        session.persist(grade);
        return grade;
    }

    @Override
    public void unenrollStudent(Student student, Course course) {
        Grade g = getGrade(student, course);
        if (g != null) {
            session.remove(g);
        }
    }

    @Override
    public void updateGrade(Student student, Course course, int gradeValue) {
        Grade g = getGrade(student, course);
        if (g != null) {
            g.setGrade(gradeValue);
            // g jest managed (bo przyszło z sesji), więc wystarczy zmienić pole
            // można też dać session.merge(g), ale nie jest konieczne
        }
    }

    @Override
    public Grade getGrade(Student student, Course course) {
        if (student == null || course == null) {
            return null;
        }


        GradeId id = new GradeId(student.getId(), course.getId());
        return session.get(Grade.class, id);
    }


    @Override
    public List<Grade> getGradesForStudent(Student student) {
        int studentId = student.getId();

        Query<Grade> query = session.createQuery(
                "FROM Grade g WHERE g.student.id = :sid",
                Grade.class
        );
        query.setParameter("sid", studentId);

        return query.getResultList();
    }

    @Override
    public List<Grade> getGradesForCourse(Course course) {
        int courseId = course.getId();

        Query<Grade> query = session.createQuery(
                "FROM Grade g WHERE g.course.id = :cid",
                Grade.class
        );
        query.setParameter("cid", courseId);

        return query.getResultList();
    }
}
