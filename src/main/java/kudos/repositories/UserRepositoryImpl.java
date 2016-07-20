package kudos.repositories;

import com.google.common.base.Strings;
import kudos.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements CustomUserRepository {

    private static final int MAX_LIST_SIZE = 100;
    private final MongoOperations operations;

    @Autowired
    public UserRepositoryImpl(MongoOperations operations) {
        this.operations = operations;
    }

    @Override
    public List<User> searchAllFields(String filter) {
        if (Strings.isNullOrEmpty(filter)) {
            return operations.findAll(User.class);
        } else {
            Query searchQuery = new Query();
            Criteria searchCriteria = new Criteria();

            List<Criteria> orCriterias = new ArrayList<>();
            orCriterias.add(Criteria.where("email").regex(filter));
            orCriterias.add(Criteria.where("firstName").regex(filter));
            orCriterias.add(Criteria.where("lastName").regex(filter));
            orCriterias.add(Criteria.where("birthday").regex(filter));
            orCriterias.add(Criteria.where("phone").regex(filter));
            orCriterias.add(Criteria.where("startedToWorkDate").regex(filter));
            orCriterias.add(Criteria.where("position").regex(filter));
            orCriterias.add(Criteria.where("department").regex(filter));
            orCriterias.add(Criteria.where("location").regex(filter));
            orCriterias.add(Criteria.where("team").regex(filter));

            searchCriteria.orOperator(orCriterias.toArray(new Criteria[orCriterias.size()]));

            searchQuery.addCriteria(searchCriteria);

            return operations.find(searchQuery, User.class);
        }
    }
}
