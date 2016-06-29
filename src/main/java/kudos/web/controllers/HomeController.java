package kudos.web.controllers;

import kudos.web.beans.response.IndexResponse;
import kudos.web.beans.response.Response;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;


/**
 * Created by chc on 15.7.29.
 */
@Api(name = "Home Controller",description = "Service to test connection / check whether someone is logged in or not.")
@Controller
public class HomeController extends BaseController {

    @ApiMethod(description = "method to check whether user is logged in or not")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody Response index(Principal principal) {

        return new IndexResponse(principal != null);
    }
}