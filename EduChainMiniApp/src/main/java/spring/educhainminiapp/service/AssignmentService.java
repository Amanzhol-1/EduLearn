package spring.educhainminiapp.service;

import org.springframework.stereotype.Service;
import spring.educhainminiapp.model.*;
import spring.educhainminiapp.repository.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final UserAssignmentRepository userAssignmentRepository;
    private final LevelService levelService;
    private final UserRepository userRepository;
    private final CourseService courseService;
    private final SectionRepository sectionRepository;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             UserAssignmentRepository userAssignmentRepository,
                             LevelService levelService,
                             UserRepository userRepository,
                             CourseService courseService,
                             SectionRepository sectionRepository) {
        this.assignmentRepository = assignmentRepository;
        this.userAssignmentRepository = userAssignmentRepository;
        this.levelService = levelService;
        this.userRepository = userRepository;
        this.courseService = courseService;
        this.sectionRepository = sectionRepository;
    }

    public Assignment findBySectionId(Long sectionId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Секция не найдена"));
        return section.getAssignment();
    }

    public void submitAssignment(Long userId, Long assignmentId, String userAnswer) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Задание не найдено"));

        // Проверяем, не было ли уже отправлено решение
        Optional<UserAssignment> existing = userAssignmentRepository.findByUserAndAssignment(user, assignment);
        if (existing.isPresent()) {
            throw new RuntimeException("Вы уже отправили ответ на это задание");
        }

        // Проверяем ответ
        boolean isCorrect = assignment.getAnswer().equalsIgnoreCase(userAnswer.trim());

        // Создаём запись о выполнении задания
        UserAssignment userAssignment = new UserAssignment();
        userAssignment.setUser(user);
        userAssignment.setAssignment(assignment);
        userAssignment.setUserAnswer(userAnswer);
        userAssignment.setCorrect(isCorrect);
        userAssignment.setCompletionDate(new Date());
        userAssignmentRepository.save(userAssignment);

        if (isCorrect) {
            // Начисляем опыт и токены
            levelService.addExperience(user, assignment.getExpReward());
            user.setTokens(user.getTokens() + assignment.getRewardTokens());
            userRepository.save(user);

            // Помечаем секцию как завершённую
            completeSection(user, assignment.getSection());
        }
    }

    private void completeSection(User user, Section section) {
        if (!user.getCompletedSections().contains(section)) {
            user.getCompletedSections().add(section);
            userRepository.save(user);

            // Проверяем завершение курса
            courseService.checkCourseCompletion(user, section.getCourse());
        }
    }
}

