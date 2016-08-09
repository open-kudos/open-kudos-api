package kudos.services;

import kudos.model.Challenge;
import kudos.model.Transaction;
import kudos.model.TransactionStatus;
import kudos.model.User;
import kudos.repositories.ChallengeRepository;
import kudos.repositories.RelationRepository;
import kudos.repositories.TransactionRepository;
import kudos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowedUsersFeedService  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RelationRepository relationRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ChallengeRepository challengeRepository;

    public List<Transaction> getFollowedUsersTransactions(User follower){
        List<Transaction> followedUsersTransactions = new ArrayList<>();
        for (User user : getFollowedUsers(follower)){
            followedUsersTransactions.addAll(transactionRepository
                    .findTransactionsBySenderOrReceiverAndStatusOrderByDateDesc(user, user, TransactionStatus.COMPLETED));
        }
        return followedUsersTransactions;
    }

    public List<Challenge> getFollowedUsersChallenges(User follower){
        List<Challenge> followedUsersChallenges = new ArrayList<>();
        for (User user : getFollowedUsers(follower)){
            followedUsersChallenges.addAll(challengeRepository.findChallengesByParticipant(user));
            followedUsersChallenges.addAll(challengeRepository.findChallengesByCreator(user));
        }
        return  followedUsersChallenges;
    }

    public List<User> getFollowedUsers(User follower){
        return relationRepository.findRelationsByFollower(follower).stream()
                .map(relation -> userRepository.findByEmail(relation.getUserToFollow().getEmail()).get())
                .collect(Collectors.toList());
    }

}
