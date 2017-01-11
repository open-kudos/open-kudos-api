package kudos.services;

import kudos.model.User;
import kudos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LevelUpService {

    private static int MAX_LEVEL = 50;

    @Autowired
    UserRepository userRepository;

    public void increaseExperience(User user, int points) {
        user.setExperiencePoints(user.getExperiencePoints() + points * 2);
        checkUserExperience(user);
        userRepository.save(user);
    }

    private void checkUserExperience(User user) {
        if (user.getExperiencePoints() >= user.getExperiencePointsToLevelUp()) {
            levelUp(user);
        }
    }

    private void levelUp(User user) {
        if (user.getLevel() <= MAX_LEVEL) {
            user.setLevel(user.getLevel() + 1);
            user.setPreviousLevelExperiencePoints(user.getExperiencePointsToLevelUp());
            user.setExperiencePointsToLevelUp(getExperiencePointsToLevelUp(user.getLevel(), user.getExperiencePointsToLevelUp()));
        }
    }

    private int getExperiencePointsToLevelUp(int level, int experienceToLevelUp) {
        if (level < 10) {
            return experienceToLevelUp * 2;
        } else if (level < 20) {
            return experienceToLevelUp * 3;
        } else if (level <= 30) {
            return experienceToLevelUp * 4;
        } else {
            return experienceToLevelUp * 5;
        }
    }

}
