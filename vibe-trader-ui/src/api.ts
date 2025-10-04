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
    // Don't set Content-Type by default; let callers provide it (e.g., multipart/form-data)
    ...init,
  });
  if (!res.ok) {
    const text = await res.text().catch(() => '');
    throw new Error(text || `HTTP ${res.status}`);
  }
  if (res.status === 204) return undefined as unknown as T;
  const ct = res.headers.get('content-type') || '';
  if (ct.includes('application/json')) {
    return res.json() as Promise<T>;
  }
  // Fallback
  return (await res.text()) as unknown as T;
}

export const api = {
  getDialogs(): Promise<DialogDto[]> {
    return http(`${BASE}/dialogs`);
  },
  createDialog(title: string): Promise<DialogDto> {
    return http(`${BASE}/dialogs`, { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify({ title }) });
  },
  getMessages(dialogId: number): Promise<ChatMessageDto[]> {
    return http(`${BASE}/dialogs/${dialogId}/messages`);
  },
  sendMessage(dialogId: number, content: string, file?: File | null): Promise<SendMessageResponse> {
    const form = new FormData();
    const data = { content: content ?? '' };
    form.append('data', new Blob([JSON.stringify(data)], { type: 'application/json' }));
    if (file) {
      form.append('file', file);
    }
    return http(`${BASE}/dialogs/${dialogId}/messages`, { method: 'POST', body: form });
  },
  getTask(taskId: number): Promise<TaskDto> {
    return http(`${BASE}/tasks/${taskId}`);
  },
  async downloadTaskSubmission(taskId: number): Promise<boolean> {
    const res = await fetch(`${BASE}/tasks/${taskId}/submission`, { headers: { 'Accept': 'text/csv' } });
    if (res.status === 404) return false;
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    const blob = await res.blob();
    const cd = res.headers.get('content-disposition') || '';
    let filename = `submissions_task_${taskId}.csv`;
    const match = /filename\*=UTF-8''([^;]+)|filename="?([^";]+)"?/i.exec(cd || '');
    if (match) {
      const fn = decodeURIComponent(match[1] || match[2] || '').trim();
      if (fn) filename = fn;
    }
    const url = URL.createObjectURL(blob);
    try {
      const a = document.createElement('a');
      a.href = url;
      a.download = filename;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
    } finally {
      URL.revokeObjectURL(url);
    }
    return true;
  }
};
