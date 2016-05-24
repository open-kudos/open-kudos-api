package kudos.services;

import kudos.exceptions.BusinessException;
import kudos.exceptions.InvalidChallengeStatusException;
import kudos.model.Challenge;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.repositories.ChallengeRepository;
import kudos.web.exceptions.UserException;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
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

    public Challenge create(User participant, String name, String description, String finishDate, int amount) throws BusinessException, UserException {

        String userEmail = usersService.getLoggedUser().get().getEmail();
        kudosService.reduceFreeKudos(usersService.getLoggedUser().get(), amount, name);
        return challengeRepository.save(
                new Challenge(
                        userEmail,
                        participant.getEmail(),
                        name,
                        description,
                        LocalDateTime.now().toString(dateTimeFormatter),
                        finishDate,
                        amount, Challenge.Status.CREATED
                )
        );
    }

    public Optional<Challenge> getChallenge(String id) {
        return Optional.ofNullable(challengeRepository.findChallengeById(id));
    }

    public Challenge accept(Challenge challenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedFailedCanceledOrAccepted(challenge);
        kudosService.reduceFreeKudos(usersService.getLoggedUser().get(), challenge.getAmount(), "");
        return setStatusAndSave(challenge, Challenge.Status.ACCEPTED);
    }

    public Challenge decline(Challenge challenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedFailedCanceledOrAccepted(challenge);
        kudosService.retrieveSystemKudos(usersService.findByEmail(challenge.getCreator()).get(), challenge.getAmount(), challenge.getName(), Transaction.Status.DECLINED_CHALLENGE);
        return setStatusAndSave(challenge, Challenge.Status.DECLINED);
    }

    public Challenge cancel(Challenge challenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedFailedCanceledOrAccepted(challenge);
        kudosService.retrieveSystemKudos(usersService.findByEmail(challenge.getCreator()).get(), challenge.getAmount(), challenge.getName(), Transaction.Status.CANCELED_CHALLENGE);
        return setStatusAndSave(challenge, Challenge.Status.CANCELED);
    }

    public Challenge accomplish(Challenge challenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedFailedOrCanceled(challenge);

        if (challenge.getParticipantStatus() == null) {
            return setCreatorStatusAndSave(challenge, challenge.getCreatorStatus());
        } else if (challenge.getCreatorStatus() == null) {
            return setParticipantStatusAndSave(challenge, challenge.getParticipantStatus());
        } else if (challenge.getCreatorStatus() == challenge.getParticipantStatus()) {
            challenge.setCreatorStatus(null);
            challenge.setParticipantStatus(null);
            setCreatorStatusAndSave(challenge, challenge.getCreatorStatus());
            return setParticipantStatusAndSave(challenge, challenge.getParticipantStatus());
        }
        System.out.println(checkWhoIsWinner(challenge));

        kudosService.takeSystemKudos(usersService.findByEmail(checkWhoIsWinner(challenge)).get(), 2 * challenge.getAmount(), challenge.getName(), Transaction.Status.COMPLETED_CHALLENGE);
        return setStatusAndSave(challenge, Challenge.Status.ACCOMPLISHED);
    }

    public Challenge fail(Challenge challenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedFailedOrCanceled(challenge);
        kudosService.retrieveSystemKudos(usersService.findByEmail(challenge.getCreator()).get(), challenge.getAmount(), challenge.getName(), Transaction.Status.FAILED_CHALENGE);
        return setStatusAndSave(challenge, Challenge.Status.FAILED);
    }

    public List<Challenge> getAllUserParticipatedChallengesByStatus(Challenge.Status status) throws UserException {
        return challengeRepository.findAllChallengesByParticipantAndStatus(usersService.getLoggedUser().get().getEmail(), status);
    }

    public List<Challenge> getAllUserParticipatedChallengesByStatusPageable(Challenge.Status status, int page, int pageSize) throws UserException {
        return challengeRepository.findAllChallengesByParticipantAndStatus(usersService.getLoggedUser().get().getEmail(), status, new PageRequest(page, pageSize));
    }

    public List<Challenge> getAllUserCreatedChallengesByStatus(Challenge.Status status) throws UserException {
        return challengeRepository.findAllChallengesByCreatorAndStatus(usersService.getLoggedUser().get().getEmail(), status);
    }

    public List<Challenge> getAllUserCreatedChallenges() throws UserException {
        return challengeRepository.findChallengesByCreator(usersService.getLoggedUser().get().getEmail());
    }

    public List<Challenge> getAllUserParticipatedChallenges() throws UserException {
       return challengeRepository.findChallengesByParticipant(usersService.getLoggedUser().get().getEmail());
    }

    public List<Challenge> getAllAcceptedChallenges() {
        return challengeRepository.findAllChallengesByStatus(Challenge.Status.ACCEPTED);
    }

    public List<Challenge> getAllCreatedChallenges() {
        return challengeRepository.findAllChallengesByStatus(Challenge.Status.CREATED);
    }

    private void checkNotAccomplishedDeclinedFailedCanceledOrAccepted(Challenge challenge) throws InvalidChallengeStatusException {
        switch (challenge.getStatus()) {
            case ACCEPTED:
                throw new InvalidChallengeStatusException("challenge_already_accepted");
        }
        checkNotAccomplishedDeclinedFailedOrCanceled(challenge);
    }

    private void checkNotAccomplishedDeclinedFailedOrCanceled(Challenge challenge) throws InvalidChallengeStatusException {
        switch (challenge.getStatus()) {
            case ACCOMPLISHED:
                throw new InvalidChallengeStatusException("challenge_already_accomplished");
            case DECLINED:
                throw new InvalidChallengeStatusException("challenge_already_declined");
            case FAILED:
                throw new InvalidChallengeStatusException("challenge_already_failed");
            case CANCELED:
                throw new InvalidChallengeStatusException("challenge_already_canceled");
        }
    }

    private String checkWhoIsWinner(Challenge challenge) {
        if (challenge.getCreatorStatus() && !challenge.getParticipantStatus()) {
            return challenge.getCreator();
        }
        return challenge.getParticipant();
    }

    private Challenge setStatusAndSave(Challenge challenge, Challenge.Status status) {
        Challenge databaseChallenge = challengeRepository.findChallengeById(challenge.getId());
        databaseChallenge.setStatus(status);
        return challengeRepository.save(databaseChallenge);
    }

    private Challenge setCreatorStatusAndSave(Challenge challenge, Boolean status) {
        Challenge databaseChallenge = challengeRepository.findChallengeById(challenge.getId());
        databaseChallenge.setCreatorStatus(status);
        return challengeRepository.save(databaseChallenge);
    }

    private Challenge setParticipantStatusAndSave(Challenge challenge, Boolean status) {
        Challenge databaseChallenge = challengeRepository.findChallengeById(challenge.getId());
        databaseChallenge.setParticipantStatus(status);
        return challengeRepository.save(databaseChallenge);
    }

}
