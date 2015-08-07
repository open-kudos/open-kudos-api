package kudos.repositories;

import kudos.model.object.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chc on 15.8.6.
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction,String> {

    Transaction findTransactionByReceiverEmailOrderByTimestampDesc(String receiverEmail);

    List<Transaction> findTransactionsByReceiverEmail(String receiverEmail);

    List<Transaction> findTransactionsBySenderEmail(String senderEmail);

    List<Transaction> findTransactionBySenderEmailOrderByTimestampDesc (String senderEmail);

}
