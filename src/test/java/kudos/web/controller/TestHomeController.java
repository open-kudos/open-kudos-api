package kudos.web.controller;

import kudos.web.config.SecurityConfig;
import kudos.web.config.WebConfig;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebConfig.class, SecurityConfig.class})
@WebAppConfiguration
public class TestHomeController extends BaseMVCTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    HomeController homeController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx).build();
    }

    @Test
    public void testLoginScenario_Success() throws Exception {
        postLogin("user", "user", "success", "");

    }

    @Test
    public void testLoginScenario_Fail() throws Exception {
        postLogin("user", "user1", "fail", "email_password_mismatch");
    }

    @Test
    public void testRegistrationWithValidData() throws Exception {
        testRegistration("mantttttas1@gmail.com", "Mantas", "Damijonaitis", "dmj", "dmj", "success",
                createJSONErrors(false, false, false, false, false, false, false));
    }

    @Test
    public void testIfRegistrationHandlesEmptyFields() throws Exception{
        testRegistration("","","","","","fail",createJSONErrors(true,true,true,true,true,true,true));
    }

    @Test
    public void testIfRegistrationHandlesWrongEmail() throws Exception{
        testRegistration("asdasd","mantas","mantas","123","123","fail"
                ,createJSONErrors(false,true,false,false,false,false,false));
    }

    @Test
    public void testIfRegistrationHandlesMismatchedPasswords() throws Exception{
        testRegistration("mant@gmail.com","man","dam","123","321","fail",
                createJSONErrors(false,false,false,false,false,false,true));
    }

    private void testRegistration(String email, String name, String surname, String password, String confirmPassword, String status, String errorJSON) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .param("email", email)
                .param("name", name)
                .param("surname", surname)
                .param("password", password)
                .param("confirmPassword", confirmPassword))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status", is(status)))
                .andExpect(jsonPath("$.response.message", containsString(errorJSON)));
    }

    private void postLogin(String email, String password, String status, String message) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/login").param("email", email).param("password", password))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.status", is(status)))
                .andExpect(jsonPath("$.response.message", is(message)));

    }


    private void postIndex(MockHttpSession session, boolean isLogged) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response.logged", is(isLogged)));
    }

    private String createJSONErrors(boolean createEmptyEmailError, boolean createIncorrectEmailError, boolean createEmptyNameError,
                                       boolean createEmptySurnameError, boolean createEmptyPasswordError, boolean createEmptyConfirmPasswordError,
                                        boolean createMismatchPasswordError) {

        JSONObject json = new JSONObject();

        if (createEmptyEmailError) {
            json.put("emailError", "required.email");
        }

        if (createIncorrectEmailError && !createEmptyEmailError) {
            json.put("emailError", "incorrect.email");
        }

        if (createEmptyNameError) {
            json.put("nameError", "name.not.specified");
        }

        if (createEmptySurnameError) {
            json.put("surnameError", "surname.not.specified");
        }

        if (createEmptyPasswordError) {
            json.put("passwordError", "password.not.specified");
        }

        if (createEmptyConfirmPasswordError) {
            json.put("confirmPasswordError", "confirm.password.not.specified");
        }

        if (createMismatchPasswordError && !createEmptyConfirmPasswordError && !createEmptyPasswordError) {
            json.put("confirmPasswordError", "no.match.password");
        }

        if(!createEmptyEmailError && !createIncorrectEmailError && !createEmptyNameError &&
        !createEmptySurnameError && !createEmptyPasswordError && !createEmptyConfirmPasswordError &&
        !createMismatchPasswordError){

            return "";

        }

        return json.toString();

    }
}
