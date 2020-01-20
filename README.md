# kotlin-spring-webflux

More details in [tutorial](./README-TUTORIAL.md)

## Run
```bash
# set up local db
docker-compose -f docker-compose.local.yml up
# run spring application
./gradlew bootRun
# request
curl http://localhost:8080/hello
```

## Development
1. generate idea project config
```bash
./gradlew idea
```

2. enable git hooks
```bash
git config --local core.hooksPath .githooks/ 
chmod 700 .githooks/pre-push
```

3. config your IDEA
- Import code style
    - Open IDEA, enter `Preferences -> Editor -> Code Style -> Import Schema`
    - Choose `config/GoogleStyle.xml`
    > This style is based on GoogleStyle and default Kotlin style of Intellij Team, is suitable for both Java and Kotlin.
- New line end of file
    - Enter `Preferences -> Editor -> General`
    - Check on `Other: Ensure line feed at file end on Save`
- Automatic format before commit when using IDEA commit dialog
    - Enter `Preferences -> Version Control -> Commit Dialog`
    - Check on all options (exclude copyright)

## Test
1. init test & integration test
```bash
# unit test & lint
./gradlew clean check
# api test (must run after test)
./gradlew clean check apiTest
# (optional) test coverage report
./gradlew jacocoTestReport
```

2. container test
```bash
# package
./gradlew clean build bootJar
# start in test container
docker-compose -f docker-compose.test.yml up --build
```

## Deploy (locally)

```bash
# build image
docker build -t kotlin-spring-webflux .
# inject environment variables, check details in config/application-dev.yml
docker run -p 8080:8080 <environment> kotlin-spring-webflux
```

**Should put this part into CI/CD, DON'T manually deploy in production**
**If you want to check it locally, please try container test**
