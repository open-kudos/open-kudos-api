package kudos.services;

import kudos.exceptions.RelationException;
import kudos.model.Relation;
import kudos.model.User;
import kudos.repositories.RelationRepository;
import kudos.repositories.UserRepository;
import kudos.web.beans.response.HistoryResponse;
import kudos.web.beans.response.RelationResponse;
import kudos.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RelationService {

//    @Autowired
//    private UsersService usersService;
//
//    @Autowired
//    private RelationRepository relationRepository;
//
//    @Autowired
//    private HistoryService historyService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public RelationResponse addRelation(Relation relation) throws RelationException {
//        if(relationRepository.getRelationByFollowerAndUserToFollow(relation.getFollower(),relation.getUserToFollow()) != null){
//            throw new RelationException("relation_already_exists");
//        }
//
//        if(relation.getFollower().equals(relation.getUserToFollow())){
//            throw new RelationException("cant_follow_yourself");
//        }
//
//        return new RelationResponse(relationRepository.save(relation));
//    }
//
//    public List<RelationResponse> getAllFollowedUsers() throws UserException {
//        User follower = usersService.getLoggedUser().get();
//        return relationRepository.getRelationsByFollower(follower).stream().map(RelationResponse::new).collect(Collectors.toList());
//    }
//
//    public List<RelationResponse> getAllFollowers() throws UserException {
//        User currentUser = usersService.getLoggedUser().get();
//        return relationRepository.getRelationsByUserToFollow(currentUser).stream().map(RelationResponse::new).collect(Collectors.toList());
//
//    }
//
//    public void removeRelation(String email) throws RelationException, UserException {
//        User loggedUser = usersService.getLoggedUser().get();
//        User userToRemoveRelation = userRepository.findByEmail(email);
//        Relation relationToRemove = relationRepository.getRelationByFollowerAndUserToFollow(loggedUser, userToRemoveRelation);
//
//        if(relationToRemove == null){
//            throw new RelationException("relation_not_exist");
//        }
//
//        relationRepository.delete(relationToRemove);
//    }
//
//    public List<HistoryResponse> getFollowedUsersNewsFeed(int startingIndex, int endingIndex) throws UserException {
//        User currentUser = usersService.getLoggedUser().get();
//        List<HistoryResponse> followedUsersNewsFeed = new ArrayList<>();
//        List<Relation> currentUserRelations = relationRepository.getRelationsByFollower(currentUser);
//
//        for (Relation relation : currentUserRelations){
//            followedUsersNewsFeed.addAll(historyService.getAllUserHistory(relation.getUserToFollow()));
//        }
//
//        return historyService.sortListByTimestamp(followedUsersNewsFeed, startingIndex, endingIndex);
//    }

}
