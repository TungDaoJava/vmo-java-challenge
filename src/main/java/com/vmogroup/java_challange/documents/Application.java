package com.vmogroup.java_challange.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applications")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Application {
    @Id
    private String id;
    private String name;
    private String description;
    private boolean enabled;
    private String type;

}
