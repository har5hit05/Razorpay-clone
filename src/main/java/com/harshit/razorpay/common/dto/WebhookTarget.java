package com.harshit.razorpay.common.dto;

import java.util.UUID;

public record WebhookTarget(
        UUID configId,
        String targetId,
        String targetUrl,
        String webhookSecret
) {
}
