package spring.educhainminiapp.service;

import spring.educhainminiapp.model.Assignment;
import org.springframework.stereotype.Service;
import spring.educhainminiapp.model.Course;
import spring.educhainminiapp.model.Section;
import spring.educhainminiapp.repository.AssignmentRepository;
import spring.educhainminiapp.repository.CourseRepository;
import spring.educhainminiapp.repository.SectionRepository;

import java.util.List;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final AssignmentRepository assignmentRepository;
    private final CourseRepository courseRepository;
    private final GeminiService geminiService;


    public SectionService(SectionRepository sectionRepository, AssignmentRepository assignmentRepository,
                          CourseRepository courseRepository, GeminiService geminiService, CourseService courseService) {
        this.sectionRepository = sectionRepository;
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
        this.geminiService = geminiService;
    }

    public Section createSection(Long courseId, String title, String content) {
        // Получаем курс по courseId
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Курс не найден"));

        Section section = new Section();
        section.setTitle(title);
        section.setContent(content);
        section.setCourse(course);
        geminiService.clearHistory();
        Assignment assignment = generateAssignment(section, 2, 1);
        assignmentRepository.save(assignment);
        section.setAssignment(assignment);
        course.getSections().add(section);
        return sectionRepository.save(section);
    }

    public Assignment generateAssignment(Section section,int expReward,int rewardTokens) {
        Assignment assignment = new Assignment();
        assignment.setSection(section);
        assignment.setExpReward(expReward);
        assignment.setRewardTokens(rewardTokens);
        String query = geminiService.generateContent("Составь один вопрос по этому материалу и напиши только этот вопрос после Question: и ответ после слова aNswer:" + section.getContent());
        String[] parts = query.split("aNswer:");
        if (parts.length == 2) {
            // Извлекаем вопрос и ответ
            String questionPart = parts[0].replace("Question:", "").trim();
            String answerPart = parts[1].trim();
            // Устанавливаем значения переменных
            String question = questionPart;
            String answer = answerPart;
            assignment.setQuestion(question);
            assignment.setAnswer(answer);
            // Вы можете использовать значения question и answer дальше в своем коде
        } else {
            // Если строка не содержит разделения или имеет некорректный формат
            throw new RuntimeException("Ответ от Gemini в неверном формате: " + query);
        }
        return assignment;
    }

    public List<Section> findByCourseId(Long courseId) {
        return sectionRepository.findByCourseId(courseId);
    }

    public Section findById(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Секция не найдена"));
    }
}