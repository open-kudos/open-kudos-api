package kudos.dao;

import com.google.common.base.Optional;
import kudos.dao.repositories.UserRepository;
import kudos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by chc on 15.7.27.
 */
@Repository
public class UserInDatabaseDAO implements UserDAO {

    @Autowired
    private UserRepository repository;

    @Override
    public Optional<User> get(String email) {
        if (repository.findByEmail(email) != null) {
            return Optional.of(repository.findByEmail(email));
        } else {
            return Optional.absent();
        }
    }

    @Override
    public Optional<User> create(User user) {
        Optional<User> storedUser = get(user.getEmail());
        if (storedUser.isPresent()) {
            throw new IllegalStateException("email.occupied.email");
        }
        repository.save(user);
        return Optional.of(user);
    }

    @Override
    public Optional<User> update(User user) {
        Optional<User> storedUser = get(user.getEmail());
        if (!storedUser.isPresent()) {
            throw new IllegalStateException("User does not exist");
        }

        repository.delete(storedUser.get());
        repository.save(user);

        return Optional.of(user);

    }

    @Override
    public void remove(String email){
        Optional<User> user = get(email);
        if(user.isPresent()){
            repository.delete(user.get());
        }
    }
}
