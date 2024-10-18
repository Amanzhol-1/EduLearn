package spring.educhainminiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.educhainminiapp.model.Achievement;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    // Дополнительные методы, если необходимо
}
