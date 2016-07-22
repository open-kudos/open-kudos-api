package kudos.services;

import kudos.exceptions.BusinessException;
import kudos.model.Challenge;
import kudos.exceptions.UserException;
import kudos.model.ChallengeStatus;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduledTasksService {

//    private Logger LOG = Logger.getLogger(ScheduledTasksService.class);
//
//    @Autowired
//    ChallengeService challengeService;
//
//    @Autowired
//    @Qualifier(value = "DBTimeFormatter")
//    DateTimeFormatter dateTimeFormatter;
//
//    @Scheduled(fixedRate = 1000 * 15)
//    public void markTasksAsExpired() {
//        List<Challenge> challengesToCheck = new ArrayList<>();
//        challengesToCheck.addAll(challengeService.getAllAcceptedChallenges());
//        challengesToCheck.addAll(challengeService.getAllCreatedChallenges());
//
//        LOG.info("challengesToCheck challenges amount is: " + challengesToCheck.size());
//
//        challengesToCheck.stream()
//                .filter(c -> !c.getStatus().equals(ChallengeStatus.EXPIRED))
//                .filter(c -> !(c.getExpirationDate() == null))
//                .filter(c -> dateTimeFormatter.parseLocalDateTime(c.getExpirationDate()).isBefore(LocalDateTime.now()))
//                .forEach((challenge) -> {
//                    try {
//                        challengeService.expire(challenge);
//                    } catch (BusinessException e) {
//                        e.printStackTrace();
//                    } catch (UserException e) {
//                        e.printStackTrace();
//                    }
//                });
//    }
}

