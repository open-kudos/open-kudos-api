package kudos.services;


import com.google.common.base.Strings;
import freemarker.template.TemplateException;
import kudos.model.Email;
import kudos.model.User;
import kudos.repositories.UserRepository;
import kudos.web.beans.form.MyProfileForm;
import kudos.web.exceptions.UserException;
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
import java.util.Date;
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
    private AuthenticationManager authenticationManager;

    public Optional<User> findByEmail(String email) throws UserException {
        // TODO hard logic of the system. Better to delete user completely instead of setting flags. And update all the transactions with default <user_deleted> tag or something
        if(userRepository.exists(email)  && !userRepository.findOne(email).isRegistered()){
            throw new UserException("user.not.exist");
        }
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public Optional<User> getLoggedUser() throws UserException {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByEmail(name);
    }

    public Optional<User>saveUser(User user) {
        return Optional.of(userRepository.save(user));
    }

    public User getKudosMaster() {
        return new User("pass", "master@of.kudos", "Kudos", "Master");
    }

    public User registerUser(User user) throws UserException, MessagingException, IOException, TemplateException {

        String email = user.getEmail();
        Optional<User> maybeExistingUser = findByEmail(email);
        if (maybeExistingUser.isPresent()) {
            throw new UserException("user.already.exists");
        }

        String hash = getRandomHash();

        emailService.send(
                new Email(email,
                        new Date().toString(),
                        "Welcome to KUDOS app. Click this link to complete registration",
                        "http://localhost:8080/reset-password-by-id?id="+hash)
        );

        String password = new StrongPasswordEncryptor().encryptPassword(user.getPassword());
        User newUser = new User(password, email, user.getFirstName(), user.getLastName());
        newUser.setEmailHash(hash);
        // TODO use userService directly
        // TODO why is it Optional here? You save the user
        return saveUser(newUser).get();
    }

    public User confirmUser(String hashedEmail) throws UserException {
        //TODO find... could be Optional
        User user = userRepository.findUserByEmailHash(hashedEmail);
        if (user != null) {
            user.markUserAsConfirmed();
            userRepository.save(user);
            return userRepository.save(user);
        } else {
            throw new UserException("user.not.found");
        }
    }

    public User completeUser(MyProfileForm myProfileForm) throws UserException, MessagingException, IOException, TemplateException {

        //TODO create new class which would hold all the variables.
        //TODO Set and validate those variables in a separate method (~30 lines).
        //TODO updateUserWithAdditionalInformation should accept newly created class, not 13 parameters
        User user = getLoggedUser().get();

        String email = user.getEmail();
        String password = user.getPassword();
        String name = user.getFirstName();
        String surname = user.getLastName();

        String newEmail = myProfileForm.getEmail();
        String newPassword = myProfileForm.getNewPassword();
        String newFirstName = myProfileForm.getFirstName();
        String newLastName = myProfileForm.getLastName();

        if (!Strings.isNullOrEmpty(newEmail) && !newEmail.equals(email)) {
            email = newEmail;
        }

        if (!Strings.isNullOrEmpty(newPassword) && !new StrongPasswordEncryptor().checkPassword(newPassword, password)) {
            password = new StrongPasswordEncryptor().encryptPassword(newPassword);
        }

        if (!Strings.isNullOrEmpty(newFirstName) && !name.equals(newFirstName)) {
            name = newFirstName;
        }

        if (!Strings.isNullOrEmpty(newLastName) && !surname.equals(newLastName)) {
            surname = newLastName;
        }

        String birthday = myProfileForm.getBirthday();
        String phone = myProfileForm.getPhone();
        String startedToWork = myProfileForm.getStartedToWorkDate();
        String position = myProfileForm.getPosition();
        String department = myProfileForm.getDepartment();
        String location = myProfileForm.getLocation();
        String team = myProfileForm.getTeam();
        boolean showBirthday = myProfileForm.getShowBirthday();

        user.updateUserWithAdditionalInformation(password, email, name, surname, birthday, phone, startedToWork, position, department,
                location, team, true, showBirthday);

        emailService.send(new Email(email, new Date().toString(), "Welcome to KUDOS app. Click this link to complete registration",
                "http://localhost:8080/confirm-user-by-id?id=" + getRandomHash()));

        //TODO why don't you use userService directly? this looks complicated
         return saveUser(user).get();
    }

    public void disableUsersAcount() throws UserException {
        User user = getLoggedUser().get();
        user.setIsRegistered(false);
        userRepository.save(user);
    }

    public User getCompletedUser() throws UserException {
        User user = getLoggedUser().get();
        if (!user.isCompleted()) {
            throw new UserException("user.not.completed");
        }
        return user;
    }

    public User login(String email, String password, HttpServletRequest request) throws AuthenticationCredentialsNotFoundException, UserException {

        // TODO move 12 lines of param validation into separate method
        if(Strings.isNullOrEmpty(email)){
            throw new UserException("email.not.specified");
        }

        if(Strings.isNullOrEmpty(password)){
            throw new UserException("password.not.specified");
        }

        if(!userRepository.exists(email) || !userRepository.findByEmail(email).isRegistered()){
            throw new UserException("user.not.exist");
        }

        if(getLoggedUser().isPresent()) {
            throw new UserException("user.already.logged");
        }

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password))
        );
        request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        return userRepository.findByEmail(email);
    }

    public void resetPassword(String email) throws UserException, MessagingException, IOException, TemplateException {
        if(Strings.isNullOrEmpty(email)){
            throw new UserException("email.not.specified");
        }

        // TODO calling findByEmail two times is not efficient, one time is enough, i'm sure :)
        // TODO findByEmail should return Optional and then do the needed checks with orElseThrow
        if(!findByEmail(email).isPresent()){
            throw new UserException("user.not.exist");
        }

        User user = findByEmail(email).get();

        if(!user.isRegistered()){
            throw new UserException("user.not.registered");
        }
        String resetHash = getRandomHash();
        user.setEmailHash(resetHash);

        emailService.send(
                new Email(email, LocalDateTime.now().toString(), "Click this link to reset your password: ","http://localhost:8080/reset-password-by-id?id="+resetHash)
        );
        userRepository.save(user);
    }

    private String getRandomHash() {
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

}
