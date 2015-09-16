package kudos.services;


import com.google.common.base.Strings;
import freemarker.template.TemplateException;
import kudos.model.Email;
import kudos.model.User;
import kudos.repositories.UserRepository;
import kudos.web.beans.form.MyProfileForm;
import kudos.web.exceptions.UserException;
import org.apache.log4j.Logger;
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
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by chc on 15.8.11.
 */
@Service
public class UsersService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private KudosService kudosService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private Logger LOG = Logger.getLogger(UsersService.class);

    private static final String DELETED_USER_TAG = "undefined";

    public Optional<User> findByEmail(String email) throws UserException {
        return Optional.ofNullable(userRepository.findOne(email));
    }

    public Optional<User> getLoggedUser() throws UserException {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByEmail(name);
    }

    public User getKudosMaster() {
        return new User("pass", "master@of.kudos");
    }

    public User registerUser(User user) throws UserException, MessagingException, IOException, TemplateException {

        if (userRepository.exists(user.getEmail())) {
            throw new UserException("user_already_exists");
        }
        /*emailService.send(
                new Email(email,
                        new Date().toString(),
                        "Welcome to KUDOS app. Click this link to complete registration",
                        "http://localhost:8080/reset-password-by-id?id="+getRandomHash())
        );*/
        String password = new StrongPasswordEncryptor().encryptPassword(user.getPassword());
        User newUser = new User(password, user.getEmail());
        newUser.setEmailHash(getRandomHash());
        return userRepository.save(newUser);
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

    public User completeUser(MyProfileForm myProfileForm) throws UserException, MessagingException, IOException, TemplateException {
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
        if (!user.isCompleted()) {
            throw new UserException("user_not_completed");
        }
        return user;
    }

    public User login(String email, String password, HttpServletRequest request) throws AuthenticationCredentialsNotFoundException, UserException {

        loginValidation(email, password);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password))
        );
        request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        return findByEmail(email).get();
    }

    private User loginValidation(String email, String password) throws UserException {
        if (Strings.isNullOrEmpty(email)) {
            throw new UserException("email_not_specified");
        }

        if (Strings.isNullOrEmpty(password)) {
            throw new UserException("password_not_specified");
        }

        if (getLoggedUser().isPresent()) {
            throw new UserException("user_already_logged");
        }
        Optional<User> maybeUser = findByEmail(email);
        if (!maybeUser.isPresent()) {
            throw new UserException("user_not_exist");
        }

        return maybeUser.get();

    }

    public void resetPassword(String email) throws UserException, MessagingException, IOException, TemplateException {
        if (Strings.isNullOrEmpty(email)) {
            throw new UserException("email_not_specified");
        }

        Optional<User> maybeUser = findByEmail(email);
        if (maybeUser.isPresent()) {
            throw new UserException("user_not_exist");
        }
        User user = maybeUser.get();
        String resetHash = getRandomHash();
        user.setEmailHash(resetHash);

        emailService.send(
                new Email(email, LocalDateTime.now().toString(), "Click this link to reset your password: ", "http://localhost:8080/reset-password-by-id?id=" + resetHash)
        );
        userRepository.save(user);
    }

    private String getRandomHash() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

    private void wipeAllUserData() throws UserException {

        challengeService.getAllUserCreatedChallenges().stream()
                .forEach(challenge -> {
                    challenge.setCreator(DELETED_USER_TAG);
                    challengeService.save(challenge);
                });

        challengeService.getAllUserParticipatedChallenges().stream()
                .forEach(challenge -> {
                    challenge.setParticipant(DELETED_USER_TAG);
                    challengeService.save(challenge);
                });

        challengeService.getAllUserReferredChallenges().stream()
                .forEach(challenge -> {
                    challenge.setReferee(DELETED_USER_TAG);
                    challengeService.save(challenge);
                });

        kudosService.getAllLoggedUserIncomingTransactions().stream()
                .forEach(transaction -> {
                    transaction.setReceiverEmail(DELETED_USER_TAG);
                    kudosService.save(transaction);
                });

        kudosService.getAllLoggedUserOutgoingTransactions().stream()
                .forEach(transaction -> {
                    transaction.setSenderEmail(DELETED_USER_TAG);
                    kudosService.save(transaction);
                });

        userRepository.delete(getLoggedUser().get());

    }

    public List<User> list(String seed) {
        return userRepository.searchAllFields(seed);
    }
}
