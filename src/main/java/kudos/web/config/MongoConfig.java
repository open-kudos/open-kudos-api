package kudos.web.config;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Configuration
class MongoConfig {

    @Value("${mongo.name}")
    private String name;

    @Value("${mongo.host}")
    private String host;

    @Value("${mongo.port}")
    private String port;

    @Bean
    public MongoFactoryBean mongo() {
        MongoFactoryBean mongo = new MongoFactoryBean();
        mongo.setHost(host);
        mongo.setPort(Integer.valueOf(port));
        return mongo;
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception{
        return new MongoTemplate(mongo().getObject(), name);
    }
}