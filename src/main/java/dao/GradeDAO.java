package dao;

import model.Course;
import model.Grade;
import model.Student;

import java.util.List;

public interface GradeDAO {
    void rollback();
    void commit();

    Grade enrollStudent(Student student, Course course);
    void unenrollStudent(Student student, Course course);
    void updateGrade(Student student, Course course, int grade);

    Grade getGrade(Student student, Course course);
    List<Grade> getGradesForStudent(Student student);
    List<Grade> getGradesForCourse(Course course);
}
