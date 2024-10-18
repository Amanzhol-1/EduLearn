package spring.educhainminiapp.controller;

import org.springframework.web.bind.annotation.*;
import spring.educhainminiapp.model.Course;
import spring.educhainminiapp.model.User;
import spring.educhainminiapp.service.CourseService;
import spring.educhainminiapp.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    // Получить список всех курсов
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.findAll();
    }

    // Получить детали курса по ID
    @GetMapping("/{courseId}")
    public Course getCourseDetails(@PathVariable Long courseId) {
        return courseService.findById(courseId);
    }

    // Зарегистрироваться на курс
    @PostMapping("/{courseId}/enroll")
    public String enrollInCourse(@PathVariable Long courseId, @RequestParam Long userId) {
        User user = userService.getUserById(userId);
        courseService.enrollUserInCourse(user, courseId);
        return "Вы успешно зарегистрировались на курс!";
    }

    // Дополнительные методы
}
