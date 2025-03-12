package com.vmogroup.java_challange.changelog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.ValidationOptions;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.io.InputStream;
import java.util.List;
import java.util.stream.StreamSupport;

@SuppressWarnings("unused")
@ChangeUnit(order = "001", id ="application-collections", author = "TungDN")
@Slf4j
public class ApplicationsChangeLog {

    private static final String COLLECTION_NAME = "applications";
    private static final String DB_JSON_PATH = "db/db.json";


    @BeforeExecution
    public void beforeExecution(MongoDatabase db) {
        boolean collectionExists = StreamSupport
                .stream(db.listCollectionNames().spliterator(), false)
                .anyMatch(name -> name.equals(COLLECTION_NAME));

        if (!collectionExists) {
            log.info("Creating collection {}", COLLECTION_NAME);

            Document validator = new Document("$jsonSchema", new Document()
                    .append("bsonType", "object")
                    .append("required", List.of("name", "description", "enabled", "type"))
                    .append("properties", new Document()
                            .append("name", new Document("bsonType", "string").append("minLength", 1))
                            .append("description", new Document("bsonType", "string").append("minLength", 1).append("maxLength", 150))
                            .append("enabled", new Document("bsonType", "bool"))
                            .append("type", new Document("bsonType", "string"))
                    )
            );

            ValidationOptions validationOptions = new ValidationOptions().validator(validator);
            db.createCollection(COLLECTION_NAME, new com.mongodb.client.model.CreateCollectionOptions().validationOptions(validationOptions));

            log.info("Collection {} created with schema validation.", COLLECTION_NAME);
        } else {
            log.info("Collection {} already exists.", COLLECTION_NAME);
        }
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution() {
        log.error("Error when execution before migration script. Check log for more details");
    }


    @Execution
    public void execution(MongoDatabase db) throws Exception {

        MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME);
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(DB_JSON_PATH);

        if (inputStream == null) {
            throw new RuntimeException("Failed to load db.json file");
        }

        List<Document> documents = objectMapper.readValue(inputStream, new TypeReference<>() {});
        collection.insertMany(documents);

    }

    @RollbackExecution
    public void rollbackExecution(){
        log.error("Error when execution migration script. Check log for more details");
    }


}
