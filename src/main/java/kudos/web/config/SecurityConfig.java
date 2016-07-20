package kudos.web.config;

import kudos.web.security.DatabaseAuthenticationProvider;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
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
    public void configureGlobal(AuthenticationManagerBuilder builder, DatabaseAuthenticationProvider authenticationProvider)
            throws Exception {
        LOG.warn("configureGlobal");
        builder.authenticationProvider(authenticationProvider);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        LOG.warn("configure");

        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/user/**").hasRole("USER")
            .and()
                .authorizeRequests()
                    .antMatchers("/challenges/**").hasRole("USER")
            .and()
                .authorizeRequests()
                    .antMatchers("/kudos/**").hasRole("USER")
            .and()
                .authorizeRequests()
                    .antMatchers("/relations/**").hasRole("USER");
    }

    @Bean(name="KudosAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}