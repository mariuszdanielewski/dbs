package com.mario.softserve.interview.controller;

import com.mario.softserve.interview.model.Repo;
import lombok.*;

/**
 * DTO representing {@link Repo} in REST layer.
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RepoDTO {
    private String fullName;
    private String description;
    private String cloneUrl;
    private long stars;
    private String createdAt;

}
