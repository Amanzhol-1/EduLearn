package spring.educhainminiapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.educhainminiapp.model.Assignment;
import spring.educhainminiapp.model.Section;
import spring.educhainminiapp.service.SectionService;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    // Создать новую секцию с заданием
    @PostMapping
    public ResponseEntity<Section> createSection(@RequestParam Long courseId,
                                                 @RequestParam String title,
                                                 @RequestParam String content,
                                                 @RequestBody Assignment assignment) {
        Section section = sectionService.createSection(courseId, title, content, assignment);
        return ResponseEntity.ok(section);
    }

    // Получить секции курса
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Section>> getSectionsByCourse(@PathVariable Long courseId) {
        List<Section> sections = sectionService.findByCourseId(courseId);
        return ResponseEntity.ok(sections);
    }

    // Получить детали секции
    @GetMapping("/{sectionId}")
    public ResponseEntity<Section> getSectionDetails(@PathVariable Long sectionId) {
        Section section = sectionService.findById(sectionId);
        return ResponseEntity.ok(section);
    }
}
