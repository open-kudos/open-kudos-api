package kudos.services;

import kudos.KudosBusinessStrategy;
import kudos.model.Transaction;
import kudos.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vytautassugintas on 25/04/16.
 */
@Service
public class TransactionService {

    SimpleDateFormat transactionDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    List<Transaction> transactionList = new ArrayList<>();

    @Autowired
    private TransactionRepository repository;
    @Autowired
    private KudosBusinessStrategy strategy;

    /**
     * Return list of all transactions made by users this week
     * @return List
     */
    public List<Transaction> getTransactionsFeed() {
        return repository.findTransactionByTimestampGreaterThanOrderByTimestampDesc(transactionDateFormat.format(strategy.getStartTime().toDate()));
    }

    /**
     * Return list of transactions made by all users, can be paged
     * @param page which page now should be shown
     * @param pageSize size of elements in page
     * @return List
     */
    public List<Transaction> getPageableTransactionsFeed(int page, int pageSize) {
        return repository.findTransactionByTimestampGreaterThanOrderByTimestampDesc(transactionDateFormat.format(strategy.getStartTime().toDate()), new PageRequest(page, pageSize));
    }

    public boolean isLastTransactionChanged(String lastTransactionTimestamp){
        return !lastTransactionTimestamp.equals(repository.findFirstByOrderByTimestampDesc().getTimestamp());
    }
}
