package com.vmogroup.java_challange.repository;

import com.vmogroup.java_challange.documents.Application;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends MongoRepository<Application, String> {
}
