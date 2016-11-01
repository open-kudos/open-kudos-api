package kudos.services;

import kudos.exceptions.RelationException;
import kudos.model.*;
import kudos.repositories.ActionRepository;
import kudos.repositories.RelationRepository;
import kudos.repositories.UserRepository;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ActionsService {

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    RelationRepository relationRepository;

    @Autowired
    private UserRepository userRepository;

    public void save(User user, Challenge challenge, ActionType type) {
        actionRepository.save(new Action(user, challenge, LocalDateTime.now().toString(), type));
    }

    public void save(User user, Transaction transaction, ActionType type) {
        actionRepository.save(new Action(user, transaction, LocalDateTime.now().toString(), type));
    }

    public void save(User user, Comment comment, ActionType type) {
        actionRepository.save(new Action(user, comment, LocalDateTime.now().toString(), type));
    }

    public void save(User follower, User followed, ActionType type) {
        Relation relation = relationRepository.findRelationByFollowerAndUserToFollow(follower, followed).get();
        actionRepository.save(new Action(follower, relation, LocalDateTime.now().toString(), type));
    }

    public void save(User user, Idea idea, ActionType type) {
        actionRepository.save(new Action(user, idea, LocalDateTime.now().toString(), type));
    }

    public void save(User user, ShopItem shopItem, ActionType type){
        actionRepository.save(new Action(user, shopItem, LocalDateTime.now().toString(), type));
    }

    public void save(User user, ActionType type) {
        actionRepository.save(new Action(user, LocalDateTime.now().toString(), type));
    }

    public void remove(User follower, User userToUnfollow) throws RelationException {
        Optional<Relation> relation = relationRepository.findRelationByFollowerAndUserToFollow(follower, userToUnfollow);
        if(relation.isPresent()){
            actionRepository.deleteByRelation(relation.get());
        } else {
            throw new RelationException("relation_does_not_exist");
        }
    }

    public void remove(Challenge challenge) {
        if (challenge != null) {
            actionRepository.deleteByChallenge(challenge);
        }
    }

    public Page<Action> getFeedPage(Pageable pageable, User currentUser) {
        return actionRepository.findAllByUserNot(currentUser, pageable);
    }

    public Page<Action> getUserFeedPage(User user, Pageable pageable) {
        return  actionRepository.findActionsByUser(user, pageable);
    }
}
