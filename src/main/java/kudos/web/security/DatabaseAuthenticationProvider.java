package kudos.web.security;

import com.google.common.base.Optional;
import kudos.dao.UserDAO;
import kudos.dao.repositories.UserRepository;
import kudos.model.User;
import org.apache.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by p998dls on 2015.07.28.
 */
@Component
public class DatabaseAuthenticationProvider implements AuthenticationProvider {

    UserDAO userDAO;

    private static Logger LOG = Logger.getLogger(DatabaseAuthenticationProvider.class.getName());

    @Autowired
    public DatabaseAuthenticationProvider(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> user = userDAO.getUserByEmail(name);

        if (user.isPresent() && new StrongPasswordEncryptor().checkPassword(password, user.get().getPassword())) {
            List<GrantedAuthority> grantedAuths = new LinkedList();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));

            return new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
        }
        throw new AuthenticationCredentialsNotFoundException("email_password_mismatch");

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
