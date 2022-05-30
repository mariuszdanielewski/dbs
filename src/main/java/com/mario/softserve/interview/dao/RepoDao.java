package com.mario.softserve.interview.dao;

import com.mario.softserve.interview.model.Repo;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface RepoDao extends ReactiveMongoRepository<Repo, String> {

    @Query("{'owner' : ?0, 'name' : ?1}")
    Mono<Repo> findByOwnerAndName(String owner, String name);
}
