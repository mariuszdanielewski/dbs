package com.mario.softserve.interview.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mario.softserve.interview.model.Repo;

/**
 * Parses response from {@link GitHubApiClient} to {@link Repo}.
 */
public class RepoParser {

    private RepoParser() {}

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final String FULL_NAME = "full_name";
    private static final String OWNER = "owner";
    private static final String LOGIN = "login";
    private static final String DESCRIPTION = "description";
    private static final String NAME = "name";
    private static final String CLONE_URL = "clone_url";
    private static final String WATCHERS_COUNT = "watchers_count";
    private static final String CREATED_AT = "created_at";

    public static Repo parse(String json) {
        try {
            JsonNode root = OBJECT_MAPPER.readTree(json);

            Repo repo = new Repo();
            repo.setName(root.get(NAME).asText());
            repo.setOwner(root.get(OWNER).get(LOGIN).asText());
            repo.setStars(root.get(WATCHERS_COUNT).asLong());
            repo.setCreatedAt(root.get(CREATED_AT).asText());
            repo.setDescription(root.get(DESCRIPTION).asText());
            repo.setCloneUrl(root.get(CLONE_URL).asText());
            repo.setFullName(root.get(FULL_NAME).asText());

            return repo;
        } catch (Exception e) {
            throw new RuntimeException("Repo parsing exception: " + e.getMessage());
        }
    }
}
