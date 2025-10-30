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

