package kudos.services;

/**
 * Created by vytautassugintas on 21/06/16.
 */

import kudos.model.Challenge;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.model.history.History;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vytautassugintas on 25/04/16.
 */
@Service
public class HistoryService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ChallengeRepository challengeRepository;

    public List<History> getPageableUserHistoryByEmail(String userEmail, int startingIndex, int endingIndex){
        List<History> historyList = transactionRepository.findTransactionsByReceiverEmailAndStatus(userEmail, Transaction.Status.COMPLETED).stream().map(this::transformTransactionModelToHistory).collect(Collectors.toList());
        historyList.addAll(transactionRepository.findTransactionsBySenderEmailAndStatus(userEmail, Transaction.Status.COMPLETED).stream().map(this::transformTransactionModelToHistory).collect(Collectors.toList()));
        historyList.addAll(challengeRepository.findAllChallengesByParticipantAndStatus(userEmail, Challenge.Status.ACCOMPLISHED).stream().map(this::transformChallengeModelToHistory).collect(Collectors.toList()));
        historyList.addAll(challengeRepository.findAllChallengesByCreatorAndStatus(userEmail, Challenge.Status.ACCOMPLISHED).stream().map(this::transformChallengeModelToHistory).collect(Collectors.toList()));
        return sortListByTimestamp(historyList, startingIndex, endingIndex);
    }

    public List<History> getPageableUserReceivedHistoryByEmail(String userEmail, int startingIndex, int endingIndex){
        List<History> historyList = transactionRepository.findTransactionsByReceiverEmailAndStatus(userEmail, Transaction.Status.COMPLETED).stream().map(this::transformTransactionModelToHistory).collect(Collectors.toList());
        return sortListByTimestamp(historyList, startingIndex, endingIndex);
    }

    public List<History> getPageableUserGivenHistoryByEmail(String userEmail, int startingIndex, int endingIndex){
        List<History> historyList = transactionRepository.findTransactionsBySenderEmailAndStatus(userEmail, Transaction.Status.COMPLETED).stream().map(this::transformTransactionModelToHistory).collect(Collectors.toList());
        return sortListByTimestamp(historyList, startingIndex, endingIndex);
    }

    public List<History> getPageableUserAllChallengesHistoryByEmail(String userEmail, int startingIndex, int endingIndex){
        List<History> historyList = (challengeRepository.findAllChallengesByParticipantAndStatus(userEmail, Challenge.Status.ACCOMPLISHED).stream().map(this::transformChallengeModelToHistory).collect(Collectors.toList()));
        historyList.addAll(challengeRepository.findAllChallengesByCreatorAndStatus(userEmail, Challenge.Status.ACCOMPLISHED).stream().map(this::transformChallengeModelToHistory).collect(Collectors.toList()));
        return sortListByTimestamp(historyList, startingIndex, endingIndex);
    }

    public History transformChallengeModelToHistory(Challenge challenge){
        return new History(challenge.getParticipant(),
                getUserFullNameByEmail(challenge.getParticipant()),
                challenge.getCreator(),
                getUserFullNameByEmail(challenge.getCreator()),
                challenge.getAmount() * 2,
                challenge.getDescription(),
                challenge.getCreateDateDate(),
                challengeStatus(challenge.getCreatorStatus(), challenge.getParticipantStatus()));
    }

    public History transformTransactionModelToHistory(Transaction transaction){
        return new History(transaction.getReceiver().getEmail(),
                getUserFullNameByEmail(transaction.getReceiver().getEmail()),
                transaction.getSender().getEmail(),
                getUserFullNameByEmail(transaction.getSender().getEmail()),
                transaction.getAmount(),
                transaction.getMessage(),
                transaction.getTimestamp(),
                transaction.getStatus());
    }

    private List<History> sortListByTimestamp(List<History> historyList, int startingIndex, int endingIndex){
        try {
            return historyList.stream().sorted((h1, h2) -> h2.getTimestamp().compareTo(h1.getTimestamp())).collect(Collectors.toList()).subList(startingIndex, endingIndex);
        } catch (IndexOutOfBoundsException e){
            return historyList.stream().sorted((h1, h2) -> h2.getTimestamp().compareTo(h1.getTimestamp())).collect(Collectors.toList());
        }
    }

    private String getUserFullNameByEmail(String email){
        User receiver = userRepository.findByEmail(email);
        return receiver.getFirstName() + " " + receiver.getLastName();
    }

    private Transaction.Status challengeStatus(Boolean creatorStatus, Boolean participantStatus){
        if (creatorStatus != null)
            if (creatorStatus) return Transaction.Status.COMPLETED_CHALLENGE_CREATOR;
            else return Transaction.Status.COMPLETED_CHALLENGE_PARTICIPANT;

        if (participantStatus != null)
            if (participantStatus) return Transaction.Status.COMPLETED_CHALLENGE_PARTICIPANT;
            else return Transaction.Status.COMPLETED_CHALLENGE_CREATOR;

        return Transaction.Status.COMPLETED_CHALLENGE;
    }

}
