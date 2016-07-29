package kudos.repositories;

import kudos.model.Idea;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface WisdomWallRepository extends MongoRepository<Idea, String> {

}
