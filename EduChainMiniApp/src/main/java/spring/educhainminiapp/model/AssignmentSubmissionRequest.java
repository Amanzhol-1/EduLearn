package spring.educhainminiapp.model;

import lombok.Data;

@Data
public class AssignmentSubmissionRequest {
    private Long assignmentId;
    private Long userId;
    private String userAnswer;
    private Long sectionId;
}
