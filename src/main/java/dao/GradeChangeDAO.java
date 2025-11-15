package dao;

import model.Course;
import model.GradeChange;
import model.Student;

import java.util.List;

public interface GradeChangeDAO {
    GradeChange logChange(Student student,
                          Course course,
                          int oldGrade,
                          int newGrade);
    List<GradeChange> getAll();
    List<GradeChange> getChangesFor(Student student, Course course);
}
