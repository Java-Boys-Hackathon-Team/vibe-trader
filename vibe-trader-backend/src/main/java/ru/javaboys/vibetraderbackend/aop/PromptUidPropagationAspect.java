package ru.javaboys.vibetraderbackend.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.javaboys.vibetraderbackend.agent.ctx.PromptUidContextHolder;

@Aspect
@Component
public class PromptUidPropagationAspect {

    @Around("@annotation(org.springframework.ai.tool.annotation.Tool)")
    public Object propagatePromptUid(ProceedingJoinPoint pjp) throws Throwable {
        String backup = PromptUidContextHolder.get();
        String candidate = extractPromptUid(pjp.getArgs());
        if (candidate != null && !candidate.isBlank()) {
            PromptUidContextHolder.set(candidate);
        }
        try {
            return pjp.proceed();
        } finally {
            if (backup == null) {
                PromptUidContextHolder.clear();
            } else {
                PromptUidContextHolder.set(backup);
            }
        }
    }

    private String extractPromptUid(Object[] args) {
        if (args == null || args.length == 0) return null;
        Object first = args[0];
        if (first instanceof String s) return s;
        for (Object a : args) {
            if (a instanceof String s) return s;
        }
        return null;
    }
}
