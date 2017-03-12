package kudos.repositories;

import kudos.model.Transaction;
import kudos.model.User;
import kudos.model.status.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction,String> {

    List<Transaction> findTransactionsBySenderAndStatusAndDateGreaterThanOrderByDateDesc(User sender, String status, String date);

    List<Transaction> findTransactionsByReceiverAndStatusAndDateGreaterThanOrderByDateDesc(User receiver, String status, String date);

    List<Transaction> findTransactionBySenderAndType(User receiver, TransactionType type);

    List<Transaction> findTransactionsBySenderAndStatusOrderByDateDesc(User sender, String status);

    List<Transaction> findTransactionsByReceiverAndStatusOrderByDateDesc(User receiver, String status);

    List<Transaction> findTransactionsBySenderAndDateGreaterThan(User sender, String date);

    List<Transaction> findTransactionsByReceiver(User receiver);

    List<Transaction> findTransactionsByStatusOrderByDateDesc(String status, Pageable pageable);

    List<Transaction> findTransactionsBySenderOrReceiverAndStatusOrderByDateDesc(User sender, User receiver, String status);

    Page<Transaction> findTransactionsBySenderAndStatusOrderByDateDesc(User sender, String status, Pageable pageable);

    Page<Transaction> findTransactionsByReceiverAndStatusOrderByDateDesc(User receiver, String status, Pageable pageable);

    Page<Transaction> findTransactionsBySenderOrReceiverAndStatusOrderByDateDesc(User sender, User receiver, String status, Pageable pageable);

    Page<Transaction> findTransactionsByReceiverAndEndorsementOrderByDateDesc(User receiver, String endorsement, Pageable pageable);

    List<Transaction> findTransactionsByReceiverAndEndorsementAndDateGreaterThanOrderByDateDesc(User receiver, String endorsement, String date);

    List<Transaction> findTransactionsByReceiverAndEndorsement(User receiver, String endorsement);

}
