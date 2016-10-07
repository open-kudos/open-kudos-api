package kudos.services;

import kudos.web.beans.request.validator.RegisterFormValidator;
import kudos.web.beans.request.RegisterForm;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertEquals;

public class RegisterFormTest {

    private RegisterForm form = new RegisterForm("FirstName", "LastName", "pass", "pass", "first.last@swedbank.lt");
    private Errors errors;
    private final String domain = "swedbank";

//    @Test
//    public void testIfUserFormValidatesPasswordsMatch() {
//        form.setPassword("a");
//        form.setConfirmPassword("a");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(false, errors.hasErrors());
//
//        form.setPassword("a");
//        form.setConfirmPassword("b");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(true , errors.hasErrors());
//
//        form.setPassword("password");
//        form.setConfirmPassword("password");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(false, errors.hasErrors());
//    }
//
//    @Test
//    public void testIfUserFormValidatesEmail() {
//        form.setEmail("name.surname@swedbank.lt");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(false, errors.hasErrors());
//
//        form.setEmail("name.surname@gmail.com");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(true, errors.hasErrors());
//
//        form.setEmail("");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(true, errors.hasErrors());
//    }
//
//    @Test
//    public void testIfUserFormValidatesFirstName() {
//        form.setFirstName("John");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(false, errors.hasErrors());
//
//        form.setFirstName("");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(true, errors.hasErrors());
//
//        form.setFirstName("LoremIpsum");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(false, errors.hasErrors());
//    }
//
//    @Test
//    public void testIfUserFormValidatesLastName() {
//        form.setLastName("John");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(false, errors.hasErrors());
//
//        form.setLastName("");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(true, errors.hasErrors());
//
//        form.setLastName("LoremIpsum");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(false, errors.hasErrors());
//    }
//
//    @Test
//    public void testIfUserFormValidatesPassword() {
//        form.setPassword("pass");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(false, errors.hasErrors());
//
//        form.setPassword("");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(true, errors.hasErrors());
//    }
//
//    @Test
//    public void testIfUserFormValidatesConfirmPassword() {
//        form.setConfirmPassword("pass");
//        form.setPassword("pass1");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(false, errors.hasErrors());
//
//        form.setConfirmPassword("");
//        errors = new BeanPropertyBindingResult(form, "request");
//        new RegisterFormValidator().validate(form, errors);
//        assertEquals(true, errors.hasErrors());
//
//    }
}
