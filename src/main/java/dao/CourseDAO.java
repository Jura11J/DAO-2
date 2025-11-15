package dao;

import model.Course;
import model.Student;

import java.util.List;

public interface CourseDAO {
    void rollback();
    void commit();

    Course addCourse(String name);
    Course getCourseById(int id);
    Course getCourseByName(String name);
    List<Course> getAllCourses();
    void updateCourse(Course course);
    void deleteCourse(Course course);

    List<Student> getEnrolledStudents(Course course);
}
