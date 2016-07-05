package kudos.repositories;

import kudos.model.Transaction;
import kudos.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by chc on 15.8.6.
 */
public interface TransactionRepository extends MongoRepository<Transaction,String> {

    Transaction findTransactionByReceiverEmailOrderByTimestampDesc(String receiverEmail);

    Transaction findFirstByOrderByTimestampDesc();




    //RECEIVERS
    List<Transaction> findTransactionsByReceiverEmail(String receiverEmail);

    List<Transaction> findTransactionsByReceiver(User receiver);

    List<Transaction> findTransactionsByReceiverAndStatusAndTimestampGreaterThanOrderByTimestampDesc(User receiver, Transaction.Status status, String timestamp);

    Transaction findTransactionByReceiverOrderByTimestampDesc(User receiver);

    //SENDERS
    List<Transaction> findTransactionsBySender(User sender);

    List<Transaction> findTransactionsBySenderAndTimestampGreaterThanOrderByTimestampDesc(User sender, String timestamp);

    List<Transaction> findTransactionBySenderOrderByTimestampDesc(User sender);






    List<Transaction> findTransactionsByReceiverEmailAndStatusAndTimestampGreaterThanOrderByTimestampDesc(String receiverEmail, Transaction.Status status, String timestamp);

    List<Transaction> findTransactionsByReceiverEmailAndTimestampGreaterThanOrderByTimestampDesc(String receiverEmail, String timestamp);

    List<Transaction> findTransactionsByReceiverEmailAndStatus(String receiverEmail, Transaction.Status status);

    List<Transaction> findTransactionsByReceiverEmailAndStatus(String receiverEmail, Transaction.Status status, Pageable pageable);

    List<Transaction> findTransactionsBySenderEmail(String senderEmail);

    List<Transaction> findTransactionsBySenderEmailAndStatus(String receiverEmail, Transaction.Status status);

    List<Transaction> findTransactionsBySenderEmailAndStatus(String receiverEmail, Transaction.Status status, Pageable pageable);

    List<Transaction> findTransactionBySenderEmailOrderByTimestampDesc (String senderEmail);

    List<Transaction> findTransactionsBySenderEmailAndTimestampGreaterThanOrderByTimestampDesc(String receiverEmail, String timestamp);

    List<Transaction> findTransactionByTimestampGreaterThanOrderByTimestampDesc(String timeStamp);

    List<Transaction> findTransactionByTimestampGreaterThanOrderByTimestampDesc(String timeStamp, Pageable pageable);

    List<Transaction> findTransactionByStatusOrderByTimestampDesc(Transaction.Status status, Pageable pageable);
}
