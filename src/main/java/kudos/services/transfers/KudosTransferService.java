package kudos.services.transfers;

import com.mongodb.MongoException;
import kudos.dao.repositories.TransactionRepository;
import kudos.model.Transaction;
import kudos.web.model.Response;
import kudos.web.model.TransferResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by chc on 15.8.5.
 */
@Service
public class KudosTransferService {

    @Autowired
    private TransactionRepository transactionRepository;

    private Logger LOG = Logger.getLogger(KudosTransferService.class.getName());

    public ResponseEntity<Response> transferKudos(Transaction transaction) {

        int amount = transaction.getKudosType().amount;
        Transaction lastTransaction = transactionRepository.findLastTransactionByReceiverEmail(transaction.getReceiver());

        if(lastTransaction == null) {
            transaction.setReceiverBalance(amount);
        } else {
            transaction.setReceiverBalance(lastTransaction.getReceiverBalance() + amount);
        }

        try{
            transactionRepository.insert(transaction);
            return new ResponseEntity<>(TransferResponse.success(transactionRepository.findAll().size()+""), HttpStatus.OK);
        } catch(MongoException e){
            return new ResponseEntity<>(TransferResponse.fail(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
