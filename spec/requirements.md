# Requirements: Support Ticket Management System

**Version**: 1.0  
**Status**: Approved for implementation  
**Workflow**: Requirement → Specification → Plan/Tasks → Implementation → Testing → Review → Fix

## 1. Purpose

Build a Support Ticket Management System that lets support agents create, track, search, filter, and resolve customer issues. The system must enforce a strict ticket lifecycle state machine on the backend and provide a web UI with meaningful validation feedback.

## 2. Stakeholders

| Role | Goal |
|------|------|
| Support Agent | Create and manage tickets, add comments, change assignee |
| Team Lead | Filter by status, search tickets, monitor workload |
| End User (customer) | Out of scope for v1 — agents act on behalf of customers |

## 3. Functional Requirements

### FR-001 — Create Ticket

- Agent can create a ticket with **title**, **description**, and **priority** (LOW, MEDIUM, HIGH, URGENT).
- New tickets start in **OPEN** status.
- Assignee is optional at creation; may be set later.
- Backend validates required fields; UI shows field-level errors.

### FR-002 — List Tickets

- Agent can view a paginated list of all tickets.
- List shows: id, title, status, priority, assignee, created date.
- Default sort: newest first.

### FR-003 — View Ticket Details

- Agent can open a single ticket and see all fields, status history context, and comments.

### FR-004 — Update Ticket Fields

- Agent can update **title**, **description**, **priority**, and **assignee**.
- Updates persist to the database.
- Backend validates input; invalid data returns structured errors.

### FR-005 — Status Transitions (State Machine)

Valid transitions (backend-enforced):

| From | To |
|------|-----|
| OPEN | IN_PROGRESS, CANCELLED |
| IN_PROGRESS | RESOLVED, CANCELLED |
| RESOLVED | CLOSED |
| CLOSED | — (terminal) |
| CANCELLED | — (terminal) |

Invalid transitions (examples) MUST be rejected with a clear error:

- CLOSED → OPEN ❌
- RESOLVED → OPEN ❌
- CANCELLED → OPEN ❌
- OPEN → CLOSED ❌ (must go through IN_PROGRESS → RESOLVED)

### FR-006 — Add Comments

- Agent can add comments to any non-deleted ticket.
- Comments are append-only, timestamped, and associated with the ticket.
- Comments do not change ticket status.

### FR-007 — Search

- Agent can search tickets by keyword matching **title** or **description** (case-insensitive).
- Search can be combined with status filter.

### FR-008 — Filter by Status

- Agent can filter the ticket list by one or more statuses.
- Filter works with search and pagination.

### FR-009 — Persistence

- All tickets and comments survive application restart.
- Database: PostgreSQL (production) or H2 (local dev/test).

### FR-010 — Error Handling

- Backend returns structured validation errors (field + message).
- UI displays errors inline on forms and as toast/banner for API failures.
- No stack traces or internal details exposed to the client.

## 4. Non-Functional Requirements

| ID | Requirement |
|----|-------------|
| NFR-001 | Java 21, Spring Boot 3.x REST API |
| NFR-002 | React or Next.js frontend |
| NFR-003 | Flyway migrations for schema; no `ddl-auto=update` in shared configs |
| NFR-004 | No secrets committed to the repository |
| NFR-005 | State-machine integration tests for all valid and invalid transitions |
| NFR-006 | API response time p95 < 500 ms for list/search under 10k tickets (local) |

## 5. Out of Scope (v1)

- User authentication / SSO (use mock assignee names)
- Email notifications
- File attachments on tickets
- SLA timers and escalations
- Multi-tenant organisations
- Audit log UI (DB timestamps on entities suffice)

## 6. Core Acceptance Criteria

The solution is complete when:

- [x] Ticket can be created from UI
- [x] Tickets can be listed
- [x] Ticket details can be viewed
- [x] Ticket fields can be updated
- [x] Assignee can be changed
- [x] Comments can be added
- [x] Search works
- [x] Status filter works
- [x] Valid status transitions work
- [x] Invalid status transitions are rejected by backend
- [x] Data survives application restart
- [x] Backend validation works
- [x] UI shows meaningful errors
- [x] State-machine integration tests pass
- [x] No secrets are committed

Traceability: see [`docs/assignment-compliance.md`](../docs/assignment-compliance.md).

## 7. Assumptions

- Single deployment; no microservice split required for the exercise.
- Assignee is a free-text name (no user directory integration).
- Priority defaults to MEDIUM if not specified.
- Pagination default page size: 20.
