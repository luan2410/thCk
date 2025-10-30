package iuh.fit.se.controllers;

import iuh.fit.se.entities.Course;
import iuh.fit.se.services.CourseService;
import iuh.fit.se.services.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String save(@ModelAttribute("course") Course course, Model model) {
        try {
            courseService.save(course);
            return "redirect:/courses";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("course", course);
            return "course-add";
        } catch (Exception ex) {
            model.addAttribute("error", "Đã xảy ra lỗi không xác định!");
            model.addAttribute("course", course);
            return "course-add";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        Course c = courseService.findById(id);
        if (c != null && c.getLecturers() != null && !c.getLecturers().isEmpty()) {
            model.addAttribute("error", "Không thể xóa! Khóa học đã có giảng viên.");
            model.addAttribute("courses", courseService.findAll());
            return "course-list";
        }

        courseService.delete(id);
        return "redirect:/courses";
    }
}
