# Clustered Data Warehouse

A Spring Boot application providing a clustered data warehouse example using **Spring Data JPA**, **PostgreSQL**, **Liquibase**, **MapStruct**, and **Test Unit**.

---

## Quick Facts

* **GroupId:** `org.clustered-data-warehouse`
* **ArtifactId:** `clustered-data-warehouse`
* **Java:** 21 (required for Spring Boot 3.5.x)
* **Database:** PostgreSQL (`jdbc:postgresql://localhost:5432/fx_deals`)
* **Default Credentials:** `postgres / postgres`
* **Liquibase Changelog:** `classpath:db/changelog/schema.sql`

---

## Prerequisites

* Java JDK 21 (set `JAVA_HOME`)
* Docker & Docker Compose (for Postgres and tests)
* Git
* PowerShell (for Windows commands; Maven wrapper included)

---

## Build & Run

### Build

```powershell
# Quick build (skip tests)
./mvnw.cmd -DskipTests package

# Run all tests (requires Docker)
./mvnw.cmd test
```

### Run Locally

```powershell
# Option A: Using Maven plugin
./mvnw.cmd spring-boot:run

# Option B: Using JAR file
./mvnw.cmd -DskipTests package
java -jar target\clustered-data-warehouse-0.0.1-SNAPSHOT.jar
```

### Run with Docker

```powershell
# Start all services (Postgres + pgAdmin + App)
docker-compose up -d

# Or build and run app container only
docker build -t clustered-data-warehouse:local .
docker run --rm -p 8080:8080 --env SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/fx_deals clustered-data-warehouse:local
```

---

## Database & Migrations

* **Migrations:** Liquibase enabled (`schema.sql`)
* **Local Data:** `data/postgres/` contains Docker volume data
* **Configuration:** Edit `src/main/resources/application.properties` or use environment variables

---

## Tests

* Uses **Testcontainers** for integration tests
* Requires Docker to be running

```powershell
./mvnw.cmd test
```

---

## Project Structure

```
clustered-data-warehouse/
├── pom.xml                          # Maven dependencies
├── Dockerfile                       # App container image
├── docker-compose.yml               # Multi-service orchestration
├── mvnw.cmd                         # Maven wrapper
├── src/
│   ├── main/
│   │   ├── java/org/clustered-data-warehouse/clustereddatawarehouse/
│   │   │   ├── ClusteredDataWarehouseApplication.java
│   │   │   ├── controller/          # REST endpoints
│   │   │   ├── service/             # Business logic
│   │   │   ├── repository/          # JPA data access
│   │   │   ├── entity/              # JPA entities
│   │   │   ├── mapper/              # MapStruct DTO mappers
│   │   │   └── exception/           # Error handling
│   │   └── resources/
│   │       ├── application.properties
│   │       └── db/changelog/schema.sql
│   └── test/                        # Unit & integration tests
└── data/
    ├── postgres/                    # PostgreSQL Docker volume
    └── pgadmin/                     # pgAdmin Docker volume
```

---

## Key Files

| File/Directory            | Purpose                                  |
| ------------------------- | ---------------------------------------- |
| `pom.xml`                 | Maven project descriptor & dependencies  |
| `application.properties`  | Spring Boot config (DB, JPA, Liquibase)  |
| `db/changelog/schema.sql` | Liquibase migrations                     |
| `Dockerfile`              | Container image build instructions       |
| `docker-compose.yml`      | Postgres, pgAdmin, and app services      |
| `data/postgres/`          | Persistent DB data for local development |

---

## Quick Navigation

* **New API endpoint:** Add to `src/main/java/.../controller/`
* **Database query:** Create interface in `src/main/java/.../repository/`
* **DB migration:** Add changeset to `src/main/resources/db/changelog/schema.sql`
* **Update dependencies:** Edit `pom.xml`, then run `./mvnw.cmd install`

---

## Contributing

1. Fork the repository
2. Create feature branches
3. Run tests before committing:

```powershell
./mvnw.cmd -DskipTests package
./mvnw.cmd test
```

