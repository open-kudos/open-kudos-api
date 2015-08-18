package kudos.web.controllers;

import com.google.common.base.Strings;
import freemarker.template.TemplateException;
import kudos.model.User;
import kudos.web.beans.form.UserForm;
import kudos.web.exceptions.FormValidationException;
import kudos.web.exceptions.UserException;
import org.apache.log4j.Logger;
import org.jsondoc.core.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * Created by chc on 15.8.10.
 */
@Api(name = "registration controller", description = "Controller fo registering")
@Controller
public class RegistrationController extends BaseController {

    private Logger LOG = Logger.getLogger(RegistrationController.class);

    @ApiMethod(description = "Service to register into system")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "password", description = "For testing use 123"),
            @ApiQueryParam(name = "confirmPassword", description = "This password must match first password. For testing use 123"),
            @ApiQueryParam(name = "email"),
            @ApiQueryParam(name = "surname", description = "User surname"),
            @ApiQueryParam(name = "name", description = "User name")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "no.match.password", description = "If passwords do not match"),
            @ApiError(code = "email.not.specified", description = "If email is not specified"),
            @ApiError(code = "email.incorrect", description = "If email is incorrect"),
            @ApiError(code = "name.not.specified", description = "If email is not specified"),
            @ApiError(code = "surname.not.specified", description = "If surname is not specified"),
            @ApiError(code = "password.not.specified", description = "If password is not specified"),
            @ApiError(code = "confirm.password.not.specified", description = "If confirm password is not specified")
    })
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public
    @ResponseBody
    User register(@ModelAttribute("form") UserForm userForm, Errors errors) throws FormValidationException, MessagingException, UserException, IOException, TemplateException {
        new UserForm.FormValidator().validate(userForm, errors);
        if (errors.hasErrors()) {
            throw new FormValidationException(errors);
        }
        return usersService.registerUser(userForm.toUser());
    }

    @ApiMethod(description = "Service to confirm user email")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "id", description = "Unique id received by email")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "id.not.specified", description = "If unique id received by email was not specified"),
            @ApiError(code = "user.not.found", description = "If user with unique id was not found")
    })
    @RequestMapping(value = "/confirm-email", method = RequestMethod.POST)
    public User confirmEmail(String id) throws UserException {
        if(Strings.isNullOrEmpty(id)){
            throw new UserException("id.not.specified");
        }
        return usersService.confirmUser(id);
    }

    @ApiMethod(description = "Resets user password by sending email. To perform this action it is not mandatory to log into system")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "email", description = "The email of user that is wanted to be reset")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "email.not.specified", description = "If email was not specified"),
            @ApiError(code = "user.not.exist", description = "If user does not exist"),
            @ApiError(code = "user.not.registered", description = "If user is not registered(disabled his account")
    })
    @RequestMapping(value = "/reset-my-password", method = RequestMethod.POST)
    public String resetMyPassword(String email) throws MessagingException, TemplateException, UserException, IOException {
        usersService.resetPassword(email);
        return "Success";
    }


}
