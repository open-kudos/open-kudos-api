package kudos.services;

import kudos.exceptions.InvalidKudosAmountException;
import kudos.exceptions.UserException;
import kudos.model.*;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChallengeService {

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
        }

        Transaction transaction = transactionRepository.save(new Transaction(creator, receiver, amount, name,
                TransactionType.CHALLENGE, LocalDateTime.now().toString(), TransactionStatus.PENDING));

        Challenge challenge = new Challenge(creator, receiver, name, transaction, ChallengeStatus.CREATED);
        challenge.setExpirationDate(expirationDate);
        challenge.setDescription(description);

        return challengeRepository.save(challenge);
    }

    public Challenge getChallengeById(String id) throws UserException {
        Optional<Challenge> challenge = challengeRepository.findChallengeById(id);
        if(challenge.isPresent()) {
            return challenge.get();
        } else {
            throw new UserException("challenge_not_found");
        }
    }

    public Challenge acceptChallenge(Challenge challenge, User user) throws UserException {
        checkIfCanAcceptOrDecline(challenge, user);
        challenge.setStatus(ChallengeStatus.ACCEPTED);
        challenge.setStartDate(LocalDateTime.now().toString());
        return challengeRepository.save(challenge);
    }

    public void declineChallenge(Challenge challenge, User user) throws UserException {
        checkIfCanAcceptOrDecline(challenge, user);
        //TODO create notification that challenge was declined

        //TODO check if decline happened in the same week and give back weekly kudos should happen auto if transaction is deleted
        challengeRepository.delete(challenge);
    }

    public void cancelChallenge(Challenge challenge, User user) throws UserException {
        //TODO create notification that challenge was canceled

        if(challenge.getStatus() != ChallengeStatus.CREATED)
            throw new UserException("cannot_cancel_challenge");

        if(challenge.getCreator() != user)
            throw new UserException("cannot_cancel_challenge");
        //TODO check if cancel happened in the same week and give back weekly kudos should happen auto if transaction is deleted
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
        challenge.setClosedDate(LocalDateTime.now().toString());
        challengeRepository.save(challenge);
    }

    public void markChallengeAsFailed(Challenge challenge, User user) throws UserException {
        checkIfCanMarkAsCompletedOrDFailed(challenge, user);

        //TODO create notification that challenge was failed

        Transaction transaction = challenge.getTransaction();
        transaction.setStatus(TransactionStatus.CANCELED);
        transactionRepository.save(transaction);

        //TODO check if failure happened in the same week and give back weekly kudos should be auto

        challenge.setStatus(ChallengeStatus.FAILED);
        challenge.setClosedDate(LocalDateTime.now().toString());
        challengeRepository.save(challenge);
    }

    public void checkIfCanAcceptOrDecline(Challenge challenge, User user) throws UserException {
        if(challenge.getStatus() != ChallengeStatus.CREATED)
            throw new UserException("cannot_accept_or_decline_challenge");

        if(!challenge.getParticipant().getId().equals(user.getId()))
            throw new UserException("cannot_accept_or_decline_challenge");

        if(LocalDateTime.parse(challenge.getExpirationDate()).isBefore(LocalDateTime.now()))
            throw new UserException("cannot_accept_or_decline_challenge");
    }

    public void checkIfCanMarkAsCompletedOrDFailed(Challenge challenge, User user) throws UserException {
        if(challenge.getStatus() != ChallengeStatus.ACCEPTED)
            throw new UserException("cannot_complete_or_fail_challenge");

        if(!challenge.getCreator().getId().equals(user.getId()))
            throw new UserException("cannot_complete_or_fail_challenge");

        //TODO can this work with optional challenge date?
//        if(LocalDateTime.now().isBefore(LocalDateTime.parse(challenge.getExpirationDate())))
//            throw new UserException("cannot_complete_or_fail_challenge");
    }

    public List<Challenge> getAllSentAndReceivedChallenges(User user) {
        return challengeRepository.findChallengesByStatusAndCreatorOrStatusAndParticipant(ChallengeStatus.CREATED, user,
                ChallengeStatus.CREATED, user);
    }

    public List<Challenge> getAllOngoingChallenges(User user) {
        return challengeRepository.findChallengesByStatusAndCreatorOrStatusAndParticipant(ChallengeStatus.ACCEPTED, user,
                ChallengeStatus.ACCEPTED, user);
    }

    public List<Challenge> getAllFailedAndAccomplishedChallenges(User user) {
        List<Challenge> challenges = new ArrayList<>();
        challenges.addAll(getAllAccomplishedChallenges(user));
        challenges.addAll(getAllFailedChallenges(user));
        return challenges;
    }

    public List<Challenge> getAllFailedChallenges(User user) {
        return challengeRepository.findChallengesByStatusAndParticipant(ChallengeStatus.FAILED, user);
    }

    public List<Challenge> getAllAccomplishedChallenges(User user) {
        return challengeRepository.findChallengesByStatusAndParticipant(ChallengeStatus.ACCOMPLISHED, user);
    }

}
