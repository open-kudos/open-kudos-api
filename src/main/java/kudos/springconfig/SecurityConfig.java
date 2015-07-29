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
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;


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

        http.csrf().disable().authorizeRequests().antMatchers("/home").hasRole("USER")
                .and().exceptionHandling().authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .and().formLogin().loginPage("/login").failureUrl("/login?error")
                .usernameParameter("email").passwordParameter("password").defaultSuccessUrl("/home")
                .and().authorizeRequests().antMatchers("/home").authenticated()
                .and().logout().logoutSuccessUrl("/home");
    }
}