package kudos.services;

import kudos.exceptions.UserException;
import kudos.model.Challenge;
import kudos.model.status.ChallengeStatus;
import kudos.repositories.ChallengeRepository;
import kudos.services.util.ChallengeUtil;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasksService {

    @Autowired
    private ChallengeRepository challengeRepository;

    private ChallengeUtil challengeUtil = new ChallengeUtil();

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void markTasksAsExpired() throws UserException {
        List<Challenge> createdChallenges = challengeRepository.findAllChallengesByStatus(ChallengeStatus.CREATED);
        List<Challenge> acceptedChallenges = challengeRepository.findAllChallengesByStatus(ChallengeStatus.ACCEPTED);

        for (Challenge challenge : createdChallenges) {
            if (challenge.getExpirationDate() != null && LocalDateTime.parse(challenge.getExpirationDate()).isBefore(LocalDateTime.now())) {
                challengeUtil.changeStatus(challenge, ChallengeStatus.EXPIRED);
            }
        }

        for (Challenge challenge : acceptedChallenges) {
            if (challenge.getExpirationDate() != null && LocalDateTime.parse(challenge.getExpirationDate()).isBefore(LocalDateTime.now())) {
                challengeUtil.changeStatus(challenge, ChallengeStatus.FAILED);
            }
        }

    }

}

