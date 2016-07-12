package kudos.services;

import kudos.model.Challenge;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.model.history.History;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import kudos.web.beans.response.HistoryResponse;
import kudos.web.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private UsersService usersService;

    public List<HistoryResponse> getPageableUserHistoryByEmail(String userEmail, int startingIndex, int endingIndex) throws UserException{
        User user = usersService.findByEmail(userEmail).get();
        List<HistoryResponse> historyList = transactionRepository.findTransactionsByReceiverAndStatus(user, Transaction.Status.COMPLETED).stream().map(HistoryResponse::new).collect(Collectors.toList());
        historyList.addAll(transactionRepository.findTransactionsBySenderAndStatus(user, Transaction.Status.COMPLETED).stream().map(HistoryResponse::new).collect(Collectors.toList()));
        historyList.addAll(challengeRepository.findAllChallengesByParticipantUserAndStatus(user, Challenge.Status.ACCOMPLISHED).stream().map(HistoryResponse::new).collect(Collectors.toList()));
        historyList.addAll(challengeRepository.findAllChallengesByCreatorUserAndStatus(user, Challenge.Status.ACCOMPLISHED).stream().map(HistoryResponse::new).collect(Collectors.toList()));
        return sortListByTimestamp(historyList, startingIndex, endingIndex);
    }

    public List<HistoryResponse> getPageableUserReceivedHistoryByEmail(String userEmail, int startingIndex, int endingIndex) throws UserException{
        User user = usersService.findByEmail(userEmail).get();
        List<HistoryResponse> historyList = transactionRepository.findTransactionsByReceiverAndStatus(user, Transaction.Status.COMPLETED).stream().map(HistoryResponse::new).collect(Collectors.toList());
        return sortListByTimestamp(historyList, startingIndex, endingIndex);
    }

    public List<HistoryResponse> getPageableUserGivenHistoryByEmail(String userEmail, int startingIndex, int endingIndex) throws UserException{
        User user = usersService.findByEmail(userEmail).get();
        List<HistoryResponse> historyList = transactionRepository.findTransactionsBySenderAndStatus(user, Transaction.Status.COMPLETED).stream().map(HistoryResponse::new).collect(Collectors.toList());
        return sortListByTimestamp(historyList, startingIndex, endingIndex);
    }

    public List<HistoryResponse> getPageableUserAllChallengesHistoryByEmail(String userEmail, int startingIndex, int endingIndex) throws UserException{
        User user = usersService.findByEmail(userEmail).get();
        List<HistoryResponse> historyList = (challengeRepository.findAllChallengesByParticipantUserAndStatus(user, Challenge.Status.ACCOMPLISHED).stream().map(HistoryResponse::new).collect(Collectors.toList()));
        historyList.addAll(challengeRepository.findAllChallengesByCreatorUserAndStatus(user, Challenge.Status.ACCOMPLISHED).stream().map(HistoryResponse::new).collect(Collectors.toList()));
        return sortListByTimestamp(historyList, startingIndex, endingIndex);
    }

    public History transformChallengeModelToHistory(Challenge challenge){
        return new History(challenge.getParticipantUser().getEmail(),
                getUserFullNameByEmail(challenge.getParticipantUser()),
                challenge.getCreatorUser().getEmail(),
                getUserFullNameByEmail(challenge.getCreatorUser()),
                challenge.getAmount() * 2,
                challenge.getDescription(),
                challenge.getCreateDateDate(),
                challengeStatus(challenge.getCreatorStatus(), challenge.getParticipantStatus()));
    }

    public History transformTransactionModelToHistory(Transaction transaction){
        return new History(transaction.getReceiver().getEmail(),
                getUserFullNameByEmail(transaction.getReceiver()),
                transaction.getSender().getEmail(),
                getUserFullNameByEmail(transaction.getSender()),
                transaction.getAmount(),
                transaction.getMessage(),
                transaction.getTimestamp(),
                transaction.getStatus());
    }

    private List<HistoryResponse> sortListByTimestamp(List<HistoryResponse> historyList, int startingIndex, int endingIndex){
        try {
            return historyList.stream().sorted((h1, h2) -> h2.getTimestamp().compareTo(h1.getTimestamp())).collect(Collectors.toList()).subList(startingIndex, endingIndex);
        } catch (IndexOutOfBoundsException e){
            return historyList.stream().sorted((h1, h2) -> h2.getTimestamp().compareTo(h1.getTimestamp())).collect(Collectors.toList());
        }
    }

    private String getUserFullNameByEmail(User user){
        User receiver = userRepository.findByEmail(user.getEmail());
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
