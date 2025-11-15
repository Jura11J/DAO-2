package model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grade_changes")
public class GradeChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "old_grade")
    private Integer oldGrade;

    @Column(name = "new_grade")
    private Integer newGrade;

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    public GradeChange() {}

    public GradeChange(Student student,
                       Course course,
                       Integer oldGrade,
                       Integer newGrade,
                       LocalDateTime dateTime) {
        this.student = student;
        this.course = course;
        this.oldGrade = oldGrade;
        this.newGrade = newGrade;
        this.dateTime = dateTime;
    }

    public int getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public Integer getOldGrade() {
        return oldGrade;
    }

    public Integer getNewGrade() {
        return newGrade;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setOldGrade(Integer oldGrade) {
        this.oldGrade = oldGrade;
    }

    public void setNewGrade(Integer newGrade) {
        this.newGrade = newGrade;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "GradeChange{" +
                "id=" + id +
                ", student=" + student +
                ", course=" + course +
                ", oldGrade=" + oldGrade +
                ", newGrade=" + newGrade +
                ", dateTime=" + dateTime +
                '}';
    }
}
