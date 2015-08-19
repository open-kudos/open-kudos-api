package kudos.services;


import com.google.common.base.Optional;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

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
        if(userRepository.exists(email)  && !userRepository.findOne(email).isRegistered()){
            throw new UserException("user.not.exist");
        }
        return Optional.fromNullable(userRepository.findByEmail(email));
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

        if(findByEmail(email).isPresent() && findByEmail(email).get().isRegistered()){
            throw new UserException("user.already.registered");
        } else if (findByEmail(email).isPresent()) {
            throw new UserException("user.already.exists");
        }

        String hash = getRandomHash();

        emailService.send(new Email(email, new Date().toString(),"Welcome to KUDOS app. Click this link to complete registration",
                "http://localhost:8080/reset-password-by-id?id="+hash));

        String password = new StrongPasswordEncryptor().encryptPassword(user.getPassword());

        User newUser = new User(password, email, user.getFirstName(), user.getLastName());
        newUser.setEmailHash(hash);
        return saveUser(newUser).get();
    }

    public User confirmUser(String hashedEmail) throws UserException {
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

         return saveUser(user).get();
    }

    public void disableMyAcount() throws UserException {
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

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        return userRepository.findByEmail(email);

    }

    public void resetPassword(String email) throws UserException, MessagingException, IOException, TemplateException {
        if(Strings.isNullOrEmpty(email)){
            throw new UserException("email.not.specified");
        }
        if(!findByEmail(email).isPresent()){
            throw new UserException("user.not.exist");
        }

        User user = findByEmail(email).get();

        if(!user.isRegistered()){
            throw new UserException("user.not.registered");
        }
        String resetHash = getRandomHash();
        user.setEmailHash(resetHash);

        emailService.send(new Email(email, LocalDateTime.now().toString(),
                "Click this link to reset your password: ","http://localhost:8080/reset-password-by-id?id="+resetHash));
        userRepository.save(user);
    }

    private String getRandomHash(){
        return new BigInteger(130, new SecureRandom()).toString(32);
    }

}
