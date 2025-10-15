# Java Spring Boot + JPA Copilot Instructions

## General Java Development Practices
- Always follow SOLID, DRY, KISS, and YAGNI principles.
- Adhere to OWASP security best practices.
- Break tasks into the smallest units and solve step by step.
- Use clean code practices (meaningful names, small methods, single responsibility).
- Write unit tests for all classes and methods (use JUnit 5 and Mockito).
- Use logging (SLF4J with Logback) instead of System.out.println.
- Use Java 17 features (records, sealed classes, pattern matching, etc.) where appropriate.
- Use Lombok to reduce boilerplate code (getters, setters, constructors, etc.).
- Use Java Streams and Optionals where appropriate.
- Use Java's built-in validation annotations (javax.validation.constraints).
- Follow RESTful API design principles (resource-based URLs, proper HTTP methods, status codes).
- Use DTOs for data transfer between layers (avoid exposing entity classes directly).
- Use exception handling best practices (custom exceptions, global exception handler).
- Use pagination for endpoints returning large datasets (Spring Data JPA's Pageable).
- Write clear and concise comments and documentation (JavaDoc for public methods).
- Regularly refactor code to improve readability and maintainability.
- Stay updated with the latest Java and Spring Boot developments and best practices.
- Ensure your code is production-ready (performance, security, scalability).
- Use environment variables or configuration files for sensitive information (e.g., database credentials).
- Continuously learn and improve your coding skills.
- Use clean architecture principles to separate concerns.
```bash
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           ├── adapters
│   │   │           │   ├── controllers
│   │   │           │   │── database
│   │   │           │   │   └── postgresql     
│   │   │           │   │       ├── entities    
│   │   │           │   │       ├── repositories 
│   │   │           │   │       └── config      
│   │   │           │   ├── services
│   │   │           │   │   └── ...
│   │   │           │   ├── frameworks
│   │   │           │   │   └── ...
│   │   │           │   └── ...
│   │   │           ├── application
│   │   │           │   │── services              
│   │   │           │   │   └── ...
│   │   │           │   │── usecases                
│   │   │           │   │   └── ...
│   │   │           │   └── ...
│   │   │           ├── domain
│   │   │           │   ├── factories           
│   │   │           │   ├── models              
│   │   │           │   └── repositories        
│   │   │           └── config
│   │   └── resources
│   │       ├── application.properties
│   │       │── db
│   │       │   └── migration                     
│   │       └── ...
└── pom.xml
```

## Spring Boot Project Structure
- Use Java Spring Boot 3 (Maven, Java 17, Spring Web, Spring Data JPA, Thymeleaf, Lombok, PostgreSQL driver).
- RestControllers handle all request/response logic.
- ServiceImpl classes handle all database operation logic using Repository methods.
- RestControllers must not autowire Repositories directly unless absolutely necessary.
- ServiceImpl classes must not query the database directly (use Repositories).
- Use DTOs for data transfer between RestControllers and ServiceImpl classes.
- Entity classes are only for carrying data from database queries.

## Entity Class Conventions
- Annotate with @Entity and @Data (Lombok).
- Use @Id and @GeneratedValue(strategy=GenerationType.IDENTITY) for IDs.
- Use FetchType.LAZY for relationships.
- Annotate properties with validation annotations (e.g., @Size, @NotEmpty, @Email).

## DTO Conventions
- Use Java records for DTOs unless otherwise specified.
- Include a compact canonical constructor for parameter validation (not null, blank, etc.).

## Repository Class Conventions
- Annotate with @Repository.
- Use interfaces extending JpaRepository<Entity, ID>.
- Use JPQL for @Query methods.
- Use @EntityGraph(attributePaths={...}) to avoid N+1 problems in relationship queries.
- Use DTOs for multi-join queries with @Query.

## Service Class Conventions
- Service classes are interfaces; implementations are ServiceImpl classes annotated with @Service.
- Autowire dependencies in ServiceImpl without constructors unless specified.
- ServiceImpl methods return DTOs (not entities) unless necessary.
- Use repository methods with .orElseThrow for existence checks.
- Use @Transactional or transactionTemplate for multiple sequential DB operations.

## RestController Conventions
- Annotate with @RestController and specify class-level @RequestMapping.
- Use best-practice HTTP method annotations (e.g., @PostMapping, @GetMapping).
- Autowire dependencies in class methods without constructors unless specified.
- Methods return ResponseEntity<ApiResponse>.
- Implement all logic in try-catch blocks; handle errors with GlobalExceptionHandler.

## ApiResponse & GlobalExceptionHandler
- ApiResponse and GlobalExceptionHandler classes must be present and follow best practices for structure and error handling.