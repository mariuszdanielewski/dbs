To start application
- to start mongo run from .compose directory command `docker-compose docker-compose.yaml up -- build`
- to start app run `./gradlew bootRun`

Rest endpoint can be found under `GET http://localhost:8080/repositories/owner/repo`

To start tests run `./gradlew test`

MongoDB uri: `mongodb://localhost:27017/repodb`

Missing parts:
- splitting test into unit and integration tests
- dropping repositories from db after some fixed amount of time
- single docker compose to run everything at once