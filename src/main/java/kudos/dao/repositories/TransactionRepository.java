package kudos.dao.repositories;

import kudos.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by chc on 15.8.6.
 */
@Repository
public interface TransactionRepository extends MongoRepository<Transaction,String> {

    Transaction findLastTransactionByReceiverEmail(String receiverEmail);

    List<Transaction> findTransactionsByReceiverEmail(String receiverEmail);

}
