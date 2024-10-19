package spring.educhainminiapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.educhainminiapp.dto.UserDto;
import spring.educhainminiapp.model.User;
import spring.educhainminiapp.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController extends BaseController {

    private final UserRepository userRepository;

    @Autowired
    public LeaderboardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Получить топ-10 пользователей с самым большим количеством токенов
    @GetMapping("/tokens")
    public ResponseEntity<List<UserDto>> getTopUsersByTokens(HttpSession session) {
        // Проверяем, что пользователь авторизован
        getCurrentUser(session);

        // Получаем топ-10 пользователей
        List<User> users = userRepository.findTop10ByOrderByTokensDesc();

        // Преобразуем список пользователей в список UserDto
        List<UserDto> userDtos = users.stream()
                .map(user -> new UserDto(user.getLevel(), user.getUsername(), user.getTokens()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }
}
