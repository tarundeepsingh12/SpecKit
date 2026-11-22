# Architecture: Support Ticket Management System

**Version**: 1.0

## Overview

```text
┌─────────────┐     REST/JSON      ┌──────────────────────┐
│  Next.js UI │ ◄────────────────► │ Spring Boot Backend  │
│  (port 3000)│                    │  (port 8080)         │
└─────────────┘                    └──────────┬───────────┘
                                              │
                                              ▼
                                   ┌──────────────────────┐
                                   │ H2 (dev) / PostgreSQL│
                                   │ Flyway migrations    │
                                   └──────────────────────┘
```

## Backend Layers

| Layer | Package | Responsibility |
|-------|---------|----------------|
| Controller | `controller` | REST endpoints, validation trigger |
| Service | `service` | Business logic, state machine |
| Domain | `domain` | JPA entities, enums |
| Repository | `repository` | Data access |
| DTO | `dto` | API request/response models |
| Exception | `exception` | Global error handling |

## Frontend Structure

| Path | Purpose |
|------|---------|
| `src/app/page.tsx` | Ticket list, search, filter |
| `src/app/tickets/new/page.tsx` | Create ticket form |
| `src/app/tickets/[id]/page.tsx` | Detail, update, status, comments |
| `src/lib/api.ts` | API client with typed errors |

## Spec-Driven Workflow

1. Requirements (`spec/requirements.md`)
2. Specifications (`spec/*.md`)
3. Feature plan (`specs/001-support-ticket-management/`)
4. Implementation (`backend/`, `frontend/`)
5. Tests (state machine integration tests)
6. Review (`docs/ai-review-log.md`)
