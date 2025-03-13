package com.vmogroup.java_challange.bean.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ApplicationRequest {
    @JsonProperty("text")
    @Size(min = 1, message = "Text must be at least 1 character")
    private String name;
    @JsonProperty("description")
    @Size(min = 1, max = 150, message = "Description must be between 1 and 255")
    private String description;
    @JsonProperty("enabled")
    private Boolean enabled;
    @JsonProperty("type")
    private String type;
}
