package kudos.services;

import kudos.KudosBusinessStrategy;
import kudos.exceptions.UserException;
import kudos.model.Transaction;
import kudos.model.TransactionStatus;
import kudos.model.User;
import kudos.model.UserStatus;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${kudos.maxNameLength}")
    private int maxNameLength;

    public User registerUser(User user) throws UserException, MessagingException {
        if (userRepository.exists(user.getEmail().toLowerCase())) throw new UserException("user_already_exists");

        if(user.getFirstName().length() > maxNameLength)
            throw new UserException("name_too_long");

        if(user.getLastName().length() > maxNameLength)
            throw new UserException("name_too_long");

        String password = new StrongPasswordEncryptor().encryptPassword(user.getPassword());
        user.setPassword(password);

        user.setEmailHash(getRandomHash());
        user.setTotalKudos(0);
        user.setWeeklyKudos(kudosBusinessStrategy.getWeeklyAmount());

        return userRepository.save(user);

    }

    public void confirmRegistration(String hashedEmail) throws UserException {
        Optional<User> user = userRepository.findUserByEmailHash(hashedEmail);
        if (user.isPresent()) {
            user.get().setStatus(UserStatus.NOT_COMPLETED);
            userRepository.save(user.get());
        } else {
            throw new UserException("user_not_found");
        }
    }

    public void login(String email, String password, HttpServletRequest request) throws AuthenticationCredentialsNotFoundException, UserException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password)));
        request.getSession().setMaxInactiveInterval(0);
        request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
    }

    public void logout(HttpSession session, Principal principal) throws UserException {
        if (principal == null) {
            throw new UserException("not_logged");
        }
        session.invalidate();
    }

    public User getLoggedInUser() throws UserException {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(name);
        if(user.isPresent()) {
            return updateWeeklyKudos(user.get());
        } else {
            throw new UserException("user_not_logged_in");
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
        return transactions.stream().filter(transaction -> transaction.getStatus() == TransactionStatus.COMPLETED
                || transaction.getStatus() == TransactionStatus.PENDING).mapToInt(Transaction::getAmount).sum();
    }

    public int countKudosToReturn(List<Transaction> transactions) {
        return transactions.stream().filter(transaction -> transaction.getStatus() == TransactionStatus.CANCELED)
                .mapToInt(Transaction::getAmount).sum();
    }

    private String getRandomHash() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

}
