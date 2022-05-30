package com.mario.softserve.interview.client;

import com.mario.softserve.interview.model.Repo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Fetches repository details. Any exceptions from github repo are catched and empty {@link Mono} is returned.
 */
@Slf4j
@Component
public class GitHubApiClient {

    private static final String ACCEPT_HEADER = "application/vnd.github.v3+json";
    private static final String GITHUB_URI = "https://api.github.com/repos/{owner}/{repositoryName}";

    public Mono<Repo> getRepository(String owner, String repositoryName) {
        return WebClient.create()
                .get()
                .uri(GITHUB_URI, owner, repositoryName)
                .header(HttpHeaders.ACCEPT, ACCEPT_HEADER)
                .retrieve()
                .bodyToMono(String.class)
                .map(json -> RepoParser.parse(json))
                .doOnError(exception -> log.error("Exception occurred during fetching data from github: {}", exception.getMessage()))
                .onErrorResume(x -> Mono.empty());
    }
}