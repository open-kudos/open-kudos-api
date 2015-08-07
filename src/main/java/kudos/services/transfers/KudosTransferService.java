package kudos.services.transfers;

import com.mongodb.MongoException;
import kudos.dao.repositories.TransactionRepository;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.services.control.KudosAmountControlService;
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

    private TransactionRepository transactionRepository;
    private KudosAmountControlService kudosAmountControlService;

    @Autowired
    public KudosTransferService(TransactionRepository transactionRepository, KudosAmountControlService kudosAmountControlService){
        this.transactionRepository = transactionRepository;
        this.kudosAmountControlService = kudosAmountControlService;
    }

    private Logger LOG = Logger.getLogger(KudosTransferService.class.getName());

    public ResponseEntity<Response> transferKudos(Transaction transaction) {

        int amount = transaction.getKudosType().amount;
        Transaction lastTransaction = transactionRepository.findTransactionByReceiverEmailOrderByTimestampDesc(transaction.getReceiver());

        if(lastTransaction == null) {
            LOG.warn("There was no last transaction detected");
            transaction.setReceiverBalance(amount);
        } else if(kudosAmountControlService.canUserCanSpendKudos(transaction.getSenderEmail())){
            transaction.setReceiverBalance(lastTransaction.getReceiverBalance() + amount);
        } else {
            return new ResponseEntity<>(TransferResponse.fail("Sorry, your weekly kudos limit has exceeded"),HttpStatus.BAD_REQUEST);
        }

        try{
            transactionRepository.save(transaction);
            return new ResponseEntity<>(TransferResponse.success(), HttpStatus.OK);
        } catch(MongoException e){
            return new ResponseEntity<>(TransferResponse.fail(e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
