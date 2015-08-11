package kudos.services;

import com.google.common.base.Optional;
import kudos.model.User;
import kudos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by chc on 15.8.11.
 */
@Service
public class UsersService {


    @Autowired
    protected UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return Optional.fromNullable(userRepository.findByEmail(email));
    }

    public Optional<User> getLoggedUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByEmail(name);
    }
}
