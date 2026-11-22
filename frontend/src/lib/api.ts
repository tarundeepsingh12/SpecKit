const API_BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080/api/v1";

export type TicketStatus =
  | "OPEN"
  | "IN_PROGRESS"
  | "RESOLVED"
  | "CLOSED"
  | "CANCELLED";

export type TicketPriority = "LOW" | "MEDIUM" | "HIGH" | "URGENT";

export interface FieldError {
  field: string;
  message: string;
}

export interface ApiError {
  status: number;
  error: string;
  message: string;
  fieldErrors: FieldError[];
}

export interface TicketSummary {
  ticketId: number;
  title: string;
  status: TicketStatus;
  priority: TicketPriority;
  assignee: string | null;
  createdAt: string;
}

export interface Comment {
  id: number;
  body: string;
  author: string;
  createdAt: string;
}

export interface TicketDetail {
  ticketId: number;
  title: string;
  description: string;
  status: TicketStatus;
  priority: TicketPriority;
  assignee: string | null;
  version: number;
  createdAt: string;
  updatedAt: string;
  comments: Comment[];
}

export interface PageResponse<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
}

async function parseError(response: Response): Promise<ApiError> {
  try {
    const body = await response.json();
    return {
      status: body.status ?? response.status,
      error: body.error ?? "Error",
      message: body.message ?? response.statusText,
      fieldErrors: body.fieldErrors ?? [],
    };
  } catch {
    return {
      status: response.status,
      error: "Error",
      message: response.statusText,
      fieldErrors: [],
    };
  }
}

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(options?.headers ?? {}),
    },
  });

  if (!response.ok) {
    throw await parseError(response);
  }

  if (response.status === 204) {
    return undefined as T;
  }

  return response.json() as Promise<T>;
}

export async function listTickets(params: {
  status?: TicketStatus;
  keyword?: string;
  page?: number;
  size?: number;
}): Promise<PageResponse<TicketSummary>> {
  const search = new URLSearchParams();
  if (params.status) search.set("status", params.status);
  if (params.keyword) search.set("keyword", params.keyword);
  search.set("page", String(params.page ?? 0));
  search.set("size", String(params.size ?? 20));
  return request(`/tickets?${search.toString()}`);
}

export async function getTicket(id: number): Promise<TicketDetail> {
  return request(`/tickets/${id}`);
}

export async function createTicket(body: {
  title: string;
  description: string;
  priority: TicketPriority;
  assignee?: string;
}): Promise<TicketDetail> {
  return request("/tickets", { method: "POST", body: JSON.stringify(body) });
}

export async function updateTicket(
  id: number,
  body: {
    title: string;
    description: string;
    priority: TicketPriority;
    assignee?: string;
  }
): Promise<TicketDetail> {
  return request(`/tickets/${id}`, { method: "PUT", body: JSON.stringify(body) });
}

export async function updateStatus(
  id: number,
  status: TicketStatus
): Promise<TicketDetail> {
  return request(`/tickets/${id}/status`, {
    method: "PATCH",
    body: JSON.stringify({ status }),
  });
}

export async function addComment(
  id: number,
  body: { body: string; author?: string }
): Promise<Comment> {
  return request(`/tickets/${id}/comments`, {
    method: "POST",
    body: JSON.stringify(body),
  });
}
