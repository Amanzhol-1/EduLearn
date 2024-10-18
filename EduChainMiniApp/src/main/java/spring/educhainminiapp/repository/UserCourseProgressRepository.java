package spring.educhainminiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.educhainminiapp.model.Course;
import spring.educhainminiapp.model.User;
import spring.educhainminiapp.model.UserCourseProgress;

import java.util.Optional;

public interface UserCourseProgressRepository extends JpaRepository<UserCourseProgress, Long> {
    Optional<UserCourseProgress> findByUserAndCourse(User user, Course course);
}
