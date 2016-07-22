package kudos.services;

import kudos.model.Idea;
import kudos.repositories.WisdomWallRepository;
import kudos.exceptions.UserException;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WisdomWallServiceTest {

//    private WisdomWallService wisdomWallService;
//    private Idea testIdea;
//    private WisdomWallRepository wisdomWallRepository;
//    private static List<Idea> ideas = new ArrayList<>();
////    static {
////        ideas.add(new Idea("author1", "postedBy1@test.lt", "idea1", LocalDateTime.now().toString()));
////        ideas.add(new Idea("author2", "postedBy2@test.lt", "idea2", LocalDateTime.now().toString()));
////        ideas.add(new Idea("author3", "postedBy3@test.lt", "idea3", LocalDateTime.now().toString()));
////    }
//
//    @Before
//    public void before() throws UserException {
//        wisdomWallService = mock(WisdomWallService.class);
//        wisdomWallRepository = mock(WisdomWallRepository.class);
//    }
//
////    @Test
////    public void testIfServiceAddsIdeaToWisdomWall() throws UserException {
////        testIdea = new Idea("author", "test@test.com", "idea", LocalDateTime.now().toString());
////        when(wisdomWallService.addIdeaToWisdomWall(any(String.class), any(String.class))).thenReturn(testIdea);
////        assertEquals(wisdomWallService.addIdeaToWisdomWall(any(String.class), any(String.class)), testIdea);
////    }
//
////    @Test
////    public void testIfServiceGetsIdeasByPersonWhoPostedIt() {
////        when(wisdomWallRepository.findIdeasByPostedByEmail(any(String.class))).thenReturn(ideas);
////        assertEquals("postedBy1@test.lt", wisdomWallRepository.findIdeasByPostedByEmail("").get(0).getPostedBy());
////    }
}
