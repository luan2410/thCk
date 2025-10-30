package iuh.fit.se.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Mã khóa học không được để trống!")
    @Size(max = 20, message = "Mã khóa học tối đa 20 ký tự!")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Mã khóa học chỉ gồm chữ in hoa và số!")
    @Column(unique = true, length = 20, nullable = false)
    private String courseCode;

    @NotBlank(message = "Tên khóa học bắt buộc nhập!")
    @Size(max = 100, message = "Tên khóa học tối đa 100 ký tự!")
    @Column(length = 100, nullable = false)
    private String courseName;

    @Min(value = 1, message = "Số tín chỉ phải từ 1 đến 10!")
    @Max(value = 10, message = "Số tín chỉ phải từ 1 đến 10!")
    private int credit;

    @ManyToMany(mappedBy = "courses")
    private List<Lecturer> lecturers = new ArrayList<>();

    public Course() {
    }

    public Course(Integer id, String courseCode, String courseName, int credit, List<Lecturer> lecturers) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credit = credit;
        this.lecturers = lecturers;
    }

    // Getters, Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public List<Lecturer> getLecturers() {
        return lecturers;
    }

    public void setLecturers(List<Lecturer> lecturers) {
        this.lecturers = lecturers;
    }
}

