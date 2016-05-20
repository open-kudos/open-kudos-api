package kudos.services;

import kudos.model.User;
import kudos.repositories.UserRepository;
import kudos.web.exceptions.UserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    private static List<User> users = new ArrayList<>();
    static {
        users.add(new User("", "aaa@aaa.com"));
        users.add(new User("", "aaa@aaa.com"));
        users.add(new User("", "bbb@bbb.com"));
    }

    @Before
    public void before() throws UserException {
        mockedUser = mock(User.class);
    }

    @Test
    public void testIfUsersServiceFindsUserByEmail() throws UserException {
        when(userRepository.findOne("")).thenReturn(mockedUser);
        assertEquals(mockedUser, usersService.findByEmail("").get());
    }

//    @Test
//    public void testUserFilter(){
//        when(userRepository.searchAllFields(any(String.class))).thenReturn(users);
//        List<User> result = usersService.list("aaa@aaa.com");
//        assertTrue(result.size() == 2);
//    }
//
//    @Test
//    public void testEmptyFilter(){
//        when(userRepository.searchAllFields(any(String.class))).thenReturn(users);
//        List<User> result = usersService.list(null);
//        assertTrue(result.size() == 3);
//    }

}
