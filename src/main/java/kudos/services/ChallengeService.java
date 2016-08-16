package kudos.services;

import com.google.common.base.Strings;
import kudos.exceptions.InvalidKudosAmountException;
import kudos.exceptions.UserException;
import kudos.model.*;
import kudos.repositories.*;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ActionRepository actionRepository;

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
        challenge.setExpirationDate(Strings.isNullOrEmpty(expirationDate) ? null : LocalDateTime.parse(expirationDate).toString());
        challenge.setCreatedDate(LocalDateTime.now().toString());
        challenge.setDescription(description);
        challengeRepository.save(challenge);
        return challenge;
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
        return challengeRepository.save(challenge);
    }

    public void declineChallenge(Challenge challenge, User user) throws UserException {
        checkIfCanAcceptOrDecline(challenge, user);
        //TODO create notification that challenge was declined
        transactionRepository.delete(challenge.getTransaction());
        challengeRepository.delete(challenge);
    }

    public void cancelChallenge(Challenge challenge, User user) throws UserException {
        //TODO create notification that challenge was canceled

        if(challenge.getStatus() != ChallengeStatus.CREATED)
            throw new UserException("cannot_cancel_challenge");

        if(!challenge.getCreator().getId().equals(user.getId()))
            throw new UserException("cannot_cancel_challenge");

        challenge.getTransaction().setStatus(TransactionStatus.CANCELED);
        transactionRepository.save(challenge.getTransaction());
        challenge.setStatus(ChallengeStatus.CANCELED);
        challengeRepository.save(challenge);
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

        challenge.setStatus(ChallengeStatus.FAILED);
        challenge.setClosedDate(LocalDateTime.now().toString());
        challengeRepository.save(challenge);

    }

    public void markChallengeAsExpiredOrFailed(Challenge challenge, ChallengeStatus challengeStatus) {

        //TODO create notification that challenge expired

        Transaction transaction = challenge.getTransaction();
        transaction.setStatus(TransactionStatus.CANCELED);
        transactionRepository.save(transaction);

        challenge.setStatus(challengeStatus);
        challengeRepository.save(challenge);
    }

    public void checkIfCanAcceptOrDecline(Challenge challenge, User user) throws UserException {
        if(challenge.getStatus() != ChallengeStatus.CREATED)
            throw new UserException("cannot_accept_or_decline_challenge");

        if(!challenge.getParticipant().getId().equals(user.getId()))
            throw new UserException("cannot_accept_or_decline_challenge");

        if(challenge.getExpirationDate() != null && LocalDateTime.parse(challenge.getExpirationDate()).isBefore(LocalDateTime.now())) {
            markChallengeAsExpiredOrFailed(challenge, ChallengeStatus.EXPIRED);
            throw new UserException("challenge_expired");
        }
    }

    public void checkIfCanMarkAsCompletedOrDFailed(Challenge challenge, User user) throws UserException {
        if(challenge.getStatus() != ChallengeStatus.ACCEPTED)
            throw new UserException("cannot_complete_or_fail_challenge");

        if(!challenge.getCreator().getId().equals(user.getId()))
            throw new UserException("cannot_complete_or_fail_challenge");

        if(challenge.getExpirationDate() != null && LocalDateTime.now().isBefore(LocalDateTime.parse(challenge.getExpirationDate()))){
            markChallengeAsExpiredOrFailed(challenge, ChallengeStatus.FAILED);
            throw new UserException("challenge_expired");
        }
    }

    public Page<Challenge> getAllSentAndReceivedChallenges(User user, Pageable pageable) {
//        Page<Challenge> challengesParticipant = challengeRepository
//                .findChallengesByStatusAndParticipantOrderByCreatedDateDesc(ChallengeStatus.CREATED, user, pageable);
//        Page<Challenge> challengesCreator = challengeRepository
//                .findChallengesByStatusAndCreatorOrderByCreatedDateDesc(ChallengeStatus.CREATED, user, pageable);
//        if(challengesCreator.hasContent() && challengesParticipant.hasContent()) {
//            return mergeChallengePages(challengesCreator, challengesParticipant, pageable,
//                    new DateComparatorDescendingByCreated());
//        } else if(challengesCreator.hasContent() && !challengesParticipant.hasContent()) {
//            return challengesCreator;
//        } else if(!challengesCreator.hasContent() && challengesParticipant.hasContent()) {
//            return challengesParticipant;
//        } else {
//            return new PageImpl<>(new ArrayList<>(), pageable, 0);
//        }
        return challengeRepository.findChallengesByStatusAndCreatorOrStatusAndParticipantOrderByCreatedDateDesc(ChallengeStatus.CREATED, user, ChallengeStatus.CREATED, user, pageable);
    }

    public Page<Challenge> getAllOngoingChallenges(User user, Pageable pageable) {
        return challengeRepository.findChallengesByStatusAndCreatorOrStatusAndParticipantOrderByCreatedDateDesc(
                ChallengeStatus.ACCEPTED, user, ChallengeStatus.ACCEPTED, user, pageable);
    }

    public Page<Challenge> getAllFailedAndAccomplishedChallenges(User user, Pageable pageable) {
        return challengeRepository.findChallengesByStatusAndParticipantOrStatusAndParticipantOrderByClosedDateDesc(
                ChallengeStatus.FAILED, user, ChallengeStatus.ACCOMPLISHED, user, pageable);
    }

    public Page<Challenge> getAllFailedChallenges(User user, Pageable pageable) {
        return challengeRepository.findChallengesByStatusAndParticipantOrderByClosedDateDesc(ChallengeStatus.FAILED, user,
                pageable);
    }

    public Page<Challenge> getAllAccomplishedChallenges(User user, Pageable pageable) {
        return challengeRepository.findChallengesByStatusAndParticipantOrderByClosedDateDesc(ChallengeStatus.ACCOMPLISHED, user,
                pageable);
    }

    public void addComment(Comment comment) throws UserException {
        commentRepository.save(comment);
    }

    public Page<Comment> getComments(Challenge challenge, Pageable pageable) throws UserException {
        return commentRepository.findCommentsByChallengeOrderByCreationDateDesc(challenge, pageable);
    }

    private Page<Challenge> mergeChallengePages(Page<Challenge> challenges1, Page<Challenge> challenges2,
                                                Pageable pageable, Comparator<Challenge> comparator) {
        List<Challenge> merged = new ArrayList<>();
        merged.addAll(challenges1.getContent());
        merged.addAll(challenges2.getContent());
        merged.sort(comparator);
        int endIndex = merged.size() < pageable.getPageSize() ? merged.size() : pageable.getPageSize();
        return new PageImpl<>(merged.subList(0, endIndex), pageable, challenges1.getTotalElements()+challenges2.getTotalElements());
    }

}
