# Toy Sample Tracking System

## Overview
- This project follows zero-downtime deployment using Blue-Green Deployment
- Nginx for traffic switching.
- Flyway for database migrations. The goal is to safely apply schema changes without breaking existing versions.
- This project supports local development with H2 (in-memory DB) and Docker-based production with PostgreSQL.

## Running Locally (H2 Database)
Ensure Required Dependencies

- Install Java 21 and Maven.
- No need to install PostgreSQL for local development.
- Run the Application Locally (Uses H2 Database)
```
mvn clean package -DskipTests
mvn spring-boot:run -Dspring.profiles.active=local
```
The app runs on http://localhost:8082.

---
## Access the Application

API Endpoint: 
- http://localhost:8082/api/orders/place
```
Request :

curl --location 'http://localhost:8082/api/orders/place' \
--header 'Content-Type: application/json' \
--data '{
    "order": [
        {
            "sample_uuid": "a1b2c3d4-e5f6-7890-ab12-cdef34567890",
            "sequence": "GATTACA"
        },
        {
            "sample_uuid": "b2c3d4e5-f678-9012-ab34-cdef56789012",
            "sequence": "CGGATCGA"
        }
    ]
}

Response :
{
    "order_uuid": "e2f8745a-c2c2-4303-97d9-a06fe40c6c25"
}
```

- http://localhost:8082/api/samples/to-process
```
Request :

curl --location 'http://localhost:8082/api/orders/place'

Response :

{
    "samples_to_make": [
        {
            "sampleUuid": "a1b2c3d4-e5f6-7890-ab12-cdef34567890",
            "sequence": "GATTACA"
        },
        {
            "sampleUuid": "b2c3d4e5-f678-9012-ab34-cdef56789012",
            "sequence": "CGGATCGA"
        }
    ]
}
```

- http://localhost:8082/api/qc/log
```
Request :

curl --location 'http://localhost/api/qc/log' \
--header 'Content-Type: application/json' \
--data '{
    "samples_made": [
        {
            "sample_uuid": "a1b2c3d4-e5f6-7890-ab12-cdef34567890",
            "plate_id": 101,
            "well": "A01",
            "qc_1": 18.7,
            "qc_2": 2.9,
            "qc_3": "PASS"
        },
        {
            "sample_uuid": "b2c3d4e5-f678-9012-ab34-cdef56789012",
            "plate_id": 101,
            "well": "B02",
            "qc_1": 12.3,
            "qc_2": 4.5,
            "qc_3": "FAIL"
        }
    ]
}'

Response :

{
    "message": "QC results logged successfully"
}
```

- http://localhost:8082/api/orders/{order_id}/samples
  - Bonus, but still needs work . Its happy path GET for now
```
Request :

curl --location 'http://localhost:8082/api/orders/place'

Response :

[
    {
        "sampleUuid": "a1b2c3d4-e5f6-7890-ab12-cdef34567890",
        "sequence": "GATTACA"
    },
    {
        "sampleUuid": "b2c3d4e5-f678-9012-ab34-cdef56789012",
        "sequence": "CGGATCGA"
    }
]
```

--- 

H2 Console: 
http://localhost:8082/h2-console
```
JDBC URL: jdbc:h2:mem:toysampletracker
Username: sa
Password: (Leave blank)
```

---
## Running in Docker (PostgreSQL Database)
Running in Docker (PostgreSQL Database)
- Ensure Docker is Installed
    ```
  docker --version
  ```

- Build and Start Services
    ```
    docker compose up -d
    ```
    - Starts:
        - PostgreSQL (Database)
        - Spring Boot App (toysampletracker)
        - Nginx (Traffic Router for Blue-Green Deployment)
        - Verify Running Containers

       ```
       docker ps
       ```
```
CONTAINER ID   IMAGE                    PORTS                   STATUS      NAMES
123abc         toysampletracker:latest  8080->8080/tcp          Up          toysampletracker_blue
456def         postgres:15              5432->5432/tcp          Up          postgres-db
789ghi         nginx:latest             80->80/tcp              Up          nginx-proxy
```

 ###  Access the endpoints in same fashion as mentioned above but without the port

---
## Key Benefits
- Local development is fast & isolated (H2, no PostgreSQL setup required).
- Production deployment uses PostgreSQL in Docker.
- Blue-Green Deployment ensures zero downtime.
- Nginx handles smooth traffic switching.
- Rollback strategy ensures stability.


---

## Future Enhancements

Future Enhancements
- Better Logging & Monitoring
- Implement structured logging using SLF4J + Logback.
- Integrate Datadog or Prometheus for real-time monitoring.
- Add trace IDs in logs for better debugging.

Improved Unit & Integration Testing
- Use JUnit 5 & Mockito for unit tests.
- Automate API contract testing using Spring REST Docs.

Token-Based Authentication
- Implement JWT-based authentication for secure API access.
- Use OAuth2 or OpenID Connect for client authentication.
- Restrict API access based on roles & permissions.

API Gateway & Rate Limiting

- Introduce Kong Gateway for API management.
- Enable rate limiting to prevent API abuse.
- Implement circuit breakers for fault tolerance.

Improved CI/CD Pipeline
- Automate deployments using GitHub Actions or Jenkins.
- Run integration tests before deployment.
- Add rollback automation for failed deployments.