package ru.javaboys.vibetraderbackend.agent.ctx;

public final class PromptUidContextHolder {

    private static final ThreadLocal<String> TL = new ThreadLocal<>();

    private PromptUidContextHolder() {}

    public static void set(String promptUid) {
        TL.set(promptUid);
    }

    public static String get() {
        return TL.get();
    }

    public static void clear() {
        TL.remove();
    }
}
