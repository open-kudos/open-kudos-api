package kudos.web.controllers;

import com.google.common.base.Strings;
import freemarker.template.TemplateException;
import kudos.model.User;
import kudos.web.beans.form.UserForm;
import kudos.web.exceptions.FormValidationException;
import kudos.web.exceptions.UserException;
import org.jsondoc.core.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import java.io.IOException;

/**
 * Created by chc on 15.8.10.
 */
@Api(name = "Registration Controller", description = "Controller fo registering")
@Controller
public class RegistrationController extends BaseController {

    @ApiMethod(description = "Service to register into system")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "password", description = "For testing use 123"),
            @ApiQueryParam(name = "confirmPassword", description = "This password must match first password. For testing use 123"),
            @ApiQueryParam(name = "email"),
            @ApiQueryParam(name = "surname", description = "User surname"),
            @ApiQueryParam(name = "name", description = "User name")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "no_match_password", description = "If passwords do not match"),
            @ApiError(code = "email_not_specified", description = "If email is not specified"),
            @ApiError(code = "email_incorrect", description = "If email is incorrect"),
            @ApiError(code = "name_not_specified", description = "If email is not specified"),
            @ApiError(code = "surname_not_specified", description = "If surname is not specified"),
            @ApiError(code = "password_not_specified", description = "If password is not specified"),
            @ApiError(code = "confirm_password_not_specified", description = "If confirm password is not specified")
    })
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public @ResponseBody User register(@ModelAttribute("form") UserForm userForm, Errors errors)
            throws FormValidationException, MessagingException, UserException, IOException, TemplateException {

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
            @ApiError(code = "id_not_specified", description = "If unique id received by email was not specified"),
            @ApiError(code = "user_not_found", description = "If user with unique id was not found")
    })
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    public User confirmEmail(String id) throws UserException {
        if(Strings.isNullOrEmpty(id))
            throw new UserException("id_not_specified");

        return usersService.confirmUser(id);
    }

    @ApiMethod(description = "Resets user password by sending email. To perform this action it is not mandatory to log into system")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "email", description = "The email of user that is wanted to be reset")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "first_name_is_required", description = "If first name was not specified"),
            @ApiError(code = "last_name_is_required", description = "If last name was not specified"),
            @ApiError(code = "email_not_specified", description = "If email was not specified"),
            @ApiError(code = "user_not_exist", description = "If user does not exist"),
            @ApiError(code = "user_not_registered", description = "If user is not registered(disabled his account")
    })
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    public String resetMyPassword(String email) throws MessagingException, TemplateException, UserException, IOException {

        usersService.resetPassword(email);
        return "Success";
    }


}
