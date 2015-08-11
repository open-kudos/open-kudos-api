package kudos.services;

import com.google.common.base.Optional;
import com.mongodb.MongoException;
import kudos.KudosBusinessStrategy;
import kudos.exceptions.KudosExceededException;
import kudos.model.User;
import kudos.repositories.TransactionRepository;
import kudos.model.Transaction;
import kudos.web.beans.response.Response;
import kudos.web.beans.response.SingleErrorResponse;
import kudos.web.beans.response.TransferResponse;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.util.List;

/**
 * Created by chc on 15.8.7.
 */
@Service
public class KudosService {

    private final Logger LOG = Logger.getLogger(KudosService.class.getName());

    private final UsersService usersService;
    private final TransactionRepository repository;
    private final KudosBusinessStrategy strategy;
    private final DateTimeFormatter dateTimeFormatter;

    @Autowired
    public KudosService(UsersService usersService,
                        TransactionRepository repository,
                        KudosBusinessStrategy strategy,
                        @Qualifier("DBTimeFormatter") DateTimeFormatter dateTimeFormatter) {

        this.usersService = usersService;
        this.repository = repository;
        this.strategy = strategy;
        this.dateTimeFormatter = dateTimeFormatter;
    }


    /*private void transferKudos(Transaction transaction) {

        int amount = transaction.getAmount();
        Transaction lastTransaction = repository.findTransactionByReceiverEmailOrderByTimestampDesc(transaction.getReceiver());

        if(lastTransaction == null) {
            LOG.warn("There was no last transaction detected");
            transaction.setReceiverBalance(amount);
        } else if(canUserCanSpendKudos(transaction.getSenderEmail())){
            transaction.setReceiverBalance(lastTransaction.getReceiverBalance() + amount);
        } else {
            return new ResponseEntity<>(TransferResponse.fail("limit.exceeded"), HttpStatus.BAD_REQUEST);
        }

        try{
            repository.save(transaction);
        } catch(MongoException e){
            return new ResponseEntity<>(new SingleErrorResponse("mongo.error"),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }*/

    public Transaction transfer(User receiver, int amount, String message) throws KudosExceededException, MongoException {

        User user = usersService.getLoggedUser().get();
        String senderEmail = user.getEmail();
        String receiverEmail = receiver.getEmail();

        Transaction lastTransaction = repository.findTransactionByReceiverEmailOrderByTimestampDesc(receiverEmail);
        Transaction newTransaction = new Transaction(receiverEmail,user.getEmail(),amount,message);

        if(!canUserCanSpendKudos(senderEmail)){
            throw new KudosExceededException();
        }

        if(lastTransaction == null) {
            newTransaction.setReceiverBalance(amount);
        } else if(canUserCanSpendKudos(senderEmail)){
            newTransaction.setReceiverBalance(lastTransaction.getReceiverBalance() + amount);
        }

        return repository.insert(newTransaction);

    }



    public boolean canUserCanSpendKudos(String senderEmail) {
        return strategy.getDeposit() - periodDeposit(senderEmail, strategy.getStartTime()) >= strategy.getMinDeposit();
    }

    public int periodBalance(String userId, LocalDateTime startTime){
        return strategy.getDeposit() - periodDeposit(userId,startTime);
    }

    public int periodDeposit(String userId, LocalDateTime startTime){
        List<Transaction> transactions = repository.findTransactionBySenderEmailOrderByTimestampDesc(userId);

        int periodDeposit = 0;

        for (int i = 0; i < transactions.size(); i++) {
            LocalDateTime transactionTime = dateTimeFormatter.parseLocalDateTime(transactions.get(i).getTimestamp());
            int amount = transactions.get(i).getAmount();
            if (startTime.isAfter(transactionTime)) {
                break;
            }
            LOG.trace("transaction amount is: " + amount);
            periodDeposit += amount;
        }

        return periodDeposit;
    }

}
