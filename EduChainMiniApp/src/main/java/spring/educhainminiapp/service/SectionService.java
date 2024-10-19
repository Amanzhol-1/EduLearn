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
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Курс не найден"));
        Section section = new Section();
        section.setTitle(title);
        section.setContent(content);
        section.setCourseId(course.getId());
        geminiService.clearHistory();
        Assignment assignment = generateAssignment(section, course.getExpReward(), course.getRewardTokens());
        section.setAssignment(assignment);
        sectionRepository.save(section);
        assignment.setSectionId(section.getId());
        assignmentRepository.save(assignment);
        course.getSectionIds().add(section.getId());
        return section;
    }

    public Assignment generateAssignment(Section section, int expReward, int rewardTokens) {
        Assignment assignment = new Assignment();
        assignment.setExpReward(expReward);
        assignment.setRewardTokens(rewardTokens);
        String jsonResponse = geminiService.generateContent("Составь один вопрос по этому материалу = " + section.getContent() + " и напиши только этот вопрос после Question: и ответ после слова aNswer:" );
        try {
            ObjectMapper objectMapper = new ObjectMapper(); // Create an ObjectMapper instance
            JsonNode rootNode = objectMapper.readTree(jsonResponse); // Parse the JSON response

            JsonNode contentNode = rootNode.path("candidates").get(0).path("content");
            String text = contentNode.path("parts").get(0).path("text").asText();

            String[] parts = text.split("aNswer:");
            if (parts.length == 2) {
                String questionPart = parts[0].replace("Question:", "").trim();
                String answerPart = parts[1].trim();
                assignment.setQuestion(questionPart);
                assignment.setAnswer(answerPart);
            } else {
                throw new RuntimeException("Ответ от Gemini в неверном формате: " + text);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Gemini API response: " + e.getMessage(), e);
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
