package kudos.services;

import com.mongodb.MongoException;
import kudos.KudosBusinessStrategy;
import kudos.exceptions.BusinessException;
import kudos.exceptions.InvalidKudosAmountException;
import kudos.model.*;
import kudos.repositories.TransactionRepository;
import kudos.exceptions.UserException;
import kudos.repositories.UserRepository;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class KudosService {

//    SimpleDateFormat transactionDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
//    SimpleDateFormat responseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

//    @Autowired
//    private DateTimeFormatter dateTimeFormatter;
//
//    @Autowired
//    public KudosService(@Qualifier("DBTimeFormatter") DateTimeFormatter dateTimeFormatter) {
//        this.dateTimeFormatter = dateTimeFormatter;
//    }
//
    public void giveKudos(User sender, User receiver, int amount, String message) throws InvalidKudosAmountException {
        if (amount < 1 || sender.getWeeklyKudos() < amount) {
            throw new InvalidKudosAmountException("invalid_kudos_amount");
        }

        transactionRepository.save(new Transaction(sender, receiver, amount, message, TransactionType.KUDOS,
                LocalDateTime.now().toString(), TransactionStatus.COMPLETED));

        sender.setWeeklyKudos(sender.getWeeklyKudos() - amount);
        userRepository.save(sender);

        receiver.setTotalKudos(receiver.getTotalKudos()+amount);
        userRepository.save(receiver);
    }
//
//    public Transaction reduceFreeKudos(User user,  int amount, String message) throws BusinessException, UserException {
//        return transferKudos(user, usersService.getKudosMaster(), amount, message, TransactionType.KUDOS);
//    }
//
//    public Transaction takeSystemKudos(User receiver, int amount, String message, TransactionType status) throws BusinessException, UserException {
//        return transferKudos(usersService.getKudosMaster(), receiver , amount, message, status);
//    }
//
//    public Transaction retrieveSystemKudos(User to, int amount, String message, TransactionType status) throws BusinessException {
//        Transaction newTransaction = new Transaction(to, to, -amount, message, status, "");
//        //newTransaction.setReceiverBalance(getKudos(to));
//        return repository.insert(newTransaction);
//    }
//
//    private Transaction transferKudos(User sender, User receiver, int amount, String message, TransactionType status) throws BusinessException {
//        Transaction newTransaction = new Transaction(sender, receiver, amount, message, status, "");
//        newTransaction.setType(status);
//
//        if (amount < strategy.getMinDeposit()) {
//            throw new InvalidKudosAmountException("invalid_kudos_amount");
//        }
//
//        if (status != TransactionType.KUDOS && getFreeKudos(sender) < amount){
//            throw new KudosExceededException("exceeded_kudos");
//        }
//
//        //newTransaction.setReceiverBalance(amount + getKudos(receiver));
//
//        return repository.insert(newTransaction);
//    }
//
//    public int getKudos(User user) {
//        Transaction lastTransaction = repository.findTransactionByReceiverOrderByTimestampDesc(user);
//        return lastTransaction == null ? 0 : user.getTotalKudos();
//    }
//
//    public int getFreeKudos(User user) {
//        return strategy.getDeposit() - calculateSpentKudos(user, strategy.getStartTime());
//    }
//
//    public int calculateSpentKudos(User user, LocalDateTime startTime){
//        return repository.findTransactionBySenderOrderByTimestampDesc(user).stream()
//                .filter(t -> dateTimeFormatter.parseLocalDateTime(t.getDate()).isAfter(startTime))
//                .mapToInt(Transaction::getAmount)
//                .sum();
//    }
//
//    public List<Transaction> getAllLoggedUserOutgoingTransactions() throws UserException {
//        User user = usersService.getLoggedUser().get();
//
//        List<Transaction> formattedDateTransactions = new ArrayList<>();
//
//        for (Transaction transaction : repository.findTransactionsBySenderAndStatus(user, TransactionType.KUDOS)){
//            try {
//                transaction.setDate(responseFormat.format(transactionDateFormat.parse(transaction.getDate())));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            if (!transaction.getReceiver().equals("master@of.kudos"))
//                if (!transaction.getReceiver().equals(user.getEmail()))
//                    formattedDateTransactions.add(transaction);
//        }
//        return formattedDateTransactions;
//    }
//
//    public List<Transaction> getAllLoggedUserIncomingTransactions() throws UserException {
//        User user = usersService.getLoggedUser().get();
//        List<Transaction> formattedDateTransactions = new ArrayList<>();
//
//        for (Transaction transaction : repository.findTransactionsByReceiverAndStatus(user, TransactionType.KUDOS)){
//            try {
//                transaction.setDate(responseFormat.format(transactionDateFormat.parse(transaction.getDate())));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            if (!transaction.getSender().getEmail().equals(user.getEmail()))
//                formattedDateTransactions.add(transaction);
//        }
//        return formattedDateTransactions;
//    }
//
//    public Transaction save(Transaction transaction){
//        return repository.save(transaction);
//    }

}
