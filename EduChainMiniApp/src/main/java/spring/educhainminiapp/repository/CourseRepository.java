package spring.educhainminiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.educhainminiapp.model.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {
    // Дополнительные методы, если необходимо
}
