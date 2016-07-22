package kudos.repositories;

import kudos.model.Transaction;
import kudos.model.TransactionType;
import kudos.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

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
//    List<Transaction> findTransactionsBySenderAndStatus(User sender, TransactionType status);
//
//    List<Transaction> findTransactionsBySenderAndTimestampGreaterThanOrderByTimestampDesc(User sender, String timestamp);
//
//    List<Transaction> findTransactionBySenderOrderByTimestampDesc(User sender);
//
//    List<Transaction> findTransactionByTimestampGreaterThanOrderByTimestampDesc(String timeStamp);
//
//    List<Transaction> findTransactionByTimestampGreaterThanOrderByTimestampDesc(String timeStamp, Pageable pageable);
//
    List<Transaction> findTransactionsByTypeOrderByDateDesc(TransactionType type, Pageable pageable);
}
