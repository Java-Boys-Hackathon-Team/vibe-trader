// TypeScript API client for AiChatController
export type MessageRole = 'USER' | 'ASSISTANT';

export interface DialogDto {
  id: number;
  title: string;
  createdAt: string; // ISO date
}

export interface ChatMessageDto {
  id: number;
  dialogId: number;
  taskId: number | null;
  role: MessageRole;
  content: string;
  createdAt: string;
}

export type TaskStatus = 'RUNNING' | 'DONE' | 'ERROR';

export interface TaskDto {
  id: number;
  status: TaskStatus;
  errorMessage?: string | null;
  createdAt: string;
  completedAt?: string | null;
}

export interface SendMessageResponse {
  taskId: number;
  userMessageId: number;
  status: TaskStatus;
}

// Base API path; during dev, Vite proxy routes /api -> http://localhost:8080
const BASE = '/api/ai';

async function http<T>(input: RequestInfo, init?: RequestInit): Promise<T> {
  const res = await fetch(input, {
    headers: {
      'Content-Type': 'application/json',
    },
    ...init,
  });
  if (!res.ok) {
    const text = await res.text().catch(() => '');
    throw new Error(text || `HTTP ${res.status}`);
  }
  if (res.status === 204) return undefined as unknown as T;
  return res.json() as Promise<T>;
}

export const api = {
  getDialogs(): Promise<DialogDto[]> {
    return http(`${BASE}/dialogs`);
  },
  createDialog(title: string): Promise<DialogDto> {
    return http(`${BASE}/dialogs`, { method: 'POST', body: JSON.stringify({ title }) });
  },
  getMessages(dialogId: number): Promise<ChatMessageDto[]> {
    return http(`${BASE}/dialogs/${dialogId}/messages`);
  },
  sendMessage(dialogId: number, content: string): Promise<SendMessageResponse> {
    return http(`${BASE}/dialogs/${dialogId}/messages`, { method: 'POST', body: JSON.stringify({ content }) });
  },
  getTask(taskId: number): Promise<TaskDto> {
    return http(`${BASE}/tasks/${taskId}`);
  },
};
