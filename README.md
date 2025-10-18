# Template Spring Boot Project

This is a template project for Spring Boot applications using Maven.

# Getting Started

### Maven commands
```bash
# Install Dependencies
./mvnw clean install -DskipTests 

### Run the Application
./mvnw clean compile spring-boot:run
./mvnw -Dspring-boot.run.profiles=prod spring-boot:run
./mvnw -Dspring-boot.run.arguments="--spring.liquibase.enabled=true" spring-boot:run
```

### Testing API with CURL
```bash
curl -i -H "Accept: application/json" http://localhost:8080/health
curl -i -H "Accept: application/json" http://localhost:8080/v1/cep/89883000 

curl -s -X GET "http://localhost:8080/v1/cep/89883000" -H "Accept: application/json"
```

by: https://start.spring.io/