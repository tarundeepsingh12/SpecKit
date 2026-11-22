# Test Strategy: Support Ticket Management System

**Version**: 1.0

## Test Layers

| Layer | Scope | Location |
|-------|-------|----------|
| Unit | State machine logic | `TicketStatusTest` |
| Integration | REST API + DB | `TicketStateMachineIntegrationTest`, `TicketApiIntegrationTest` |
| Manual | UI flows | Acceptance criteria checklist |

## State Machine Integration Tests (mandatory)

Cover:
- OPEN → IN_PROGRESS → RESOLVED → CLOSED (happy path)
- OPEN → CANCELLED
- IN_PROGRESS → CANCELLED
- CLOSED → OPEN (409)
- RESOLVED → OPEN (409)
- CANCELLED → OPEN (409)
- OPEN → CLOSED (409)

## API Integration Tests

- Validation errors return structured field errors
- Search by keyword
- Filter by status
- Update ticket fields
- Add comments

## Running Tests

```bash
cd backend
./gradlew test
```
