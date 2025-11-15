package service;

import dao.hibernate.CourseHibernate;
import dao.hibernate.GradeHibernate;
import dao.hibernate.StudentHibernate;
import dao.hibernate.GradeChangeHibernate;
import dao.jdbc.CourseJDBC;
import dao.jdbc.GradeJDBC;
import dao.jdbc.StudentJDBC;
import model.Course;
import model.Grade;
import model.Student;
import org.junit.jupiter.api.*;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseServiceTest {

    DatabaseService databaseService;
    @BeforeEach
    void setUp() {
        this.databaseService = new DatabaseService(new StudentHibernate(),new CourseHibernate(), new GradeHibernate());
    }

    @AfterEach
    void tearDown() {
        databaseService.rollback();
    }


    @Test
    void registerStudent() {
        Student student = databaseService.registerStudent("test", "rigged", "test@test.com");
        assertEquals(student.toString(), databaseService.getStudent(4).toString());
    }

    @Test
    void calculateStudentGpa() {
        assertEquals(3, databaseService.calculateStudentGpa(3), 0.01);
    }

    @Test
    void getEnrolledCourses() {
        System.out.println(databaseService.getEnrolledCourses(1));
        assertEquals(2, databaseService.getEnrolledCourses(1).size());
    }

    @Test
    void removeStudent() {
        databaseService.removeStudent(1);
        assertEquals(2, databaseService.getAllStudents().size());
    }

    @Test
    void addCourse() {
        Course course = databaseService.addCourse("test");
        assertEquals(course.toString(), databaseService.getCourse(4).toString());
    }

    @Test
    void getNumberOfEnrolledStudents() {
        assertEquals(2, databaseService.getNumberOfEnrolledStudents(1));
    }

    @Test
    void getEnrolledStudents() {
        assertEquals(2, databaseService.getEnrolledStudents(1).size());
    }

    @Test
    void removeCourse() {
        databaseService.removeCourse(1);
        assertEquals(2, databaseService.getAllCourses().size());
    }

    @Test
    void calculateCourseGpa() {
        assertEquals(2, databaseService.calculateCourseGpa(3), 0.01);
    }

    @Test
    void enrollStudentInCourse() {
        Grade grade = databaseService.enrollStudentInCourse(3, 1);
        assertEquals(grade.getStudent().toString(), databaseService.getGrade(3, 1).getStudent().toString());
        assertEquals(grade.getCourse().toString(), databaseService.getGrade(3, 1).getCourse().toString());
        assertEquals(grade.getGrade(), databaseService.getGrade(3, 1).getGrade());
    }

    @Test
    void updateGrade() {
        databaseService.updateGrade(1,2,5);
        assertEquals(5, databaseService.calculateStudentGpa(1), 0.01);
    }

    @Test
    void getGrade() {
        assertEquals(5, databaseService.getGrade(1,1).getGrade(),0.01);
    }

    @Test
    void unenrollStudent() {
        assertDoesNotThrow(() -> databaseService.unenrollStudent(1,1));
    }

    @Test
    void shouldLogGradeChange() {
        databaseService.logChange(databaseService.getStudent(1), databaseService.getCourse(1), 5, 3);

    }
}