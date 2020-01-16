# redsky-case-study
This project is built using spring boot and the Groovy language with a Redis backend for some storage.

To run the project use the command 

```bash
./docker-deploy.sh
```
This will run the application. It will build the application using gradle load it into a docker container and start it up and Redis

You can then hit the health endpoint at
```bash
curl localhost:8080/actuator/health
``` 

or the product endpoint at
```bash
curl localhost:8080/products/13860428 
```

When the application is running you can run the functional tests

```bash
./gradlew functionaltest
```

Other tests can be run with the following commands

Integration tests
```bash
./gradlew integrationtest
```

Unit tests

```bash
./gradlew test
```

Test results are stored at build/reports/test/<test type>



Future enhancements 
- [ ] add swagger doc
- [ ] full exception handling
- [ ] additional instrumentation
- [ ] logging
