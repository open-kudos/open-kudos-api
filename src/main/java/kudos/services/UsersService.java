package kudos.services;

import kudos.model.User;
import kudos.model.UserStatus;
import kudos.repositories.UserRepository;
import kudos.exceptions.UserException;
import org.jasypt.util.password.StrongPasswordEncryptor;
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
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Optional;

@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private TransactionRepository transactionRepository;
//
//    @Autowired
//    private ChallengeRepository challengeRepository;
//
    @Autowired
    private EmailService emailService;
//
//    @Autowired
//    private ChallengeService challengeService;
//
//    @Autowired
//    private KudosService kudosService;
//
    @Autowired
    private AuthenticationManager authenticationManager;
//
//    private Logger LOG = Logger.getLogger(UsersService.class);
//
//    private static final User DELETED_USER_TAG = null;
//
//    public User getKudosMaster() throws UserException {
//        Optional<User> masterOfKudos = findByEmail("master@of.kudos");
//        if(masterOfKudos.isPresent()) {
//            return masterOfKudos.get();
//        } else {
//            User newUser = new User("Master", "Kudos", "pass", "master@of.kudos", UserStatus.NOT_COMPLETED);
//            newUser.setEmailHash(getRandomHash());
//            userRepository.save(newUser);
//            return findByEmail("master@of.kudos").get();
//        }
//    }
//
//    public Optional<User> findByEmail(String email) throws UserException {
//        return Optional.ofNullable(userRepository.findByEmail(email));
//    }
//
//    public Optional<User> findById(String id) throws UserException {
//        return Optional.ofNullable(userRepository.findById(id));
//    }
//
//    public Optional<User> getLoggedUser() throws UserException {
//        String name = SecurityContextHolder.getContext().getAuthentication().getName();
//        return findByEmail(name);
//    }
//
//    public void registerUser(User user) throws UserException, MessagingException {
//        if (userRepository.exists(user.getEmail().toLowerCase())) throw new UserException("user_already_exists");
//
//        String password = new StrongPasswordEncryptor().encryptPassword(user.getPassword());
//        user.setPassword(password);
//
//        user.setEmailHash(getRandomHash());
//        user.setTotalKudos(0);
//        user.setLastNotificationCheckTime("");
//
//        userRepository.save(user);
//
//        String message = "Your confirmation code is : <b>" + user.getEmailHash() + "</b>";
//        emailService.sendEmail(user.getEmail(), message, "Greetings from Acorns app");
//    }
//
//    public void confirmRegistration(String hashedEmail) throws UserException {
//        Optional<User> user = userRepository.findUserByEmailHash(hashedEmail);
//        if (user.isPresent()) {
//            user.get().setStatus(UserStatus.NOT_COMPLETED);
//            userRepository.save(user.get());
//        } else {
//            throw new UserException("user_not_found");
//        }
//    }
//
//    public User updateUser(MyProfileForm myProfileForm) throws UserException, MessagingException, IOException, TemplateException {
//        User user = getLoggedUser().get();
//        user.setBirthday(myProfileForm.getBirthday());
//        user.setStartedToWorkDate(myProfileForm.getStartedToWorkDate());
//        userRepository.save(user);
//        return user;
//    }
//
//    public void disableUsersAccount() throws UserException {
//        wipeAllUserData();
//    }
//
//    public UserResponse getCompletedUser() throws UserException {
//        User user = getLoggedUser().get();
//        return new UserResponse(user);
//    }
//
//    public void login(String email, String password, HttpServletRequest request) throws AuthenticationCredentialsNotFoundException, UserException {
//        SecurityContext securityContext = SecurityContextHolder.getContext();
//        securityContext.setAuthentication(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)));
//        request.getSession().setMaxInactiveInterval(0);
//        request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
//    }
//
////    public boolean subscribe() throws UserException{
////        return setSubscribe(true);
////    }
////
////    public boolean unsubscribe() throws UserException{
////        return setSubscribe(false);
////    }
////
////    private boolean setSubscribe(boolean subscribing) throws UserException {
////        User user = getLoggedUser().get();
////        user.setSubscribing(subscribing);
////        userRepository.save(user);
////        return user.isSubscribing();
////    }
//
//    private User loginValidation(String email, String password) throws UserException {
//        Optional<User> maybeUser = findByEmail(email);
//
//        if (Strings.isNullOrEmpty(email)) throw new UserException("email_not_specified");
//
//        if (Strings.isNullOrEmpty(password)) throw new UserException("password_not_specified");
//
//        if (getLoggedUser().isPresent()) throw new UserException("user_already_logged");
//
//        if (!maybeUser.isPresent()) throw new UserException("user_not_exist");
//
//        if (maybeUser.get().getStatus().equals(UserStatus.NOT_CONFIRMED)) throw new UserException("user_not_confirmed");
//
//        return maybeUser.get();
//    }
//
//    public void resetPassword(String email) throws UserException, MessagingException, IOException, TemplateException {
//        Optional<User> maybeUser = findByEmail(email);
//
//        if (Strings.isNullOrEmpty(email)) throw new UserException("email_not_specified");
//
//        if (maybeUser.isPresent()) throw new UserException("user_not_exist");
//
//        User user = maybeUser.get();
//        String resetHash = getRandomHash();
//        user.setEmailHash(resetHash);
//
//        userRepository.save(user);
//    }
//
//
//    public List<LeaderBoardItem> getTopSenders(String period) {
//        List<LeaderBoardItem> topSenders = getAllConfirmedUsers().stream().map(user -> createLeaderBoardItem(user, calculateSendersTransactionsAmount(user, period))).collect(Collectors.toList());
//        return sortListByAmountOfKudos(topSenders, 0, 5);
//    }
//
//    public List<LeaderBoardItem> getTopReceivers(String period) {
//        List<LeaderBoardItem> topReceivers = getAllConfirmedUsers().stream().map(user -> createLeaderBoardItem(user, calculateReceiversTransactionsAmount(user, period))).collect(Collectors.toList());
//        return sortListByAmountOfKudos(topReceivers, 0, 5);
//    }
//
//    private List<LeaderBoardItem> sortListByAmountOfKudos(List<LeaderBoardItem> leaderBoardItems, int startingIndex, int endingIndex) {
//        try {
//            return leaderBoardItems.stream().sorted((l1, l2) -> l2.getKudosAmount().compareTo(l1.getKudosAmount())).collect(Collectors.toList()).subList(startingIndex, endingIndex);
//        } catch (IndexOutOfBoundsException e) {
//            return leaderBoardItems.stream().sorted((l1, l2) -> l2.getKudosAmount().compareTo(l1.getKudosAmount())).collect(Collectors.toList()).subList(0, leaderBoardItems.size());
//        }
//    }
//
//    private int calculateSendersTransactionsAmount(User user, String period) {
//        if (period.equals("week"))
//            return transactionRepository.findTransactionsBySenderAndTimestampGreaterThanOrderByTimestampDesc(user, LocalDateTime.now().minusDays(7).toString())
//                    .stream().filter(transaction -> transaction.getType() == TransactionType.KUDOS || transaction.getType() == TransactionType.CHALLENGE_COMPLETED)
//                    .mapToInt(Transaction::getAmount).sum();
//        else if (period.equals("month"))
//            return transactionRepository.findTransactionsBySenderAndTimestampGreaterThanOrderByTimestampDesc(user, LocalDateTime.now().minusDays(30).toString())
//                    .stream().filter(transaction -> transaction.getType() == TransactionType.KUDOS || transaction.getType() == TransactionType.CHALLENGE_COMPLETED)
//                    .mapToInt(Transaction::getAmount).sum();
//        else return transactionRepository.findTransactionsBySender(user)
//                    .stream().filter(transaction -> transaction.getType() == TransactionType.KUDOS || transaction.getType() == TransactionType.CHALLENGE_COMPLETED)
//                    .mapToInt(Transaction::getAmount).sum();
//    }
//
//    private int calculateReceiversTransactionsAmount(User user, String period) {
//        if (period.equals("week")) return calculateReceiverTransactionAmountByPeriod(user, 7);
//        else if (period.equals("month")) return calculateReceiverTransactionAmountByPeriod(user, 30);
//        else return transactionRepository.findTransactionsByReceiver(user)
//                    .stream().filter(transaction -> transaction.getType() == TransactionType.KUDOS || transaction.getType() == TransactionType.CHALLENGE_COMPLETED)
//                    .mapToInt(Transaction::getAmount).sum();
//    }
//
//    private int calculateReceiverTransactionAmountByPeriod(User user, int period) {
//        return transactionRepository.findTransactionsByReceiverAndTimestampGreaterThanOrderByTimestampDesc(user, LocalDateTime.now().minusDays(period).toString())
//                .stream().filter(transaction -> transaction.getType() == TransactionType.KUDOS || transaction.getType() == TransactionType.CHALLENGE_COMPLETED)
//                .mapToInt(Transaction::getAmount).sum();
//    }
//
//    private LeaderBoardItem createLeaderBoardItem(User user, int kudosAmount) {
//        return new LeaderBoardItem(user.getFirstName(), user.getLastName(), user.getId(), kudosAmount);
//    }
//
//    private String getRandomHash() {
//        return new BigInteger(130, new SecureRandom()).toString(32);
//    }
//
//    private void wipeAllUserData() throws UserException {
//
//        challengeService.getAllUserCreatedChallenges().stream()
//                .forEach(challenge -> {
//                    challenge.setCreator(DELETED_USER_TAG);
//                    challengeService.save(challenge);
//                });
//
//        challengeService.getAllUserParticipatedChallenges().stream()
//                .forEach(challenge -> {
//                    challenge.setParticipant(DELETED_USER_TAG);
//                    challengeService.save(challenge);
//                });
//
//        kudosService.getAllLoggedUserIncomingTransactions().stream()
//                .forEach(transaction -> {
//                    transaction.setReceiver(DELETED_USER_TAG);
//                    kudosService.save(transaction);
//                });
//
//        kudosService.getAllLoggedUserOutgoingTransactions().stream()
//                .forEach(transaction -> {
//                    transaction.setSender(DELETED_USER_TAG);
//                    kudosService.save(transaction);
//                });
//
//        userRepository.delete(getLoggedUser().get());
//    }
//
//    public List<UserResponse> list() {
//        return userRepository.findAll().stream().map(UserResponse::new).collect(Collectors.toList());
//    }
//
//    public List<User> getAllConfirmedUsers(){
//        return userRepository.findUsersByIsConfirmed(true);
//    }
//
//    public List<UserResponse> getAllConfirmedUsersResponse() {
//        return userRepository.findUsersByIsConfirmed(true).stream().map(UserResponse::new).collect(Collectors.toList());
//    }

}
