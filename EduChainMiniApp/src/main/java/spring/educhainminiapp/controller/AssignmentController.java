package spring.educhainminiapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.educhainminiapp.model.Assignment;
import spring.educhainminiapp.model.User;
import spring.educhainminiapp.service.AssignmentService;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController extends BaseController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    // Получить задание по ID секции
    @GetMapping("/section/{sectionId}")
    public ResponseEntity<Assignment> getAssignmentBySectionId(@PathVariable Long sectionId, HttpSession session) {
        getCurrentUser(session);
        Assignment assignment = assignmentService.findBySectionId(sectionId);
        return ResponseEntity.ok(assignment);
    }

    // Отправить ответ на задание
    @PostMapping("/{assignmentId}/submit")
    public ResponseEntity<String> submitAssignmentAnswer(@PathVariable Long assignmentId,
                                                         @RequestParam String userAnswer,
                                                         HttpSession session) {
        User user = getCurrentUser(session);
        assignmentService.submitAssignment(user.getId(), assignmentId, userAnswer);
        return ResponseEntity.ok("Ответ успешно отправлен!");
    }
}

