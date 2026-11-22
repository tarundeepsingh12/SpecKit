# Support Ticket Management System (SpecKit)

Spec-driven exercise: **Requirement → Specification → Plan/Tasks → Implementation → Testing → Review → Fix**.

## Quick start

```bash
# Terminal 1 — API (H2 file DB under backend/data/)
cd backend
./gradlew bootRun

# Terminal 2 — UI
cd frontend
cp env.local.example .env.local
npm install
npm run dev
```

- API: http://localhost:8080/api/v1  
- UI: http://localhost:3000  

Run tests: `cd backend && ./gradlew test`

## Repository map

| Area | Purpose |
|------|---------|
| [`spec/`](spec/requirements.md) | Approved requirements and cross-cutting specs |
| [`specs/001-support-ticket-management/`](specs/001-support-ticket-management/spec.md) | Feature spec, plan, tasks, contracts |
| [`rules/`](rules/java-springboot.md) | Human-readable steering (Java, API, testing) |
| [`.cursor/rules/`](.cursor/rules/speckit-workflow.mdc) | Cursor agent rules (same intent as `rules/`) |
| [`.cursor/commands/`](.cursor/commands/review-code.md) | Review code, spec, generate tests |
| [`.cursor/skills/`](.cursor/skills/documentation/SKILL.md) | Reusable skills (docs, endpoints, tests) |
| [`docs/`](docs/prompt-history.md) | Prompt history and AI review log |
| [`.specstory/history/`](.specstory/history/) | SpecStory export location (Cursor/VS Code) |

## Application features

Create/list/view/update tickets, assignee, comments, search, status filter, backend state machine (`OPEN` → `IN_PROGRESS` → `RESOLVED` → `CLOSED`, cancel paths), validation and UI error display. See [`spec/requirements.md`](spec/requirements.md).

## AI tooling

- **Cursor**: rules, commands, skills under `.cursor/`
- **GitHub Copilot**: [`.github/copilot-instructions.md`](.github/copilot-instructions.md)
- **Prompt capture**: SpecStory → `.specstory/history/`; summary in [`docs/prompt-history.md`](docs/prompt-history.md)
- **Token / context**: [`docs/ai-context.md`](docs/ai-context.md)

Assignment traceability: [`docs/assignment-compliance.md`](docs/assignment-compliance.md).