"use client";

import Link from "next/link";
import { FormEvent, useEffect, useState } from "react";
import { ErrorBanner } from "@/components/ErrorBanner";
import { StatusBadge } from "@/components/StatusBadge";
import {
  ApiError,
  TicketStatus,
  TicketSummary,
  listTickets,
} from "@/lib/api";

const STATUSES: Array<TicketStatus | ""> = [
  "",
  "OPEN",
  "IN_PROGRESS",
  "RESOLVED",
  "CLOSED",
  "CANCELLED",
];

export default function HomePage() {
  const [tickets, setTickets] = useState<TicketSummary[]>([]);
  const [keyword, setKeyword] = useState("");
  const [status, setStatus] = useState<TicketStatus | "">("");
  const [error, setError] = useState<ApiError | string | null>(null);
  const [loading, setLoading] = useState(true);

  async function loadTickets(searchKeyword = keyword, searchStatus = status) {
    setLoading(true);
    setError(null);
    try {
      const response = await listTickets({
        keyword: searchKeyword || undefined,
        status: searchStatus || undefined,
      });
      setTickets(response.content);
    } catch (err) {
      setError(err as ApiError);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadTickets();
  }, []);

  function handleSearch(event: FormEvent) {
    event.preventDefault();
    loadTickets(keyword, status);
  }

  return (
    <main>
      <header>
        <div>
          <h1>Support Tickets</h1>
          <p>Spec-driven ticket management</p>
        </div>
        <Link className="button primary" href="/tickets/new">
          New Ticket
        </Link>
      </header>

      <ErrorBanner error={error} />

      <section className="card">
        <form className="filters" onSubmit={handleSearch}>
          <input
            className="input"
            placeholder="Search by title or description"
            value={keyword}
            onChange={(event) => setKeyword(event.target.value)}
          />
          <select
            className="select"
            value={status}
            onChange={(event) => setStatus(event.target.value as TicketStatus | "")}
          >
            {STATUSES.map((value) => (
              <option key={value || "all"} value={value}>
                {value ? value.replace("_", " ") : "All statuses"}
              </option>
            ))}
          </select>
          <button className="button secondary" type="submit">
            Search
          </button>
        </form>

        {loading ? (
          <p>Loading tickets...</p>
        ) : tickets.length === 0 ? (
          <p>No tickets found.</p>
        ) : (
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Status</th>
                <th>Priority</th>
                <th>Assignee</th>
                <th>Created</th>
              </tr>
            </thead>
            <tbody>
              {tickets.map((ticket) => (
                <tr key={ticket.ticketId}>
                  <td>
                    <Link href={`/tickets/${ticket.ticketId}`}>#{ticket.ticketId}</Link>
                  </td>
                  <td>{ticket.title}</td>
                  <td><StatusBadge status={ticket.status} /></td>
                  <td>{ticket.priority}</td>
                  <td>{ticket.assignee ?? "Unassigned"}</td>
                  <td>{new Date(ticket.createdAt).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </section>
    </main>
  );
}
