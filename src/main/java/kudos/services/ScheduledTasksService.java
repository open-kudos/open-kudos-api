package kudos.services;

import kudos.exceptions.BusinessException;
import kudos.exceptions.InvalidChallengeStatusException;
import kudos.model.Challenge;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by chc on 15.8.12.
 */
@Component
public class ScheduledTasksService {

    private Logger LOG = Logger.getLogger(ScheduledTasksService.class);

    @Autowired
    ChallengeService challengeService;

    @Autowired
    @Qualifier(value = "DBTimeFormatter")
    DateTimeFormatter dateTimeFormatter;

    @Scheduled(fixedRate = 1000 * 15)
    public void markTasksAsFailed() throws BusinessException {
        List<Challenge> acceptedChallenges = challengeService.getAllAcceptedChallenges();
        List<Challenge> createdChallenges = challengeService.getAllCreatedChallenges();

        LOG.info("accepted challenges amount is: "+acceptedChallenges.size()+" created challenges amount is: "+ createdChallenges.size());

        LOG.info("rate");
        for(Challenge challenge : acceptedChallenges){
            LocalDateTime challengeFinishTime = dateTimeFormatter.parseLocalDateTime(challenge.getFinishDate());
            Challenge.Status challengeStatus = challenge.getStatus();
            if(challengeFinishTime.isBefore(LocalDateTime.now())  && !challengeStatus.equals(Challenge.Status.FAILED)) {
                challengeService.fail(challenge);
            }
        }

        for(Challenge challenge : createdChallenges){
            LocalDateTime challengeFinishTime = dateTimeFormatter.parseLocalDateTime(challenge.getFinishDate());
            Challenge.Status challengeStatus = challenge.getStatus();
            if(challengeFinishTime.isBefore(LocalDateTime.now()) && !challengeStatus.equals(Challenge.Status.FAILED)) {
                LOG.info("created challenge was marked as failed");
                challengeService.fail(challenge);
            }
        }
    }
}

