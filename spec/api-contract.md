# API Contract: Support Ticket Management System

**Version**: 1.0  
**Base path**: `/api/v1`

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/tickets` | Create ticket |
| GET | `/tickets` | List tickets (search, filter, paginate) |
| GET | `/tickets/{id}` | Get ticket detail with comments |
| PUT | `/tickets/{id}` | Update title, description, priority, assignee |
| PATCH | `/tickets/{id}/status` | Transition ticket status |
| POST | `/tickets/{id}/comments` | Add comment |

## Create Ticket

**Request**
```json
{
  "title": "Login page 500 error",
  "description": "Users cannot log in since 09:00 UTC",
  "priority": "HIGH",
  "assignee": "Alice"
}
```

**Response** `201 Created` — ticket detail with `status: OPEN`.

## List Tickets

**Query params**: `status`, `keyword`, `page` (default 0), `size` (default 20)

**Response** `200 OK`
```json
{
  "content": [{ "ticketId": 1, "title": "...", "status": "OPEN", "priority": "HIGH", "assignee": "Alice", "createdAt": "..." }],
  "page": 0,
  "size": 20,
  "totalElements": 1,
  "totalPages": 1
}
```

## Update Status

**Request**
```json
{ "status": "IN_PROGRESS" }
```

**Responses**
- `200 OK` — valid transition
- `409 Conflict` — invalid transition with message `Invalid status transition from X to Y`

## Error Response

```json
{
  "timestamp": "2026-09-09T12:00:00Z",
  "status": 400,
  "error": "Validation Failed",
  "message": "Request validation failed",
  "fieldErrors": [{ "field": "title", "message": "Title must be between 3 and 200 characters" }]
}
```
