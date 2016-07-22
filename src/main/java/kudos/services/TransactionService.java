package kudos.services;

import kudos.model.TransactionStatus;
import kudos.model.TransactionType;
import kudos.repositories.TransactionRepository;
import kudos.web.beans.response.KudosTransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

//    SimpleDateFormat transactionDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
//
//    @Autowired
//    private UsersService usersService;
//    @Autowired
//    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
//    @Autowired
//    private KudosBusinessStrategy strategy;
//
//    public List<Transaction> getAllTrransactionsMadeThisWeek() {
//        return repository.findTransactionByTimestampGreaterThanOrderByTimestampDesc(transactionDateFormat.format(strategy.getStartTime().toDate()));
//    }
//
//    public List<Transaction> getPageableTransactionsMadeThisWeek(int page, int pageSize) {
//        return repository.findTransactionByTimestampGreaterThanOrderByTimestampDesc(transactionDateFormat.format(strategy.getStartTime().toDate()), new PageRequest(page, pageSize));
//    }
//
    public List<KudosTransactionResponse> getLatestTransactions(){
        return transactionRepository.findTransactionsByStatusOrderByDateDesc(TransactionStatus.COMPLETED,
                new PageRequest(0, 10)).stream().map(KudosTransactionResponse::new).collect(Collectors.toList());
    }
//
//    public List<TransactionResponse> getNewTransactions(String timestamp) throws UserException{
//        User currentUser = usersService.getLoggedUser().get();
//        if(timestamp == null){
//            setLastSeenTransactionTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS")));
//        }
//        return repository.findTransactionsByReceiverAndStatusAndTimestampGreaterThanOrderByTimestampDesc(currentUser, TransactionType.KUDOS, timestamp)
//                .stream().map(TransactionResponse::new).collect(Collectors.toList());
//    }
//
//    public boolean isLastTransactionChanged(String lastTransactionTimestamp){
//        return !lastTransactionTimestamp.equals(repository.findFirstByOrderByTimestampDesc().getDate());
//    }
//
//    public void setLastSeenTransactionTimestamp(String timestamp) throws UserException{
//        User currentUser = usersService.getLoggedUser().get();
//        currentUser.setLastNotificationCheckTime(timestamp);
//        userRepository.save(currentUser);
//    }
//
//    public List<Transaction> test(String id) throws UserException{
//        User user = usersService.findById(id).get();
//        return repository.findTransactionsByReceiver(user);
//    }

}
