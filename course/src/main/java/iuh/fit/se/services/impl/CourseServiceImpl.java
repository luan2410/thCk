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
