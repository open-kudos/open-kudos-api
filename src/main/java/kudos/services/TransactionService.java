package kudos.services;

import kudos.model.Transaction;
import kudos.model.TransactionStatus;
import kudos.model.User;
import kudos.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> getLatestTransactions(){
        return transactionRepository.findTransactionsByStatusOrderByDateDesc(TransactionStatus.COMPLETED,
                new PageRequest(0, 10));
    }

    public Page<Transaction> getGivenKudosHistory(User user, Pageable pageable) {
        return transactionRepository.findTransactionsBySenderAndStatusOrderByDateDesc(user,
                TransactionStatus.COMPLETED, pageable);
    }

    public Page<Transaction> getReceivedKudosHistory(User user, Pageable pageable) {
        return transactionRepository.findTransactionsByReceiverAndStatusOrderByDateDesc(user,
                TransactionStatus.COMPLETED, pageable);
    }

    public Page<Transaction> getKudosHistory(User user, Pageable pageable) {
        return transactionRepository.findTransactionsBySenderOrReceiverAndStatusOrderByDateDesc(user, user,
                TransactionStatus.COMPLETED, pageable);
    }

}
