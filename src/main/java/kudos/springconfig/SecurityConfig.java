package kudos.springconfig;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static Logger LOG = Logger.getLogger(SecurityConfig.class.getName());

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder, AuthenticationProvider authenticationProvider)
            throws Exception {
        LOG.warn("configureGlobal");
        builder.authenticationProvider(authenticationProvider);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

       LOG.warn("configure");
        http.csrf().disable().formLogin().loginPage("/index").failureUrl("/user/registration").usernameParameter("email")
                .passwordParameter("password").defaultSuccessUrl("/user/home").and().authorizeRequests().antMatchers("/user/home").hasRole("USER");

        /*http.csrf().disable().authorizeRequests()
                .antMatchers("/user/home/").hasRole("USER")
                .and()
                .formLogin()
                .loginPage("/").failureUrl("/user/register")
                .usernameParameter("username")
                .passwordParameter("password");*/

    }
}