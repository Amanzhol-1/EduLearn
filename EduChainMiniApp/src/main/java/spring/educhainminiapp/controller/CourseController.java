package spring.educhainminiapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.educhainminiapp.model.Course;
import spring.educhainminiapp.model.User;
import spring.educhainminiapp.service.CourseService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/courses")
public class CourseController extends BaseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Получить список всех курсов
    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses(HttpSession session) {
        // Проверяем, что пользователь авторизован
        getCurrentUser(session);
        List<Course> courses = courseService.findAll();
        return ResponseEntity.ok(courses);
    }

    // Получить детали курса по ID
    @GetMapping("/{courseId}")
    public ResponseEntity<Course> getCourseDetails(@PathVariable Long courseId, HttpSession session) {
        getCurrentUser(session);
        Course course = courseService.findById(courseId);
        return ResponseEntity.ok(course);
    }

    // Зарегистрироваться на курс
    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<String> enrollInCourse(@PathVariable Long courseId, HttpSession session) {
        User user = getCurrentUser(session);
        courseService.enrollUserInCourse(user, courseId);
        return ResponseEntity.ok("Вы успешно зарегистрировались на курс!");
    }

    // Получить курсы, на которые пользователь записан
    @GetMapping("/enrolled")
    public ResponseEntity<Set<Course>> getEnrolledCourses(HttpSession session) {
        User user = getCurrentUser(session);
        Set<Course> enrolledCourses = user.getEnrolledCourses();
        return ResponseEntity.ok(enrolledCourses);
    }

    // Получить курсы, которые пользователь завершил
    @GetMapping("/completed")
    public ResponseEntity<Set<Course>> getCompletedCourses(HttpSession session) {
        User user = getCurrentUser(session);
        Set<Course> completedCourses = user.getCompletedCourses();
        return ResponseEntity.ok(completedCourses);
    }
}
