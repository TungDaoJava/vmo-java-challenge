## 0. Technology choice


-   Due to the applicant's familiarity with the Spring Boot ecosystem, Spring Boot will be chosen as the Java framework for developing the service
-   To accomplice with the challenge's requirement, MongoDB is chosen as the stored database.
-   Docker Compose is chosen as the deployment tool, so the service can be started on any machine with Docker without further installation/

## 1. Setup Mongo database

-  For set up the Mongo database to match the requirement, the best option is relying on Mongock. The tool, as in self-description, is a Java based migration tool that focused in managing changes of NoSQL databases. This is important, as the consistence of database version is critical for minimizing potential issues of switching environment (from dev to prod, for example)

```xml
		<dependency>
			<groupId>io.mongock</groupId>
			<artifactId>mongock-springboot-v3</artifactId>
			<version>5.5.0</version>
		</dependency>
		<dependency>
			<groupId>io.mongock</groupId>
			<artifactId>mongodb-springdata-v4-driver</artifactId>
			<version>5.5.0</version>
		</dependency>
```

-  Furthermore, we want to minimize the potential unwanted change in database. Therefore, only Mongock will have the priviledge of creating collections. The application will use a MongoDB user will only read and write permission.

```js

adminDb.createUser({
    user: "mongock",
    pwd: "password",
    roles: [
        { role: "dbOwner", db: "applications" },
        { role: "readWrite", db: "applications" }
    ]
});

adminDb.createUser({
    user: "app",
    pwd: "apppassword",
    roles: [
        { role: "readWrite", db: "applications" }
    ]
});

```

- Normally, this will be done by a DevOps team. To reduce the complexity, this action will be mock as load immediately into MongoDB start script
```yaml
    volumes:
      - mongodb_data:/data/db
      - ./mongo/mongo-role.js:/docker-entrypoint-initdb.d/mongo-init.js:ro
```

- The final piece in the setup is how can we use Mongock into our system:

```yaml
mongock:
  migration-scan-package: com.vmogroup.java_challange.changelog
  mongo-uri: ${MONGOCK_URI:mongodb://mongock:password@localhost:27017}
```

All the changelog will be stored in the changelog folder, such as 
```java
@ChangeUnit(order = "001", id ="application-collections", author = "TungDN")
@Slf4j
public class ApplicationsIndex {
// Implementation in here
}
```

- The initial database, follow as in the requirement, is stored on `resources/db/db.json` file within the Spring Boot Project

## 2. REST Controller

- We will setup a simple Create - Read - Update operations with the documents. (note that due to the time limitation, Delete is not implemented)

```java
@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @GetMapping
    // Read method for multiple record
    
    @GetMapping("/{id}")
    // Read method for single record
    
    @PostMapping
    // Create method

    @PatchMapping("/{id}")
    // Update method
}
```

- For error handling, we will rely on the `@RestControllerAdvice` that already provided by Spring Boot framework
```java
@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(produces = "application/json", exception = {ResponseStatusException.class})
    // Handle Error
    
    @ExceptionHandler(produces = "application/json", exception = {RuntimeException.class})
    // Handle Error
}

```

- Examiner can check the perform of the API with the [Postman Collection](VMO-Challange.postman_collection.json) attached in the repository. 

## 3. Potential directions

- Obviously, this project is not really production-ready grade. There are still more works can be done before deploying on production environment

- Monitoring: ELK stack - Grafana is the standard tool for monitoring microservices. However, Uptrace, a new open-source observability tool, shows many promises, with simple integration, low effort to setup the monitoring tool

- Handle peak load: This system alone wont be able to handle a traffic spike with hugh amount of Concurrent Users. This can be solved with a right combination of memory cache, load balancing, and auto scaling  