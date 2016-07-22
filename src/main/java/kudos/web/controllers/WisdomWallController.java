package kudos.web.controllers;

import org.jsondoc.core.annotation.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(name = "Wisdom Wall Controller", description = "Controller for managing wisdom wall")
@Controller
@RequestMapping("/wisdomwall")
public class WisdomWallController extends BaseController {

//    @Autowired
//    @Qualifier(value = "DBTimeFormatter")
//    DateTimeFormatter dateTimeFormatter;
//
//    @Value("${kudos.maxNameLength}")
//    private String maxAuthorNameLength;
//    @Value("${kudos.maxIdeaLength}")
//    private String maxIdeaLength;
//    @ApiMethod(description = "Service to add idea to wisdom wall")
//    @RequestMapping(value = "/addidea", method = RequestMethod.POST)
//    public @ApiResponseObject
//    @ResponseBody Idea addIdea(WisdomWallForm wisdomWallForm, Errors errors) throws UserException, FormValidationException {
//        new WisdomWallForm.WisdomWallFormValidator(maxAuthorNameLength, maxIdeaLength).validate(wisdomWallForm, errors);
//        if (errors.hasErrors())
//            throw new FormValidationException(errors);
//
//        return wisdomWallService.addIdeaToWisdomWall(wisdomWallForm.getAuthorName(), wisdomWallForm.getIdea());
//    }
//
//    @ApiMethod(description = "Service to get all posted ideas")
//    @RequestMapping(value = "/getideas", method = RequestMethod.GET)
//    public @ApiResponseObject
//    @ResponseBody List<Idea> getAllIdeas() throws ParseException {
//        List<Idea> allIdeas = new ArrayList<>();
//        for (User user : usersService.getAllConfirmedUsers()) {
//            for (Idea idea : wisdomWallService.getIdeasByPostedBy(user.getEmail())){
//
//                if (Strings.isNullOrEmpty(idea.getCreationDate())) {
//                    idea.setCreationDate(LocalDateTime.now().toString(dateTimeFormatter));
//                    wisdomWallService.updateIdeaCreationTime(idea);
//                }
//
//                if (dateTimeFormatter.parseLocalDateTime(idea.getCreationDate()).plusDays(90).isAfter(LocalDateTime.now()))
//                    allIdeas.add(idea);
//            }
//        }
//        return allIdeas;
//    }
//
//    @ApiMethod(description = "Service to get random idea from the list")
//    @RequestMapping(value = "/randomIdea", method = RequestMethod.GET)
//    public @ApiResponseObject
//    @ResponseBody Idea getRandomIdea() throws ParseException {
//        List<Idea> ideas = getAllIdeas();
//        Random randomGenerator = new Random();
//        if (ideas.size() > 0) {
//            int index = randomGenerator.nextInt(ideas.size());
//            return ideas.get(index);
//        }
//        //TODO logic should be in service to return a custom idea fake created by logged in user
//        return null;
//    }

}
