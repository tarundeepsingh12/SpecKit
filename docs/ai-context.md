# AI context and token optimisation

Practices for this project when using Cursor, Copilot, or MCP tooling.

## Steer before you generate

1. Read [`spec/requirements.md`](../spec/requirements.md) and the relevant `spec/*.md` file **before** implementation prompts.
2. Point the agent at **narrow paths** (`backend/.../TicketService.java`) instead of the whole repo.
3. Use [`.cursor/commands/`](../.cursor/commands/review-spec.md) for consistency checks instead of re-pasting full specs into chat.

## Reusable context (low token cost)

| Source | Use |
|--------|-----|
| [`rules/`](../rules/java-springboot.md) | Always-on conventions |
| [`.cursor/rules/*.mdc`](../.cursor/rules/speckit-workflow.mdc) | Cursor auto-attached by glob |
| [`.cursor/skills/`](../.cursor/skills/documentation/SKILL.md) | On-demand procedures |

## MCP / plugins (assignment)

Optional tools mentioned in the exercise:

- **Graphify** — relationship graphs for large changes (use when exploring unfamiliar packages).
- **Caveman / codebase-memory MCP** — cache stable facts (package layout, API base path) to avoid re-reading files.

Configure MCP servers in Cursor settings; do not commit API keys.

## Anti-patterns

- Avoid: *"Build the complete application."*
- Prefer: *"Implement FR-005 status transition per `spec/state-machine.md`; add integration test row for CLOSED→OPEN."*

Document AI corrections in [`docs/ai-review-log.md`](ai-review-log.md).