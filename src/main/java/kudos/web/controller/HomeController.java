package kudos.web.controller;

import com.google.common.base.Strings;
import kudos.model.UserForm;
import kudos.web.model.ErrorResponse;
import kudos.web.model.IndexResponse;
import kudos.web.model.DataResponse;
import kudos.web.model.Response;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

/**
 * Created by chc on 15.7.29.
 */
@Controller
public class HomeController extends BaseController {

    Logger LOG = Logger.getLogger(HomeController.class.getName());

    @RequestMapping(value="/", method = RequestMethod.GET)
    public Response index(Principal principal) {
        IndexResponse response = new IndexResponse();
        response.setIsLogged(principal != null);
        return response;
    }


    @RequestMapping(value="/login", method = RequestMethod.POST)
    public Response login(String email,
                          String password, HttpServletRequest request) {

        if(Strings.isNullOrEmpty(email)){
            return DataResponse.fail("Please enter your email");
        }

        if(Strings.isNullOrEmpty(password)){
            return DataResponse.fail("Please enter your password");
        }

        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);
        } catch (AuthenticationException e) {
            return DataResponse.fail(e.getMessage());
        }

        return DataResponse.success();
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    public Response register(@ModelAttribute("form") UserForm userForm, Errors errors){
        JSONObject json = new JSONObject();
        new UserForm.FormValidator().validate(userForm, errors);

        if(!errors.hasErrors() && !userDAO.getUserByEmail(userForm.getEmail()).isPresent()) {
            userDAO.create(userForm.toUser());
            return DataResponse.success();
        } else if(!errors.hasErrors()){
            json.put("emailError","email.already.occupied");
            return DataResponse.fail(json.toString());
        } else {

            List<FieldError> emailErrors = errors.getFieldErrors("email");
            List<FieldError> confirmPasswordErrors = errors.getFieldErrors("confirmPassword");

            FieldError nameError = errors.getFieldError("name");
            FieldError surnameError = errors.getFieldError("surname");
            FieldError passwordError = errors.getFieldError("password");

            if(emailErrors != null && emailErrors.size() > 0){
                for(FieldError error : emailErrors){
                    json.put("emailError",error.getCode());
                }
            }

            if(confirmPasswordErrors != null && confirmPasswordErrors.size() > 0){
                for(FieldError error : confirmPasswordErrors){
                    json.put("confirmPasswordError",error.getCode());
                }
            }

            if(nameError != null){
                json.put("nameError",nameError.getCode());
            }

            if(surnameError != null){
                json.put("surnameError",surnameError.getCode());
            }

            if(passwordError != null){
                json.put("passwordError",passwordError.getCode());
            }

            return DataResponse.fail(json.toString());
        }
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Response logout(HttpSession session,Principal principal){
        if(principal == null){
            return new ErrorResponse("You cannot logout because you are not logged in");
        }
        session.invalidate();
        return DataResponse.success();
    }

}
