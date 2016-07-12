package kudos.services;

import kudos.KudosBusinessStrategy;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import kudos.web.beans.response.TransactionResponse;
import kudos.web.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    SimpleDateFormat transactionDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    @Autowired
    private UsersService usersService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository repository;
    @Autowired
    private KudosBusinessStrategy strategy;

    public List<Transaction> getAllTrransactionsMadeThisWeek() {
        return repository.findTransactionByTimestampGreaterThanOrderByTimestampDesc(transactionDateFormat.format(strategy.getStartTime().toDate()));
    }

    public List<Transaction> getPageableTransactionsMadeThisWeek(int page, int pageSize) {
        return repository.findTransactionByTimestampGreaterThanOrderByTimestampDesc(transactionDateFormat.format(strategy.getStartTime().toDate()), new PageRequest(page, pageSize));
    }

    public List<TransactionResponse> getPageableTransactionsByStatus(Transaction.Status status, int page, int pageSize){
        return repository.findTransactionByStatusOrderByTimestampDesc(status, new PageRequest(page, pageSize)).stream().map(TransactionResponse::new).collect(Collectors.toList());
    }

    public List<TransactionResponse> getNewTransactions(String timestamp) throws UserException{
        User currentUser = usersService.getLoggedUser().get();
        if(timestamp == null){
            setLastSeenTransactionTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS")));
        }
        return repository.findTransactionsByReceiverAndStatusAndTimestampGreaterThanOrderByTimestampDesc(currentUser, Transaction.Status.COMPLETED, timestamp)
                .stream().map(TransactionResponse::new).collect(Collectors.toList());
    }

    public boolean isLastTransactionChanged(String lastTransactionTimestamp){
        return !lastTransactionTimestamp.equals(repository.findFirstByOrderByTimestampDesc().getTimestamp());
    }

    public void setLastSeenTransactionTimestamp(String timestamp) throws UserException{
        User currentUser = usersService.getLoggedUser().get();
        currentUser.setLastSeenTransactionTimestamp(timestamp);
        userRepository.save(currentUser);
    }

    public List<Transaction> test(String id) throws UserException{
        User user = usersService.findById(id).get();
        return repository.findTransactionsByReceiver(user);
    }

}
