package spring.educhainminiapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.educhainminiapp.model.User;
import spring.educhainminiapp.repository.UserRepository;
import spring.educhainminiapp.service.TelegramAuthService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Контроллер для обработки авторизации через Telegram Mini App.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final TelegramAuthService telegramAuthService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public AuthController(TelegramAuthService telegramAuthService, UserRepository userRepository) {
        this.telegramAuthService = telegramAuthService;
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
    }

    @PostMapping("/telegram")
    public ResponseEntity<?> telegramAuth(@RequestBody Map<String, String> requestBody, HttpSession session) {
        String initData = requestBody.get("initData");
        if (initData == null || initData.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Параметр 'initData' отсутствует"));
        }

        // Парсим initData в Map параметров
        Map<String, String> params = telegramAuthService.parseInitData(initData);

        String userData = params.get("user");
        String hash = params.get("hash");
        String authDateStr = params.get("auth_date");

        // Проверка наличия необходимых параметров
        if (userData == null || userData.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Параметр 'user' отсутствует"));
        }

        if (hash == null || hash.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Параметр 'hash' отсутствует"));
        }

        if (authDateStr == null || authDateStr.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Параметр 'auth_date' отсутствует"));
        }

        try {
            // Проверка подлинности initData
            boolean isValid = telegramAuthService.checkAuthorization(initData);
            if (!isValid) {
                return ResponseEntity.ok(Map.of("success", false, "message", "Неверные данные авторизации"));
            }

            // Декодируем и парсим userData
            String decodedUserData = URLDecoder.decode(userData, StandardCharsets.UTF_8.name());
            Map<String, Object> authData = objectMapper.readValue(decodedUserData, new TypeReference<Map<String, Object>>() {});

            Long id = Long.parseLong(authData.get("id").toString());
            String firstName = authData.getOrDefault("first_name", "").toString();
            String lastName = authData.getOrDefault("last_name", "").toString();
            String username = authData.getOrDefault("username", "").toString();
            String photoUrl = authData.getOrDefault("photo_url", "").toString();

            // Сохраняем пользователя в базе данных
            User user = userRepository.findById(id).orElse(new User());
            user.setId(id);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            user.setPhotoUrl(photoUrl);
            userRepository.save(user);

            // Сохраняем пользователя в сессии
            session.setAttribute("user", user);

            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(Map.of("success", false, "message", "Ошибка при обработке данных авторизации"));
        }
    }
}
