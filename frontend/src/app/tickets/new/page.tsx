"use client";

import Link from "next/link";
import { FormEvent, useState } from "react";
import { useRouter } from "next/navigation";
import { ErrorBanner } from "@/components/ErrorBanner";
import { ApiError, TicketPriority, createTicket } from "@/lib/api";

const PRIORITIES: TicketPriority[] = ["LOW", "MEDIUM", "HIGH", "URGENT"];

export default function NewTicketPage() {
  const router = useRouter();
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState<TicketPriority>("MEDIUM");
  const [assignee, setAssignee] = useState("");
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [error, setError] = useState<ApiError | string | null>(null);
  const [submitting, setSubmitting] = useState(false);

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();
    setSubmitting(true);
    setError(null);
    setFieldErrors({});

    try {
      const ticket = await createTicket({
        title,
        description,
        priority,
        assignee: assignee || undefined,
      });
      router.push(`/tickets/${ticket.ticketId}`);
    } catch (err) {
      const apiError = err as ApiError;
      setError(apiError);
      const nextFieldErrors: Record<string, string> = {};
      apiError.fieldErrors?.forEach((fieldError) => {
        nextFieldErrors[fieldError.field] = fieldError.message;
      });
      setFieldErrors(nextFieldErrors);
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <main>
      <header>
        <div>
          <h1>Create Ticket</h1>
          <p><Link href="/">Back to list</Link></p>
        </div>
      </header>

      <ErrorBanner error={error} />

      <form className="card" onSubmit={handleSubmit}>
        <div className="field">
          <label htmlFor="title">Title</label>
          <input
            id="title"
            className="input"
            value={title}
            onChange={(event) => setTitle(event.target.value)}
          />
          {fieldErrors.title && <div className="field-error">{fieldErrors.title}</div>}
        </div>

        <div className="field">
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            className="textarea"
            value={description}
            onChange={(event) => setDescription(event.target.value)}
          />
          {fieldErrors.description && (
            <div className="field-error">{fieldErrors.description}</div>
          )}
        </div>

        <div className="field">
          <label htmlFor="priority">Priority</label>
          <select
            id="priority"
            className="select"
            value={priority}
            onChange={(event) => setPriority(event.target.value as TicketPriority)}
          >
            {PRIORITIES.map((value) => (
              <option key={value} value={value}>{value}</option>
            ))}
          </select>
          {fieldErrors.priority && <div className="field-error">{fieldErrors.priority}</div>}
        </div>

        <div className="field">
          <label htmlFor="assignee">Assignee</label>
          <input
            id="assignee"
            className="input"
            value={assignee}
            onChange={(event) => setAssignee(event.target.value)}
          />
          {fieldErrors.assignee && <div className="field-error">{fieldErrors.assignee}</div>}
        </div>

        <button className="button primary" type="submit" disabled={submitting}>
          {submitting ? "Creating..." : "Create Ticket"}
        </button>
      </form>
    </main>
  );
}
