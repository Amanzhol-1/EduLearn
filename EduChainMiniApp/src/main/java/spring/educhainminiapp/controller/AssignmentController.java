package spring.educhainminiapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.educhainminiapp.model.Assignment;
import spring.educhainminiapp.service.AssignmentService;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // Получить задание по ID секции
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<Assignment> getAssignmentBySectionId(@PathVariable Long sectionId) {
        Assignment assignment = assignmentService.findBySectionId(sectionId);
        return ResponseEntity.ok(assignment);
    }

    // Отправить ответ на задание
    @PostMapping("/{assignmentId}/submit")
    public ResponseEntity<String> submitAssignmentAnswer(@PathVariable Long assignmentId,
                                                         @RequestParam Long userId,
                                                         @RequestParam String userAnswer) {
        assignmentService.submitAssignment(userId, assignmentId, userAnswer);
        return ResponseEntity.ok("Ответ успешно отправлен!");
    }
}
