package kudos.services;


import kudos.model.Idea;
import kudos.model.User;
import kudos.repositories.WisdomWallRepository;
import kudos.web.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WisdomWallService {

    @Autowired
    private WisdomWallRepository repository;
    @Autowired
    private UsersService usersService;


    public Idea addIdeaToWisdomWall(User author, String idea) throws UserException {
        User postedBy = usersService.getLoggedUser().get();
        Idea newIdea = new Idea(author.getEmail(), postedBy.getEmail(), idea);
        return repository.insert(newIdea);
    }
}
