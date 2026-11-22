"use client";

import Link from "next/link";
import { FormEvent, useCallback, useEffect, useState } from "react";
import { useParams } from "next/navigation";
import { ErrorBanner } from "@/components/ErrorBanner";
import { StatusBadge } from "@/components/StatusBadge";
import {
  ApiError,
  TicketDetail,
  TicketPriority,
  TicketStatus,
  addComment,
  getTicket,
  updateStatus,
  updateTicket,
} from "@/lib/api";

const PRIORITIES: TicketPriority[] = ["LOW", "MEDIUM", "HIGH", "URGENT"];

const NEXT_STATUSES: Record<TicketStatus, TicketStatus[]> = {
  OPEN: ["IN_PROGRESS", "CANCELLED"],
  IN_PROGRESS: ["RESOLVED", "CANCELLED"],
  RESOLVED: ["CLOSED"],
  CLOSED: [],
  CANCELLED: [],
};

export default function TicketDetailPage() {
  const params = useParams<{ id: string }>();
  const ticketId = Number(params.id);

  const [ticket, setTicket] = useState<TicketDetail | null>(null);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [priority, setPriority] = useState<TicketPriority>("MEDIUM");
  const [assignee, setAssignee] = useState("");
  const [commentBody, setCommentBody] = useState("");
  const [commentAuthor, setCommentAuthor] = useState("Agent");
  const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});
  const [error, setError] = useState<ApiError | string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);

  const loadTicket = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await getTicket(ticketId);
      setTicket(data);
      setTitle(data.title);
      setDescription(data.description);
      setPriority(data.priority);
      setAssignee(data.assignee ?? "");
    } catch (err) {
      setError(err as ApiError);
    } finally {
      setLoading(false);
    }
  }, [ticketId]);

  useEffect(() => {
    loadTicket();
  }, [loadTicket]);

  async function handleUpdate(event: FormEvent) {
    event.preventDefault();
    setError(null);
    setSuccess(null);
    setFieldErrors({});

    try {
      const updated = await updateTicket(ticketId, {
        title,
        description,
        priority,
        assignee: assignee || undefined,
      });
      setTicket(updated);
      setSuccess("Ticket updated successfully.");
    } catch (err) {
      const apiError = err as ApiError;
      setError(apiError);
      const nextFieldErrors: Record<string, string> = {};
      apiError.fieldErrors?.forEach((fieldError) => {
        nextFieldErrors[fieldError.field] = fieldError.message;
      });
      setFieldErrors(nextFieldErrors);
    }
  }

  async function handleStatusChange(nextStatus: TicketStatus) {
    setError(null);
    setSuccess(null);

    try {
      const updated = await updateStatus(ticketId, nextStatus);
      setTicket(updated);
      setSuccess(`Status changed to ${nextStatus.replace("_", " ")}.`);
    } catch (err) {
      setError(err as ApiError);
    }
  }

  async function handleComment(event: FormEvent) {
    event.preventDefault();
    setError(null);
    setSuccess(null);

    try {
      await addComment(ticketId, { body: commentBody, author: commentAuthor });
      setCommentBody("");
      await loadTicket();
      setSuccess("Comment added.");
    } catch (err) {
      setError(err as ApiError);
    }
  }

  if (loading) {
    return <main><p>Loading ticket...</p></main>;
  }

  if (!ticket) {
    return (
      <main>
        <ErrorBanner error={error ?? "Ticket not found"} />
        <Link href="/">Back to list</Link>
      </main>
    );
  }

  const allowedTransitions = NEXT_STATUSES[ticket.status];

  return (
    <main>
      <header>
        <div>
          <h1>Ticket #{ticket.ticketId}</h1>
          <p><Link href="/">Back to list</Link></p>
        </div>
        <StatusBadge status={ticket.status} />
      </header>

      <ErrorBanner error={error} />
      {success && <div className="banner success">{success}</div>}

      <section className="card">
        <h2>Details</h2>
        <form onSubmit={handleUpdate}>
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
          </div>

          <div className="field">
            <label htmlFor="assignee">Assignee</label>
            <input
              id="assignee"
              className="input"
              value={assignee}
              onChange={(event) => setAssignee(event.target.value)}
            />
          </div>

          <button className="button primary" type="submit">Save Changes</button>
        </form>
      </section>

      <section className="card">
        <h2>Status</h2>
        {allowedTransitions.length === 0 ? (
          <p>This ticket is in a terminal state.</p>
        ) : (
          <div style={{ display: "flex", gap: "8px", flexWrap: "wrap" }}>
            {allowedTransitions.map((status) => (
              <button
                key={status}
                className="button secondary"
                type="button"
                onClick={() => handleStatusChange(status)}
              >
                Move to {status.replace("_", " ")}
              </button>
            ))}
          </div>
        )}
      </section>

      <section className="card">
        <h2>Comments</h2>
        {ticket.comments.length === 0 ? (
          <p>No comments yet.</p>
        ) : (
          ticket.comments.map((comment) => (
            <div key={comment.id} className="comment">
              <div className="comment-meta">
                {comment.author} · {new Date(comment.createdAt).toLocaleString()}
              </div>
              <div>{comment.body}</div>
            </div>
          ))
        )}

        <form onSubmit={handleComment} style={{ marginTop: "16px" }}>
          <div className="field">
            <label htmlFor="comment">Add comment</label>
            <textarea
              id="comment"
              className="textarea"
              value={commentBody}
              onChange={(event) => setCommentBody(event.target.value)}
            />
          </div>
          <div className="field">
            <label htmlFor="author">Author</label>
            <input
              id="author"
              className="input"
              value={commentAuthor}
              onChange={(event) => setCommentAuthor(event.target.value)}
            />
          </div>
          <button className="button primary" type="submit">Add Comment</button>
        </form>
      </section>
    </main>
  );
}
