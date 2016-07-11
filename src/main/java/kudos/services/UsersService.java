package kudos.services;

import com.google.common.base.Strings;
import freemarker.template.TemplateException;
import kudos.model.Challenge;
import kudos.model.LeaderboardUser;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import kudos.web.beans.form.MyProfileForm;
import kudos.web.exceptions.UserException;
import org.apache.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private KudosService kudosService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private Logger LOG = Logger.getLogger(UsersService.class);

    private static final User DELETED_USER_TAG = null;

    public User getKudosMaster() throws UserException {
        User masterOfKudos;
        try {
            masterOfKudos = findByEmail("master@of.kudos").get();
        } catch (NoSuchElementException e) {
            masterOfKudos = new User("pass", "master@of.kudos");
            userRepository.save(masterOfKudos);
        }
        return masterOfKudos;
    }

    public Optional<User> findByEmail(String email) throws UserException {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public Optional<User> findById(String id) throws UserException {
        return Optional.ofNullable(userRepository.findById(id));
    }

    public Optional<User> getLoggedUser() throws UserException {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByEmail(name);
    }

    public User registerUser(User user) throws UserException, MessagingException, IOException, TemplateException {
        if (userRepository.exists(user.getEmail().toLowerCase())) throw new UserException("user_already_exists");

        String password = new StrongPasswordEncryptor().encryptPassword(user.getPassword());
        User newUser = new User(user.getFirstName(), user.getLastName(), password, user.getEmail().toLowerCase());
        newUser.setEmailHash(getRandomHash());
        userRepository.save(newUser);
        sendConfirmationCodeEmail(user.getEmail().toLowerCase());
        return newUser;
    }

    public User confirmUser(String hashedEmail) throws UserException {
        Optional<User> maybeUser = userRepository.findUserByEmailHash(hashedEmail);
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            user.markUserAsConfirmed();
            userRepository.save(user);
            return userRepository.save(user);
        } else {
            throw new UserException("user_not_found");
        }
    }

    public User updateUser(MyProfileForm myProfileForm) throws UserException, MessagingException, IOException, TemplateException {
        User user = getLoggedUser().get();
        User updatedUser = user.getUpdatedUser(myProfileForm);
        userRepository.save(updatedUser);
        return updatedUser;
    }

    public void disableUsersAccount() throws UserException {
        wipeAllUserData();
    }

    public User getCompletedUser() throws UserException {
        User user = getLoggedUser().get();
        return user;
    }

    public User login(String email, String password, HttpServletRequest request) throws AuthenticationCredentialsNotFoundException, UserException {
        loginValidation(email.toLowerCase(), password);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email.toLowerCase(), password)));
        request.getSession().setMaxInactiveInterval(0);
        request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        return findByEmail(email.toLowerCase()).get();
    }

    private User loginValidation(String email, String password) throws UserException {
        Optional<User> maybeUser = findByEmail(email);

        if (Strings.isNullOrEmpty(email)) throw new UserException("email_not_specified");

        if (Strings.isNullOrEmpty(password)) throw new UserException("password_not_specified");

        if (getLoggedUser().isPresent()) throw new UserException("user_already_logged");

        if (!maybeUser.isPresent()) throw new UserException("user_not_exist");

        if (!maybeUser.get().isConfirmed()) throw new UserException("user_not_confirmed");

        return maybeUser.get();
    }

    public void resetPassword(String email) throws UserException, MessagingException, IOException, TemplateException {
        Optional<User> maybeUser = findByEmail(email);

        if (Strings.isNullOrEmpty(email)) throw new UserException("email_not_specified");

        if (maybeUser.isPresent()) throw new UserException("user_not_exist");

        User user = maybeUser.get();
        String resetHash = getRandomHash();
        user.setEmailHash(resetHash);

        userRepository.save(user);
    }

    public void sendConfirmationCodeEmail(String email) throws UserException, MessagingException, IOException, TemplateException {
        Optional<User> maybeUser = findByEmail(email);
        User user = maybeUser.get();
        String message = "Your confirmation code is : <b>" + user.getEmailHash() + "</b>";
        emailService.generateAndSendEmail(email, message);
    }

    public List<LeaderboardUser> getTopSenders(String period) {
        List<LeaderboardUser> topSenders = getAllConfirmedUsers().stream().map(user -> createLeaderboardUser(user, calculateSendersTransactionsAmount(user, period))).collect(Collectors.toList());
        return sortListByAmountOfKudos(topSenders, 0, 5);
    }

    public List<LeaderboardUser> getTopReceivers(String period) {
        List<LeaderboardUser> topReceivers = getAllConfirmedUsers().stream().map(user -> createLeaderboardUser(user, calculateReceiversTransactionsAmount(user, period))).collect(Collectors.toList());
        return sortListByAmountOfKudos(topReceivers, 0, 5);
    }

    private List<LeaderboardUser> sortListByAmountOfKudos(List<LeaderboardUser> leaderboardUserList, int startingIndex, int endingIndex) {
        try {
            return leaderboardUserList.stream().sorted((l1, l2) -> l2.getAmountOfKudos().compareTo(l1.getAmountOfKudos())).collect(Collectors.toList()).subList(startingIndex, endingIndex);
        } catch (IndexOutOfBoundsException e) {
            return leaderboardUserList.stream().sorted((l1, l2) -> l2.getAmountOfKudos().compareTo(l1.getAmountOfKudos())).collect(Collectors.toList()).subList(0, leaderboardUserList.size());
        }
    }

    private int calculateSendersTransactionsAmount(User user, String period) {
        if (period.equals("week"))
            return transactionRepository.findTransactionsBySenderAndTimestampGreaterThanOrderByTimestampDesc(user, LocalDateTime.now().minusDays(7).toString())
                    .stream().filter(transaction -> transaction.getStatus() == Transaction.Status.COMPLETED || transaction.getStatus() == Transaction.Status.COMPLETED_CHALLENGE)
                    .mapToInt(Transaction::getAmount).sum();
        else if (period.equals("month"))
            return transactionRepository.findTransactionsBySenderAndTimestampGreaterThanOrderByTimestampDesc(user, LocalDateTime.now().minusDays(30).toString())
                    .stream().filter(transaction -> transaction.getStatus() == Transaction.Status.COMPLETED || transaction.getStatus() == Transaction.Status.COMPLETED_CHALLENGE)
                    .mapToInt(Transaction::getAmount).sum();
        else return transactionRepository.findTransactionsBySender(user)
                    .stream().filter(transaction -> transaction.getStatus() == Transaction.Status.COMPLETED || transaction.getStatus() == Transaction.Status.COMPLETED_CHALLENGE)
                    .mapToInt(Transaction::getAmount).sum();
    }

    private int calculateReceiversTransactionsAmount(User user, String period) {
        if (period.equals("week")) return calculateReceiverTransactionAmountByPeriod(user, 7);
        else if (period.equals("month")) return calculateReceiverTransactionAmountByPeriod(user, 30);
        else return transactionRepository.findTransactionsByReceiver(user)
                    .stream().filter(transaction -> transaction.getStatus() == Transaction.Status.COMPLETED || transaction.getStatus() == Transaction.Status.COMPLETED_CHALLENGE)
                    .mapToInt(Transaction::getAmount).sum();
    }

    private int calculateReceiverTransactionAmountByPeriod(User user, int period) {
        return transactionRepository.findTransactionsByReceiverAndTimestampGreaterThanOrderByTimestampDesc(user, LocalDateTime.now().minusDays(period).toString())
                .stream().filter(transaction -> transaction.getStatus() == Transaction.Status.COMPLETED || transaction.getStatus() == Transaction.Status.COMPLETED_CHALLENGE)
                .mapToInt(Transaction::getAmount).sum();
    }

    private LeaderboardUser createLeaderboardUser(User user, int kudosAmount) {
        return new LeaderboardUser(user.getFirstName(), user.getLastName(), user.getEmail(), kudosAmount);
    }

    private String getRandomHash() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

    private void wipeAllUserData() throws UserException {

        challengeService.getAllUserCreatedChallenges().stream()
                .forEach(challenge -> {
                    challenge.setCreatorUser(DELETED_USER_TAG);
                    challengeService.save(challenge);
                });

        challengeService.getAllUserParticipatedChallenges().stream()
                .forEach(challenge -> {
                    challenge.setParticipantUser(DELETED_USER_TAG);
                    challengeService.save(challenge);
                });

        kudosService.getAllLoggedUserIncomingTransactions().stream()
                .forEach(transaction -> {
                    transaction.setReceiver(DELETED_USER_TAG);
                    kudosService.save(transaction);
                });

        kudosService.getAllLoggedUserOutgoingTransactions().stream()
                .forEach(transaction -> {
                    transaction.setSender(DELETED_USER_TAG);
                    kudosService.save(transaction);
                });

        userRepository.delete(getLoggedUser().get());
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
                System.out.println("---GG: " + e);
            }

            try {
                transactionsToChangeBySender = transactionRepository.findTransactionsBySenderEmail(user.getEmail());
                for (Transaction transaction : transactionsToChangeBySender){
                    transaction.setSender(user);
                    transactionRepository.save(transaction);
                }
            } catch (Exception e){
                System.out.println("---GG: " + e);
            }

            try{
                challengesToChangeByCreator = challengeRepository.findChallengesByCreator(user.getEmail());
                for (Challenge challenge : challengesToChangeByCreator){
                    challenge.setCreatorUser(user);
                    challengeRepository.save(challenge);
                }
            }catch (Exception e){
                System.out.println("---GG: " + e);
            }

            try {
                challengesToChangeByParticipant = challengeRepository.findChallengesByParticipant(user.getEmail());
                for (Challenge challenge : challengesToChangeByParticipant){
                    challenge.setParticipantUser(user);
                    challengeRepository.save(challenge);
                }
            }catch (Exception e){
                System.out.println("---GG: " + e);
            }

        }
    }

    public List<Challenge> migrateChallengesToNewModel(){

        List<Challenge> challenges;

        challenges = challengeRepository.findAll();

        return challenges;
    }

    public List<User> list(String filter) {
        return userRepository.searchAllFields(filter);
    }

    public List<User> getAllConfirmedUsers() {
        return userRepository.findUsersByIsConfirmed(true);
    }

}
