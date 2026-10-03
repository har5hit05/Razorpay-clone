package com.harshit.razorpay.operations.webhook;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.harshit.razorpay.common.dto.WebhookTarget;
import com.harshit.razorpay.common.enums.WebhookEventStatus;
import com.harshit.razorpay.common.util.SignerUtil;
import com.harshit.razorpay.merchant.api.MerchantWebhookApi;
import com.harshit.razorpay.operations.entity.WebhookEvent;
import com.harshit.razorpay.operations.repository.WebhookEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebhookKafkaConsumer {

    private final MerchantWebhookApi merchantWebhookApi;
    private final ObjectMapper objectMapper;
    private final SignerUtil signerUtil;
    private final WebhookEventRepository webhookEventRepository;
    private final WebhookRetryQueue retryQueue;

    @KafkaListener(topics = {
            "${app.kafka.topics.payments:payments.events}",
            "${app.kafka.topics.orders:orders.events}",
            "${app.kafka.topics.refunds:refunds.events}",
            "${app.kafka.topics.settlements:settlements.events}"
    })
    public void onWebhookEvent(ConsumerRecord<String, Map<String, Object>> record, Acknowledgment ack) throws JsonProcessingException {
        try {
            Map<String, Object> envelope = record.value();

            Map<String, Object> data = (Map<String, Object>) envelope.get("data");
            String eventType = (String) envelope.get("eventType");

            Object merchantIdRaw = data.get("merchantId");
            if (merchantIdRaw == null) {
                log.warn("No merchantId was found, skipping event: {}", eventType);
                ack.acknowledge();
                return;
            }

            UUID merchantId = UUID.fromString(merchantIdRaw.toString());

            List<WebhookTarget> targets = merchantWebhookApi.getActiveConfigForEvent(merchantId, eventType);
            if (targets.isEmpty()) {
                log.debug("No webhook target was found, skipping event: {}", eventType);
                ack.acknowledge();
                return;
            }

            Map<String, Object> signatureData = Map.of("event", eventType, "payload", data);
            String signatureJson = objectMapper.writeValueAsString(signatureData);

            for (WebhookTarget target : targets) {
                String signature = signerUtil.sign(signatureJson, target.webhookSecret());

                WebhookEvent webhookEvent = WebhookEvent.builder()
                        .merchantId(merchantId)
                        .eventType(eventType)
                        .payload(data)
                        .targetUrl(target.targetUrl())
                        .signature(signature)
                        .status(WebhookEventStatus.PENDING)
                        .nextRetryAt(LocalDateTime.now())
                        .build();

                webhookEvent = webhookEventRepository.save(webhookEvent);

                //Redis - enqueue
                retryQueue.enqueue(webhookEvent.getId(), webhookEvent.getNextRetryAt());
            }

            ack.acknowledge();
        } catch (Exception e){
            log.error("Webhook consumer failed to process the record, offset:{}", record.offset());

            //TODO: check exceptions for acknoledging
        }
    }
}
