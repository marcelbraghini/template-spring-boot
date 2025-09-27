# Template Spring Boot Project

This is a template project for Spring Boot applications using Maven.

# Getting Started

### Maven commands
```bash
# Install Dependencies
./mvnw clean install -DskipTests 

### Run the Application
./mvnw clean compile spring-boot:run
```

### Testing API with CURL
```bash
curl -i -H "Accept: application/json" http://localhost:8080/health
curl -i -H "Accept: application/json" http://localhost:8080/brasilapi/cep/89883000 
```

by: https://start.spring.io/