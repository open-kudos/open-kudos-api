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
        if(relationRepository.getRelationByFollowerEmailAndUserEmail(relation.getFollowerEmail(),relation.getUserEmail()) != null){
            throw new RelationException("relation_already_exists");
        }

        if(relation.getFollowerEmail().equals(relation.getUserEmail())){
            throw new RelationException("cant_follow_yourself");
        }

        return relationRepository.save(relation);
    }

    public List<Relation> getAllFollowedUsers() throws UserException {
        String followerEmail = usersService.getLoggedUser().get().getEmail();
        return relationRepository.getRelationsByFollowerEmail(followerEmail);

        /* return relationList.stream().collect(() -> new ArrayList<>(),
                (c, e) -> c.add(e.getUserEmail()),
                (c1, c2) -> c1.addAll(c2)); */
    }

    public List<Relation> getAllFollowers() throws UserException {
        String userEmail = usersService.getLoggedUser().get().getEmail();
        return relationRepository.getRelationsByUserEmail(userEmail);

         /*relationList.stream().collect(() -> new ArrayList<>(),
                (c, e) -> c.add(e.getFollowerEmail()),
                (c1, c2) -> c1.addAll(c2)); */
    }


    public void removeRelation(String followerUserEmail) throws RelationException, UserException {
        User follower = usersService.getLoggedUser().get();
        Relation relationToRemove = relationRepository.getRelationByFollowerEmailAndUserEmail(follower.getEmail(), followerUserEmail);

        if(relationToRemove == null){
            throw new RelationException("relation_not_exist");
        }
        relationRepository.delete(relationToRemove);
    }

}
