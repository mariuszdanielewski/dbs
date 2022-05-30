package com.mario.softserve.interview.dao;

import category.TestCategory;
import com.mario.softserve.interview.MongoDbContainer;
import com.mario.softserve.interview.model.Repo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = RepoDaoIntegrationTest.MongoDbInitializer.class)
@Tag(TestCategory.INTEGRATION_TEST)
public class RepoDaoIntegrationTest {

    private static MongoDbContainer mongoDbContainer;

    public static class MongoDbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

            TestPropertyValues values = TestPropertyValues.of(
                    "spring.data.mongodb.host=" + mongoDbContainer.getContainerIpAddress(),
                    "spring.data.mongodb.port=" + mongoDbContainer.getPort()

            );
            values.applyTo(configurableApplicationContext);
        }
    }


    @BeforeAll
    public static void startContainer() {
        mongoDbContainer = new MongoDbContainer();
        mongoDbContainer.start();
    }

    @Autowired
    private RepoDao repoDao;

    @Test
    public void shouldProperlyStoreAndFetchData() {
        //given
        Repo repo = new Repo();
        repo.setOwner("repo_owner");
        repo.setName("repo_name");
        repo.setStars(100);
        repo.setDescription("repo_description");
        repo.setCreatedAt("2010-02-26T16:15:28Z");
        repo.setFullName("repo_full_name");
        repo.setCloneUrl("https://github.com/repo_owner/repo_name");

        //when
        Publisher<Repo> removeAllAndInsert = repoDao.deleteAll().then(repoDao.insert(repo));
        Mono<Repo> findByOwnerAndName = repoDao.findByOwnerAndName("repo_owner", "repo_name");
        Publisher<Repo> composite = Mono
                .from(removeAllAndInsert)
                .then(findByOwnerAndName);

        //then
        StepVerifier
                .create(composite)
                .consumeNextWith(r -> {
                    assertEquals(r.getCloneUrl(), "https://github.com/repo_owner/repo_name");
                    assertEquals(r.getCreatedAt(), "2010-02-26T16:15:28Z");
                    assertEquals(r.getDescription(), "repo_description");
                    assertEquals(r.getName(), "repo_name");
                    assertEquals(r.getFullName(), "repo_full_name");
                    assertEquals(r.getStars(), 100);
                    assertEquals(r.getOwner(), "repo_owner");
                })
                .verifyComplete();
    }

    @Test
    public void shouldFindRepoByOwnerAndName() {
        //given
        Repo repo1 = new Repo();
        repo1.setOwner("mario");
        repo1.setName("dbs1");
        repo1.setStars(1);
        repo1.setDescription("interview1");
        repo1.setCreatedAt("2017-02-26T16:15:28Z");
        repo1.setFullName("repo with interview task1");
        repo1.setCloneUrl("https://github.com/mario/interview1");

        Repo repo2 = new Repo();
        repo1.setOwner("mario");
        repo1.setName("dbs2");
        repo1.setStars(2);
        repo1.setDescription("interview2");
        repo1.setCreatedAt("2017-02-26T16:15:28Z");
        repo1.setFullName("repo with interview task2");
        repo1.setCloneUrl("https://github.com/mario/interview2");

        Repo repo3 = new Repo();
        repo1.setOwner("mario");
        repo1.setName("dbs3");
        repo1.setStars(3);
        repo1.setDescription("interview3");
        repo1.setCreatedAt("2017-02-26T16:15:28Z");
        repo1.setFullName("repo with interview task3");
        repo1.setCloneUrl("https://github.com/mario/interview3");

        //when
        Publisher<Repo> setup = repoDao.deleteAll().
                then(repoDao.insert(repo1)).
                then(repoDao.insert(repo2)).
                then(repoDao.insert(repo3));
        Mono<Repo> find = repoDao.findByOwnerAndName("mario", "dbs3");
        Publisher<Repo> composite = Mono
                .from(setup)
                .then(find);

        //then
        StepVerifier
                .create(composite)
                .consumeNextWith(r -> {
                    assertEquals(r.getCloneUrl(), "https://github.com/mario/interview3");
                    assertEquals(r.getCreatedAt(), "2017-02-26T16:15:28Z");
                    assertEquals(r.getDescription(), "interview3");
                    assertEquals(r.getName(), "dbs3");
                    assertEquals(r.getFullName(), "repo with interview task3");
                    assertEquals(r.getStars(), 3);
                    assertEquals(r.getOwner(), "mario");
                })
                .verifyComplete();
    }
}
