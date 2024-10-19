package spring.educhainminiapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.educhainminiapp.model.Section;
import spring.educhainminiapp.model.User;
import spring.educhainminiapp.repository.UserRepository;
import spring.educhainminiapp.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // Получить список всех пользователей
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    // Получить информацию о пользователе
    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    // Получить завершённые секции пользователя
    @GetMapping("/{userId}/completed-sections")
    public ResponseEntity<Set<Section>> getCompletedSections(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        Set<Section> completedSections = user.getCompletedSections();
        return ResponseEntity.ok(completedSections);
    }
}


