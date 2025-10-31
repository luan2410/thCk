# FULL CODE - COURSE MANAGEMENT SYSTEM

## 1. CONFIG - application.properties
```properties
spring.application.name=demo01

# Database Configuration
spring.datasource.url=jdbc:mariadb://localhost:3306/course_management_db
spring.datasource.username=root
spring.datasource.password=sapassword

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MariaDBDialect

# Thymeleaf Configuration (optional - these are defaults)
spring.thymeleaf.cache=false
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

## 2. ENTITY - Lecturer.java
```java
package iuh.fit.se.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lecturers")
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, unique = true)
    private String lecturerCode;

    @Column(length = 100)
    private String lecturerName;

    private String email;
    private String department;

    @ManyToMany
    @JoinTable(
            name = "lecturer_course",
            joinColumns = @JoinColumn(name = "lecturer_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();

    public Lecturer() {
    }

    public Lecturer(Integer id, String lecturerCode, String lecturerName, String email, String department, List<Course> courses) {
        this.id = id;
        this.lecturerCode = lecturerCode;
        this.lecturerName = lecturerName;
        this.email = email;
        this.department = department;
        this.courses = courses;
    }

    // Getters, Setters


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLecturerCode() {
        return lecturerCode;
    }

    public void setLecturerCode(String lecturerCode) {
        this.lecturerCode = lecturerCode;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
```

## 3. ENTITY - Course.java
```java
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
```

## 4. REPOSITORY - LecturerRepository.java
```java
package iuh.fit.se.repositories;

import iuh.fit.se.entities.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
    @Query("SELECT l FROM Lecturer l JOIN l.courses c WHERE c.id = :courseId")
    List<Lecturer> findByCourseId(@Param("courseId") int courseId);
}
```

## 5. REPOSITORY - CourseRepository.java
```java
package iuh.fit.se.repositories;

import iuh.fit.se.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    boolean existsByCourseCode(String code);
}
```

## 6. SERVICE - LecturerService.java
```java
package iuh.fit.se.services;

import iuh.fit.se.entities.Lecturer;

import java.util.List;

public interface LecturerService {
    List<Lecturer> findAll();
    List<Lecturer> findByCourseId(int id);
}
```

## 7. SERVICE - CourseService.java
```java
package iuh.fit.se.services;

import iuh.fit.se.entities.Course;

import java.util.List;

public interface CourseService {
    List<Course> findAll();
    Course findById(int id);
    void save(Course course);
    void delete(int id);
    boolean existsByCode(String code);
}
```

## 8. SERVICE IMPL - LecturerServiceImpl.java
```java
package iuh.fit.se.services.impl;

import iuh.fit.se.entities.Lecturer;
import iuh.fit.se.repositories.LecturerRepository;
import iuh.fit.se.services.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LecturerServiceImpl implements LecturerService {

    private final LecturerRepository repo;

    @Autowired
    public LecturerServiceImpl(LecturerRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Lecturer> findAll() {
        return repo.findAll();
    }

    @Override
    public List<Lecturer> findByCourseId(int id) {
        return repo.findByCourseId(id);
    }
}
```

## 9. SERVICE IMPL - CourseServiceImpl.java
```java
package iuh.fit.se.services.impl;

import iuh.fit.se.entities.Course;
import iuh.fit.se.repositories.CourseRepository;
import iuh.fit.se.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository repo;

    @Autowired
    public CourseServiceImpl(CourseRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Course> findAll() {
        return repo.findAll();
    }

    @Override
    public Course findById(int id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public void save(Course course) {

        if (course.getCourseCode() == null || course.getCourseCode().isBlank()) {
            throw new IllegalArgumentException("Mã khóa học không được để trống!");
        }

        if (!course.getCourseCode().matches("^[A-Z0-9]{2,20}$")) {
            throw new IllegalArgumentException("Mã khóa học chỉ gồm chữ in hoa và số, tối đa 20 ký tự!");
        }

        if (repo.existsByCourseCode(course.getCourseCode())) {
            throw new IllegalArgumentException("Mã khóa học đã tồn tại!");
        }

        if (course.getCourseName() == null || course.getCourseName().isBlank()) {
            throw new IllegalArgumentException("Tên khóa học bắt buộc nhập!");
        }

        if (course.getCourseName().length() > 100) {
            throw new IllegalArgumentException("Tên khóa học tối đa 100 ký tự!");
        }

        if (course.getCredit() < 1 || course.getCredit() > 10) {
            throw new IllegalArgumentException("Số tín chỉ phải từ 1 đến 10!");
        }

        repo.save(course);
    }

    @Override
    public void delete(int id) {
        repo.deleteById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return repo.existsByCourseCode(code);
    }
}
```

## 10. CONTROLLER - CourseController.java
```java
package iuh.fit.se.controllers;

import iuh.fit.se.entities.Course;
import iuh.fit.se.services.CourseService;
import iuh.fit.se.services.LecturerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final LecturerService lecturerService;

    @Autowired
    public CourseController(CourseService courseService, LecturerService lecturerService) {
        this.courseService = courseService;
        this.lecturerService = lecturerService;
    }

    @GetMapping
    public String list(Model model) {
        List<Course> list = courseService.findAll();
        Map<Integer, Integer> countLecturers = new HashMap<>();
        for (Course c : list) {
            countLecturers.put(c.getId(), c.getLecturers().size());
        }
        model.addAttribute("courses", list);
        model.addAttribute("countMap", countLecturers);
        return "course-list";
    }

    @GetMapping("/lecturers/{id}")
    public String lecturersByCourse(@PathVariable int id, Model model) {
        model.addAttribute("lecturers", lecturerService.findByCourseId(id));
        model.addAttribute("course", courseService.findById(id));
        return "lecturer-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("course", new Course());
        return "course-add";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("course") Course course, 
                      BindingResult result, 
                      Model model,
                      RedirectAttributes redirectAttributes) {
        // Kiểm tra validation errors
        if (result.hasErrors()) {
            return "course-add";
        }
        
        try {
            courseService.save(course);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm khóa học thành công!");
            return "redirect:/courses";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "course-add";
        } catch (Exception ex) {
            model.addAttribute("error", "Đã xảy ra lỗi không xác định!");
            return "course-add";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Course c = courseService.findById(id);
        if (c != null && c.getLecturers() != null && !c.getLecturers().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa! Khóa học đã có giảng viên.");
        } else {
            courseService.delete(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa khóa học thành công!");
        }
        return "redirect:/courses";
    }
}
```

## 11. CONTROLLER - LecturerController.java
```java
package iuh.fit.se.controllers;

import iuh.fit.se.entities.Lecturer;
import iuh.fit.se.services.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/lecturers")
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @GetMapping
    public String list(Model model) {
        List<Lecturer> lecturers = lecturerService.findAll();
        model.addAttribute("lecturers", lecturers);
        return "lecturer-list";
    }
}
```

## 12. MAIN APPLICATION - Demo01Application.java
```java
package iuh.fit.se;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Demo01Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo01Application.class, args);
    }

}
```

## 13. VIEW - course-list.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách Khóa học</title>
</head>
<body>
    <h1>THỐNG KÊ GIẢNG VIÊN THEO KHÓA HỌC</h1>
    <p><strong>SoMay:</strong> PC01 | <strong>Họ tên:</strong> NGUYEN VAN A | <strong>MSSV:</strong> 21012345</p>
    <hr>

    <!-- THÔNG BÁO -->
    <div th:if="${successMessage}">
        <p style="color: green;" th:text="${successMessage}"></p>
    </div>

    <div th:if="${errorMessage}">
        <p style="color: red;" th:text="${errorMessage}"></p>
    </div>

    <div th:if="${error}">
        <p style="color: red;" th:text="${error}"></p>
    </div>

    <p><a th:href="@{/courses/add}">Thêm mới Khóa học</a></p>

    <h2>Danh sách Khóa học</h2>

    <table border="1" cellpadding="5" cellspacing="0">
        <thead>
            <tr>
                <th>STT</th>
                <th>Mã Khóa học</th>
                <th>Tên Khóa học</th>
                <th>Số tín chỉ</th>
                <th>Số lượng GV</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <tr th:each="course, iterStat : ${courses}">
                <td th:text="${iterStat.count}">1</td>
                <td th:text="${course.courseCode}">CS101</td>
                <td th:text="${course.courseName}">Tên khóa học</td>
                <td th:text="${course.credit}">3</td>
                <td th:text="${countMap[course.id]}">0</td>
                <td>
                    <a th:href="@{/courses/lecturers/{id}(id=${course.id})}">Xem GV</a> |
                    <a th:href="@{/courses/delete/{id}(id=${course.id})}" 
                       onclick="return confirm('Bạn có chắc muốn xóa khóa học này?')">Xóa</a>
                </td>
            </tr>
            <tr th:if="${courses == null or courses.isEmpty()}">
                <td colspan="6">Chưa có khóa học nào.</td>
            </tr>
        </tbody>
    </table>
</body>
</html>
```

## 14. VIEW - course-add.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm Khóa học</title>
</head>
<body>
    <h1>THÊM KHÓA HỌC MỚI</h1>
    <p><strong>SoMay:</strong> PC01 | <strong>Họ tên:</strong> NGUYEN VAN A | <strong>MSSV:</strong> 21012345</p>
    <hr>

    <!-- THÔNG BÁO LỖI -->
    <div th:if="${error}">
        <p style="color: red;" th:text="${error}"></p>
    </div>

    <!-- FORM THÊM KHÓA HỌC -->
    <form th:action="@{/courses/save}" method="post" th:object="${course}">
        
        <table>
            <tr>
                <td><label for="courseCode">Mã khóa học *</label></td>
                <td>
                    <input type="text" 
                           th:field="*{courseCode}"
                           id="courseCode"
                           maxlength="20"
                           size="30"/>
                    <br>
                    <span th:if="${#fields.hasErrors('courseCode')}" 
                          th:errors="*{courseCode}" 
                          style="color: red; font-size: 12px;"></span>
                    <span th:unless="${#fields.hasErrors('courseCode')}" 
                          style="font-size: 12px; color: gray;">
                        (Chỉ gồm chữ IN HOA và số, tối đa 20 ký tự)
                    </span>
                </td>
            </tr>

            <tr>
                <td><label for="courseName">Tên khóa học *</label></td>
                <td>
                    <input type="text" 
                           th:field="*{courseName}"
                           id="courseName"
                           maxlength="100"
                           size="30"/>
                    <br>
                    <span th:if="${#fields.hasErrors('courseName')}" 
                          th:errors="*{courseName}" 
                          style="color: red; font-size: 12px;"></span>
                    <span th:unless="${#fields.hasErrors('courseName')}" 
                          style="font-size: 12px; color: gray;">
                        (Tối đa 100 ký tự)
                    </span>
                </td>
            </tr>

            <tr>
                <td><label for="credit">Số tín chỉ *</label></td>
                <td>
                    <input type="number" 
                           th:field="*{credit}"
                           id="credit"
                           min="1"
                           max="10"
                           size="10"/>
                    <br>
                    <span th:if="${#fields.hasErrors('credit')}" 
                          th:errors="*{credit}" 
                          style="color: red; font-size: 12px;"></span>
                    <span th:unless="${#fields.hasErrors('credit')}" 
                          style="font-size: 12px; color: gray;">
                        (Từ 1 đến 10)
                    </span>
                </td>
            </tr>

            <tr>
                <td colspan="2">
                    <br>
                    <button type="submit">Lưu khóa học</button>
                    <a th:href="@{/courses}"><button type="button">Hủy</button></a>
                </td>
            </tr>
        </table>
    </form>
</body>
</html>
```

## 15. VIEW - lecturer-list.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách Giảng viên</title>
</head>
<body>
    <h1>DANH SÁCH GIẢNG VIÊN</h1>
    <p><strong>SoMay:</strong> PC01 | <strong>Họ tên:</strong> NGUYEN VAN A | <strong>MSSV:</strong> 21012345</p>
    <hr>

    <!-- THÔNG TIN KHÓA HỌC -->
    <div th:if="${course != null}">
        <p>
            <strong>Khóa học:</strong> <span th:text="${course.courseName}">Tên khóa học</span><br>
            <strong>Mã:</strong> <span th:text="${course.courseCode}">CS101</span> | 
            <strong>Tín chỉ:</strong> <span th:text="${course.credit}">3</span> | 
            <strong>Số giảng viên:</strong> <span th:text="${lecturers != null ? lecturers.size() : 0}">0</span>
        </p>
    </div>

    <p><a th:href="@{/courses}">Quay lại Danh sách Khóa học</a></p>

    <h2>Danh sách Giảng viên</h2>

    <table border="1" cellpadding="5" cellspacing="0">
        <thead>
            <tr>
                <th>STT</th>
                <th>Mã Giảng viên</th>
                <th>Tên Giảng viên</th>
                <th>Email</th>
                <th>Khoa/Bộ môn</th>
            </tr>
        </thead>
        <tbody>
            <tr th:each="lecturer, iterStat : ${lecturers}">
                <td th:text="${iterStat.count}">1</td>
                <td th:text="${lecturer.lecturerCode}">LEC001</td>
                <td th:text="${lecturer.lecturerName}">Tên giảng viên</td>
                <td th:text="${lecturer.email != null ? lecturer.email : '-'}">email@example.com</td>
                <td th:text="${lecturer.department != null ? lecturer.department : '-'}">Khoa CNTT</td>
            </tr>
            <tr th:if="${lecturers == null or lecturers.isEmpty()}">
                <td colspan="5">Khóa học này chưa có giảng viên nào.</td>
            </tr>
        </tbody>
    </table>
</body>
</html>
```

## 16. TEST - Demo01ApplicationTests.java
```java
package iuh.fit.se;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Demo01ApplicationTests {

    @Test
    void contextLoads() {
    }

}
```
