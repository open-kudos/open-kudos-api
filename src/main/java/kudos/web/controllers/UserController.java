package kudos.web.controllers;

import freemarker.template.TemplateException;
import kudos.model.User;
import kudos.web.beans.form.MyProfileForm;
import kudos.web.exceptions.FormValidationException;
import kudos.web.exceptions.UserException;
import org.apache.log4j.Logger;
import org.jsondoc.core.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * Created by chc on 15.7.23.
 */
@Api(name = "user controller", description = "Controller for managing user account")
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private Logger LOG = Logger.getLogger(UserController.class.getName());

    @ApiMethod(description = "Service to disable user account")
    @RequestMapping(value = "/disable", method = RequestMethod.POST)
    public String disableMyAccount(HttpSession session) throws UserException {
        usersService.disableUsersAcount();
        session.invalidate();
        // TODO no Strings should be returned
        return "disabled";
    }

    @ApiMethod(description = "Service to show user account")
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    // TODO why is the method name "showHomePage" ?
    public User showHomePage() throws UserException {
        return usersService.getCompletedUser();
    }

    @ApiMethod(description = "Service to complete user profile")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "email", required = false, description = "New email, for testing use newEM@gmail.com"),
            @ApiQueryParam(name = "firstName", required = false, description = "New name, for testing use XYZ"),
            @ApiQueryParam(name = "lastName", required = false, description = "New surname, for testing use ZYX"),
            @ApiQueryParam(name = "birthday", required = true, description = "User birthday. For testing use 1970-08-08"),
            @ApiQueryParam(name = "phone", required = false, description = "User phone number. For testing use +37067504333"),
            @ApiQueryParam(name = "startedToWorkDate", required = true,
                    description = "The date when user started to work. For testing use 2015-05-05"),
            @ApiQueryParam(name = "position", required = false, description = "The position in company, for testing use CEO"),
            @ApiQueryParam(name = "department", required = false, description = "The department of user, for testing use Wix"),
            @ApiQueryParam(name = "location", required = false, description = "The location of user company. For testing use Lithuania"),
            @ApiQueryParam(name = "team", required = false, description = "The team of user. For testing use TDI"),
            @ApiQueryParam(name = "showBirthday", required = false, allowedvalues = {"true","false"},
                    description = "The decision of user to show his birthday date or not. For testing use true"),
            @ApiQueryParam(name = "oldPassword", required = false, description = "Old user password, for testing use google"),
            @ApiQueryParam(name = "newPassword", required = true,
                    description = "New user password. Required if old password was specified. For testing use microsoft"),
            @ApiQueryParam(name = "newPasswordConfirm", required = true,
                    description = "The confirm of new password. Must match with the new password. For testing use microsoft")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "birthday.date.not.specified", description = "If birthday date was not specified"),
            @ApiError(code = "birthday.date.incorrect", description = "If specified birthday date was incorrect"),
            @ApiError(code = "incorrect.phone", description = "If entered phone number was incorrect"),
            @ApiError(code = "startedToWorkDate.not.specified", description = "If started to work date was not specified"),
            @ApiError(code = "startedToWorkDate.incorrect", description = "If started to work date was incorrect"),
            @ApiError(code = "newPassword.not.specified", description = "If new password was not specified"),
            @ApiError(code = "newPasswordConfirm.not.specified", description = "If new password confirm was not specified"),
            @ApiError(code = "no.new.password.match", description = "If confirm passwords do not match")
    })
    @RequestMapping(value = "complete-profile", method = RequestMethod.POST)
    public User completeUserProfile(@ModelAttribute("form") MyProfileForm myProfileForm, Errors errors) throws FormValidationException, UserException, MessagingException, IOException, TemplateException {
        new MyProfileForm.MyProfileValidator().validate(myProfileForm, errors);
        if (errors.hasErrors()) {
            throw new FormValidationException(errors);
        }
        return usersService.completeUser(myProfileForm);
    }
}
