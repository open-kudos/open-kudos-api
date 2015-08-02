package kudos.web.controller;

import kudos.model.UserForm;
import kudos.web.model.DataResponse;
import kudos.web.model.Response;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;


/**
 * Created by chc on 15.7.23.
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private static Logger LOG = Logger.getLogger(UserController.class.getName());

    @RequestMapping(value = "/delete-me", method = RequestMethod.POST)
    public Response deleteMyAccount(HttpSession session,Principal principal){
        userDAO.remove(principal.getName());
        session.invalidate();
        return DataResponse.success();
    }

}
