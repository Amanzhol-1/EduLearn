package spring.educhainminiapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.educhainminiapp.model.Course;
import spring.educhainminiapp.model.User;
import spring.educhainminiapp.service.CourseService;
import spring.educhainminiapp.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    // Создать новый курс
    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        Course createdCourse = courseService.createCourse(course);
        return ResponseEntity.ok(createdCourse);
    }

    // Получить список всех курсов
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.findAll();
        return ResponseEntity.ok(courses);
    }

    // Получить детали курса по ID
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseDetails(@PathVariable Long courseId) {
        Course course = courseService.findById(courseId);
        return ResponseEntity.ok(course);
    }

    // Зарегистрироваться на курс
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<String> enrollInCourse(@PathVariable Long courseId, @RequestParam Long userId) {
        User user = userService.getUserById(userId);
        courseService.enrollUserInCourse(user, courseId);
        return ResponseEntity.ok("Вы успешно зарегистрировались на курс!");
    }

    // Получить курсы, на которые пользователь записан
    @GetMapping("/enrolled")
    public ResponseEntity<Set<Course>> getEnrolledCourses(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        Set<Course> enrolledCourses = user.getEnrolledCourses();
        return ResponseEntity.ok(enrolledCourses);
    }

    // Получить курсы, которые пользователь завершил
    @GetMapping("/completed")
    public ResponseEntity<Set<Course>> getCompletedCourses(@RequestParam Long userId) {
        User user = userService.getUserById(userId);
        Set<Course> completedCourses = user.getCompletedCourses();
        return ResponseEntity.ok(completedCourses);
    }
}
