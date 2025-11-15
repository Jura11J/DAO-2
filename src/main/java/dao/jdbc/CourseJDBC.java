package dao.jdbc;

import dao.CourseDAO;
import model.Course;
import model.Student;
import util.DatabaseConnection;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class CourseJDBC implements CourseDAO {

    Connection connection;

    public CourseJDBC() {
        try {
            connection = new DatabaseConnection().getConnection();
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
    public Course addCourse(String name) {
        String sql = "insert into courses (name) values (?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to add course");
            }

            return getCourseByName(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Course getCourseById(int id) {
        String sql = "select * from courses where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Course(resultSet.getInt("id"), resultSet.getString("name"));
            }
            throw new SQLException("No data found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Course getCourseByName(String name) {
        String sql = "select * from courses where name = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Course(resultSet.getInt("id"), resultSet.getString("name"));
            }
            throw new SQLException("No data found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> getAllCourses() {
        String sql = "select * from courses";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Course> courseList = new ArrayList<>();
            while (resultSet.next()) {
                courseList.add(new Course(resultSet.getInt("id"), resultSet.getString("name")));
            }
            return courseList;

        } catch (SQLException e)  {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCourse(Course course) {
        String sql = "update courses set name = ? where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, course.getCourseName());
            preparedStatement.setInt(2, course.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to update student in database");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteCourse(Course course) {
        String sql = "delete from courses where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, course.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to delete student in database");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> getEnrolledStudents(Course course) {
        String gradesSql = "select * from grades where course_id = ?";
        String studentSql = "select * from students where id = ?";
        List<Student> studentList = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(gradesSql);
            preparedStatement.setInt(1, course.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                PreparedStatement preparedStatement1 = connection.prepareStatement(studentSql);
                preparedStatement1.setInt(1, resultSet.getInt("student_id"));
                ResultSet resultSet1 = preparedStatement1.executeQuery();
                if (resultSet1.next()) {
                    studentList.add(new Student(resultSet1.getInt("id"), resultSet1.getString("first_name"), resultSet1.getString("last_name"), resultSet1.getString("email")));
                }
            }
            return studentList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
