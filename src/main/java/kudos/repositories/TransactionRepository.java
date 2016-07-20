package kudos.repositories;

import kudos.model.Transaction;
import kudos.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction,String> {

    /**
     * TEMP METHODS
     */

    List<Transaction> findTransactionsByReceiverEmail(String receiverEmail);

    List<Transaction> findTransactionsBySenderEmail(String senderEmail);

    /**
     * TEMP METHODS
     */

    Transaction findFirstByOrderByTimestampDesc();

    Transaction findTransactionByReceiverOrderByTimestampDesc(User receiver);

    List<Transaction> findTransactionsByReceiver(User receiver);

    List<Transaction> findTransactionsByReceiverAndStatus(User receiver, Transaction.Status status);

    List<Transaction> findTransactionsByReceiverAndTimestampGreaterThanOrderByTimestampDesc(User receiver, String timestamp);

    List<Transaction> findTransactionsByReceiverAndStatusAndTimestampGreaterThanOrderByTimestampDesc(User receiver, Transaction.Status status, String timestamp);

    List<Transaction> findTransactionsBySender(User sender);

    List<Transaction> findTransactionsBySenderAndStatus(User sender, Transaction.Status status);

    List<Transaction> findTransactionsBySenderAndTimestampGreaterThanOrderByTimestampDesc(User sender, String timestamp);

    List<Transaction> findTransactionBySenderOrderByTimestampDesc(User sender);

    List<Transaction> findTransactionByTimestampGreaterThanOrderByTimestampDesc(String timeStamp);

    List<Transaction> findTransactionByTimestampGreaterThanOrderByTimestampDesc(String timeStamp, Pageable pageable);

    List<Transaction> findTransactionByStatusOrderByTimestampDesc(Transaction.Status status, Pageable pageable);
}
