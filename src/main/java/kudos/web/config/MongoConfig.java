package kudos.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
class MongoConfig {

    @Bean
    public MongoFactoryBean mongo() {
        MongoFactoryBean mongo = new MongoFactoryBean();
        mongo.setHost("localhost");
        mongo.setPort(27017);
        return mongo;
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception{
        return new MongoTemplate(mongo().getObject(),"local");
    }
}