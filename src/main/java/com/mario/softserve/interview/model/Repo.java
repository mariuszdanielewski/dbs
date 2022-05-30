package com.mario.softserve.interview.model;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Model representing repo in database.
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(value = "repositories")
@CompoundIndex(def = "{'owner': 1, 'name': 1}")
public class Repo {

    private String owner;
    private String name;
    private String fullName;
    private String description;
    private String cloneUrl;
    private long stars;
    private String createdAt;

}
