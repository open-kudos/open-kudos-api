package kudos.services;


import kudos.model.Idea;
import kudos.model.User;
import kudos.repositories.WisdomWallRepository;
import kudos.web.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WisdomWallService {

    @Autowired
    private WisdomWallRepository repository;
    @Autowired
    private UsersService usersService;

    public Idea addIdeaToWisdomWall(String author, String idea) throws UserException {
        User postedBy = usersService.getLoggedUser().get();
        return repository.insert(new Idea(author, postedBy.getEmail(), idea));
    }

    /**
     * Returns list of ideas posted by certain user
     * @return List
     */
    public List<Idea> getIdeasByPostedBy(String postedBy) {
        return repository.findIdeasByPostedByEmail(postedBy);
    }

}
