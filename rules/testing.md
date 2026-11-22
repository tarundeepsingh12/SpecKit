# Testing Guidelines

## Required Coverage

1. **Unit tests** for pure domain logic (state machine, validators)
2. **Integration tests** for REST endpoints with real DB (H2 in-memory)
3. **State machine integration tests** for every valid and invalid transition

## Conventions

- Use `@SpringBootTest` + `@AutoConfigureMockMvc` for API integration tests
- Use `@ActiveProfiles("test")` with `application-test.yml`
- Test files mirror production package structure under `src/test/java`
- Assert on HTTP status codes and JSON response bodies

## Running Tests

```bash
cd backend
./gradlew test
```

## What to Test

| Area | Example |
|------|---------|
| Validation | Empty title returns 400 with field errors |
| State machine | CLOSED → OPEN returns 409 |
| Search/filter | Keyword matches title or description |
| Comments | Comment appears in ticket detail after POST |
| Persistence | Data survives restart (H2 file store) |

## Do Not

- Mock the database in integration tests
- Skip state machine edge cases
- Commit secrets or real credentials in test configs
