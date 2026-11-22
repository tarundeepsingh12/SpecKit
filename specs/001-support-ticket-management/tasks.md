# Tasks: Support Ticket Management System

**Feature**: `004-support-ticket-management`

## Phase 1 — Setup

- [X] T001 Confirm repository layout (`backend/`, `frontend/`, `spec/`) with no unrelated modules
- [X] T002 Create `backend/` Spring Boot project (Java 21, Flyway, H2)
- [X] T003 Create `frontend/` Next.js project
- [X] T004 Add `.gitignore`, rules, and Cursor commands

## Phase 2 — Backend Core

- [X] T005 Flyway migration `V1__create_ticket_tables.sql`
- [X] T006 JPA entities: `Ticket`, `TicketComment`, enums
- [X] T007 `TicketStatus.canTransitionTo()` state machine
- [X] T008 DTOs with Bean Validation
- [X] T009 `TicketService` — CRUD, search, filter, comments
- [X] T010 `TicketController` — REST endpoints per `api-contract.md`
- [X] T011 `GlobalExceptionHandler` — 400/404/409 structured errors
- [X] T012 CORS config for `localhost:3000`

## Phase 3 — Tests

- [X] T013 Unit test: `TicketStatusTest`
- [X] T014 Integration test: `TicketStateMachineIntegrationTest`
- [X] T015 Integration test: `TicketApiIntegrationTest`

## Phase 4 — Frontend

- [X] T016 API client (`src/lib/api.ts`) with typed errors
- [X] T017 Ticket list page — search, filter, pagination
- [X] T018 Create ticket form with inline validation errors
- [X] T019 Ticket detail — edit fields, status buttons, comments
- [X] T020 Error banner component for API failures

## Phase 5 — Documentation & Review

- [X] T021 Complete spec artifacts (`spec/*.md`)
- [X] T022 AI review log (`docs/ai-review-log.md`)
- [X] T023 README with quick start
- [X] T024 Prompt history (`docs/prompt-history.md`)
