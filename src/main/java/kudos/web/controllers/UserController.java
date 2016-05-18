package kudos.web.controllers;

import freemarker.template.TemplateException;
import kudos.model.Transaction;
import kudos.model.User;
import kudos.services.KudosService;
import kudos.web.beans.form.MyProfileForm;
import kudos.web.exceptions.FormValidationException;
import kudos.web.exceptions.UserException;
import org.apache.log4j.Logger;
import org.jsondoc.core.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by chc on 15.7.23.
 */
@Api(name = "User Controller", description = "Controller for managing user account")
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    private Logger LOG = Logger.getLogger(UserController.class.getName());

    @ApiMethod(description = "Service to disable user account")
    @RequestMapping(value = "/disable", method = RequestMethod.GET)
    public void disableMyAccount(HttpSession session) throws UserException {
        usersService.disableUsersAccount();
        session.invalidate();

    }

    @ApiMethod(description = "Service to show user account")
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public User getUserAccount() throws UserException {
        return usersService.getCompletedUser();
    }

    @ApiMethod(description = "Service to complete user profile")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "email", required = false, description = "New email, for testing use newEM@gmail.com"),
            @ApiQueryParam(name = "firstName", required = false, description = "New name, for testing use XYZ"),
            @ApiQueryParam(name = "lastName", required = false, description = "New surname, for testing use ZYX"),
            @ApiQueryParam(name = "birthday", required = false, description = "User birthday. For testing use 1970-08-08"),
            @ApiQueryParam(name = "phone", required = false, description = "User phone number. For testing use +37067504333"),
            @ApiQueryParam(name = "startedToWorkDate", required = false,
                    description = "The date when user started to work. For testing use 2015-05-05"),
            @ApiQueryParam(name = "position", required = false, description = "The position in company, for testing use CEO"),
            @ApiQueryParam(name = "department", required = false, description = "The department of user, for testing use Wix"),
            @ApiQueryParam(name = "location", required = false, description = "The location of user company. For testing use Lithuania"),
            @ApiQueryParam(name = "team", required = false, description = "The team of user. For testing use TDI"),
            @ApiQueryParam(name = "showBirthday", required = false, allowedvalues = {"true", "false"},
                    description = "The decision of user to show his birthday date or not. For testing use true"),
            @ApiQueryParam(name = "oldPassword", required = false, description = "Old user password, for testing use google"),
            @ApiQueryParam(name = "newPassword", required = false,
                    description = "New user password. Required if old password was specified. For testing use microsoft"),
            @ApiQueryParam(name = "newPasswordConfirm", required = true,
                    description = "The confirm of new password. Must match with the new password. For testing use microsoft")
    })
    @ApiErrors(apierrors = {
            @ApiError(code = "birthday_date_not_specified", description = "If birthday date was not specified"),
            @ApiError(code = "birthday_date_incorrect", description = "If specified birthday date was incorrect"),
            @ApiError(code = "incorrect_phone", description = "If entered phone number was incorrect"),
            @ApiError(code = "startedToWorkDate_not_specified", description = "If started to work date was not specified"),
            @ApiError(code = "startedToWorkDate_incorrect", description = "If started to work date was incorrect"),
            @ApiError(code = "newPassword_not_specified", description = "If new password was not specified"),
            @ApiError(code = "newPasswordConfirm_not_specified", description = "If new password confirm was not specified"),
            @ApiError(code = "no_new_password_match", description = "If confirm passwords do not match")
    })
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody User updateUserProfile(MyProfileForm myProfileForm, Errors errors)
            throws FormValidationException, UserException, MessagingException, IOException, TemplateException {

        new MyProfileForm.MyProfileValidator().validate(myProfileForm, errors);
        if (errors.hasErrors()) {
            throw new FormValidationException(errors);
        }
        return usersService.updateUser(myProfileForm);
    }

    @ApiMethod(description = "Service to listing users")
    @ApiParams(queryparams = {
            @ApiQueryParam(name = "filter", required = false, description = "User list filter")
    })
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public List<User> listUsers(String filter) throws Exception{
        return usersService.list(filter);
    }

    @ApiMethod(description = "Gets all confirmed users")
    @RequestMapping(value = "/confirmedUsers", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody List<User> confirmedUsers() throws UserException {
        return usersService.getAllConfirmedUsers();
    }

    @ApiMethod(description = "Gets top kudos receivers")
    @RequestMapping(value = "/topreceivers", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody Map<String, Integer> getTopReceivers() throws UserException {
        Map<String, Integer> topReceivers = new HashMap<>();
        for (User user : usersService.getAllConfirmedUsers()) {
            int incomingKudos = kudosService.getKudos(user);
            topReceivers.put(user.getFirstName() + " " + user.getLastName(), incomingKudos);
        }

        return topReceivers.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new));
    }

    @ApiMethod(description = "Gets top kudos senders")
    @RequestMapping(value = "/topsenders", method = RequestMethod.GET)
    public @ApiResponseObject @ResponseBody Map<String, Integer> getTopSenders() throws UserException {
        Map<String, Integer> topSenders = new HashMap<>();
        int outgoingKudos;
        for (User user : usersService.getAllConfirmedUsers()) {
            outgoingKudos = 0;
            for (Transaction transaction : transactionService.getTransactionsByEmailAndStatus(user.getEmail())) {
                outgoingKudos += transaction.getAmount();
            }
            topSenders.put(user.getFirstName() + " " + user.getLastName(), outgoingKudos);
        }

        return topSenders.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e2, LinkedHashMap::new));
    }
    
}
