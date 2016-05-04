package kudos.services;

import kudos.KudosBusinessStrategy;
import kudos.model.Transaction;
import kudos.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by vytautassugintas on 25/04/16.
 */
@Service
public class TransactionService {

    SimpleDateFormat transactionDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");


    @Autowired
    private TransactionRepository repository;
    @Autowired
    private KudosBusinessStrategy strategy;

    public List<Transaction> getAllTrransactionsMadeThisWeek() {
        return repository.findTransactionByTimestampGreaterThanOrderByTimestampDesc(transactionDateFormat.format(strategy.getStartTime().toDate()));
    }

    public List<Transaction> getPageableTransactionsMadeThisWeek(int page, int pageSize) {
        return repository.findTransactionByTimestampGreaterThanOrderByTimestampDesc(transactionDateFormat.format(strategy.getStartTime().toDate()), new PageRequest(page, pageSize));
    }

    public List<Transaction> getPageableTransactionsByStatus(Transaction.Status status, int page, int pageSize){
        return repository.findTransactionByStatusOrderByTimestampDesc(status, new PageRequest(page, pageSize));
    }

    public boolean isLastTransactionChanged(String lastTransactionTimestamp){
        return !lastTransactionTimestamp.equals(repository.findFirstByOrderByTimestampDesc().getTimestamp());
    }

}