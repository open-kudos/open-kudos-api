package kudos.services;

import kudos.model.Transaction;
import kudos.model.status.TransactionStatus;
import kudos.model.User;
import kudos.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getLatestTransactions() {
        return transactionRepository.findTransactionsByStatusOrderByDateDesc(TransactionStatus.COMPLETED.toValue(),
                new PageRequest(0, 10));
    }

    public Page<Transaction> getGivenKudosHistory(User user, Pageable pageable) {
        return transactionRepository.findTransactionsBySenderAndStatusOrderByDateDesc(user,
                TransactionStatus.COMPLETED.toValue(), pageable);
    }

    public Page<Transaction> getReceivedKudosHistory(User user, Pageable pageable) {
        return transactionRepository.findTransactionsByReceiverAndStatusOrderByDateDesc(user,
                TransactionStatus.COMPLETED.toValue(), pageable);
    }

    public Page<Transaction> getKudosHistory(User user, Pageable pageable) {
        return transactionRepository.findTransactionsBySenderOrReceiverAndStatusOrderByDateDesc(user, user,
                TransactionStatus.COMPLETED.toValue(), pageable);
    }

    public Page<Transaction> getKudosTransactionsByEdorsement(User user, String endorsement, Pageable pageable) {
        return transactionRepository.findTransactionsByReceiverAndEndorsementOrderByDateDesc(user, endorsement, pageable);
    }

    public Map<String, Integer> getEndorsementsMap(User user) {

        Map<String, Integer> endorsements = new HashMap<>();

        transactionRepository.findTransactionsByReceiver(user).stream().filter(
                transaction -> transaction.getEndorsement() != null).forEach(transaction -> {
            if (endorsements.containsKey(transaction.getEndorsement())) {
                endorsements.put(transaction.getEndorsement(), endorsements.get(transaction.getEndorsement()) + transaction.getAmount());
            } else {
                endorsements.put(transaction.getEndorsement(), transaction.getAmount());
            }
        });

        return endorsements.entrySet().stream()
                .sorted((k1, k2) -> -k1.getValue().compareTo(k2.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, HashMap::new));
    }

}
