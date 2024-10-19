package spring.educhainminiapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.educhainminiapp.model.Section;
import spring.educhainminiapp.service.SectionService;

import java.util.List;

@RestController
@RequestMapping("/api/sections")
public class SectionController extends BaseController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    // Получить секции курса
    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<Section>> getSectionsByCourse(@PathVariable Long courseId, HttpSession session) {
        getCurrentUser(session);
        List<Section> sections = sectionService.findByCourseId(courseId);
        return ResponseEntity.ok(sections);
    }

    // Получить детали секции
    @GetMapping("/{sectionId}")
    public ResponseEntity<Section> getSectionDetails(@PathVariable Long sectionId, HttpSession session) {
        getCurrentUser(session);
        Section section = sectionService.findById(sectionId);
        return ResponseEntity.ok(section);
    }
}

