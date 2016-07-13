package kudos.services;

import kudos.exceptions.BusinessException;
import kudos.exceptions.InvalidChallengeStatusException;
import kudos.model.Challenge;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.repositories.ChallengeRepository;
import kudos.web.beans.response.ChallengeResponse;
import kudos.web.exceptions.UserException;
import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Scope("prototype")
public class ChallengeService {

    private ChallengeRepository challengeRepository;
    private KudosService kudosService;
    private UsersService usersService;


    @Autowired
    private EmailService emailService;

    @Autowired
    @Qualifier(value = "DBTimeFormatter")
    DateTimeFormatter dateTimeFormatter;

    @Autowired
    public ChallengeService(ChallengeRepository challengeRepository, KudosService kudosService, UsersService usersService) {
        this.challengeRepository = challengeRepository;
        this.kudosService = kudosService;
        this.usersService = usersService;
    }

    public Challenge save(Challenge challenge) {
        return challengeRepository.save(challenge);
    }

    public Challenge create(String participantEmail, String name, String description, String finishDate, int amount) throws BusinessException, UserException, MessagingException {
        User participant = usersService.findByEmail(participantEmail).get();
        User creator = usersService.getLoggedUser().get();
        kudosService.reduceFreeKudos(usersService.getLoggedUser().get(), amount, name);

        Challenge challenge = new Challenge(
                creator,
                participant,
                name,
                description,
                LocalDateTime.now().toString(dateTimeFormatter),
                finishDate,
                amount, Challenge.Status.CREATED
        );

        emailService.generateEmailForNewChallenge(creator, participant, challenge);

        return save(challenge);
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
        User creator = usersService.findById(challenge.getCreatorUser().getId()).orElseThrow(() -> new UserException("receiver.not.exist"));
        if (!checkIfNewWeekStarted(challenge)) {
            kudosService.retrieveSystemKudos(creator, challenge.getAmount(), challenge.getName(), Transaction.Status.DECLINED_CHALLENGE);
        }
        return setStatusAndSave(challenge, Challenge.Status.DECLINED);
    }

    public Challenge cancel(Challenge challenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedFailedCanceledOrAccepted(challenge);
        User creator = usersService.findById(challenge.getCreatorUser().getId()).orElseThrow(() -> new UserException("receiver.not.exist"));
        if (!checkIfNewWeekStarted(challenge)) {
            kudosService.retrieveSystemKudos(creator, challenge.getAmount(), challenge.getName(), Transaction.Status.CANCELED_CHALLENGE);
        }
        return setStatusAndSave(challenge, Challenge.Status.CANCELED);
    }

    public Challenge accomplish(Challenge challenge) throws BusinessException, UserException, MessagingException {
        checkNotAccomplishedDeclinedFailedOrCanceled(challenge);

        if (challenge.getParticipantStatus() == null) {
            emailService.generateEmailForOngoingChallengeSelection(challenge);
            return setCreatorStatusAndSave(challenge, challenge.getCreatorStatus());
        } else if (challenge.getCreatorStatus() == null) {
            emailService.generateEmailForOngoingChallengeSelection(challenge);
            return setParticipantStatusAndSave(challenge, challenge.getParticipantStatus());
        } else if (challenge.getCreatorStatus() == challenge.getParticipantStatus()) {
            challenge.setCreatorStatus(null);
            challenge.setParticipantStatus(null);
            setCreatorStatusAndSave(challenge, challenge.getCreatorStatus());
            return setParticipantStatusAndSave(challenge, challenge.getParticipantStatus());
        }

        kudosService.takeSystemKudos(checkWhoIsWinner(challenge), 2 * challenge.getAmount(), challenge.getName(), Transaction.Status.COMPLETED_CHALLENGE);
        return setStatusAndSave(challenge, Challenge.Status.ACCOMPLISHED);
    }

    public Challenge expire(Challenge challenge) throws BusinessException, UserException {
        checkNotAccomplishedDeclinedFailedOrCanceled(challenge);
        if (!checkIfNewWeekStarted(challenge)) {
            kudosService.retrieveSystemKudos(challenge.getCreatorUser(), challenge.getAmount(), challenge.getName(), Transaction.Status.EXPIRED_CHALLENGE);
        }
        return setStatusAndSave(challenge, Challenge.Status.EXPIRED);
    }

    public List<Challenge> getAllUserParticipatedChallengesByStatus(Challenge.Status status) throws UserException {
        return challengeRepository.findAllChallengesByParticipantUserAndStatus(usersService.getLoggedUser().get(), status);
    }

    public List<Challenge> getAllUserParticipatedChallengesByStatusPageable(Challenge.Status status, int page, int pageSize) throws UserException {
        return challengeRepository.findAllChallengesByParticipantUserAndStatus(usersService.getLoggedUser().get(), status, new PageRequest(page, pageSize));
    }

    public List<Challenge> getAllUserCreatedChallengesByStatus(Challenge.Status status) throws UserException {
        return challengeRepository.findAllChallengesByCreatorUserAndStatus(usersService.getLoggedUser().get(), status);
    }

    public List<Challenge> getAllUserCreatedChallenges() throws UserException {
        return challengeRepository.findChallengesByCreatorUser(usersService.getLoggedUser().get());
    }

    public List<Challenge> getAllUserParticipatedChallenges() throws UserException {
        return challengeRepository.findChallengesByParticipantUser(usersService.getLoggedUser().get());
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

    public List<ChallengeResponse> getAllNewChallenges() throws UserException {
        List<Challenge> newChallenges = new ArrayList<>();
        newChallenges.addAll(challengeRepository.findAllChallengesByCreatorUserAndStatus(usersService.getLoggedUser().get(), Challenge.Status.CREATED));
        newChallenges.addAll(challengeRepository.findAllChallengesByParticipantUserAndStatus(usersService.getLoggedUser().get(), Challenge.Status.CREATED));
        return newChallenges.stream().map(ChallengeResponse::new).collect(Collectors.toList());
    }

    public List<ChallengeResponse> getAllOngoingChallenges() throws UserException {
        List<Challenge> ongoingChallenges = new ArrayList<>();
        ongoingChallenges.addAll(challengeRepository.findAllChallengesByCreatorUserAndStatus(usersService.getLoggedUser().get(), Challenge.Status.ACCEPTED));
        ongoingChallenges.addAll(challengeRepository.findAllChallengesByParticipantUserAndStatus(usersService.getLoggedUser().get(), Challenge.Status.ACCEPTED));
        return ongoingChallenges.stream().map(ChallengeResponse::new).collect(Collectors.toList());
    }

    public List<ChallengeResponse> getAllCompletedChallenges() throws UserException {
        List<Challenge> completedChallenges = new ArrayList<>();

        completedChallenges.addAll(challengeRepository.findAllChallengesByCreatorUserAndStatus(usersService.getLoggedUser().get(), Challenge.Status.ACCOMPLISHED));
        completedChallenges.addAll(challengeRepository.findAllChallengesByCreatorUserAndStatus(usersService.getLoggedUser().get(), Challenge.Status.CANCELED));
        completedChallenges.addAll(challengeRepository.findAllChallengesByCreatorUserAndStatus(usersService.getLoggedUser().get(), Challenge.Status.DECLINED));
        completedChallenges.addAll(challengeRepository.findAllChallengesByCreatorUserAndStatus(usersService.getLoggedUser().get(), Challenge.Status.EXPIRED));

        completedChallenges.addAll(challengeRepository.findAllChallengesByParticipantUserAndStatus(usersService.getLoggedUser().get(), Challenge.Status.ACCOMPLISHED));
        completedChallenges.addAll(challengeRepository.findAllChallengesByParticipantUserAndStatus(usersService.getLoggedUser().get(), Challenge.Status.CANCELED));
        completedChallenges.addAll(challengeRepository.findAllChallengesByParticipantUserAndStatus(usersService.getLoggedUser().get(), Challenge.Status.DECLINED));
        completedChallenges.addAll(challengeRepository.findAllChallengesByParticipantUserAndStatus(usersService.getLoggedUser().get(), Challenge.Status.EXPIRED));

        return completedChallenges.stream().map(ChallengeResponse::new).collect(Collectors.toList());
    }

    private void checkNotAccomplishedDeclinedFailedOrCanceled(Challenge challenge) throws InvalidChallengeStatusException {
        switch (challenge.getStatus()) {
            case ACCOMPLISHED:
                throw new InvalidChallengeStatusException("challenge_already_accomplished");
            case DECLINED:
                throw new InvalidChallengeStatusException("challenge_already_declined");
            case EXPIRED:
                throw new InvalidChallengeStatusException("challenge_already_expired");
            case CANCELED:
                throw new InvalidChallengeStatusException("challenge_already_canceled");
        }
    }

    private User checkWhoIsWinner(Challenge challenge) {
        if (challenge.getCreatorStatus() && !challenge.getParticipantStatus()) {
            return challenge.getCreatorUser();
        }
        return challenge.getParticipantUser();
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

    private boolean checkIfNewWeekStarted(Challenge challenge) {
        LocalDateTime startOfWeek = new LocalDateTime().withDayOfWeek(DateTimeConstants.MONDAY).withHourOfDay(0).withMinuteOfHour(0);
        return dateTimeFormatter.parseLocalDateTime(challenge.getCreateDateDate()).isBefore(startOfWeek);

    }
}
