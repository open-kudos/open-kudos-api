package kudos.web.controllers;

import kudos.web.beans.response.IndexResponse;
import kudos.web.beans.response.Response;
import org.apache.log4j.Logger;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;


/**
 * Created by chc on 15.7.29.
 */
@Api(name = "home-controller",description = "Service to test connection / check whether someone is logged in or not.")
@Controller
public class HomeController extends BaseController {

    private Logger LOG = Logger.getLogger(HomeController.class.getName());

    @ApiMethod(description = "method to check whether user is logged in or not")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ApiResponseObject
    @ResponseBody Response index(Principal principal) {
        IndexResponse response = new IndexResponse();
        response.setIsLogged(principal != null);
        return response;
    }




}