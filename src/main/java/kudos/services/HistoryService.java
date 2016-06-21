package kudos.services;

/**
 * Created by vytautassugintas on 21/06/16.
 */

import kudos.KudosBusinessStrategy;
import kudos.model.Challenge;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.model.history.History;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vytautassugintas on 25/04/16.
 */
@Service
public class HistoryService {

    SimpleDateFormat transactionDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    @Autowired
    private UsersService usersService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private KudosBusinessStrategy strategy;

    public List<Transaction> getPageableTransactionsByStatus(Transaction.Status status, int page, int pageSize){
        return transactionRepository.findTransactionByStatusOrderByTimestampDesc(status, new PageRequest(page, pageSize));
    }

    public List<History> getPageableUserHistoryByEmail(String userEmail){
        List<History> historyList = transactionRepository.findTransactionsByReceiverEmailAndStatus(userEmail, Transaction.Status.COMPLETED).stream().map(this::transformTransactionModelToHistory).collect(Collectors.toList());
        historyList.addAll(transactionRepository.findTransactionsBySenderEmailAndStatus(userEmail, Transaction.Status.COMPLETED).stream().map(this::transformTransactionModelToHistory).collect(Collectors.toList()));
        historyList.addAll(challengeRepository.findAllChallengesByParticipantAndStatus(userEmail, Challenge.Status.ACCOMPLISHED).stream().map(this::transformChallengeModelToHistory).collect(Collectors.toList()));
        historyList.addAll(challengeRepository.findAllChallengesByCreatorAndStatus(userEmail, Challenge.Status.ACCOMPLISHED).stream().map(this::transformChallengeModelToHistory).collect(Collectors.toList()));
        return historyList.stream().sorted((h1, h2) -> h2.getTimestamp().compareTo(h1.getTimestamp())).collect(Collectors.toList());
    }

    public History transformChallengeModelToHistory(Challenge challenge){
        User participant = userRepository.findByEmail(challenge.getParticipant());
        User creator = userRepository.findByEmail(challenge.getCreator());
        String participantFullName = participant.getFirstName() + " " + participant.getLastName();
        String creatorFullName = creator.getFirstName() + " " + creator.getLastName();
        return new History(challenge.getParticipant(),
                participantFullName,
                challenge.getCreator(),
                creatorFullName,
                challenge.getAmount() * 2,
                challenge.getDescription(),
                challenge.getCreateDateDate(),
                getStatus(challenge.getStatus()));
    }

    public History transformTransactionModelToHistory(Transaction transaction){
        User receiver = userRepository.findByEmail(transaction.getReceiverEmail());
        User sender = userRepository.findByEmail(transaction.getSenderEmail());
        String receiverFullName = receiver.getFirstName() + " " + receiver.getLastName();
        String senderFullName = sender.getFirstName() + " " + sender.getLastName();
        return new History(transaction.getReceiverEmail(),
                receiverFullName,
                transaction.getSenderEmail(),
                senderFullName,
                transaction.getAmount(),
                transaction.getMessage(),
                transaction.getTimestamp(),
                transaction.getStatus());
    }



    Transaction.Status getStatus(Challenge.Status status){
        if (status == Challenge.Status.ACCOMPLISHED){
            return Transaction.Status.COMPLETED_CHALLENGE;
        }else{
            return null;
        }
    }
}
