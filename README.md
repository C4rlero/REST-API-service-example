# REST-API-service-example
A sample Spring Boot microservice for managing books through REST API endpoints. This project demonstrates a layered architecture including separate command and query controllers, DTO mapping, custom exception handling with RFC 7807 compliant responses, and integration with Spring Data JPA.

## Overview

**ServiceExample** is a sample microservice that demonstrates the development of a RESTful API using Spring Boot. The project includes:
- CRUD operations for "books" using Spring Data JPA.
- Exception handling with a custom RFC 7807 compliant error response.
- DTO and Mapper patterns for data transformation.
- Integration with additional tools such as Lombok for reducing boilerplate code, Spring Cloud OpenFeign for service-to-service calls, and springdoc-openapi for auto-generating API documentation.

## Features

- **CRUD Endpoints:** Create, update, delete, and retrieve books.
- **Validation:** Uses `@Valid` annotations to ensure data integrity.
- **Custom Exceptions:** Demonstrates custom exception handling with RFC 7807 compliant error responses.
- **DTO Mapping:** Clean separation between entity objects and data transfer objects.
- **Documentation:** Auto-generated API documentation using springdoc-openapi.

## Project Configuration

This project is built using:
- **Java 17:** The project requires JDK 17.
- **Maven:** For dependency and build management.
- **Spring Boot 3.2.3:** As the parent project.
- **MySQL & H2:** MySQL is used at runtime (or you can configure your own) and H2 is used for testing.
- **Other Dependencies:**  
  - Lombok  
  - Spring Boot Starter Web, Data JPA, Test, Validation  
  - Spring Cloud OpenFeign  
  - springdoc-openapi  
  - ModelMapper, Mockito, and additional libraries

See the `pom.xml` for full details.

## Getting Started

### Prerequisites

- **Java SDK 17:** Ensure Java 17 is installed.  
  Verify by running:
  ```bash
  java -version



### Bibliographie

- https://medium.com/@pratik.941/building-rest-api-using-spring-boot-a-comprehensive-guide-3e9b6d7a8951
- https://restfulapi.net/http-status-codes/
- https://spring.io/guides/tutorials/rest
- https://medium.com/kth-distributed-systems/testing-microservices-in-spring-boot-applications-tools-and-techniques-b9c27d865f88
- https://www.baeldung.com/java-dto-pattern
- https://medium.com/@aedemirsen/spring-boot-global-exception-handler-842d7143cf2a#:~:text=When%20an%20exception%20occurs%2C%20the,error%20messages%20to%20the%20clients.
- https://www.baeldung.com/global-error-handler-in-a-spring-rest-api
- https://datatracker.ietf.org/doc/html/rfc7807
- https://www.geeksforgeeks.org/returning-errors-using-problemdetail-in-spring-boot/

