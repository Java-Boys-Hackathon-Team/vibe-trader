package ru.javaboys.vibetraderbackend.aop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javaboys.vibetraderbackend.aop.repository.ToolCallingHistoryRepository;
import ru.javaboys.vibetraderbackend.chat.model.ToolCallingHistory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class ToolCallingHistoryService {

    private final ToolCallingHistoryRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordSuccess(String toolName, long tookMs, String result) {
        try {
            BigDecimal durationSec = BigDecimal.valueOf(tookMs)
                    .divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);
            long bytes = result == null ? 0L : result.getBytes(StandardCharsets.UTF_8).length;

            ToolCallingHistory entry = repository.findByToolName(toolName)
                    .map(existing -> {
                        existing.setDurationSec(durationSec);
                        existing.setTotalCalls(safeInc(existing.getTotalCalls()));
                        existing.setTotalSuccess(safeInc(existing.getTotalSuccess()));
                        existing.setSuccess(Boolean.TRUE);
                        existing.setSuccessResult(result);
                        existing.setCumulativeSuccessBytes(safeAdd(existing.getCumulativeSuccessBytes(), bytes));
                        // Reset last error details on success
                        existing.setErrorClass(null);
                        existing.setErrorMessage(null);
                        existing.setErrorStackTrace(null);
                        return existing;
                    })
                    .orElseGet(() -> ToolCallingHistory.builder()
                            .toolName(toolName)
                            .durationSec(durationSec)
                            .totalCalls(1L)
                            .totalSuccess(1L)
                            .totalFailure(0L)
                            .success(Boolean.TRUE)
                            .successResult(result)
                            .cumulativeSuccessBytes(bytes)
                            .build());

            repository.save(entry);
        } catch (Exception e) {
            log.warn("Failed to record tool success history for {}: {}", toolName, e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void recordFailure(String toolName, long tookMs, String errorClass, String errorMessage, String stackTrace) {
        try {
            BigDecimal durationSec = BigDecimal.valueOf(tookMs)
                    .divide(BigDecimal.valueOf(1000), 3, RoundingMode.HALF_UP);

            ToolCallingHistory entry = repository.findByToolName(toolName)
                    .map(existing -> {
                        existing.setDurationSec(durationSec);
                        existing.setTotalCalls(safeInc(existing.getTotalCalls()));
                        existing.setTotalFailure(safeInc(existing.getTotalFailure()));
                        existing.setSuccess(Boolean.FALSE);
                        existing.setErrorClass(errorClass);
                        existing.setErrorMessage(errorMessage);
                        existing.setErrorStackTrace(stackTrace);
                        // Do not change successResult or cumulativeSuccessBytes on failure
                        return existing;
                    })
                    .orElseGet(() -> ToolCallingHistory.builder()
                            .toolName(toolName)
                            .durationSec(durationSec)
                            .totalCalls(1L)
                            .totalSuccess(0L)
                            .totalFailure(1L)
                            .success(Boolean.FALSE)
                            .successResult(null)
                            .cumulativeSuccessBytes(0L)
                            .errorClass(errorClass)
                            .errorMessage(errorMessage)
                            .errorStackTrace(stackTrace)
                            .build());

            repository.save(entry);
        } catch (Exception e) {
            log.warn("Failed to record tool failure history for {}: {}", toolName, e.getMessage());
        }
    }

    private long safeInc(Long v) {
        return v == null ? 1L : v + 1;
    }

    private long safeAdd(Long a, long b) {
        return (a == null ? 0L : a) + b;
    }
}
