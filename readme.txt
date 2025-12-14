# BankingSystem Plain Java Web App Skeleton

## Java / Servlet Runtime
- **Java Version:** 17 (LTS, Servlet 5+ compatible)
- **Servlet Container:** Apache Tomcat 10.x (Servlet 5 API). Drop the compiled classes plus `web.xml`/JSPs under `src/main/webapp`.
- **Build Output:** Compile Java sources into `build/classes` (or Tomcat `WEB-INF/classes`) using `javac` + `classpath` pointing to `lib/*` + Tomcat servlet-api jar.

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
1. Place `jakarta.servlet-api-5.0.0.jar` (provided by Tomcat) and `postgresql-42.x.x.jar` inside `lib/` for compilation.
2. Configure your IDE to compile `src/main/java` → `build/classes` and deploy `src/main/webapp` to Tomcat (or configure VS Code extension with Tomcat server pointing to this folder).
3. Define environment config utility (e.g., `DBConfig`) under `util` for JDBC connection pooling.
4. After confirmation, start implementing entities + DAO interfaces, then services, controllers, and JSP views following SOLID interface-driven structure.
