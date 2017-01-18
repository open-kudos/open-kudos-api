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
                    .antMatchers("/user/**").hasAnyRole("USER", "ADMIN")
            .and()
                .authorizeRequests()
                    .antMatchers("/challenge/**").hasRole("USER")
            .and()
                .authorizeRequests()
                    .antMatchers("/kudos/**").hasRole("USER")
            .and()
                .authorizeRequests()
                    .antMatchers("/relation/**").hasRole("USER")
            .and()
                .authorizeRequests()
                    .antMatchers("/inventory/add").hasRole("ADMIN")
            .and()
                .authorizeRequests()
                    .antMatchers("/inventory/*/delete").hasRole("ADMIN")
            .and()
                .authorizeRequests()
                    .antMatchers("/order/*/approve").hasRole("ADMIN")
            .and()
                .authorizeRequests()
                    .antMatchers("/order/pending").hasRole("ADMIN")
            .and()
                .authorizeRequests()
                    .antMatchers("/order/place/*").hasRole("USER")
            .and()
                .authorizeRequests()
                    .antMatchers("/order/*").hasAnyRole("USER", "ADMIN");
    }

    @Bean(name="KudosAuthenticationManager")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}