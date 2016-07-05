package kudos.web.controllers;

import kudos.model.User;
import kudos.web.exceptions.UserException;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/migration")
public class MigrationController extends BaseController  {

    @RequestMapping(value = "/transferUsers", method = RequestMethod.GET)
    public
    @ApiResponseObject
    @ResponseBody
    List<User> transferUsers() throws UserException {
        return usersService.findAllAndCreateNewUsers();
    }

}
