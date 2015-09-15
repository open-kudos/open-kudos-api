package kudos.services;

import freemarker.template.TemplateException;
import kudos.model.User;
import kudos.repositories.UserRepository;
import kudos.web.exceptions.UserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;

import javax.mail.MessagingException;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * UsersService tests suite
 * Created by chc on 15.8.27.
 */
@RunWith(MockitoJUnitRunner.class)
public class UsersServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private ChallengeService challengeService;
    @Mock
    private KudosService kudosService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UsersService usersService = new UsersService();

    private User mockedUser;

    @Before
    public void before() throws UserException {
        mockedUser = mock(User.class);
    }

    @Test
    public void testIfUsersServiceFindsUserByEmail() throws UserException {
        when(userRepository.findOne("")).thenReturn(mockedUser);
        assertEquals(mockedUser, usersService.findByEmail("").get());
    }

    @Test
    public void testIfUsersServiceRegistersUser() throws MessagingException, TemplateException, UserException, IOException {
        when(mockedUser.getEmail()).thenReturn("none@none.no");
        when(mockedUser.getPassword()).thenReturn("123");
        when(userRepository.exists(any(String.class))).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mock(User.class));
        ArgumentCaptor<User> savedUser = ArgumentCaptor.forClass(User.class);

        usersService.registerUser(mockedUser);
        verify(userRepository).save(savedUser.capture());

        assertTrue(savedUser.getValue().getEmail().equals(mockedUser.getEmail()));
        assertNotNull(savedUser.getValue().getPassword());
    }

}
