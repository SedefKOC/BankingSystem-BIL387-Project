# BankingSystem Plain Java Web App Skeleton

## Java / Servlet Runtime
- **Java Version:** 17 (LTS, Servlet 5+ compatible)
- **Servlet Container:** Apache Tomcat 10.x (Servlet 5 API). Deploy the generated WAR to Tomcat’s `webapps` directory.
- **Build Output:** Maven handles compilation + packaging; run `mvn clean package` to produce `target/BankingSystem.war`.

## Directory Layout
```
BankingSystem/
 ├─ lib/                      # JDBC driver (e.g., postgresql) + servlet API jars for compilation
 ├─ docs/
 │   └─ architecture.md       # Layering decisions, conventions
 ├─ src/
 │   ├─ main/
 │   │   ├─ java/
 │   │   │   └─ com/bankingsystem/
 │   │   │        ├─ controller/
 │   │   │        ├─ service/
 │   │   │        │   └─ impl/
 │   │   │        ├─ dao/
 │   │   │        │   └─ impl/
 │   │   │        ├─ entity/
 │   │   │        └─ util/
 │   │   ├─ resources/
 │   │   │   └─ db/           # SQL migrations / seed data
 │   │   └─ webapp/
 │   │        ├─ assets/      # css/js/img
 │   │        └─ WEB-INF/
 │   │             ├─ web.xml
 │   │             └─ views/
 │   └─ test/                 # future unit/integration tests
 └─ readme.txt
```

## Initial Files
- `src/main/webapp/index.jsp`: entry JSP landing page, forwards to controller soon.
- `src/main/webapp/WEB-INF/web.xml`: deployment descriptor, sets Java 17 / Servlet 5 schema + welcome-file.
- `docs/architecture.md`: outlines controller/service/dao/entity/util responsibilities.
- `src/main/resources/db/README.md`: placeholder for SQL scripts.

## Next Setup Steps
1. Install Maven (3.9+) and run `mvn clean package` to compile sources, process resources, and assemble the WAR artifact.
2. Deploy `target/BankingSystem.war` to Tomcat 10.x (`$CATALINA_HOME/webapps`). Tomcat will explode it automatically.
3. Configure IDE run/debug using the Maven project (import the root `pom.xml`).
4. After confirmation, start implementing entities + DAO interfaces, then services, controllers, and JSP views following SOLID interface-driven structure.

## Database Connection Test
1. Ensure PostgreSQL is running locally with database `BankingSystemDb`, user `postgres`, password `7841` (override via `BANKING_DB_*` env vars if needed).
2. Maven already includes the PostgreSQL driver in the WAR (`WEB-INF/lib`). Ensure Tomcat loads the freshly built artifact.
3. Deploy the app to Tomcat and visit `http://localhost:8080/BankingSystem/health/db`. A 200 OK response indicates a successful JDBC connection; 500 status will print the SQL error message for troubleshooting.
