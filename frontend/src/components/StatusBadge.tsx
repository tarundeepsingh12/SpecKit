import { TicketStatus } from "@/lib/api";

const LABELS: Record<TicketStatus, string> = {
  OPEN: "Open",
  IN_PROGRESS: "In Progress",
  RESOLVED: "Resolved",
  CLOSED: "Closed",
  CANCELLED: "Cancelled",
};

export function StatusBadge({ status }: { status: TicketStatus }) {
  const cssClass = status.toLowerCase();
  return <span className={`badge ${cssClass}`}>{LABELS[status]}</span>;
}
