package ru.javaboys.vibetraderbackend.agent.ctx;

import ru.javaboys.vibetraderbackend.chat.model.ChatMessage;

public final class AssistantMessageContextHolder {

    private static final ThreadLocal<ChatMessage> TL = new ThreadLocal<>();

    private AssistantMessageContextHolder() {}

    public static void set(ChatMessage message) {
        TL.set(message);
    }

    public static ChatMessage get() {
        return TL.get();
    }

    public static void clear() {
        TL.remove();
    }
}
