package kudos.services;

import com.mongodb.MongoException;
import kudos.KudosBusinessStrategy;
import kudos.exceptions.BusinessException;
import kudos.exceptions.InvalidKudosAmountException;
import kudos.exceptions.KudosExceededException;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.repositories.TransactionRepository;
import kudos.web.exceptions.UserException;
import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chc on 15.8.7.
 */
@Service
public class KudosService {

    private final Logger LOG = Logger.getLogger(KudosService.class.getName());
    SimpleDateFormat transactionDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
    SimpleDateFormat responseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    private UsersService usersService;
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private KudosBusinessStrategy strategy;
    @Autowired
    private DateTimeFormatter dateTimeFormatter;

    @Autowired
    public KudosService(@Qualifier("DBTimeFormatter") DateTimeFormatter dateTimeFormatter) {
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public Transaction giveKudos(User to, int amount, String message) throws BusinessException, MongoException, UserException {
        User user = usersService.getLoggedUser().get();
        return transferKudos(to, user, amount, message, Transaction.Status.COMPLETED);
    }

    public Transaction reduceFreeKudos(User user,  int amount, String message) throws BusinessException {
        return transferKudos(usersService.getKudosMaster(), user, amount, message, Transaction.Status.PENDING_CHALLENGE);
    }

    public Transaction takeSystemKudos(User to, int amount, String message, Transaction.Status status) throws BusinessException {
        return transferKudos(to, usersService.getKudosMaster(), amount, message, status);
    }

    public Transaction retrieveSystemKudos(User to, int amount, String message, Transaction.Status status) throws BusinessException {
        Transaction newTransaction = new Transaction(to.getEmail(), to.getFirstName(), to.getEmail(), to.getFirstName(), -amount, message, status);
        newTransaction.setReceiverBalance(getKudos(to));
        return repository.insert(newTransaction);
    }

    private Transaction transferKudos(User to, User from, int amount, String message, Transaction.Status status) throws BusinessException {
        Transaction newTransaction = new Transaction(to.getEmail(), to.getFirstName(), from.getEmail(), from.getFirstName(), amount, message, status);
        newTransaction.setStatus(status);

        if (amount < strategy.getMinDeposit()) {
            throw new InvalidKudosAmountException("invalid_kudos_amount");
        }

        if (getFreeKudos(from) < amount && status != Transaction.Status.COMPLETED_CHALLENGE){
            throw new KudosExceededException("exceeded_kudos");
        }

        newTransaction.setReceiverBalance(amount + getKudos(to));

        return repository.insert(newTransaction);
    }

    public int getKudos(User user) {
        Transaction lastTransaction = repository.findTransactionByReceiverEmailOrderByTimestampDesc(user.getEmail());
        return lastTransaction == null ? 0 : lastTransaction.getReceiverBalance();
    }

    public int getFreeKudos(User user) {
        return strategy.getDeposit() - calculateSpentKudos(user, strategy.getStartTime());
    }

    public int calculateSpentKudos(User user, LocalDateTime startTime){
        return repository.findTransactionBySenderEmailOrderByTimestampDesc(user.getEmail()).stream()
                .filter(t -> dateTimeFormatter.parseLocalDateTime(t.getTimestamp()).isAfter(startTime))
                .mapToInt(Transaction::getAmount)
                .sum();
    }

    public List<Transaction> getAllLoggedUserOutgoingTransactions() throws UserException {
        User user = usersService.getLoggedUser().get();

        List<Transaction> formattedDateTransactions = new ArrayList<>();

        for (Transaction transaction : repository.findTransactionsBySenderEmailAndStatus(user.getEmail(), Transaction.Status.COMPLETED)){
            try {
                transaction.setTimestamp(responseFormat.format(transactionDateFormat.parse(transaction.getTimestamp())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!transaction.getReceiver().equals("master@of.kudos"))
                if (!transaction.getReceiver().equals(user.getEmail()))
                    formattedDateTransactions.add(transaction);
        }
        return formattedDateTransactions;
    }

    public List<Transaction> getAllLoggedUserIncomingTransactions() throws UserException {
        User user = usersService.getLoggedUser().get();
        List<Transaction> formattedDateTransactions = new ArrayList<>();

        for (Transaction transaction : repository.findTransactionsByReceiverEmailAndStatus(user.getEmail(), Transaction.Status.COMPLETED)){
            try {
                transaction.setTimestamp(responseFormat.format(transactionDateFormat.parse(transaction.getTimestamp())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (!transaction.getSenderEmail().equals(user.getEmail()))
                formattedDateTransactions.add(transaction);
        }
        return formattedDateTransactions;
    }

    public Transaction save(Transaction transaction){
        return repository.save(transaction);
    }

}
