package spring.educhainminiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.educhainminiapp.model.Assignment;
import spring.educhainminiapp.model.User;
import spring.educhainminiapp.model.UserAssignment;

import java.util.Optional;

public interface UserAssignmentRepository extends JpaRepository<UserAssignment, Long> {
    Optional<UserAssignment> findByUserAndAssignment(User user, Assignment assignment);
}
