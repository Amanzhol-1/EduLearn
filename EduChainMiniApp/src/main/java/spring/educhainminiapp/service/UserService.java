package spring.educhainminiapp.service;

import org.springframework.stereotype.Service;
import spring.educhainminiapp.model.User;
import spring.educhainminiapp.repository.UserRepository;

import java.util.HashSet;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final LevelService levelService;

    public UserService(UserRepository userRepository, LevelService levelService) {
        this.userRepository = userRepository;
        this.levelService = levelService;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseGet(() -> {
                    // Если пользователь не найден, создаём нового
                    User newUser = new User();
                    newUser.setId(id);
                    newUser.setLevel(1);
                    newUser.setExp(0);
                    newUser.setTotalExp(0);
                    newUser.setTokens(0);
                    newUser.setEnrolledCourses(new HashSet<>());
                    newUser.setCompletedCourses(new HashSet<>());
                    newUser.setCompletedSections(new HashSet<>());
                    return userRepository.save(newUser);
                });
    }

    public void updateUserInfo(User user, String firstName, String lastName, String username, String photoUrl) {
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPhotoUrl(photoUrl);
        userRepository.save(user);
    }

    // Дополнительные методы для управления пользователями
}
