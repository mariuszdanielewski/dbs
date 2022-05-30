package com.mario.softserve.interview.controller;

import com.mario.softserve.interview.service.RepoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(RepoController.class)
public class RepoControllerTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private RepoService repoService;

    @Test
    public void shouldReturnRepo() {
        //given
        String repo_owner = "repo_owner";
        String repo_name = "repo_name";

        RepoDTO repoDTO = RepoDTO.builder()
                .createdAt("2010-02-26T16:15:28Z")
                .description("descripton_owner")
                .cloneUrl("clone_url")
                .stars(10)
                .fullName("full_name")
                .build();

        //when
        when(repoService.getRepository(repo_owner, repo_name)).thenReturn(Mono.just(repoDTO));

        //then
        webClient
                .get().uri("/repositories/{owner}/{name}", repo_owner, repo_name)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(RepoDTO.class);
    }

    @Test
    public void shouldReturnNotFoundResponse() {
        //given
        String repo_owner = "repo_owner";
        String repo_name = "repo_name";

        //when
        when(repoService.getRepository(repo_owner, repo_name)).thenReturn(Mono.empty());

        //then
        webClient
                .get().uri("/repositories/{owner}/{name}", repo_owner, repo_name)
                .exchange()
                .expectStatus()
                .isNotFound();
    }


}
