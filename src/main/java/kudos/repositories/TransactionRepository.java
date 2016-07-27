package kudos.repositories;

import kudos.model.Transaction;
import kudos.model.TransactionStatus;
import kudos.model.TransactionType;
import kudos.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction,String> {

//    Transaction findFirstByOrderByTimestampDesc();
//
//    Transaction findTransactionByReceiverOrderByTimestampDesc(User receiver);
//
//    List<Transaction> findTransactionsByReceiver(User receiver);
//
//    List<Transaction> findTransactionsByReceiverAndStatus(User receiver, TransactionType status);
//
//    List<Transaction> findTransactionsByReceiverAndTimestampGreaterThanOrderByTimestampDesc(User receiver, String timestamp);
//
//    List<Transaction> findTransactionsByReceiverAndStatusAndTimestampGreaterThanOrderByTimestampDesc(User receiver, TransactionType status, String timestamp);
//
//    List<Transaction> findTransactionsBySender(User sender);
//
    List<Transaction> findTransactionsBySenderAndStatusAndDateGreaterThanOrderByDateDesc(User sender, TransactionStatus status, String date);

    List<Transaction> findTransactionsByReceiverAndStatusAndDateGreaterThanOrderByDateDesc(User receiver, TransactionStatus status, String date);

    List<Transaction> findTransactionsBySenderAndStatusOrderByDateDesc(User sender, TransactionStatus status);

    List<Transaction> findTransactionsByReceiverAndStatusOrderByDateDesc(User receiver, TransactionStatus status);

    List<Transaction> findTransactionsBySenderAndDateGreaterThan(User sender, String date);
//
//    List<Transaction> findTransactionsBySenderAndTimestampGreaterThanOrderByTimestampDesc(User sender, String timestamp);
//
//    List<Transaction> findTransactionBySenderOrderByTimestampDesc(User sender);
//
//    List<Transaction> findTransactionByTimestampGreaterThanOrderByTimestampDesc(String timeStamp);
//
//    List<Transaction> findTransactionByTimestampGreaterThanOrderByTimestampDesc(String timeStamp, Pageable pageable);
//
    List<Transaction> findTransactionsByStatusOrderByDateDesc(TransactionStatus status, Pageable pageable);

    Page<Transaction> findTransactionsBySenderAndStatusOrderByDateDesc(User sender, TransactionStatus status, Pageable pageable);

    Page<Transaction> findTransactionsByReceiverAndStatusOrderByDateDesc(User receiver, TransactionStatus status, Pageable pageable);

    Page<Transaction> findTransactionsBySenderOrReceiverAndStatusOrderByDateDesc(User sender, User receiver, TransactionStatus status, Pageable pageable);

    //Transaction findTransactionById(String id);
}
