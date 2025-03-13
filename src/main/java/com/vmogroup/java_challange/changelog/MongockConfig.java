package com.vmogroup.java_challange.changelog;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.vmogroup.java_challange.configuration.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MongockConfig {
    private final AppConfig appConfig;

    @Bean("mongock")
    public MongoDatabase mongockDatabase() {
        return MongoClients.create(appConfig.getMongockDb()).getDatabase(appConfig.getDatabaseName());
    }

}
