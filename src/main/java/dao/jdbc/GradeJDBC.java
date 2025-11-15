package dao.jdbc;

import dao.GradeDAO;
import model.Course;
import model.Grade;
import model.Student;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GradeJDBC implements GradeDAO {

    Connection connection;
    boolean test;

    public GradeJDBC() {
        try {
            this.connection = new DatabaseConnection().getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Grade enrollStudent(Student student, Course course) {
        String sql = "INSERT INTO grades VALUES (?, ?, null)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, student.getId());
            preparedStatement.setInt(2, course.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to insert student in database");
            }
            return getGrade(student, course);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unenrollStudent(Student student, Course course) {
        String sql = "DELETE FROM grades WHERE student_id = ? and course_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, student.getId());
            preparedStatement.setInt(2, course.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to unenroll student in database");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGrade(Student student, Course course, int grade) {
        String sql = "UPDATE grades SET grade = ? WHERE student_id = ? and course_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, grade);
            preparedStatement.setInt(2, student.getId());
            preparedStatement.setInt(3, course.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update grade in database");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Grade getGrade(Student student, Course course) {
        String sql = "SELECT grade FROM grades WHERE student_id = ? and course_id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, student.getId());
            preparedStatement.setInt(2, course.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Grade(student, course, resultSet.getInt("grade"));
            }
            throw new SQLException("Execution error");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Grade> getGradesForStudent(Student student) {
        String sql =  "SELECT * FROM grades WHERE student_id = ?";
        List<Grade> grades = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, student.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("grade") != null) {
                    int courseId = resultSet.getInt("course_id");
                    Course course = new CourseJDBC().getCourseById(courseId);
                    grades.add(new Grade(student, course,  resultSet.getInt("grade")));
                }
            }
            return grades;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Grade> getGradesForCourse(Course course) {
        String sql = "SELECT * FROM grades WHERE course_id = ?";
        List<Grade> grades = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, course.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("grade") != null) {
                    int studentId = resultSet.getInt("student_id");
                    Student student = new StudentJDBC().getStudentById(studentId);
                    grades.add(new Grade(student, course,  resultSet.getInt("grade")));
                }
            }
            return grades;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
