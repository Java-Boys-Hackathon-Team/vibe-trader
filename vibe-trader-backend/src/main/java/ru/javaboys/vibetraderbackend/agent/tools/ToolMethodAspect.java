package ru.javaboys.vibetraderbackend.agent.tools;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ToolMethodAspect {
    private final ObjectMapper objectMapper;

    @Around(
            "@annotation(org.springframework.ai.tool.annotation.Tool) && " +
            "within(ru.javaboys.vibetraderbackend.agent.tools..*)"
    )
    public Object aroundTool(ProceedingJoinPoint pjp) throws Throwable {
        String sig = pjp.getSignature().toShortString();
        try {
            Object ret = pjp.proceed();
            String jsonRes = objectMapper.writeValueAsString(ret);
            log.info(">>>>> Calling {} returned {} bytes", sig, jsonRes.length());

            return ret;
        } catch (Throwable ex) {
            log.warn(">>>>> Calling ERR: {} -> {}", sig, ex.toString());
            throw ex;
        }
    }
}