package kudos.services;

import com.google.common.base.Strings;
import kudos.exceptions.UserException;
import kudos.model.User;
import kudos.model.status.UserStatus;
import kudos.repositories.UserRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

    @Value("${kudos.maxNameLength}")
    private int maxNameLength;

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByUserId(String id) throws UserException {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        } else {
            throw new UserException("user_not_found");
        }
    }

    public void updateUserProfile(User user, String firstName, String lastName, String birthday, String startedToWork) throws UserException {
        if(firstName != null && firstName.length() > maxNameLength)
            throw new UserException("first_name_too_long");

        if(lastName != null && lastName.length() > maxNameLength)
            throw new UserException("last_name_too_long");

        user.setFirstName(Strings.isNullOrEmpty(firstName) ? user.getFirstName() : firstName);
        user.setLastName(Strings.isNullOrEmpty(lastName) ? user.getLastName() : lastName);

        if(!Strings.isNullOrEmpty(birthday) && !Strings.isNullOrEmpty(startedToWork))
            user.setStatus(UserStatus.COMPLETED);
        userRepository.save(user);
    }

    public List<User> getUserEmailPredicate(String emailPredicate, String userId){
        return userRepository.findUsersByFirstNameLikeIgnoreCaseAndIdNot(emailPredicate, userId);
    }

    public void subscribe(User user){
        userRepository.save(user);
    }

    public void unsubscribe(User user){
        userRepository.save(user);
    }

}

