package kudos.services;

import kudos.exceptions.InvalidKudosAmountException;
import kudos.model.*;
import kudos.model.status.TransactionStatus;
import kudos.model.status.TransactionType;
import kudos.repositories.TransactionRepository;
import kudos.exceptions.UserException;
import kudos.repositories.UserRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KudosService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction giveKudos(User sender, User receiver, int amount, String message, String endorsement) throws InvalidKudosAmountException,
            UserException {
        if (sender.getEmail().equals(receiver.getEmail())) {
            throw new UserException("cant_give_kudos_to_yourself");
        }

        if (amount < 1 || sender.getWeeklyKudos() < amount) {
            throw new InvalidKudosAmountException("invalid_kudos_amount");
        }

        Transaction transaction = transactionRepository.save(new Transaction(sender, receiver, amount, message, endorsement,
                TransactionType.KUDOS, LocalDateTime.now().toString(), TransactionStatus.COMPLETED));

        receiver.setTotalKudos(receiver.getTotalKudos() + amount);
        userRepository.save(receiver);
        return transaction;
    }

}
