package kudos.services;

import kudos.exceptions.RelationException;
import kudos.model.Relation;
import kudos.model.User;
import kudos.repositories.RelationRepository;
import kudos.repositories.UserRepository;
import kudos.web.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by chc on 15.8.20.
 */
@Service
public class RelationService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private UserRepository userRepository;

    public Relation addRelation(Relation relation) throws RelationException {
        if(relationRepository.getRelationByFollowerAndUserToFollow(relation.getFollower(),relation.getUserToFollow()) != null){
            throw new RelationException("relation_already_exists");
        }

        if(relation.getFollower().equals(relation.getUserToFollow())){
            throw new RelationException("cant_follow_yourself");
        }

        return relationRepository.save(relation);
    }

    public List<Relation> getAllFollowedUsers() throws UserException {
        User follower = usersService.getLoggedUser().get();
        return relationRepository.getRelationsByFollower(follower);
    }

    public List<Relation> getAllFollowers() throws UserException {
        User currentUser = usersService.getLoggedUser().get();
        return relationRepository.getRelationsByUserToFollow(currentUser);

    }


    public void removeRelation(String email) throws RelationException, UserException {
        User loggedUser = usersService.getLoggedUser().get();
        User userToRemoveRelation = userRepository.findByEmail(email);
        Relation relationToRemove = relationRepository.getRelationByFollowerAndUserToFollow(loggedUser, userToRemoveRelation);

        if(relationToRemove == null){
            throw new RelationException("relation_not_exist");
        }
        relationRepository.delete(relationToRemove);
    }

}
