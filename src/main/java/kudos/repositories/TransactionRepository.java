package kudos.repositories;

import kudos.model.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by chc on 15.8.6.
 */
public interface TransactionRepository extends MongoRepository<Transaction,String> {

    Transaction findTransactionByReceiverEmailOrderByTimestampDesc(String receiverEmail);

    Transaction findFirstByOrderByTimestampDesc();

    List<Transaction> findTransactionsByReceiverEmail(String receiverEmail);

    List<Transaction> findTransactionsByReceiverEmailAndStatusAndTimestampGreaterThanOrderByTimestampDesc(String receiverEmail, Transaction.Status status, String timestamp);

    List<Transaction> findTransactionsByReceiverEmailAndStatus(String receiverEmail, Transaction.Status status);

    List<Transaction> findTransactionsByReceiverEmailAndStatus(String receiverEmail, Transaction.Status status, Pageable pageable);

    List<Transaction> findTransactionsBySenderEmail(String senderEmail);

    List<Transaction> findTransactionsBySenderEmailAndStatus(String receiverEmail, Transaction.Status status);

    List<Transaction> findTransactionsBySenderEmailAndStatus(String receiverEmail, Transaction.Status status, Pageable pageable);

    List<Transaction> findTransactionBySenderEmailOrderByTimestampDesc (String senderEmail);

    List<Transaction> findTransactionByTimestampGreaterThanOrderByTimestampDesc(String timeStamp);

    List<Transaction> findTransactionByTimestampGreaterThanOrderByTimestampDesc(String timeStamp, Pageable pageable);

    List<Transaction> findTransactionByStatusOrderByTimestampDesc(Transaction.Status status, Pageable pageable);
}
