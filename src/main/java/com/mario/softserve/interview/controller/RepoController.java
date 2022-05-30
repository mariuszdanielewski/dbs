package com.mario.softserve.interview.controller;

import com.mario.softserve.interview.service.RepoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("repositories")
public class RepoController {

    private final RepoService repoService;

    public RepoController(RepoService repoService) {
        this.repoService = repoService;
    }

    @GetMapping("/{owner}/{name}")
    public Mono<ResponseEntity<RepoDTO>> getRepository(@PathVariable String owner, @PathVariable String name) {
        Mono<RepoDTO> repository = repoService.getRepository(owner, name);

        return repository.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
