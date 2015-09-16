package kudos.repositories;

import com.google.common.base.Strings;
import kudos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Modestas on 2015-09-16.
 */
public class UserRepositoryImpl implements CustomUserRepository {

    private static final int MAX_LIST_SIZE = 100;
    private final MongoOperations operations;

    @Autowired
    public UserRepositoryImpl(MongoOperations operations) {
        this.operations = operations;
    }

    @Override
    public List<User> searchAllFields(String seed) {
        if (Strings.isNullOrEmpty(seed)) {
            return operations.findAll(User.class);
        } else {
            Query searchQuery = new Query();
            Criteria searchCriteria = new Criteria();

            List<Criteria> orCriterias = new ArrayList<>();
            orCriterias.add(Criteria.where("email").regex(seed));
            orCriterias.add(Criteria.where("firstName").regex(seed));
            orCriterias.add(Criteria.where("lastName").regex(seed));
            orCriterias.add(Criteria.where("birthday").regex(seed));
            orCriterias.add(Criteria.where("phone").regex(seed));
            orCriterias.add(Criteria.where("startedToWorkDate").regex(seed));
            orCriterias.add(Criteria.where("position").regex(seed));
            orCriterias.add(Criteria.where("department").regex(seed));
            orCriterias.add(Criteria.where("location").regex(seed));
            orCriterias.add(Criteria.where("team").regex(seed));

            searchCriteria.orOperator(orCriterias.toArray(new Criteria[orCriterias.size()]));

            searchQuery.addCriteria(searchCriteria);

            return operations.find(searchQuery, User.class);
        }
    }
}
