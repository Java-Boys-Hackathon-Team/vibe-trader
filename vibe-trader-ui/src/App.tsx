import {useEffect, useMemo, useRef, useState} from 'react'
import type {ChatMessageDto, DialogDto, TaskDto} from './api'
import {api} from './api'
import './styles.css'

function Thinking() {
    return (
        <div className="thinking" aria-live="polite" aria-busy>
            <span className="dot"></span>
            <span className="dot"></span>
            <span className="dot"></span>
            <span>Агент размышляет…</span>
        </div>
    )
}

function App() {
    const [dialogs, setDialogs] = useState<DialogDto[]>([])
    const [sidebarOpen, setSidebarOpen] = useState<boolean>(true)
    const [activeId, setActiveId] = useState<number | null>(null)
    const [messages, setMessages] = useState<ChatMessageDto[]>([])
    const [loadingDialogs, setLoadingDialogs] = useState(false)
    const [loadingMessages, setLoadingMessages] = useState(false)
    const [input, setInput] = useState('')
    const [showModal, setShowModal] = useState(false)
    const [newTitle, setNewTitle] = useState('')
    const [error, setError] = useState<string | null>(null)
    const [file, setFile] = useState<File | null>(null)

    // task polling state
    const [pendingTask, setPendingTask] = useState<TaskDto | null>(null)
    const pollTimer = useRef<number | null>(null)
    const messagesRef = useRef<HTMLDivElement | null>(null)
    const shouldStickToBottomRef = useRef<boolean>(true)

    const activeDialog = useMemo(() => dialogs.find(d => d.id === activeId) || null, [dialogs, activeId])

    useEffect(() => {
        // Load dialogs on mount
        setLoadingDialogs(true)
        api.getDialogs()
            .then(list => {
                setDialogs(list)
                if (list.length && activeId === null) setActiveId(list[0].id)
            })
            .catch(e => setError(e.message))
            .finally(() => setLoadingDialogs(false))
    }, [])

    useEffect(() => {
        // Auto-collapse sidebar on small screens by default
        const mq = window.matchMedia('(max-width: 768px)')
        if (mq.matches) setSidebarOpen(false)
        const onChange = (e: MediaQueryListEvent) => setSidebarOpen(!e.matches)
        mq.addEventListener?.('change', onChange)
        return () => mq.removeEventListener?.('change', onChange)
    }, [])

    useEffect(() => {
        // Load messages when dialog changes
        if (activeId == null) return
        setLoadingMessages(true)
        api.getMessages(activeId)
            .then(m => {
                setMessages(m)
                // jump to bottom on initial load of a dialog
                queueMicrotask(() => {
                    const el = messagesRef.current
                    if (el) {
                        el.scrollTop = el.scrollHeight
                        shouldStickToBottomRef.current = true
                    }
                })
                checkPendingFromMessages(m)
            })
            .catch(e => setError(e.message))
            .finally(() => setLoadingMessages(false))
    }, [activeId])

    // Keep view at bottom when new messages arrive if user was at bottom
    useEffect(() => {
        const el = messagesRef.current
        if (!el) return
        if (shouldStickToBottomRef.current) {
            el.scrollTop = el.scrollHeight
        }
    }, [messages.length, pendingTask?.status])

    function checkPendingFromMessages(msgs: ChatMessageDto[]) {
        // If last message is from USER, find its task and start polling
        if (!msgs?.length) {
            stopPolling();
            setPendingTask(null);
            return
        }
        const last = msgs[msgs.length - 1]
        if (last.role === 'USER' && last.taskId) {
            startPolling(last.taskId)
        } else {
            stopPolling()
            setPendingTask(null)
        }
    }

    function startPolling(taskId: number) {
        stopPolling()
        const run = async () => {
            try {
                const t = await api.getTask(taskId)
                setPendingTask(t)
                if (t.status === 'DONE' || t.status === 'ERROR') {
                    stopPolling()
                    // refresh messages to include assistant reply or show error
                    if (activeId != null) {
                        const m = await api.getMessages(activeId)
                        setMessages(m)
                    }
                    // Try to download CSV submissions when task is DONE
                    if (t.status === 'DONE') {
                        try {
                            await api.downloadTaskSubmission(taskId)
                        } catch (err) {
                            const msg = err instanceof Error ? err.message : String(err)
                            setError(`Ошибка при загрузке CSV: ${msg}`)
                        }
                    }
                }
            } catch (e: unknown) {
                const msg = e instanceof Error ? e.message : String(e)
                setError(msg)
            }
        }
        run()
        pollTimer.current = window.setInterval(run, 2000)
    }

    function stopPolling() {
        if (pollTimer.current) {
            clearInterval(pollTimer.current)
            pollTimer.current = null
        }
    }

    async function onSend() {
        if ((activeId == null) || (!input.trim() && !file)) return
        const content = input.trim()
        setInput('')
        const pickedFile = file
        setFile(null)
        try {
            const res = await api.sendMessage(activeId, content, pickedFile || undefined)
            // Optimistically add user message (content may be empty if only file was sent)
            setMessages(prev => [...prev, {
                id: res.userMessageId,
                dialogId: activeId,
                taskId: res.taskId,
                role: 'USER',
                content: content,
                createdAt: new Date().toISOString()
            }])
            startPolling(res.taskId)
        } catch (e: unknown) {
            const msg = e instanceof Error ? e.message : String(e)
            setError(msg)
        }
    }

    async function onCreateDialog() {
        const title = newTitle.trim() || 'Новый диалог'
        try {
            const d = await api.createDialog(title)
            setDialogs(prev => [d, ...prev])
            setActiveId(d.id)
            setShowModal(false)
            setNewTitle('')
            setMessages([])
            setPendingTask(null)
            stopPolling()
        } catch (e: unknown) {
            const msg = e instanceof Error ? e.message : String(e)
            setError(msg)
        }
    }

    return (
        <div className={`app ${sidebarOpen ? '' : 'collapsed'}`}>
            <div className={`sidebar ${sidebarOpen ? '' : 'collapsed'} ${sidebarOpen ? 'shown' : 'hidden'}`}>
                <div className="sidebar-header">
                    <div style={{display: 'flex', gap: 8, alignItems: 'center'}}>
                        <div style={{fontSize: 14, color: 'var(--muted)'}}>Диалоги</div>
                    </div>
                </div>
                <div className="dialogs">
                    <button className="btn new-dialog" onClick={() => setShowModal(true)}>Новый диалог</button>
                    {loadingDialogs && <div className="dialog">Загрузка…</div>}
                    {dialogs.map(d => (
                        <div key={d.id} className={`dialog ${activeId === d.id ? 'active' : ''}`}
                             onClick={() => setActiveId(d.id)}>
                            <div className="dialog-title">{d.title}</div>
                        </div>
                    ))}
                    {dialogs.length === 0 && !loadingDialogs && (
                        <div className="dialog" style={{color: 'var(--muted)'}}>Диалогов пока нет</div>
                    )}
                </div>
            </div>

            <div className="chat">
                <div className="messages" ref={messagesRef} onScroll={(e) => {
                    const el = e.currentTarget
                    const atBottom = el.scrollHeight - el.scrollTop - el.clientHeight < 40
                    shouldStickToBottomRef.current = atBottom
                }}>
                    {activeDialog == null && (
                        <div className="message"
                             style={{textAlign: 'center', background: 'transparent', border: 'none'}}>
                            Выберите диалог или создайте новый
                        </div>
                    )}
                    {activeDialog && (
                        <>
                            {messages.map(m => (
                                <div key={m.id} className={`message ${m.role === 'USER' ? 'user' : 'assistant'}`}>
                                    <div className="role">{m.role === 'USER' ? 'Вы' : 'AI агент'}</div>
                                    <div>{m.content}</div>
                                </div>
                            ))}
                            {pendingTask && pendingTask.status === 'RUNNING' && (
                                <div className="message assistant">
                                    <Thinking/>
                                </div>
                            )}
                            {pendingTask && pendingTask.status === 'ERROR' && (
                                <div className="message assistant">
                                    Ошибка агента: {pendingTask.errorMessage || 'неизвестная ошибка'}
                                </div>
                            )}
                            {loadingMessages && (
                                <div className="message" style={{textAlign: 'center', background: 'transparent'}}>
                                    Загрузка сообщений…
                                </div>
                            )}
                        </>
                    )}
                </div>
                <div className="input-bar">
                    <div className="input">
                        <div className="file-input-wrapper">
                            <label className="btn file-btn">
                                Выбрать CSV
                                <input
                                    type="file"
                                    accept=".csv,text/csv"
                                    onChange={(e) => {
                                        const f = e.target.files?.[0] || null
                                        if (f && !f.name.toLowerCase().endsWith('.csv')) {
                                            setError('Пожалуйста, выберите CSV файл (.csv)')
                                            e.currentTarget.value = ''
                                            setFile(null)
                                            return
                                        }
                                        setFile(f || null)
                                    }}
                                    style={{ display: 'none' }}
                                />
                            </label>
                            {file && (
                                <div className="file-chip" title={file.name}>
                                    <span className="file-name">{file.name}</span>
                                    <button className="chip-close" onClick={() => setFile(null)} aria-label="Убрать файл">×</button>
                                </div>
                            )}
                        </div>
                        <textarea className="textarea" placeholder="Введите сообщение…" value={input}
                                  onChange={e => setInput(e.target.value)} onKeyDown={e => {
                            if (e.key === 'Enter' && !e.shiftKey) {
                                e.preventDefault();
                                onSend();
                            }
                        }}/>
                        <button className="btn send-btn" onClick={onSend}
                                disabled={!activeDialog || (!input.trim() && !file)}>Отправить
                        </button>
                    </div>
                </div>
            </div>
            
            {/* Persistent top-left burger toggle */}
            <button className="btn top-left-toggle" onClick={() => setSidebarOpen(s => !s)} aria-label="Открыть/закрыть панель">
                ☰
            </button>
            
            {showModal && (
                <div className="modal-backdrop" onClick={() => setShowModal(false)}>
                    <div className="modal" onClick={e => e.stopPropagation()}>
                        <h3>Новый диалог</h3>
                        <div className="row">
                            <input placeholder="Название диалога" value={newTitle}
                                   onChange={e => setNewTitle(e.target.value)}/>
                            <button className="btn" onClick={onCreateDialog}>Создать</button>
                        </div>
                    </div>
                </div>
            )}

            <div className="theme-toggle" aria-hidden>
                {/* Placeholder for future theme toggle */}
            </div>

            {error && (
                <div style={{
                    position: 'fixed',
                    bottom: 12,
                    right: 12,
                    background: '#2a2030',
                    border: '1px solid var(--border)',
                    padding: '10px 12px',
                    borderRadius: 8
                }} onClick={() => setError(null)}>
                    {String(error)}
                </div>
            )}
        </div>
    )
}

export default App
