package kudos.services;

import kudos.web.beans.form.LoginForm;
import org.junit.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertEquals;

public class LoginFormTest {

    private LoginForm form = new LoginForm("first.last@swedbank.lt", "pass");
    private Errors errors;
    private final String domain = "swedbank";

    @Test
    public void testIfLoginFormValidatesEmail() {
        form.setEmail("name.surname@swedbank.lt");
        errors = new BeanPropertyBindingResult(form, "form");
        new LoginForm.LoginFormValidator(domain).validate(form, errors);
        assertEquals(false, errors.hasErrors());

        form.setEmail("name.surname@gmail.com");
        errors = new BeanPropertyBindingResult(form, "form");
        new LoginForm.LoginFormValidator(domain).validate(form, errors);
        assertEquals(true, errors.hasErrors());

        form.setEmail("");
        errors = new BeanPropertyBindingResult(form, "form");
        new LoginForm.LoginFormValidator(domain).validate(form, errors);
        assertEquals(true, errors.hasErrors());
    }

    @Test
    public void testIfLoginFormValidatesPassword() {
        form.setPassword("pass");
        errors = new BeanPropertyBindingResult(form, "form");
        new LoginForm.LoginFormValidator(domain).validate(form, errors);
        assertEquals(false, errors.hasErrors());

        form.setPassword("");
        errors = new BeanPropertyBindingResult(form, "form");
        new LoginForm.LoginFormValidator(domain).validate(form, errors);
        assertEquals(true, errors.hasErrors());
    }

}
