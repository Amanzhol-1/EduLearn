package spring.educhainminiapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.educhainminiapp.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    // Дополнительные методы поиска при необходимости
}
