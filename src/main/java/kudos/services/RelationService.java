package kudos.services;

import kudos.exceptions.RelationException;
import kudos.model.Relation;
import kudos.model.User;
import kudos.repositories.RelationRepository;
import kudos.web.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            throw new RelationException("relation.already.exists");
        }
        return relationRepository.save(relation);
    }

    public List<String> getAllFollowedUsers() throws UserException {
        String followerEmail = usersService.getLoggedUser().get().getEmail();
        List<Relation>relationList = relationRepository.getRelationsByFollowerEmail(followerEmail);

        return relationList.stream().collect(() -> new ArrayList<>(),
                (c, e) -> c.add(e.getUserEmail()),
                (c1, c2) -> c1.addAll(c2));

    }

    public List<String> getAllFollowers() throws UserException {
        String userEmail = usersService.getLoggedUser().get().getEmail();
        List<Relation>relationList = relationRepository.getRelationsByUserEmail(userEmail);

        return relationList.stream().collect(() -> new ArrayList<>(),
                (c, e) -> c.add(e.getFollowerEmail()),
                (c1, c2) -> c1.addAll(c2));
    }

    public void removeRelation(Relation relation) throws RelationException {
        if(relationRepository.getRelationByFollowerEmailAndUserEmail(relation.getFollowerEmail(),relation.getUserEmail()) == null){
            throw new RelationException("relation.not.exist");
        }
        relationRepository.delete(relation);
    }

}
