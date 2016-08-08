package kudos.services;

import kudos.model.*;
import kudos.repositories.FeedRepository;
import kudos.repositories.RelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class FeedService  {

    @Autowired
    FeedRepository feedRepository;

    @Autowired
    RelationRepository relationRepository;

    public void save(User user, Challenge challenge, FeedType type){
        feedRepository.save(new Feed(user, challenge, LocalDateTime.now().toString(), type));
    }

    public void save(User user, Transaction transaction, FeedType type){
        feedRepository.save(new Feed(user, transaction, LocalDateTime.now().toString(), type));
    }

    public void save(User user, Comment comment, FeedType type){
        feedRepository.save(new Feed(user, comment, LocalDateTime.now().toString(), type));
    }

    public void save(User follower, User followed, FeedType type){
        Relation relation = relationRepository.findRelationByFollowerAndUserToFollow(followed, followed).get();
        feedRepository.save(new Feed(follower, relation, LocalDateTime.now().toString(), type));
    }

    public void save(User user, Idea idea, FeedType type){
        feedRepository.save(new Feed(user, idea, LocalDateTime.now().toString(), type));
    }

    public void save(User user, FeedType type){
        feedRepository.save(new Feed(user, LocalDateTime.now().toString(), type));
    }

}