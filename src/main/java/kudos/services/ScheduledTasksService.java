package kudos.services;

import kudos.exceptions.UserException;
import kudos.model.Challenge;
import kudos.model.ChallengeStatus;
import kudos.repositories.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledTasksService {

    @Autowired
    ChallengeService challengeService;

    @Autowired
    ChallengeRepository challengeRepository;

    @Scheduled(fixedRate = 1000 * 60 * 24)
    public void markTasksAsExpired() throws UserException {
        List<Challenge> createdChallenges = challengeRepository.findAllChallengesByStatus(ChallengeStatus.CREATED);
        List<Challenge> acceptedChallenges = challengeRepository.findAllChallengesByStatus(ChallengeStatus.ACCEPTED);

        for (Challenge challenge : createdChallenges) {
            challengeService.markChallengeAsExpiredOrFailed(challenge, ChallengeStatus.EXPIRED);
        }

        for (Challenge challenge : acceptedChallenges) {
            challengeService.markChallengeAsExpiredOrFailed(challenge, ChallengeStatus.FAILED);
        }

    }
}

