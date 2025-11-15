package dao.jdbc;

import dao.StudentDAO;
import model.Course;
import model.Student;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentJDBC implements StudentDAO {
    Connection connection;

    public StudentJDBC() {
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
    public Student addStudent(String firstName, String lastName, String email) {
        String sql = "insert into students (first_name, last_name, email) values(?,?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to insert student into database");
            }

            return getStudentByEmail(email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Student getStudentById(int id) {
        String sql = "select * from students where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Student(resultSet.getInt("id"), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("email"));
            }
            throw new SQLException("Student not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Student getStudentByEmail(String email) {
        String sql = "select * from students where email = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Student(resultSet.getInt("id"), resultSet.getString("first_name"), resultSet.getString("last_name"), resultSet.getString("email"));
            }
            throw new SQLException("Student not found");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Student> getAllStudents() {
        String sql = "SELECT * FROM students";
        List<Student> students = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Student student = new Student(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"));
                students.add(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return students;
    }

    @Override
    public void updateStudent(Student student) {
        String sql = "update students set first_name = ?, last_name = ?, email = ? where id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, student.getFirstName());
            preparedStatement.setString(2, student.getLastName());
            preparedStatement.setString(3, student.getEmail());
            preparedStatement.setInt(4, student.getId());
            int rowsAffected = preparedStatement.executeUpdate();
            if  (rowsAffected == 0) {
                throw new SQLException("Failed to update student in database");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void deleteStudent(Student student) {
        String sql = "DELETE FROM students WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, student.getId());
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to delete student in database");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Course> getEnrolledCourses(Student student) {
        String gradesSql = "select * from grades where student_id = ?";
        String coursesSql = "select * from courses where id = ?";
        List<Course> enrolledCourses = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement(gradesSql);
            ps.setInt(1, student.getId());
            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                PreparedStatement ps2 = connection.prepareStatement(coursesSql);
                ps2.setInt(1, resultSet.getInt("student_id"));
                ResultSet resultSet2 = ps2.executeQuery();
                if  (resultSet2.next()) {
                    enrolledCourses.add(new Course(resultSet2.getInt("id"), resultSet2.getString("name")));
                }
            }
            return enrolledCourses;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
