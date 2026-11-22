# State Machine: Ticket Status

**Version**: 1.0

## States

`OPEN` → `IN_PROGRESS` → `RESOLVED` → `CLOSED`

Cancellation branches:
- `OPEN` → `CANCELLED`
- `IN_PROGRESS` → `CANCELLED`

Terminal states: `CLOSED`, `CANCELLED` (no outgoing transitions)

## Valid Transitions

| From | Allowed targets |
|------|-----------------|
| OPEN | IN_PROGRESS, CANCELLED |
| IN_PROGRESS | RESOLVED, CANCELLED |
| RESOLVED | CLOSED |
| CLOSED | — |
| CANCELLED | — |

## Invalid Examples (must return 409)

| From | To | Reason |
|------|-----|--------|
| CLOSED | OPEN | Terminal state |
| RESOLVED | OPEN | Cannot reopen |
| CANCELLED | OPEN | Terminal state |
| OPEN | CLOSED | Must progress through workflow |

## Enforcement

- Backend validates every `PATCH /tickets/{id}/status` request
- `TicketStatus.canTransitionTo()` is the single source of truth
- Integration tests cover all valid and invalid paths
