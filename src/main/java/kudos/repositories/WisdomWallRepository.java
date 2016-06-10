package kudos.repositories;

import kudos.model.Idea;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface WisdomWallRepository extends MongoRepository<Idea, String> {

      List<Idea> findIdeasByPostedByEmail(String postedBy);

}
