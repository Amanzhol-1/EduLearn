package spring.educhainminiapp.service;

import org.springframework.stereotype.Service;
import spring.educhainminiapp.model.*;
import spring.educhainminiapp.repository.*;

import java.util.Date;
import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final LevelService levelService;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository,
                         SectionRepository sectionRepository,
                         LevelService levelService,
                         UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.levelService = levelService;
        this.userRepository = userRepository;
    }

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course findById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Курс не найден"));
    }

    public void enrollUserInCourse(User user, Long courseId) {
        Course course = findById(courseId);

        // Проверяем, не зарегистрирован ли пользователь уже на этот курс
        if (user.getEnrolledCourses().contains(course)) {
            throw new RuntimeException("Вы уже зарегистрированы на этот курс");
        }

        // Проверяем, соответствует ли уровень пользователя требуемому уровню курса
        if (user.getLevel() < course.getRequiredLevel()) {
            throw new RuntimeException("Ваш уровень недостаточен для записи на этот курс");
        }

        // Добавляем курс в список зарегистрированных курсов пользователя
        user.getEnrolledCourses().add(course);
        userRepository.save(user);
    }

    public void checkCourseCompletion(User user, Course course) {
        List<Section> courseSections = sectionRepository.findByCourseId(course.getId());

        boolean allSectionsCompleted = courseSections.stream()
                .allMatch(section -> user.getCompletedSections().contains(section));

        if (allSectionsCompleted) {
            // Проверяем, не завершил ли пользователь уже этот курс
            if (!user.getCompletedCourses().contains(course)) {
                // Добавляем курс в список завершённых курсов пользователя
                user.getCompletedCourses().add(course);

                // Начисляем опыт и токены за завершение курса
                levelService.addExperience(user, course.getExpReward());
                user.setTokens(user.getTokens() + course.getRewardTokens());
                userRepository.save(user);
            }
        }
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    // Дополнительные методы
}

