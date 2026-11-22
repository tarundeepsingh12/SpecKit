# API Standards

## Base Path

`/api/v1`

## HTTP Methods

| Method | Use |
|--------|-----|
| GET | Read resources |
| POST | Create resources |
| PUT | Full update of mutable fields |
| PATCH | Partial/specialised update (e.g. status transition) |

## Status Codes

| Code | When |
|------|------|
| 200 | Successful read or update |
| 201 | Resource created |
| 400 | Validation failure (with `fieldErrors`) |
| 404 | Resource not found |
| 409 | Business rule violation (e.g. invalid status transition) |

## Error Response Shape

```json
{
  "timestamp": "ISO-8601",
  "status": 400,
  "error": "Validation Failed",
  "message": "Human-readable summary",
  "fieldErrors": [{ "field": "title", "message": "..." }]
}
```

## Pagination

List endpoints accept `page` (0-based) and `size` (default 20). Response includes `content`, `totalElements`, `totalPages`.

## Naming

- Resource IDs exposed as `ticketId` in JSON (not raw `id`)
- Enum values in UPPER_SNAKE_CASE (`IN_PROGRESS`, not `in_progress`)
