package kudos.web.controllers;

import kudos.model.Idea;
import kudos.model.User;
import kudos.web.beans.form.WisdomWallForm;
import kudos.web.exceptions.FormValidationException;
import kudos.web.exceptions.UserException;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Api(name = "Wisdom Wall Controller", description = "Controller for managing wisdom wall")
@Controller
@RequestMapping("/wisdomwall")
public class WisdomWallController extends BaseController {

//    @ApiMethod(description = "Service to get all ideas of wisdom wall by author")
//    @RequestMapping(value = "/byauthor", method = RequestMethod.GET)
//    public @ApiResponseObject
//    @ResponseBody
//    List<Idea> getIdeasByAuthor() {
//        List<Idea> ideasByAuthor = new ArrayList<>();
//        for (User user : usersService.getAllConfirmedUsers()) {
//            ideasByAuthor.add(wisdomWallService.getWisdomWallIdeasByPostedBy(user.getEmail()));
//        }
//        return ideasByAuthor;
//    }

    @ApiMethod(description = "Service to add idea to wisdom wall")
    @RequestMapping(value = "/addidea", method = RequestMethod.POST)
    public @ApiResponseObject
    @ResponseBody Idea addIdea(WisdomWallForm wisdomWallForm, Errors errors) throws UserException, FormValidationException {

        new WisdomWallForm.WisdomWallFormValidator().validate(wisdomWallForm, errors);

        if (errors.hasErrors())
            throw new FormValidationException(errors);

        User author = usersService.findByEmail(wisdomWallForm.getAuthorEmail())
                .orElseThrow(() -> new UserException("receiver.not.exist"));

        return wisdomWallService.addIdeaToWisdomWall(author, wisdomWallForm.getIdea());
    }



}
