package dao.hibernate;

import dao.GradeChangeDAO;
import model.Course;
import model.GradeChange;
import model.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.List;

public class GradeChangeHibernate implements GradeChangeDAO {

    SessionFactory factory;
    Session session;
    Transaction transaction;

    public GradeChangeHibernate() {
        factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        session = factory.openSession();
        transaction = session.beginTransaction();
    }

    private void ensureTransaction() {
        if (transaction == null || !transaction.isActive()) {
            transaction = session.beginTransaction();
        }
    }

    @Override
    public GradeChange logChange(Student student, Course course,
                                 int oldGrade, int newGrade) {
        ensureTransaction();
        GradeChange change = new GradeChange(
                student,
                course,
                oldGrade,
                newGrade,
                LocalDateTime.now()
        );
        session.persist(change);
        session.flush();
        return change;
    }



    @Override
    public List<GradeChange> getAll() {
        Query<GradeChange> q = session.createQuery(
                "FROM GradeChange", GradeChange.class);
        return q.getResultList();
    }

    @Override
    public List<GradeChange> getChangesFor(Student student, Course course) {
        Query<GradeChange> q = session.createQuery(
                "FROM GradeChange gc WHERE gc.student.id = :sid AND gc.course.id = :cid",
                GradeChange.class
        );
        q.setParameter("sid", student.getId());
        q.setParameter("cid", course.getId());
        return q.getResultList();
    }
}
