# Research: Support Ticket Management System

## Decision: Standalone backend and frontend modules

**Decision**: Implement the ticket system in dedicated `backend/` (Spring Boot) and `frontend/` (Next.js) modules at the repository root.

**Rationale**: Keeps the Support Ticket domain self-contained with local JPA entities, Flyway migrations, and a clear package layout (`com.speckit.tickets`).

**Alternatives considered**:
- Monolithic single module without a separate UI — rejected; requirements call for a web UI and REST API
- Shared external persistence libraries — rejected; couples schema and releases to unrelated systems

## Decision: Flyway for schema migrations

**Decision**: Flyway with local migrations.

**Rationale**: Requirements (NFR-003) specify Flyway. Local migrations are reviewable and independent.

## Decision: H2 file store for dev persistence

**Decision**: `jdbc:h2:file:./data/tickets` for local dev.

**Rationale**: Meets acceptance criterion "data survives application restart" without requiring PostgreSQL for local development.

## Decision: Separate status transition endpoint

**Decision**: `PATCH /tickets/{id}/status` separate from `PUT /tickets/{id}`.

**Rationale**: Clear separation of field edits vs lifecycle transitions; consistent 409 responses for invalid transitions.
