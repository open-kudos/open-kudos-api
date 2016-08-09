package kudos.repositories;

import kudos.model.Transaction;
import kudos.model.TransactionStatus;
import kudos.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction,String> {

    List<Transaction> findTransactionsBySenderAndStatusAndDateGreaterThanOrderByDateDesc(User sender, TransactionStatus status, String date);

    List<Transaction> findTransactionsByReceiverAndStatusAndDateGreaterThanOrderByDateDesc(User receiver, TransactionStatus status, String date);

    List<Transaction> findTransactionsBySenderAndStatusOrderByDateDesc(User sender, TransactionStatus status);

    List<Transaction> findTransactionsByReceiverAndStatusOrderByDateDesc(User receiver, TransactionStatus status);

    List<Transaction> findTransactionsBySenderAndDateGreaterThan(User sender, String date);

    List<Transaction> findTransactionsByStatusOrderByDateDesc(TransactionStatus status, Pageable pageable);

    List<Transaction> findTransactionsBySenderOrReceiverAndStatusOrderByDateDesc(User sender, User receiver, TransactionStatus status);

    Page<Transaction> findTransactionsBySenderAndStatusOrderByDateDesc(User sender, TransactionStatus status, Pageable pageable);

    Page<Transaction> findTransactionsByReceiverAndStatusOrderByDateDesc(User receiver, TransactionStatus status, Pageable pageable);

    Page<Transaction> findTransactionsBySenderOrReceiverAndStatusOrderByDateDesc(User sender, User receiver, TransactionStatus status, Pageable pageable);

}
