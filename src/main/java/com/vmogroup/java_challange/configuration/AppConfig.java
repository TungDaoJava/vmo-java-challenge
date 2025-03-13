package com.vmogroup.java_challange.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class AppConfig {
    @Value("${mongock.mongo-uri}")
    private String mongockDb;

    @Value("${mongock.migration-scan-package}")
    private String mongockScanPackage;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;
}
