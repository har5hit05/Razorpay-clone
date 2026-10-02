package com.harshit.razorpay.payment.outbox;

import com.harshit.razorpay.common.enums.OutboxStatus;
import com.harshit.razorpay.payment.entity.OutboxEvent;
import com.harshit.razorpay.payment.repository.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OutboxResultHandler {

    private final OutboxEventRepository outboxEventRepository;
    private final Integer MAX_ATTEMPTS = 3;

    @Transactional
    public void handleEventPublished(OutboxEvent event){
        event.setStatus(OutboxStatus.PUBLISHED);
        event.setPublishedAt(LocalDateTime.now());
        outboxEventRepository.save(event);
    }

    @Transactional
    public void handleEventFailed(OutboxEvent event, String errorMessage) {
        event.setAttempts(event.getAttempts()+1);
        event.setLastError(
                errorMessage.length() < 1000 ? errorMessage : errorMessage.substring(0, 1000));
        if(event.getAttempts() >= MAX_ATTEMPTS){
            event.setStatus(OutboxStatus.FAILED);
        }

        outboxEventRepository.save(event);
    }
}
