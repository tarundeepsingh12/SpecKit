# API Contract: Support Ticket Management (SpecKit)

**Feature**: `004-support-ticket-management`  
**Base**: `/api/v1`

Canonical reference: [`spec/api-contract.md`](../../spec/api-contract.md)

## Endpoints Summary

| Method | Path | Purpose |
|--------|------|---------|
| POST | `/api/v1/tickets` | Create ticket |
| GET | `/api/v1/tickets` | List (search, filter, page) |
| GET | `/api/v1/tickets/{id}` | Detail + comments |
| PUT | `/api/v1/tickets/{id}` | Update fields (not status) |
| PATCH | `/api/v1/tickets/{id}/status` | Status transition |
| POST | `/api/v1/tickets/{id}/comments` | Add comment |

## Status Codes

- `201` create ticket / comment
- `400` validation
- `404` not found
- `409` invalid status transition
