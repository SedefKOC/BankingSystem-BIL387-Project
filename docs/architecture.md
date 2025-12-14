# Architecture Overview

## Layering
- **controller (Servlet):** Thin HTTP adapters living under `com.bankingsystem.controller`. They receive requests, delegate to services, and resolve JSP views inside `WEB-INF/views`.
- **service:** `com.bankingsystem.service` exposes interfaces describing business operations. Implementations under `service.impl` enforce validation, transactional boundaries, and orchestrate DAO calls.
- **dao:** `com.bankingsystem.dao` interfaces provide persistence contracts using plain JDBC. Implementations under `dao.impl` handle SQL + result mapping, depending only on `entity` and `util` helpers.
- **entity:** Plain Java classes mirroring database tables (no ORM). Serializable, with validation helpers if needed.
- **util:** Cross-cutting utilities such as database connection factory, password hashing, common constants.

## Key Principles
1. **Interface-driven design:** Controllers depend on service interfaces; services depend on DAO interfaces, enabling test doubles.
2. **Single Responsibility:** Each class focuses on one reason to change (e.g., `AccountService` handles account-specific logic only).
3. **Dependency Inversion:** High-level modules (controllers/services) never import JDBC specifics. Instead, `util` provides `ConnectionManager` fed into DAO implementations.
4. **Resource Management:** DAO implementations must close JDBC resources via try-with-resources and rely on pooled `DataSource` once added.
5. **Configuration Isolation:** Credentials/config in `util.Config` (backed by environment variables or `.properties` file stored outside VCS) so codebase stays clean.

## Build & Deployment Flow
1. Compile `src/main/java` with classpath containing Tomcat's `jakarta.servlet-api` and PostgreSQL driver.
2. Copy compiled classes into `src/main/webapp/WEB-INF/classes` or package into a WAR using a simple script.
3. Deploy to Tomcat 10.x. JSP files stay under `src/main/webapp`; assets served statically.
4. Database migrations (SQL files under `src/main/resources/db`) executed manually or via future utility before first run.

## Next Components
- `DBConnectionUtil` (util) returning JDBC connections from configured properties.
- `BaseServlet` abstract class to centralize common response helpers (JSON, redirects).
- Global error JSP + logging utility.
