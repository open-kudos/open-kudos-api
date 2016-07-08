package kudos.web.controllers;

import com.google.common.base.Strings;
import kudos.exceptions.IdNotSpecifiedException;
import kudos.exceptions.RelationException;
import kudos.model.Relation;
import kudos.model.User;
import kudos.web.exceptions.UserException;
import org.apache.log4j.Logger;
import org.jsondoc.core.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

/**
 * Created by chc on 15.8.20.
 */
@Api(description = "service to manage user relations", name = "Relation Controller")
@Controller
@RequestMapping("/relations")
public class RelationController extends BaseController {

    private Logger LOG = Logger.getLogger(RelationController.class);

    @ApiMethod(description = "service to add relation")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "email", description = "The email of user which logged user wants to make a relationship")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "id_not_specified", description = "if email was not specified"),
            @ApiError(code = "user_not_exist", description = "If user with specified email does not exist"),
            @ApiError(code = "relation_already_exists", description = "If relation that logged user wants to create already exists")
    })
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody Relation addRelation(String email) throws IdNotSpecifiedException, UserException, RelationException {
        if(Strings.isNullOrEmpty(email)){
            throw new IdNotSpecifiedException("email_not_specified");
        }
        Optional<User> maybeUser = usersService.findByEmail(email);
        if(!maybeUser.isPresent()){
            throw new UserException("user_not_exist");
        }
        User userToFollow = maybeUser.get();
        User follower = usersService.getLoggedUser().get();
        return relationService.addRelation(new Relation(follower, userToFollow));
    }

    @ApiMethod(description = "service to remove relation")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "email", description = "The email of user which logged user wants to discontinue relationship")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "email_not_specified", description = "If email of user was not specified"),
            @ApiError(code = "user_not_exist", description = "If user which email was specified does not exist"),
            @ApiError(code = "relation_not_exist", description = "If relationship which is wanted to be discontinued does not exist")
    })
    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public void removeRelation(String email) throws UserException, RelationException {

        if(Strings.isNullOrEmpty(email)) throw new RelationException("email_not_specified");

        Optional<User> maybeUser = usersService.findByEmail(email);
        if(!maybeUser.isPresent()) throw new UserException("user_not_exist");


        relationService.removeRelation(email);
    }

    @ApiMethod(description = "service to get all emails of users that follows logged user")
    @RequestMapping(value = "/followers", method = RequestMethod.GET)
    public @ResponseBody List<Relation> getFollowers() throws UserException {
        return relationService.getAllFollowers();
    }

    @ApiMethod(description = "service to get all email of users that are followed by logged user")
    @RequestMapping(value = "/followed", method = RequestMethod.GET)
    public @ResponseBody List<Relation> getFollowed() throws UserException {
        return relationService.getAllFollowedUsers();
    }


}
