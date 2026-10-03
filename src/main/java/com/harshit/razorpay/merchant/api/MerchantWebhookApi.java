package com.harshit.razorpay.merchant.api;

import com.harshit.razorpay.common.dto.WebhookTarget;

import java.util.List;
import java.util.UUID;

public interface MerchantWebhookApi {
    List<WebhookTarget> getActiveConfigForEvent(UUID merchantId, String eventType);
}
