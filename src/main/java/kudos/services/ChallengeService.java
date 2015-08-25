package kudos.services;

import kudos.exceptions.BusinessException;
import kudos.exceptions.InvalidChallengeStatusException;
import kudos.model.Challenge;
import kudos.model.User;
import kudos.repositories.ChallengeRepository;
import kudos.web.exceptions.UserException;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by chc on 15.8.7.
 */
@Service
@Scope("prototype")
public class ChallengeService {

    private ChallengeRepository challengeRepository;
    private KudosService kudosService;
    private UsersService usersService;

    @Autowired
    @Qualifier(value = "DBTimeFormatter")
    DateTimeFormatter dateTimeFormatter;

    @Autowired
    public ChallengeService(ChallengeRepository challengeRepository, KudosService kudosService, UsersService usersService) {
        this.challengeRepository = challengeRepository;
        this.kudosService = kudosService;
        this.usersService = usersService;
    }

    public Challenge save(Challenge challenge){
        return challengeRepository.save(challenge);
    }

    public Challenge create(User participant, User referee, String name, LocalDateTime finishDate, int amount) throws BusinessException, UserException {

        String userEmail = usersService.getLoggedUser().get().getEmail();
        kudosService.reduceFreeKudos(usersService.getLoggedUser().get(), amount, name);
        return challengeRepository.save(
                new Challenge(
                        userEmail,
                        participant.getEmail(),
                        referee.getEmail(),
                        name,
                        LocalDateTime.now().toString(dateTimeFormatter),
                        finishDate.toString(dateTimeFormatter), amount, Challenge.Status.CREATED
                )
        );
    }

    public Optional<Challenge> getChallenge(String id) {
        return Optional.ofNullable(challengeRepository.findChallengeById(id));
    }

    public Challenge accept(Challenge challenge) throws InvalidChallengeStatusException {
        checkNotAccomplishedDeclinedFailedOrAccepted(challenge);
        return setStatusAndSave(challenge, Challenge.Status.ACCEPTED);
    }

    public Challenge decline(Challenge challenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedFailedOrAccepted(challenge);
        kudosService.retrieveSystemKudos(usersService.findByEmail(challenge.getCreator()).get(), challenge.getAmount(), challenge.getName());
        return setStatusAndSave(challenge, Challenge.Status.DECLINED);
    }

    public Challenge accomplish(Challenge challenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedOrFailed(challenge);
        kudosService.takeSystemKudos(usersService.findByEmail(challenge.getParticipant()).get(), challenge.getAmount(), challenge.getName());
        return setStatusAndSave(challenge, Challenge.Status.ACCOMPLISHED);
    }

    public Challenge fail(Challenge challenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedOrFailed(challenge);
        kudosService.retrieveSystemKudos(usersService.findByEmail(challenge.getCreator()).get(), challenge.getAmount(), challenge.getName());
        return setStatusAndSave(challenge, Challenge.Status.FAILED);
    }

    public List<Challenge> getAllUserCreatedChallenges() throws UserException {
        return challengeRepository.findChallengesByCreator(usersService.getLoggedUser().get().getEmail());
    }

    public List<Challenge> getAllUserParticipatedChallenges() throws UserException {
       return challengeRepository.findChallengesByParticipant(usersService.getLoggedUser().get().getEmail());
    }

    public List<Challenge> getAllUserReferredChallenges() throws UserException {
        return challengeRepository.findAllChallengesByReferee(usersService.getLoggedUser().get().getEmail());
    }

    public List<Challenge> getAllAcceptedChallenges() {
        return challengeRepository.findAllChallengesByStatus(Challenge.Status.ACCEPTED);
    }

    public List<Challenge> getAllCreatedChallenges() {
        return challengeRepository.findAllChallengesByStatus(Challenge.Status.CREATED);
    }

    private void checkNotAccomplishedDeclinedFailedOrAccepted(Challenge challenge) throws InvalidChallengeStatusException {
        switch (challenge.getStatus()) {
            case ACCEPTED:
                throw new InvalidChallengeStatusException("challenge_already_accepted");
        }
        checkNotAccomplishedDeclinedOrFailed(challenge);
    }

    private void checkNotAccomplishedDeclinedOrFailed(Challenge challenge) throws InvalidChallengeStatusException {
        switch (challenge.getStatus()) {
            case ACCOMPLISHED:
                throw new InvalidChallengeStatusException("challenge_already_accomplished");
            case DECLINED:
                throw new InvalidChallengeStatusException("challenge_already_declined");
            case FAILED:
                throw new InvalidChallengeStatusException("challenge_already_failed");
        }
    }

    private Challenge setStatusAndSave(Challenge challenge, Challenge.Status status) {
        Challenge databaseChallenge = challengeRepository.findChallengeById(challenge.getId());
        databaseChallenge.setStatus(status);
        return challengeRepository.save(databaseChallenge);
    }

}
