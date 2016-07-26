package kudos.services;

import kudos.exceptions.BusinessException;
import kudos.exceptions.InvalidChallengeStatusException;
import kudos.exceptions.InvalidKudosAmountException;
import kudos.model.*;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import kudos.web.beans.response.ChallengeActions;
import kudos.web.beans.response.ChallengeResponse;
import kudos.exceptions.UserException;
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
public class ChallengeService {

//
//
//    @Autowired
//    private EmailService emailService;
//
//    @Autowired
//    @Qualifier(value = "DBTimeFormatter")
//    DateTimeFormatter dateTimeFormatter;
//
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    public Challenge giveChallenge(User creator, User receiver, String name, String description, String expirationDate,
                                   int amount) throws UserException, InvalidKudosAmountException {
        if (creator.getEmail().equals(receiver.getEmail())){
            throw new UserException("cant_give_challenge_to_yourself");
        }

        if (amount < 1 || creator.getWeeklyKudos() < amount) {
            throw new InvalidKudosAmountException("invalid_kudos_amount");
        }

        if(expirationDate != null && LocalDateTime.parse(expirationDate).isBefore(LocalDateTime.now())) {
            throw new UserException("invalid_challenge_date");
        } else {
            expirationDate = LocalDateTime.now().plusYears(10).toString();
        }

        Transaction transaction = transactionRepository.save(new Transaction(creator, receiver, amount, name,
                TransactionType.CHALLENGE, LocalDateTime.now().toString(), TransactionStatus.PENDING));

        creator.setWeeklyKudos(creator.getWeeklyKudos() - amount);
        userRepository.save(creator);

        return challengeRepository.save(new Challenge(creator, receiver, name, description, transaction, expirationDate,
                ChallengeStatus.CREATED));
    }

    public Challenge getChallengeById(String id) throws UserException {
        Optional<Challenge> challenge = challengeRepository.findChallengeById(id);
        if(challenge.isPresent()) {
            return challenge.get();
        } else {
            throw new UserException("challenge_not_found");
        }
    }

    public void acceptChallenge(Challenge challenge, User user) throws UserException {
        checkIfCanAcceptOrDecline(challenge, user);
        challenge.setStatus(ChallengeStatus.ACCEPTED);
        challengeRepository.save(challenge);
    }

    public void declineChallenge(Challenge challenge, User user) throws UserException {
        checkIfCanAcceptOrDecline(challenge, user);
        //TODO create notification that challenge was declined

        //TODO check if decline happened in the same week and give back weekly kudos
        challengeRepository.delete(challenge);
    }

    public void cancelChallenge(Challenge challenge, User user) throws UserException {
        //TODO create notification that challenge was canceled

        //TODO check if cancel happened in the same week and give back weekly kudos

        if(challenge.getStatus() != ChallengeStatus.CREATED)
            throw new UserException("cannot_cancel_challenge");

        if(challenge.getCreator() != user)
            throw new UserException("cannot_cancel_challenge");

        challengeRepository.delete(challenge);
    }

    public void markChallengeAsCompleted(Challenge challenge, User user) throws UserException {
        checkIfCanMarkAsCompletedOrDFailed(challenge, user);

        //TODO create notification that challenge was completed

        Transaction transaction = challenge.getTransaction();
        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);

        User participant = challenge.getParticipant();
        participant.setTotalKudos(participant.getTotalKudos()+transaction.getAmount());
        userRepository.save(participant);

        challenge.setStatus(ChallengeStatus.ACCOMPLISHED);
        challengeRepository.save(challenge);
    }

    public void markChallengeAsFailed(Challenge challenge, User user) throws UserException {
        checkIfCanMarkAsCompletedOrDFailed(challenge, user);

        //TODO create notification that challenge was failed

        Transaction transaction = challenge.getTransaction();
        transaction.setStatus(TransactionStatus.CANCELED);
        transactionRepository.save(transaction);

        //TODO check if failure happened in the same week and give back weekly kudos

        challenge.setStatus(ChallengeStatus.FAILED);
        challengeRepository.save(challenge);
    }

    public void checkIfCanAcceptOrDecline(Challenge challenge, User user) throws UserException {
        if(challenge.getStatus() != ChallengeStatus.CREATED)
            throw new UserException("cannot_accept_or_decline_challenge");

        if(challenge.getParticipant() != user)
            throw new UserException("cannot_accept_or_decline_challenge");

        if(LocalDateTime.parse(challenge.getExpirationDate()).isBefore(LocalDateTime.now()))
            throw new UserException("cannot_accept_or_decline_challenge");
    }

    public void checkIfCanMarkAsCompletedOrDFailed(Challenge challenge, User user) throws UserException {
        if(challenge.getStatus() != ChallengeStatus.ACCEPTED)
            throw new UserException("cannot_complete_or_fail_challenge");

        if(challenge.getCreator() != user)
            throw new UserException("cannot_complete_or_fail_challenge");

        //TODO can this work with optional challenge date?
//        if(LocalDateTime.now().isBefore(LocalDateTime.parse(challenge.getExpirationDate())))
//            throw new UserException("cannot_complete_or_fail_challenge");
    }

    public List<Challenge> getAllSentAndReceivedChallenges(User user) {
        List<Challenge> challenges = new ArrayList<>();
        challenges.addAll(challengeRepository.findChallengesByCreatorAndStatus(user, ChallengeStatus.CREATED));
        challenges.addAll(challengeRepository.findChallengesByParticipantAndStatus(user, ChallengeStatus.CREATED));
        return challenges;
    }

    public List<Challenge> getAllOngoingChallenges(User user) {
        List<Challenge> challenges = new ArrayList<>();
        challenges.addAll(challengeRepository.findChallengesByCreatorAndStatus(user, ChallengeStatus.ACCEPTED));
        challenges.addAll(challengeRepository.findChallengesByParticipantAndStatus(user, ChallengeStatus.ACCEPTED));
        return challenges;
    }

    public List<Challenge> getAllFailedAndCompletedChallenges(User user) {
        List<Challenge> challenges = new ArrayList<>();
        challenges.addAll(challengeRepository.findChallengesByParticipantAndStatus(user, ChallengeStatus.ACCOMPLISHED));
        challenges.addAll(challengeRepository.findChallengesByParticipantAndStatus(user, ChallengeStatus.FAILED));
        challenges.addAll(challengeRepository.findChallengesByCreatorAndStatus(user, ChallengeStatus.ACCOMPLISHED));
        challenges.addAll(challengeRepository.findChallengesByCreatorAndStatus(user, ChallengeStatus.FAILED));
        return challenges;
    }

}
