package com.harshit.razorpay.operations.webhook;

import com.harshit.razorpay.common.enums.WebhookEventStatus;
import com.harshit.razorpay.operations.entity.WebhookEvent;
import com.harshit.razorpay.operations.repository.WebhookEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebhookDeliveryScheduler {

    private final WebhookRetryQueue retryQueue;
    private final WebhookEventRepository webhookEventRepository;

    @Value("${app.webhook.delivery.poll-batch-size:100}")
    private int batchSize;

    public void pollAndDeliver() {
        Set<UUID> due = retryQueue.pollDue(batchSize);

        if (due.isEmpty()) return;

        for(UUID webhookEventId: due){
            //executor.deliver(webhookEventId)
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void reconcileFromDatabase(){
        LocalDateTime now = LocalDateTime.now();
        List<WebhookEvent> due = webhookEventRepository.findByStatusAndNextRetryAtBefore(WebhookEventStatus.PENDING, now);

        for(WebhookEvent event: due){
            retryQueue.enqueueIfAbsent(event.getId(), event.getNextRetryAt());
        }
    }
}
