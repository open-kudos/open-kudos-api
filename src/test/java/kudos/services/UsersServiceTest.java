package kudos.services;

import freemarker.template.TemplateException;
import kudos.model.User;
import kudos.model.UserStatus;
import kudos.repositories.UserRepository;
import kudos.exceptions.UserException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
public class UsersServiceTest {

//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private EmailService emailService;
//    @Mock
//    private ChallengeService challengeService;
//    @Mock
//    private KudosService kudosService;
//    @Mock
//    private AuthenticationManager authenticationManager;
//
//    @InjectMocks
//    private UsersService usersService = new UsersService();
//
//    private User mockedUser;
//
//    private static List<User> mockedUsers;
//
//    @Before
//    public void before() throws UserException {
//        mockedUser = mock(User.class);
//        mockedUsers = new ArrayList<>();
//        mockedUsers.add(new User("name1", "lastname1", "pass1", "aaa1@aaa.com", UserStatus.NOT_CONFIRMED));
//        mockedUsers.add(new User("name2", "lastname2", "pass2", "aaa2@aaa.com", UserStatus.NOT_COMPLETED));
//        mockedUsers.add(new User("name3", "lastname3", "pass3", "aaa3@aaa.com", UserStatus.COMPLETED));
//
//    }
//
//    @Test
//    public void testIfUsersServiceReturnsKudosMaster() throws UserException {
//        assertEquals("master@of.kudos", usersService.getKudosMaster().getEmail());
//    }
//
//    @Test
//    public void testIfSaveMethodSavesNewUser() throws UserException, MessagingException, IOException, TemplateException {
//        when(userRepository.save(mockedUser)).thenReturn(mockedUser);
//        assertEquals(userRepository.save(mockedUser), mockedUser);
//    }
//
//    @Test
//    public void testIfServiceReturnsAllConfirmedUsers() {
//        assertEquals(userRepository.findUsersByIsConfirmed(true), usersService.getAllConfirmedUsers());
//    }
//
////    @Test
////    public void testUserFilter(){
////        when(userRepository.searchAllFields(any(String.class))).thenReturn(users);
////        List<User> result = usersService.list("aaa@aaa.com");
////        for (User res : result)
////        assertTrue(result.size() == 2);
////    }
////
////    @Test
////    public void testEmptyFilter(){
////        when(userRepository.searchAllFields(any(String.class))).thenReturn(users);
////        List<User> result = usersService.list(null);
////        assertTrue(result.size() == 3);
////    }

}
