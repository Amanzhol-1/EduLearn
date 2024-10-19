package spring.educhainminiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.educhainminiapp.model.Assignment;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    Assignment findBySectionId(Long sectionId);
}
