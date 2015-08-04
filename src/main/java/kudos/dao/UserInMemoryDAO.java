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
public class UserInMemoryDAO implements UserDAO{

    //private Map<String,User> users = new HashMap<String,User>();

    @Autowired
    private UserRepository repository;

    @Override
    public Optional<User> getUserByEmail(String email) {
        if (/*users.containsKey(email)*/repository.findByEmail(email) != null) {
            return Optional.of(repository.findByEmail(email));
        } else {
            return Optional.absent();
        }
    }

    @Override
    public User create(User user) {
        Optional<User> storedUser = getUserByEmail(user.getEmail());
        if (storedUser.isPresent()) {
            throw new IllegalStateException("email.occupied.email");
        }

        repository.save(user);
        //users.put(user.getEmail(), user);

        return user;
    }

    @Override
    public User update(User user) {
        Optional<User> storedUser = getUserByEmail(user.getEmail());
        if (!storedUser.isPresent()) {
            throw new IllegalStateException("User does not exist");
        }

        repository.delete(storedUser.get());
        repository.save(user);
        //users.put(user.getEmail(), user);

        return user;
    }

    @Override
    public void remove(String email){
        Optional<User> user = getUserByEmail(email);
        if(user.isPresent()){
            repository.delete(user.get());
            //users.remove(email);
        }
    }
}
