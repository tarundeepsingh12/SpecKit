# Data Model: Support Ticket Management System

**Version**: 1.0

## 1. Entity Relationship

```text
┌─────────────────┐       1     *  ┌──────────────────┐
│     ticket      │────────────────│  ticket_comment  │
└─────────────────┘                └──────────────────┘
```

## 2. Table: `ticket`

| Column | Type | Constraints | Notes |
|--------|------|-------------|-------|
| id | BIGSERIAL | PK | Exposed as `ticketId` in API |
| title | VARCHAR(200) | NOT NULL | 3–200 chars |
| description | TEXT | NOT NULL | Max 5000 chars |
| status | VARCHAR(30) | NOT NULL, DEFAULT 'OPEN' | Enum: OPEN, IN_PROGRESS, RESOLVED, CLOSED, CANCELLED |
| priority | VARCHAR(20) | NOT NULL, DEFAULT 'MEDIUM' | Enum: LOW, MEDIUM, HIGH, URGENT |
| assignee | VARCHAR(100) | NULL | Free-text agent name |
| version | BIGINT | NOT NULL, DEFAULT 0 | Optimistic lock |
| created_at | TIMESTAMP | NOT NULL | Auditing |
| updated_at | TIMESTAMP | NOT NULL | Auditing |

### Indexes

- `idx_ticket_status` on `(status)`
- `idx_ticket_priority` on `(priority)`
- `idx_ticket_created_at` on `(created_at DESC)`
- `idx_ticket_title_lower` on `LOWER(title)` — optional for search performance

## 3. Table: `ticket_comment`

| Column | Type | Constraints | Notes |
|--------|------|-------------|-------|
| id | BIGSERIAL | PK | |
| ticket_id | BIGINT | NOT NULL, FK → ticket(id) | CASCADE on delete |
| body | TEXT | NOT NULL | 1–2000 chars |
| author | VARCHAR(100) | NOT NULL | Free-text; default "Agent" in v1 |
| created_at | TIMESTAMP | NOT NULL | Immutable after insert |

### Indexes

- `idx_ticket_comment_ticket_id` on `(ticket_id)`

## 4. Enumerations

### TicketStatus

```text
OPEN → IN_PROGRESS → RESOLVED → CLOSED
OPEN → CANCELLED
IN_PROGRESS → CANCELLED
```

### TicketPriority

```text
LOW < MEDIUM < HIGH < URGENT  (display ordering only; no auto-escalation in v1)
```

## 5. JPA Entity Sketch

```java
@Entity
@Table(name = "ticket")
public class Ticket extends BaseEntity {
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    @Enumerated(EnumType.STRING)
    private TicketPriority priority;
    private String assignee;
    @Version
    private Long version;
    @OneToMany(mappedBy = "ticket", fetch = FetchType.LAZY)
    private List<TicketComment> comments = new ArrayList<>();
}
```

## 6. Validation Rules

| Field | Rule |
|-------|------|
| title | `@NotBlank`, `@Size(min=3, max=200)` |
| description | `@NotBlank`, `@Size(max=5000)` |
| priority | `@NotNull`, valid enum |
| status (on update) | Must be valid transition from current status |
| assignee | `@Size(max=100)` when present |
| comment.body | `@NotBlank`, `@Size(max=2000)` |

## 7. Sample Data (Seed / Dev)

```sql
INSERT INTO ticket (title, description, status, priority, assignee)
VALUES
  ('Login page 500 error', 'Users cannot log in since 09:00 UTC', 'OPEN', 'HIGH', 'Alice'),
  ('Billing export slow', 'CSV export takes > 2 minutes', 'IN_PROGRESS', 'MEDIUM', 'Bob');
```
