package kudos.services;

import org.springframework.stereotype.Component;

@Component
public class ScheduledTasksService {


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

