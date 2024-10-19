package spring.educhainminiapp.service;

import org.springframework.stereotype.Service;
import spring.educhainminiapp.model.*;
import spring.educhainminiapp.repository.*;

import java.util.Date;
import java.util.Optional;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final UserAssignmentRepository userAssignmentRepository;
    private final LevelService levelService;
    private final UserRepository userRepository;
    private final CourseService courseService;
    private final GeminiService geminiService;
    private final SectionService sectionService;

    public AssignmentService(AssignmentRepository assignmentRepository,
                             UserAssignmentRepository userAssignmentRepository,
                             LevelService levelService,
                             UserRepository userRepository,
                             CourseService courseService, GeminiService geminiService, SectionService sectionService) {
        this.assignmentRepository = assignmentRepository;
        this.userAssignmentRepository = userAssignmentRepository;
        this.levelService = levelService;
        this.userRepository = userRepository;
        this.courseService = courseService;
        this.geminiService = geminiService;
        this.sectionService = sectionService;
    }

    public Assignment findBySectionId(Long sectionId) {
        return assignmentRepository.findBySectionId(sectionId);
    }

    public void submitAssignment(Long sectionId, Long userId, Long assignmentId, String userAnswer) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Задание не найдено"));

        geminiService.clearHistory(); // new

        // Проверяем, не было ли уже отправлено решение
        Optional<UserAssignment> existing = userAssignmentRepository.findByUserAndAssignment(user, assignment);
        if (existing.isPresent()) {
            throw new RuntimeException("Вы уже отправили ответ на это задание");
        }

        // Проверяем ответ
        boolean isCorrect; // new
        if(!assignment.getQuestion().isBlank()) {
            isCorrect = checkCorrect(assignment.getAnswer(), userAnswer.trim());
        }
        else { // answer и question пустые
            isCorrect = checkCorrect(sectionService.findById(sectionId).getContent(), userAnswer.trim());
        } //

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

            // Проверяем завершение всех заданий в секции
            checkSectionCompletion(user, sectionService.findById(sectionId));
        }
    }

    private boolean checkCorrect(String answer, String userAnswer) { // new
        boolean result = false;
        String res = "";
        if(!answer.isBlank()){
            String prompt = "Сравни идею текста: " + userAnswer + ". И идею этого текста:" + answer + " равны ли они , если да напиши только true иначе напиши только false";
            res = geminiService.generateContent(prompt);
            if(res.contains("true")) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    } //

    private void checkSectionCompletion(User user, Section section) {
        Assignment assignment = assignmentRepository.findBySectionId(section.getId());
        boolean isAssignmentCompleted = userAssignmentRepository.findByUserAndAssignment(user, assignment)
                .map(UserAssignment::isCorrect)
                .orElse(false);

        if (isAssignmentCompleted) {
            // Добавляем секцию в список завершённых пользователем
            user.getCompletedSections().add(section);
            userRepository.save(user);

            // Проверяем завершение курса
            courseService.checkCourseCompletion(user, courseService.findById(section.getCourseId()));
        }
    }
}
