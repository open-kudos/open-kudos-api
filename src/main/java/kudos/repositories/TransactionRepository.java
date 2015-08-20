package kudos.repositories;

import kudos.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chc on 15.8.6.
 */
public interface TransactionRepository extends MongoRepository<Transaction,String> {

    Transaction findTransactionByReceiverEmailOrderByTimestampDesc(String receiverEmail);

    List<Transaction> findTransactionsByReceiverEmail(String receiverEmail);

    List<Transaction> findTransactionsBySenderEmail(String senderEmail);

    List<Transaction> findTransactionBySenderEmailOrderByTimestampDesc (String senderEmail);



}
