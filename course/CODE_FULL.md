# CODE ĐẦY ĐỦ - SPRING BOOT + THYMELEAF

## 1. ENTITY - Course.java
```java
package iuh.fit.se.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Mã không được trống!")
    @Size(max = 20)
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Chỉ chữ IN HOA và số!")
    @Column(name = "course_code", unique = true)
    private String courseCode;
    
    @NotBlank(message = "Tên bắt buộc!")
    @Size(max = 100)
    @Column(name = "course_name")
    private String courseName;
    
    @Min(1) @Max(10)
    private Integer credit;
    
    @ManyToMany(mappedBy = "courses")
    private List<Lecturer> lecturers = new ArrayList<>();
    
    public Course() {}
    
    // Getters/Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String c) { this.courseCode = c; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String c) { this.courseName = c; }
    public Integer getCredit() { return credit; }
    public void setCredit(Integer c) { this.credit = c; }
    public List<Lecturer> getLecturers() { return lecturers; }
    public void setLecturers(List<Lecturer> l) { this.lecturers = l; }
}
```

## 2. ENTITY - Lecturer.java
```java
package iuh.fit.se.entities;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "lecturers")
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "lecturer_code")
    private String lecturerCode;
    
    @Column(name = "lecturer_name")
    private String lecturerName;
    
    private String email;
    private String department;
    
    @ManyToMany
    @JoinTable(name = "lecturer_course",
        joinColumns = @JoinColumn(name = "lecturer_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses = new ArrayList<>();
    
    public Lecturer() {}
    
    // Getters/Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getLecturerCode() { return lecturerCode; }
    public void setLecturerCode(String l) { this.lecturerCode = l; }
    public String getLecturerName() { return lecturerName; }
    public void setLecturerName(String l) { this.lecturerName = l; }
    public String getEmail() { return email; }
    public void setEmail(String e) { this.email = e; }
    public String getDepartment() { return department; }
    public void setDepartment(String d) { this.department = d; }
    public List<Course> getCourses() { return courses; }
    public void setCourses(List<Course> c) { this.courses = c; }
}
```

## 3. REPOSITORY - CourseRepository.java
```java
package iuh.fit.se.repositories;
import iuh.fit.se.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    boolean existsByCourseCode(String courseCode);
}
```

## 4. REPOSITORY - LecturerRepository.java
```java
package iuh.fit.se.repositories;
import iuh.fit.se.entities.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, Integer> {
}
```

## 5. SERVICE - CourseService.java
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

## 6. SERVICE - CourseServiceImpl.java
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
    public CourseServiceImpl(CourseRepository repo) { this.repo = repo; }
    
    @Override
    public List<Course> findAll() { return repo.findAll(); }
    
    @Override
    public Course findById(int id) { return repo.findById(id).orElse(null); }
    
    @Override
    public void save(Course course) {
        if (course.getCourseCode() == null || course.getCourseCode().isBlank())
            throw new IllegalArgumentException("Mã không được trống!");
        if (!course.getCourseCode().matches("^[A-Z0-9]+$"))
            throw new IllegalArgumentException("Chỉ chữ IN HOA và số!");
        if (repo.existsByCourseCode(course.getCourseCode()))
            throw new IllegalArgumentException("Mã đã tồn tại!");
        if (course.getCourseName() == null || course.getCourseName().isBlank())
            throw new IllegalArgumentException("Tên bắt buộc!");
        if (course.getCredit() == null || course.getCredit() < 1 || course.getCredit() > 10)
            throw new IllegalArgumentException("Tín chỉ 1-10!");
        repo.save(course);
    }
    
    @Override
    public void delete(int id) { repo.deleteById(id); }
    
    @Override
    public boolean existsByCode(String code) { return repo.existsByCourseCode(code); }
}
```

## 7. SERVICE - LecturerService.java
```java
package iuh.fit.se.services;
import iuh.fit.se.entities.Lecturer;
import java.util.List;

public interface LecturerService {
    List<Lecturer> findByCourseId(int courseId);
}
```

## 8. SERVICE - LecturerServiceImpl.java
```java
package iuh.fit.se.services.impl;
import iuh.fit.se.entities.*;
import iuh.fit.se.repositories.CourseRepository;
import iuh.fit.se.services.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LecturerServiceImpl implements LecturerService {
    private final CourseRepository courseRepo;
    
    @Autowired
    public LecturerServiceImpl(CourseRepository r) { this.courseRepo = r; }
    
    @Override
    public List<Lecturer> findByCourseId(int id) {
        Course c = courseRepo.findById(id).orElse(null);
        return (c != null) ? c.getLecturers() : List.of();
    }
}
```

## 9. CONTROLLER - CourseController.java
```java
package iuh.fit.se.controllers;
import iuh.fit.se.entities.Course;
import iuh.fit.se.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
@RequestMapping("/courses")
public class CourseController {
    private final CourseService courseService;
    
    @Autowired
    public CourseController(CourseService s) { this.courseService = s; }
    
    @GetMapping
    public String list(Model model) {
        List<Course> courses = courseService.findAll();
        Map<Integer, Integer> countMap = new HashMap<>();
        for (Course c : courses) countMap.put(c.getId(), c.getLecturers().size());
        model.addAttribute("courses", courses);
        model.addAttribute("countMap", countMap);
        return "course-list";
    }
    
    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("course", new Course());
        return "course-add";
    }
    
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("course") Course course,
                      BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) return "course-add";
        try {
            courseService.save(course);
            ra.addFlashAttribute("successMessage", "Thêm thành công!");
            return "redirect:/courses";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            return "course-add";
        }
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes ra) {
        Course c = courseService.findById(id);
        if (c != null && !c.getLecturers().isEmpty()) {
            ra.addFlashAttribute("errorMessage", "Không thể xóa! Đã có GV.");
        } else {
            courseService.delete(id);
            ra.addFlashAttribute("successMessage", "Xóa thành công!");
        }
        return "redirect:/courses";
    }
    
    @GetMapping("/lecturers/{id}")
    public String viewLecturers(@PathVariable int id, Model model) {
        Course course = courseService.findById(id);
        model.addAttribute("course", course);
        model.addAttribute("lecturers", course.getLecturers());
        return "lecturer-list";
    }
}
```

## 10. VIEW - course-list.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head><meta charset="UTF-8"><title>Danh sách</title></head>
<body>
<h1>THỐNG KÊ GIẢNG VIÊN THEO KHÓA HỌC</h1>
<p><strong>SoMay:</strong> PC01 | <strong>Họ tên:</strong> NGUYEN VAN A | <strong>MSSV:</strong> 21012345</p>
<hr>
<p th:if="${successMessage}" style="color:green;" th:text="${successMessage}"></p>
<p th:if="${errorMessage}" style="color:red;" th:text="${errorMessage}"></p>
<p><a th:href="@{/courses/add}">Thêm mới</a></p>
<table border="1" cellpadding="5">
<tr><th>STT</th><th>Mã</th><th>Tên</th><th>Tín chỉ</th><th>Số GV</th><th>Hành động</th></tr>
<tr th:each="c, i : ${courses}">
<td th:text="${i.count}">1</td>
<td th:text="${c.courseCode}">CS101</td>
<td th:text="${c.courseName}">Tên</td>
<td th:text="${c.credit}">3</td>
<td th:text="${countMap[c.id]}">0</td>
<td>
<a th:href="@{/courses/lecturers/{id}(id=${c.id})}">Xem GV</a> |
<a th:href="@{/courses/delete/{id}(id=${c.id})}" onclick="return confirm('Xóa?')">Xóa</a>
</td>
</tr>
</table>
</body>
</html>
```

## 11. VIEW - course-add.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head><meta charset="UTF-8"><title>Thêm</title></head>
<body>
<h1>THÊM KHÓA HỌC</h1>
<p><strong>SoMay:</strong> PC01 | <strong>Họ tên:</strong> NGUYEN VAN A | <strong>MSSV:</strong> 21012345</p>
<hr>
<p th:if="${error}" style="color:red;" th:text="${error}"></p>
<form th:action="@{/courses/save}" method="post" th:object="${course}">
<table>
<tr><td>Mã *</td><td>
<input type="text" th:field="*{courseCode}" maxlength="20"/><br>
<span th:if="${#fields.hasErrors('courseCode')}" th:errors="*{courseCode}" style="color:red;"></span>
</td></tr>
<tr><td>Tên *</td><td>
<input type="text" th:field="*{courseName}" maxlength="100"/><br>
<span th:if="${#fields.hasErrors('courseName')}" th:errors="*{courseName}" style="color:red;"></span>
</td></tr>
<tr><td>Tín chỉ *</td><td>
<input type="number" th:field="*{credit}" min="1" max="10"/><br>
<span th:if="${#fields.hasErrors('credit')}" th:errors="*{credit}" style="color:red;"></span>
</td></tr>
<tr><td colspan="2">
<button type="submit">Lưu</button>
<a th:href="@{/courses}"><button type="button">Hủy</button></a>
</td></tr>
</table>
</form>
</body>
</html>
```

## 12. VIEW - lecturer-list.html
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head><meta charset="UTF-8"><title>GV</title></head>
<body>
<h1>DANH SÁCH GIẢNG VIÊN</h1>
<p><strong>SoMay:</strong> PC01 | <strong>Họ tên:</strong> NGUYEN VAN A | <strong>MSSV:</strong> 21012345</p>
<hr>
<p><strong>Khóa học:</strong> <span th:text="${course.courseName}"></span></p>
<p><a th:href="@{/courses}">Quay lại</a></p>
<table border="1" cellpadding="5">
<tr><th>STT</th><th>Mã</th><th>Tên</th><th>Email</th><th>Khoa</th></tr>
<tr th:each="l, i : ${lecturers}">
<td th:text="${i.count}">1</td>
<td th:text="${l.lecturerCode}">LEC001</td>
<td th:text="${l.lecturerName}">Tên</td>
<td th:text="${l.email} ?: '-'">email</td>
<td th:text="${l.department} ?: '-'">Khoa</td>
</tr>
</table>
</body>
</html>
```

## 13. application.properties
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/course_management_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.thymeleaf.cache=false
```

**ĐỔI:** password, thông tin sinh viên trong 3 file HTML!
