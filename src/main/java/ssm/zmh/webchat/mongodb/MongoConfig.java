//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ssm.zmh.webchat.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    public MongoConfig() {
    }

    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://root:1023@192.168.1.103:27017");
    }

    protected String getDatabaseName() {
        return "webchat";
    }

    @Bean
    MongoTransactionManager mongoTransactionManager(MongoDbFactory mongoDbFactory) {
        return new MongoTransactionManager(mongoDbFactory);
    }

    @Bean
    public MongoOperations mongoOperations() {
        return new MongoTemplate(this.mongoClient(), this.getDatabaseName());
    }
}
