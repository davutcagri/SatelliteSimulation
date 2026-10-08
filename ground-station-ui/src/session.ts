const SESSION_STORAGE_KEY = 'satsim-session-id';

function readOrCreateSessionId(): string {
  try {
    const existing = window.sessionStorage.getItem(SESSION_STORAGE_KEY);
    if (existing) return existing;
    const created = crypto.randomUUID();
    window.sessionStorage.setItem(SESSION_STORAGE_KEY, created);
    return created;
  } catch {
    return crypto.randomUUID();
  }
}

export const SESSION_ID = readOrCreateSessionId();
