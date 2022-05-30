To start application
- to start mongo run from .compose directory command `docker-compose docker-compose.yaml up -- build`
- to start app run `./gradlew bootRun`

Rest endpoint can be found under `GET http://localhost:8080/repositories/owner/repo`

To start tests run `./gradlew test`

MongoDB uri: `mongodb://localhost:27017/repodb`