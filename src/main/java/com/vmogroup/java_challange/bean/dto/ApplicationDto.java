package com.vmogroup.java_challange.bean.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ApplicationDto {
    @JsonProperty("id")
    private String id;
    @JsonProperty("text")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("enabled")
    private boolean enabled;
    @JsonProperty("type")
    private String type;
}
