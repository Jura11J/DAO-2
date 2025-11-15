package dao;

import model.Course;
import model.Student;

import java.util.List;

public interface StudentDAO {
    void rollback();
    void commit();

    Student addStudent(String firstName, String lastName, String email);
    Student getStudentById(int id);
    Student getStudentByEmail(String email);
    List<Student> getAllStudents();
    void updateStudent(Student student);
    void deleteStudent(Student student);

    List<Course> getEnrolledCourses(Student student);
}
