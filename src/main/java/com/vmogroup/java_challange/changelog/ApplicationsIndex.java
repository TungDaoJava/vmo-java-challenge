package com.vmogroup.java_challange.changelog;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ValidationOptions;
import io.mongock.api.annotations.BeforeExecution;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackBeforeExecution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.InputStream;
import java.util.List;
import java.util.stream.StreamSupport;

@SuppressWarnings("unused")
@ChangeUnit(order = "002", id ="application-index", author = "TungDN")
@Slf4j
public class ApplicationsIndex {

    private static final String COLLECTION_NAME = "applications";

    @BeforeExecution
    public void beforeExecution(@Qualifier("mongock") MongoDatabase db) {
        boolean collectionExists = StreamSupport
                .stream(db.listCollectionNames().spliterator(), false)
                .anyMatch(name -> name.equals(COLLECTION_NAME));

        if (!collectionExists) {
            throw new RuntimeException("Collection " + COLLECTION_NAME + " does not exist");
        }
    }

    @RollbackBeforeExecution
    public void rollbackBeforeExecution() {
        log.error("Error when execution before migration script. Check log for more details");
    }


    @Execution
    public void execution(@Qualifier("mongock") MongoDatabase db) {

        MongoCollection<Document> collection = db.getCollection(COLLECTION_NAME);
        collection.createIndex(Indexes.ascending("name"));
        collection.createIndex(Indexes.ascending("description"));
        collection.createIndex(Indexes.ascending("type"));

    }

    @RollbackExecution
    public void rollbackExecution(){
        log.error("Error when execution migration script. Check log for more details");
    }
}
