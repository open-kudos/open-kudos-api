package kudos.services;

import kudos.model.*;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderBoardService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;


    public List<LeaderBoardItem> getTopSenders(int periodInDays) {
        List<LeaderBoardItem> topSenders = userRepository.findUsersByStatusNot(UserStatus.NOT_CONFIRMED)
                .stream().map(user -> createLeaderBoardItem(user, calculateSendersTransactionsAmount(user, periodInDays)))
                .collect(Collectors.toList());
        return sortListByAmountOfKudos(topSenders, 0, 5);
    }

    public List<LeaderBoardItem> getTopReceivers(int periodInDays) {
        List<LeaderBoardItem> topReceivers = userRepository.findUsersByStatusNot(UserStatus.NOT_CONFIRMED)
                .stream().map(user -> createLeaderBoardItem(user, calculateReceiversTransactionsAmount(user, periodInDays)))
                .collect(Collectors.toList());
        return sortListByAmountOfKudos(topReceivers, 0, 5);
    }

    public List<LeaderBoardItem> getTopReceiversFromAllTime() {
        List<LeaderBoardItem> topReceivers = userRepository.findUsersByStatusNot(UserStatus.NOT_CONFIRMED)
                .stream().map(user -> createLeaderBoardItem(user, calculateReceiversTransactionsAmount(user)))
                .collect(Collectors.toList());
        return sortListByAmountOfKudos(topReceivers, 0, 5);
    }

    public List<LeaderBoardItem> getTopSendersFromAllTime() {
        List<LeaderBoardItem> topSenders = userRepository.findUsersByStatusNot(UserStatus.NOT_CONFIRMED)
                .stream().map(user -> createLeaderBoardItem(user, calculateSendersTransactionsAmount(user)))
                .collect(Collectors.toList());
        return sortListByAmountOfKudos(topSenders, 0, 5);
    }

    public List<LeaderBoardItem> sortListByAmountOfKudos(List<LeaderBoardItem> leaderBoardItems, int startingIndex, int endingIndex) {
        try {
            return leaderBoardItems.stream().sorted((l1, l2) -> Integer.valueOf(l2.getKudosAmount())
                    .compareTo(Integer.valueOf(l1.getKudosAmount()))).collect(Collectors.toList())
                    .subList(startingIndex, endingIndex);
        } catch (IndexOutOfBoundsException e) {
            return leaderBoardItems.stream().sorted((l1, l2) -> Integer.valueOf(l2.getKudosAmount())
                    .compareTo(Integer.valueOf(l1.getKudosAmount()))).collect(Collectors.toList())
                    .subList(0, leaderBoardItems.size());
        }
    }

    public int calculateSendersTransactionsAmount(User user, int periodInDays) {
        List<Transaction> transactions = transactionRepository
                .findTransactionsBySenderAndStatusAndDateGreaterThanOrderByDateDesc(user, TransactionStatus.COMPLETED,
                        LocalDateTime.now().minusDays(periodInDays).toString());
        return calculateTransactionsAmountForChallengesAndKudosGiving(transactions);
    }

    public int calculateReceiversTransactionsAmount(User user, int periodInDays) {
        List<Transaction> transactions = transactionRepository
                .findTransactionsByReceiverAndStatusAndDateGreaterThanOrderByDateDesc(user, TransactionStatus.COMPLETED,
                        LocalDateTime.now().minusDays(periodInDays).toString());
        return calculateTransactionsAmountForChallengesAndKudosGiving(transactions);
    }

    public int calculateSendersTransactionsAmount(User user) {
        List<Transaction> transactions = transactionRepository
                .findTransactionsBySenderAndStatusOrderByDateDesc(user, TransactionStatus.COMPLETED);
        return calculateTransactionsAmountForChallengesAndKudosGiving(transactions);
    }

    public int calculateReceiversTransactionsAmount(User user) {
        List<Transaction> transactions = transactionRepository
                .findTransactionsByReceiverAndStatusOrderByDateDesc(user, TransactionStatus.COMPLETED);
        return calculateTransactionsAmountForChallengesAndKudosGiving(transactions);
    }

    public int calculateTransactionsAmountForChallengesAndKudosGiving(List<Transaction> transactions) {
        return transactions.stream().filter(transaction -> transaction.getType() == TransactionType.KUDOS
                || transaction.getType() == TransactionType.CHALLENGE).mapToInt(Transaction::getAmount).sum();
    }

    private LeaderBoardItem createLeaderBoardItem(User user, int kudosAmount) {
        return new LeaderBoardItem(user.getFirstName() + " " + user.getLastName(), user.getId(), kudosAmount);
    }

}
