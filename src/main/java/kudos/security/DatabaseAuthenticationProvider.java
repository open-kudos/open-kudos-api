package kudos.security;

import com.google.common.base.Optional;
import kudos.dao.UserDAO;
import kudos.model.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by p998dls on 2015.07.28.
 */
public class DatabaseAuthenticationProvider implements AuthenticationProvider {

    private final UserDAO userDAO;

    public DatabaseAuthenticationProvider(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();

        Optional<User> user = userDAO.getUserByEmail(name);

        if (user.isPresent() && password.equals(user.get().getPassword())) {
            List<GrantedAuthority> grantedAuths = new LinkedList();
            grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
            return new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
