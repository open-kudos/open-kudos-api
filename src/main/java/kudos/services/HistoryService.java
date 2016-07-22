package kudos.services;

import kudos.model.Challenge;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.TransactionRepository;
import kudos.web.beans.response.HistoryResponse;
import kudos.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistoryService {

//    @Autowired
//    private TransactionRepository transactionRepository;
//    @Autowired
//    private ChallengeRepository challengeRepository;
//    @Autowired
//    private UsersService usersService;
//
//    public List<HistoryResponse> getPageableUserHistoryByEmail(String userEmail, int startingIndex, int endingIndex) throws UserException{
//        User user = usersService.findByEmail(userEmail).get();
//        return sortListByTimestamp(getAllUserHistory(user), startingIndex, endingIndex);
//    }
//
//    public List<HistoryResponse> getPageableUserReceivedHistoryByEmail(String userEmail, int startingIndex, int endingIndex) throws UserException{
//        User user = usersService.findByEmail(userEmail).get();
//        List<HistoryResponse> historyList = transactionRepository.findTransactionsByReceiverAndStatus(user, Transaction.Status.COMPLETED).stream().map(HistoryResponse::new).collect(Collectors.toList());
//        return sortListByTimestamp(historyList, startingIndex, endingIndex);
//    }
//
//    public List<HistoryResponse> getPageableUserGivenHistoryByEmail(String userEmail, int startingIndex, int endingIndex) throws UserException{
//        User user = usersService.findByEmail(userEmail).get();
//        List<HistoryResponse> historyList = transactionRepository.findTransactionsBySenderAndStatus(user, Transaction.Status.COMPLETED).stream().map(HistoryResponse::new).collect(Collectors.toList());
//        return sortListByTimestamp(historyList, startingIndex, endingIndex);
//    }
//
//    public List<HistoryResponse> getPageableUserAllChallengesHistoryByEmail(String userEmail, int startingIndex, int endingIndex) throws UserException{
//        User user = usersService.findByEmail(userEmail).get();
//        List<HistoryResponse> historyList = (challengeRepository.findAllChallengesByParticipantUserAndStatus(user, Challenge.Status.ACCOMPLISHED).stream().map(HistoryResponse::new).collect(Collectors.toList()));
//        historyList.addAll(challengeRepository.findAllChallengesByCreatorUserAndStatus(user, Challenge.Status.ACCOMPLISHED).stream().map(HistoryResponse::new).collect(Collectors.toList()));
//        return sortListByTimestamp(historyList, startingIndex, endingIndex);
//    }
//
//    public List<HistoryResponse> getAllUserHistory(User user){
//        List<HistoryResponse> historyList = transactionRepository.findTransactionsByReceiverAndStatus(user, Transaction.Status.COMPLETED).stream().map(HistoryResponse::new).collect(Collectors.toList());
//        historyList.addAll(transactionRepository.findTransactionsBySenderAndStatus(user, Transaction.Status.COMPLETED).stream().map(HistoryResponse::new).collect(Collectors.toList()));
//        historyList.addAll(challengeRepository.findAllChallengesByParticipantUserAndStatus(user, Challenge.Status.ACCOMPLISHED).stream().map(HistoryResponse::new).collect(Collectors.toList()));
//        historyList.addAll(challengeRepository.findAllChallengesByCreatorUserAndStatus(user, Challenge.Status.ACCOMPLISHED).stream().map(HistoryResponse::new).collect(Collectors.toList()));
//        return historyList;
//    }
//
//    public List<HistoryResponse> sortListByTimestamp(List<HistoryResponse> historyList, int startingIndex, int endingIndex){
//        try {
//            return historyList.stream().sorted((h1, h2) -> h2.getTimestamp().compareTo(h1.getTimestamp())).collect(Collectors.toList()).subList(startingIndex, endingIndex);
//        } catch (IndexOutOfBoundsException e){
//            return historyList.stream().sorted((h1, h2) -> h2.getTimestamp().compareTo(h1.getTimestamp())).collect(Collectors.toList());
//        }
//    }

}
