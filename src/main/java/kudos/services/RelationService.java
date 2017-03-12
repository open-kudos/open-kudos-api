package kudos.services;

import kudos.exceptions.RelationException;
import kudos.exceptions.UserException;
import kudos.model.Relation;
import kudos.model.User;
import kudos.repositories.RelationRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RelationService {

    @Autowired
    private RelationRepository relationRepository;

    public void follow(User follower, User userToFollow) throws RelationException {
        if(relationRepository.findRelationByFollowerAndUserToFollow(follower, userToFollow).isPresent())
            throw new RelationException("relation_already_exists");

        if(follower.getId().equals(userToFollow.getId()))
            throw new RelationException("cant_follow_yourself");

        Relation relation = new Relation(follower, userToFollow, LocalDateTime.now().toString());
        relationRepository.save(relation);
    }

    public void unfollow(User follower, User userToUnfollow) throws RelationException {
        Optional<Relation> relation = relationRepository.findRelationByFollowerAndUserToFollow(follower, userToUnfollow);
        if(relation.isPresent()){
            relationRepository.delete(relation.get());
        } else {
            throw new RelationException("relation_does_not_exist");
        }
    }

    public Relation checkIfUserIsFollowed(User currentUser, User userToCheck){
        Optional<Relation> relation = relationRepository.findRelationByFollowerAndUserToFollow(currentUser, userToCheck);
        return relation.isPresent() ? relation.get() : null;
    }

    public List<Relation> findAllFollowedUsers(User user) {
        return relationRepository.findRelationsByFollower(user);
    }

    public Page<Relation> getUsersWhoFollowUser(User user, Pageable pageable) throws UserException {
        return relationRepository.findRelationsByUserToFollowOrderByAddedDateDesc(user, pageable);
    }

    public Page<Relation> getUsersFollowedByUser(User user, Pageable pageable) throws UserException {
        return relationRepository.findRelationsByFollowerOrderByAddedDateDesc(user, pageable);
    }

}
