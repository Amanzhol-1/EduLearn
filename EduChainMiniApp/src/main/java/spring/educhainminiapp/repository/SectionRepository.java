package spring.educhainminiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.educhainminiapp.model.Section;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByCourseId(Long courseId);
}
