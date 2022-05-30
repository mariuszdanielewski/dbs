package com.mario.softserve.interview.service;

import com.mario.softserve.interview.client.GitHubApiClient;
import com.mario.softserve.interview.controller.RepoDTO;
import com.mario.softserve.interview.dao.RepoDao;
import com.mario.softserve.interview.model.Repo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
@Slf4j
public class RepoService {

    private final RepoDao repoDao;
    private final GitHubApiClient gitHubApiClient;

    @Autowired
    public RepoService(RepoDao repoDao, GitHubApiClient gitHubApiClient) {
        this.repoDao = repoDao;
        this.gitHubApiClient = gitHubApiClient;
    }

    /**
     * Get repo from db. If not found try to fetch from github.
     */
    public Mono<RepoDTO> getRepository(String owner, String name) {
        Mono<Repo> repoFromDb = repoDao.findByOwnerAndName(owner, name).doOnSuccess(x -> log.info("fetching from db"));
        Mono<Repo> repoFromGithub = gitHubApiClient.getRepository(owner, name).doOnSuccess(x -> log.info("fetching from github"));

        return repoFromDb.map(getRepoRepoDTOFunction())
                .switchIfEmpty(repoFromGithub.flatMap(x -> repoDao.save(x).doOnSuccess(xx -> log.info("storing into db"))).map(getRepoRepoDTOFunction())
                        .switchIfEmpty(Mono.empty()));
    }

    private Function<Repo, RepoDTO> getRepoRepoDTOFunction() {
        return x -> RepoDTO.builder()
                .fullName(x.getFullName())
                .stars(x.getStars())
                .cloneUrl(x.getCloneUrl())
                .description(x.getDescription())
                .createdAt(x.getCreatedAt())
                .build();
    }
}
