# 🏦 Fund Management System

A secure and extensible Spring Boot application to manage mutual fund investments, enabling user registration, order placement (buy/redeem), fund NAV tracking, and role-based access control.

---

## 🚀 Features

- 🛡️ **Spring Security** with role-based access (`USER`, `ADMIN`)
- 🧩 **MapStruct** for clean DTO ↔ Entity mapping
- 🔐 **BCrypt password encryption**
- 🧾 **OpenAPI 3** compliant APIs with integrated Swagger UI
- 🧠 Uses **Lombok** for boilerplate code reduction
- 🗃️ **Spring Data JPA** for database interaction
- 📅 Order processing based on latest NAV for the fund
- 🚨 Error, Exception cases are handled with GlobalExceptionHandler using @RestControllerAdvice and @ExceptionHandler
- 🧪 JUnit 5 and Mockito-based integration and service tests

---

## 🛠️ Tech Stack

| Layer            | Technology                |
|------------------|---------------------------|
| Backend          | Spring Boot               |
| ORM              | Spring Data JPA + Hibernate |
| Database         | PostgreSQL (or any JPA-compatible DB) |
| Security         | Spring Security (HTTP Basic Auth) |
| Mapping          | MapStruct (DTO <-> Entity) |
| Logging          | Log4j2 + Custom Logger     |
| Validation       | Jakarta Bean Validation    |
| Dependency Mgmt  | Maven                      |
| Documentation    | OpenAPI 3.0                |
| Annotations      | Lombok (to reduce boilerplate) |

---

## 🔗 API Endpoints

| Method | Endpoint                 | Access                       | Description              |
|--------|--------------------------|------------------------------|--------------------------|
| POST   | `/v1/api/funds`          | Admin only                   | Add a new fund           |
| PUT    | `/v1/api/funds?{fundId}` | Get user details by username | Updated fund NAV         |
| POST   | `/v1/api/funds/order`    | Authenticated                | Create Order BUY/ REDEEM |

---

## 📁 Project Structure

```
com.subash.fund.management
├── controller
│   └── FundController, OrderController
├── service
│   └── FundServiceImpl, OrderServiceImpl
├── repository
│   └── UserRepository, FundRepository, ...
├── model
│   └── DTOs and Entity Models
├── mapper
│   └── FundMapper
├── security
│   └── SecurityConfig, CustomUserDetailsService
└── util
    └── Constants, GenericLogger
```
## 🔐 Security Configuration

Spring Security is configured with:

- **HTTP Basic Authentication**
- **Role-based access control**:
    - `/v1/api/funds` (GET, POST, PUT, DELETE) → `ROLE_ADMIN` only
    - `/v1/api/funds/order` (POST) → `ROLE_USER`
- **Public endpoints**:
    - Swagger UI → `/swagger-ui/**`, `/v3/api-docs/**`
- **CSRF disabled** for REST API statelessness

Example security snippet:

```
http.csrf(csrf -> csrf.disable())
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
        .requestMatchers(HttpMethod.POST, "/v1/api/funds").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST, "/v1/api/funds/order").hasRole("USER")
        .anyRequest().authenticated()
    )
    .httpBasic();

```

## 🚨 Exception Handling

Handled using `@RestControllerAdvice` and `@ExceptionHandler` which helps to returns appropriate HttpStatus and JSON error messages.

| Exception Type                   | Response                            |
|----------------------------------|-------------------------------------|
| MethodArgumentNotValidException  | 400 Bad Request with field errors   |   
| HttpMessageNotReadableException  | 400 Bad Request for malformed JSON  |  
| Exception                        | 500 Internal Server Error           |    

## 🚀 Run Locally
### ✅ Prerequisites
- Java 21+
- Maven 3.8+

### 🏃‍Start the App

````
mvn clean install
mvn spring-boot:run
````

## 🔎 API Docs (Swagger UI)
Visit: http://localhost:8080/swagger-ui.html or /swagger-ui/index.html

## 📌 Future Enhancements
- Switch to JWT-based authentication
- Dockerize the application
- Add integration test coverage
- API rate limiting for production use