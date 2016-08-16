package kudos.services;

import kudos.model.Challenge;
import kudos.model.ChallengeStatus;
import kudos.repositories.ChallengeRepository;
import org.joda.time.LocalDateTime;
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
    public void markTasksAsExpired() {
        List<Challenge> challengeList = challengeRepository.findAllChallengesByStatus(ChallengeStatus.CREATED);
        challengeList.addAll(challengeRepository.findAllChallengesByStatus(ChallengeStatus.ACCEPTED));
        challengeList.stream().filter(challenge -> challenge.getExpirationDate() != null && LocalDateTime.parse(challenge.getExpirationDate()).isBefore(LocalDateTime.now())).forEach(challenge -> {
            challengeService.markChallengeAsExpired(challenge);
        });

    }
}

