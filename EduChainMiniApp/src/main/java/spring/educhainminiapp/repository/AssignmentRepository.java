package spring.educhainminiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.educhainminiapp.model.Assignment;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findBySectionId(Long sectionId);
}
