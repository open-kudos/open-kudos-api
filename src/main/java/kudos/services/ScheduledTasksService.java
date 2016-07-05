package kudos.services;

import kudos.exceptions.BusinessException;
import kudos.model.Challenge;
import kudos.web.exceptions.UserException;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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
    public void markTasksAsFailed() {
        List<Challenge> challengesToCheck = new ArrayList<>();
        challengesToCheck.addAll(challengeService.getAllAcceptedChallenges());
        challengesToCheck.addAll(challengeService.getAllCreatedChallenges());

        LOG.info("challengesToCheck challenges amount is: " + challengesToCheck.size());

        challengesToCheck.stream()
                .filter(c -> !c.getStatus().equals(Challenge.Status.FAILED))
                .filter(c -> !(c.getFinishDate() == null))
                .filter(c -> dateTimeFormatter.parseLocalDateTime(c.getFinishDate()).isBefore(LocalDateTime.now()))
                .forEach((challenge) -> {
                    try {
                        challengeService.fail(challenge);
                    } catch (BusinessException e) {
                        e.printStackTrace();
                    } catch (UserException e) {
                        e.printStackTrace();
                    }
                });
    }
}

