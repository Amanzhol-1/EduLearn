package spring.educhainminiapp.service;

import org.springframework.stereotype.Service;
import spring.educhainminiapp.model.User;
import spring.educhainminiapp.repository.UserRepository;

@Service
public class LevelService {

    private final UserRepository userRepository;

    public LevelService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addExperience(User user, int exp) {
        user.setExp(user.getExp() + exp);
        user.setTotalExp(user.getTotalExp() + exp);

        int newLevel = calculateLevel(user.getExp());
        if (newLevel > user.getLevel()) {
            user.setLevel(newLevel);
            handleLevelUp(user, newLevel);
        }

        userRepository.save(user);
    }

    private int calculateLevel(int exp) {
        return (int) Math.floor(Math.sqrt(exp / 100));
    }

    private void handleLevelUp(User user, int newLevel) {
        int bonusTokens = newLevel * 10;
        user.setTokens(user.getTokens() + bonusTokens);
        // Дополнительные действия при повышении уровня
    }
}
