package kudos.web.controllers;

import com.google.common.base.Strings;
import kudos.exceptions.IdNotSpecifiedException;
import kudos.exceptions.RelationException;
import kudos.model.Relation;
import kudos.model.User;
import kudos.web.exceptions.UserException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

/**
 * Created by chc on 15.8.20.
 */
@Controller
@RequestMapping("/relations")
public class RelationController extends BaseController {

    private Logger LOG = Logger.getLogger(RelationController.class);

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody Relation addRelation(String email) throws IdNotSpecifiedException, UserException, RelationException {
        if(Strings.isNullOrEmpty(email)){
            throw new IdNotSpecifiedException("id.not.specified");
        }
        Optional<User> maybeUser = usersService.findByEmail(email);
        if(!maybeUser.isPresent()){
            throw new UserException("user.not.exist");
        }
        User user = maybeUser.get();
        User follower = usersService.getLoggedUser().get();
        return relationService.addRelation(new Relation(follower.getEmail(),follower.getFirstName(),follower.getLastName(),
                user.getEmail(),user.getFirstName(),user.getLastName()));
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    public void removeRelation(String email) throws IdNotSpecifiedException, UserException, RelationException {
        if(Strings.isNullOrEmpty(email)){
            throw new IdNotSpecifiedException("id.not.specified");
        }
        Optional<User> maybeUser = usersService.findByEmail(email);
        if(!maybeUser.isPresent()){
            throw new UserException("user.not.exist");
        }
        User user = maybeUser.get();
        User follower = usersService.getLoggedUser().get();
        relationService.removeRelation(new Relation(follower.getEmail(),follower.getFirstName(),follower.getLastName(),
                user.getEmail(),user.getFirstName(),user.getLastName()));
    }

    @RequestMapping(value = "/followers", method = RequestMethod.GET)
    public @ResponseBody List<String> getFollowers() throws UserException {
        return relationService.getAllFollowers();
    }

    @RequestMapping(value = "/followed", method = RequestMethod.GET)
    public @ResponseBody List<String> getFollowed() throws UserException {
        return relationService.getAllFollowedUsers();
    }

}
