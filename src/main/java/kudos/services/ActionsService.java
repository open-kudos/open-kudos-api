package kudos.services;

import kudos.model.*;
import kudos.repositories.ActionRepository;
import kudos.repositories.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActionsService {

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    RelationRepository relationRepository;

    public void save(User user, Challenge challenge, ActionType type){
        actionRepository.save(new Action(user, challenge, LocalDateTime.now().toString(), type));
    }

    public void save(User user, Transaction transaction, ActionType type){
        actionRepository.save(new Action(user, transaction, LocalDateTime.now().toString(), type));
    }

    public void save(User user, Comment comment, ActionType type){
        actionRepository.save(new Action(user, comment, LocalDateTime.now().toString(), type));
    }

    public void save(User follower, User followed, ActionType type){
        Relation relation = relationRepository.findRelationByFollowerAndUserToFollow(follower, followed).get();
        actionRepository.save(new Action(follower, relation, LocalDateTime.now().toString(), type));
    }

    public void save(User user, Idea idea, ActionType type){
        actionRepository.save(new Action(user, idea, LocalDateTime.now().toString(), type));
    }

    public void save(User user, ActionType type){
        actionRepository.save(new Action(user, LocalDateTime.now().toString(), type));
    }

    public Page<Action> getFeedPage(Pageable pageable){
        return actionRepository.findAll(pageable);
    }

}