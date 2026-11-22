# Requirements Checklist: Support Ticket Management System

## Functional

- [X] FR-001 Create ticket with title, description, priority
- [X] FR-002 List tickets (paginated, sorted newest first)
- [X] FR-003 View ticket details with comments
- [X] FR-004 Update title, description, priority, assignee
- [X] FR-005 Status transitions enforced by backend state machine
- [X] FR-006 Add comments (append-only, timestamped)
- [X] FR-007 Search by keyword (title/description)
- [X] FR-008 Filter by status
- [X] FR-009 Persistence (H2 file / PostgreSQL)
- [X] FR-010 Structured validation errors in UI

## Non-Functional

- [X] NFR-001 Java 21, Spring Boot 3.x
- [X] NFR-002 Next.js frontend
- [X] NFR-003 Flyway migrations, ddl-auto=validate
- [X] NFR-004 No secrets committed
- [X] NFR-005 State machine integration tests

## Acceptance Criteria

- [X] Ticket created from UI
- [X] Tickets listed
- [X] Ticket details viewed
- [X] Ticket fields updated
- [X] Assignee changed
- [X] Comments added
- [X] Search works
- [X] Status filter works
- [X] Valid status transitions work
- [X] Invalid transitions rejected (409)
- [X] Data survives restart
- [X] Backend validation works
- [X] UI shows meaningful errors
- [X] State machine integration tests pass
- [X] No secrets committed
