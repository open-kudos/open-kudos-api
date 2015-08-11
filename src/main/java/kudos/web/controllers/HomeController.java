package kudos.web.controllers;

import kudos.web.beans.response.IndexResponse;
import kudos.web.beans.response.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;


/**
 * Created by chc on 15.7.29.
 */
@Controller
public class HomeController extends BaseController {

    Logger LOG = Logger.getLogger(HomeController.class.getName());

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response index(Principal principal) {
        IndexResponse response = new IndexResponse();
        response.setIsLogged(principal != null);
        return response;
    }




}