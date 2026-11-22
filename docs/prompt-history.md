# Prompt history

Append-only log of significant prompts (SpecStory exports live under [`.specstory/history/`](../.specstory/history/)).

| When (UTC+5:30) | Prompt summary | Outcome |
|-----------------|----------------|---------|
| 2026-11-21 | Restructure `.cursor` commands, rules, skills with project-specific content | Renamed artefacts (`audit-*`, `st-*`, `ticket-*` skills) |
| 2026-11-21 | Remove stray references from unrelated product domains | Updated specs and `.specify` templates |
| 2026-11-21 | Verify SE/SSE assignment requirements; add missing artefacts | `docs/`, `.specstory/`, README, assignment commands, compliance doc |

## How to record prompts

1. Enable **SpecStory** (or equivalent) in Cursor/VS Code so sessions save under `.specstory/history/`.
2. After each working session, add one row above with the **intent** of the prompt (not necessarily full text).
3. For long sessions, link to the SpecStory export filename in `.specstory/history/`.

## Template row

```text
| YYYY-MM-DD | <what you asked the AI to do> | <result / files touched> |
```