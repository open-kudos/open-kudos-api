package kudos.services;

import kudos.KudosBusinessStrategy;
import kudos.exceptions.UserException;
import kudos.model.*;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
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
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    protected KudosBusinessStrategy kudosBusinessStrategy;

    @Autowired
    private TransactionRepository transactionRepository;

    public User registerUser(User user) throws UserException, MessagingException {
        if (userRepository.findByEmail(user.getEmail().toLowerCase()).isPresent())
            throw new UserException("user_already_exists");

        String password = new StrongPasswordEncryptor().encryptPassword(user.getPassword());
        user.setPassword(password);

        user.setEmailHash(getRandomHash());
        user.setTotalKudos(0);
        user.setWeeklyKudos(kudosBusinessStrategy.getWeeklyAmount());

        user.setSubscribing(false);

        return userRepository.save(user);

    }

    public void confirmRegistration(String hashedEmail) throws UserException {
        Optional<User> user = userRepository.findUserByEmailHash(hashedEmail);
        if (user.isPresent() && user.get().getStatus().equals(UserStatus.NOT_CONFIRMED)) {
            user.get().setStatus(UserStatus.NOT_COMPLETED);
            userRepository.save(user.get());
        } else if (user.isPresent() && user.get().getStatus().equals(UserStatus.NOT_COMPLETED) || user.get().getStatus().equals(UserStatus.COMPLETED)) {
            throw new UserException("user_already_confirmed");
        } else {
            throw new UserException("user_not_found");
        }
    }

    public String resetPassword(String email) throws UserException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String newPassword = getRandomPassword();
            String password = new StrongPasswordEncryptor().encryptPassword(newPassword);
            user.get().setPassword(password);
            userRepository.save(user.get());
            return newPassword;
        } else {
            throw new UserException("user_not_found");
        }
    }

    public void changePassword(String newPassword) throws UserException {
        User user = getLoggedInUser();
        String password = new StrongPasswordEncryptor().encryptPassword(newPassword);
        user.setPassword(password);
        userRepository.save(user);
    }

    public void login(String email, String password, HttpServletRequest request) throws AuthenticationCredentialsNotFoundException, UserException {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            throw new UserException("user_not_found");
        }

        if (user.get().getStatus() == null) {
            throw new UserException("user_status_undefined");
        }

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)));
        request.getSession().setMaxInactiveInterval(0);
        request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }

    public void logout(HttpSession session, Principal principal) throws UserException {
        if (principal == null) {
            throw new UserException("user_not_logged_in");
        }
        session.invalidate();
    }

    public User getLoggedInUser() throws UserException {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(name);
        if (user.isPresent()) {
            return updateWeeklyKudos(user.get());
        } else {
            throw new UserException("user_not_found");
        }
    }

    public User updateWeeklyKudos(User user) {
        List<Transaction> transactions = transactionRepository.findTransactionsBySenderAndDateGreaterThan(user,
                kudosBusinessStrategy.getStartTime().toString());

        int usedThisWeek = countUsedKudos(transactions);
        int shouldBeReturnedThisWeek = countKudosToReturn(transactions);

        user.setWeeklyKudos(kudosBusinessStrategy.getWeeklyAmount() - usedThisWeek + shouldBeReturnedThisWeek);
        return userRepository.save(user);
    }

    public int countUsedKudos(List<Transaction> transactions) {
        return transactions.stream().filter(transaction -> (transaction.getStatus() == TransactionStatus.COMPLETED
                || transaction.getStatus() == TransactionStatus.PENDING) && transaction.getType() != TransactionType.SHOP)
                .mapToInt(Transaction::getAmount).sum();
    }

    public int countKudosToReturn(List<Transaction> transactions) {
        return transactions.stream().filter(transaction -> transaction.getStatus() == TransactionStatus.CANCELED
                || transaction.getStatus() == TransactionStatus.CANCELED)
                .mapToInt(Transaction::getAmount).sum();
    }

    private String getRandomHash() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

    private String getRandomPassword() {
        return new BigInteger(20, new SecureRandom()).toString(16);
    }

}
