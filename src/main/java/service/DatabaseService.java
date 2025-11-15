package service;

import dao.CourseDAO;
import dao.GradeDAO;
import dao.StudentDAO;
import model.Course;
import model.Grade;
import model.Student;

import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final GradeDAO gradeDAO;

    public DatabaseService(StudentDAO studentDAO, CourseDAO courseDAO, GradeDAO gradeDAO) {
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
        this.gradeDAO = gradeDAO;
    }

    public void commit() {
        studentDAO.commit();
        courseDAO.commit();
        gradeDAO.commit();
    }

    public void rollback() {
        studentDAO.rollback();
        courseDAO.rollback();
        gradeDAO.rollback();
    }

    // Student

    public Student registerStudent(String firstName, String lastName, String email) {
        return studentDAO.addStudent(firstName, lastName, email);
    }

    public double calculateStudentGpa(int studentId) {
        Student student = studentDAO.getStudentById(studentId);
        List<Grade> grades = gradeDAO.getGradesForStudent(student);
        double total = 0;
        int count = 0;
        for (Grade grade : grades) {
            total += grade.getGrade();
            count++;
        }
        return total/count;
    }

    public List<Course> getEnrolledCourses(int studentId) {
        return studentDAO.getEnrolledCourses(studentDAO.getStudentById(studentId));
    }

    public void removeStudent(int studentId) {
        studentDAO.deleteStudent(studentDAO.getStudentById(studentId));
    }

    public Student getStudent(int studentId) {
        return studentDAO.getStudentById(studentId);
    }

    public Student getStudent(String email) {
        return studentDAO.getStudentByEmail(email);
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    // Course

    public Course addCourse(String courseName) {
        return courseDAO.addCourse(courseName);
    }

    public int getNumberOfEnrolledStudents(int courseId) {
        return courseDAO.getEnrolledStudents(courseDAO.getCourseById(courseId)).size();
    }

    public List<Student> getEnrolledStudents(int courseId) {
        return courseDAO.getEnrolledStudents(courseDAO.getCourseById(courseId));
    }

    public void removeCourse(int courseId) {
        courseDAO.deleteCourse(courseDAO.getCourseById(courseId));
    }

    public double calculateCourseGpa(int courseId) {
        Course course = courseDAO.getCourseById(courseId);
        List<Grade> grades = gradeDAO.getGradesForCourse(course);
        double total = 0;
        int count = 0;
        for (Grade grade : grades) {
            if (grade.getGrade() != null) {
                total += grade.getGrade();
                count++;
            }

        }
        return total/count;
    }

    public Course getCourse(int courseId) {
        return courseDAO.getCourseById(courseId);
    }

    public List<Course> getAllCourses() {
        return courseDAO.getAllCourses();
    }

    // Grade

    public Grade enrollStudentInCourse(int studentId, int courseId) {
        Student student = getStudent(studentId);
        Course course = getCourse(courseId);
        return gradeDAO.enrollStudent(student, course);
    }

    public void updateGrade(int studentId, int courseId, int grade) {
        Student student = getStudent(studentId);
        Course course = getCourse(courseId);
        gradeDAO.updateGrade(student, course, grade);
    }

    public Grade getGrade(int studentId, int courseId) {
        Student student = getStudent(studentId);
        Course course = getCourse(courseId);
        return gradeDAO.getGrade(student, course);
    }

    public void unenrollStudent(int studentId, int courseId) {
        Student student = getStudent(studentId);
        Course course = getCourse(courseId);
        gradeDAO.unenrollStudent(student, course);

    }
}
