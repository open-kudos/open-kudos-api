package kudos.services;

import kudos.model.Challenge;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import kudos.web.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MigrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    public Optional<User> findByEmail(String email) throws UserException {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public List<User> findAllAndCreateNewUsers() {
        List<User> allUsers = userRepository.findAll();

        User masterOfKudos;
        try {
            masterOfKudos = findByEmail("master@of.kudos").get();
        } catch (NoSuchElementException e) {
            masterOfKudos = new User("pass", "master@of.kudos");
            userRepository.save(masterOfKudos);
        } catch (UserException e) {
            e.printStackTrace();
        }

        for (User user : allUsers) {
            User userToCreate;
            if (user.getEmail() == null) {
                userToCreate = new User(user.getFirstName(), user.getLastName(), user.getPassword(), user.getId().toLowerCase());
            } else{
                userToCreate = new User(user.getFirstName(), user.getLastName(), user.getPassword(), user.getEmail().toLowerCase());
            }

            if (user.getEmailHash() != null) {
                userToCreate.setEmailHash(user.getEmailHash());
            }

            if (user.isConfirmed()) {
                userToCreate.markUserAsConfirmed();
            }

            if (user.isCompleted()) {
                userToCreate.setCompleted(true);
            }

            if (user.getLastSeenTransactionTimestamp() != null) {
                userToCreate.setLastSeenTransactionTimestamp(user.getLastSeenTransactionTimestamp());
            }

            userRepository.save(userToCreate);

            List<Transaction> transactionsToChangeByReceiver = new ArrayList<>();

            try {
                transactionsToChangeByReceiver = transactionRepository.findTransactionsByReceiver(user);
            } catch (Exception e) {
                if (transactionRepository.findTransactionsByReceiverEmail(user.getEmail()) != null) {
                    transactionsToChangeByReceiver = transactionRepository.findTransactionsByReceiverEmail(user.getEmail());
                }
            }

            for (Transaction transaction : transactionsToChangeByReceiver) {
                transaction.setReceiver(userToCreate);
                transactionRepository.save(transaction);
            }

            List<Transaction> transactionsToChangeBySender = new ArrayList<>();

            try {
                transactionsToChangeBySender = transactionRepository.findTransactionsBySender(user);
            } catch (Exception e) {
                if (transactionRepository.findTransactionsBySenderEmail(user.getEmail()) != null) {
                    transactionsToChangeBySender = transactionRepository.findTransactionsBySenderEmail(user.getEmail());
                }
            }

            for (Transaction transaction : transactionsToChangeBySender) {
                transaction.setSender(userToCreate);
                transactionRepository.save(transaction);
            }


            List<Challenge> challengesToChangeByCreator = new ArrayList<>();

            try {
                challengesToChangeByCreator = challengeRepository.findChallengesByCreatorUser(user);
            }catch (Exception e){
                if (challengeRepository.findChallengesByCreator(user.getEmail()) != null) {
                    challengesToChangeByCreator = challengeRepository.findChallengesByCreator(user.getEmail());
                }
            }

            for (Challenge challenge : challengesToChangeByCreator) {
                challenge.setCreatorUser(userToCreate);
                challengeRepository.save(challenge);
            }

            List<Challenge> challengesToChangeByParticipant = new ArrayList<>();

            try{
                challengesToChangeByParticipant = challengeRepository.findChallengesByParticipantUser(user);
            }catch (Exception e){
                if (challengeRepository.findChallengesByParticipant(user.getEmail()) != null) {
                    challengesToChangeByParticipant = challengeRepository.findChallengesByParticipant(user.getEmail());
                }
            }

            for (Challenge challenge : challengesToChangeByParticipant) {
                challenge.setParticipantUser(userToCreate);
                challengeRepository.save(challenge);
            }

            userRepository.delete(user);
        }

        changeAllTransactionsAndChallenges();
        return userRepository.findAll();
    }

    public void changeAllTransactionsAndChallenges() {
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers){

            List<Transaction> transactionsToChangeByReceiver;
            List<Transaction> transactionsToChangeBySender;
            List<Challenge> challengesToChangeByCreator;
            List<Challenge> challengesToChangeByParticipant;

            try {
                transactionsToChangeByReceiver = transactionRepository.findTransactionsByReceiverEmail(user.getEmail());
                for (Transaction transaction : transactionsToChangeByReceiver){
                    transaction.setReceiver(user);
                    transactionRepository.save(transaction);
                }
            } catch (Exception e){
                System.out.println(e);
            }

            try {
                transactionsToChangeBySender = transactionRepository.findTransactionsBySenderEmail(user.getEmail());
                for (Transaction transaction : transactionsToChangeBySender){
                    transaction.setSender(user);
                    transactionRepository.save(transaction);
                }
            } catch (Exception e){
                System.out.println(e);
            }

            try{
                challengesToChangeByCreator = challengeRepository.findChallengesByCreator(user.getEmail());
                for (Challenge challenge : challengesToChangeByCreator){
                    challenge.setCreatorUser(user);
                    challengeRepository.save(challenge);
                }
            }catch (Exception e){
                System.out.println(e);
            }

            try {
                challengesToChangeByParticipant = challengeRepository.findChallengesByParticipant(user.getEmail());
                for (Challenge challenge : challengesToChangeByParticipant){
                    challenge.setParticipantUser(user);
                    challengeRepository.save(challenge);
                }
            }catch (Exception e){
                System.out.println(e);
            }

        }
    }

}
