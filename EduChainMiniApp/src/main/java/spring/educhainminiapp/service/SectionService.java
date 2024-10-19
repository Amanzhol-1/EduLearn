package spring.educhainminiapp.service;

import org.springframework.stereotype.Service;
import spring.educhainminiapp.model.Assignment;
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

    public SectionService(SectionRepository sectionRepository, AssignmentRepository assignmentRepository,
                          CourseRepository courseRepository) {
        this.sectionRepository = sectionRepository;
        this.assignmentRepository = assignmentRepository;
        this.courseRepository = courseRepository;
    }

    public Section createSection(Long courseId, String title, String content, Assignment assignment) {
        // Получаем курс по courseId
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Курс не найден"));

        Section section = new Section();
        section.setTitle(title);
        section.setContent(content);
        section.setCourse(course);

        // Сохраняем задание
        Assignment savedAssignment = assignmentRepository.save(assignment);
        section.setAssignment(savedAssignment);

        return sectionRepository.save(section);
    }

    public List<Section> findByCourseId(Long courseId) {
        return sectionRepository.findByCourseId(courseId);
    }

    public Section findById(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Секция не найдена"));
    }
}

