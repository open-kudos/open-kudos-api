package kudos.services;

import kudos.exceptions.RelationException;
import kudos.model.Relation;
import kudos.model.User;
import kudos.repositories.RelationRepository;
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

    public Relation addRelation(Relation relation) throws RelationException {
        if(relationRepository.getRelationByFollowerAndCurrentUser(relation.getFollower(),relation.getCurrentUser()) != null){
            throw new RelationException("relation_already_exists");
        }

        if(relation.getFollower().equals(relation.getCurrentUser())){
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
        return relationRepository.getRelationsByCurrentUser(currentUser);

    }


    public void removeRelation(User follower) throws RelationException, UserException {
        User loggedUser = usersService.getLoggedUser().get();
        Relation relationToRemove = relationRepository.getRelationByFollowerAndCurrentUser(loggedUser, follower);

        if(relationToRemove == null){
            throw new RelationException("relation_not_exist");
        }
        relationRepository.delete(relationToRemove);
    }

}
