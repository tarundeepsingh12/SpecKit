# UI Flow: Support Ticket Management System

**Version**: 1.0

## Screens

### 1. Ticket List (`/`)

- Table: ID, title, status badge, priority, assignee, created date
- Search box: keyword on title/description
- Status filter dropdown
- "New Ticket" button

### 2. Create Ticket (`/tickets/new`)

- Form: title, description, priority, assignee (optional)
- Inline field errors from backend validation
- Banner for non-field API errors
- On success → redirect to detail page

### 3. Ticket Detail (`/tickets/{id}`)

- View/edit title, description, priority, assignee
- Status transition buttons (only valid next states shown)
- Comments list (chronological)
- Add comment form
- Error banner for invalid transitions (409)

## Error Display Rules

| Error type | UI behaviour |
|------------|--------------|
| Field validation (400) | Inline under field + banner summary |
| Invalid transition (409) | Banner with backend message |
| Not found (404) | Banner + link back to list |
