# Quickstart: Support Ticket Management System

## Prerequisites

- Java 21
- Node.js 20+
- Gradle (or use wrapper after generation)

## 1. Start Backend

```bash
cd backend
gradle wrapper    # first time only
./gradlew bootRun
```

API available at `http://localhost:8080/api/v1/tickets`

## 2. Start Frontend

```bash
cd frontend
npm install
npm run dev
```

UI available at `http://localhost:3000`

## 3. Run Tests

```bash
cd backend
./gradlew test
```

## 4. Verify Acceptance Criteria

1. Create a ticket from UI (`/tickets/new`)
2. List shows the ticket; search and filter work
3. Open detail page; update fields and assignee
4. Transition: OPEN → IN_PROGRESS → RESOLVED → CLOSED
5. Try invalid transition (e.g. reopen CLOSED) — UI shows error
6. Add a comment; verify it appears
7. Restart backend — data persists (H2 file store in `backend/data/`)

## Production Profile

```bash
# Set DB credentials via your environment or shell profile (not in this repo).
# Example keys: DATABASE_URL, DATABASE_USERNAME, DATABASE_USER_PASSWORD
./gradlew bootRun --args='--spring.profiles.active=prod'
```

Use environment variables for credentials — never commit secrets. See `application-prod.yml` for variable names.
