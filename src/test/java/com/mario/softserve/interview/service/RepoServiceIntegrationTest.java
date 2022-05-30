package com.mario.softserve.interview.service;

import com.mario.softserve.interview.client.GitHubApiClient;
import com.mario.softserve.interview.controller.RepoDTO;
import com.mario.softserve.interview.dao.RepoDao;
import com.mario.softserve.interview.model.Repo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class RepoServiceIntegrationTest {

    @InjectMocks
    private RepoService repoService;

    @Mock
    private RepoDao repoDao;

    @Mock
    private GitHubApiClient gitHubApiClient;

    @Test
    public void shouldGetRepoFromDb() {
        //given
        String owner = "repo_owner";
        String name = "repo_name";

        Repo repo = new Repo();
        repo.setName(name);
        repo.setStars(10);
        repo.setOwner(owner);
        repo.setDescription("repo_description");
        repo.setFullName("repo_full_name");
        repo.setCloneUrl("https://github.com/repo_owner/repo_name");
        repo.setCreatedAt("2010-02-26T16:15:28Z");

        when(repoDao.findByOwnerAndName(owner, name)).thenReturn(Mono.just(repo));
        when(gitHubApiClient.getRepository(owner, name)).thenReturn(Mono.empty());

        //when
        Mono<RepoDTO> repoMono = repoService.getRepository(owner, name);

        //then
        StepVerifier
                .create(repoMono)
                .consumeNextWith(r -> {
                    assertEquals(r.getCloneUrl(), repo.getCloneUrl());
                    assertEquals(r.getDescription(), repo.getDescription());
                    assertEquals(r.getCreatedAt(), repo.getCreatedAt());
                    assertEquals(r.getStars(), repo.getStars());
                    assertEquals(r.getFullName(), repo.getFullName());
                })
                .verifyComplete();
    }

    @Test
    public void shouldGetRepoFromGithub() {
        //given
        String owner = "repo_owner";
        String name = "repo_name";

        Repo repo = new Repo();
        repo.setName(name);
        repo.setStars(10);
        repo.setOwner(owner);
        repo.setDescription("repo_description");
        repo.setFullName("repo_full_name");
        repo.setCloneUrl("https://github.com/repo_owner/repo_name");
        repo.setCreatedAt("2010-02-26T16:15:28Z");

        when(gitHubApiClient.getRepository(owner, name)).thenReturn(Mono.just(repo));
        when(repoDao.findByOwnerAndName(owner, name)).thenReturn(Mono.empty());
        when(repoDao.save(repo)).thenReturn(Mono.just(repo));

        //when
        Mono<RepoDTO> repoMono = repoService.getRepository(owner, name);

        //then
        StepVerifier
                .create(repoMono)
                .consumeNextWith(r -> {
                    assertEquals(r.getCloneUrl(), repo.getCloneUrl());
                    assertEquals(r.getDescription(), repo.getDescription());
                    assertEquals(r.getCreatedAt(), repo.getCreatedAt());
                    assertEquals(r.getStars(), repo.getStars());
                    assertEquals(r.getFullName(), repo.getFullName());
                })
                .verifyComplete();
    }

    @Test
    public void shouldGetEmptyMonoWhenRepoOnGithubNotFound() {
        //given
        String owner = "repo_owner";
        String name = "repo_name";

        when(repoDao.findByOwnerAndName(owner, name)).thenReturn(Mono.empty());
        when(gitHubApiClient.getRepository(owner, name)).thenReturn(Mono.empty());

        //when
        Mono<RepoDTO> repoMono = repoService.getRepository(owner, name);

        //then
        StepVerifier
                .create(repoMono)
                .verifyComplete();
    }
}
