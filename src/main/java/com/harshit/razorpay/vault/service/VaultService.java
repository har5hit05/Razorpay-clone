package com.harshit.razorpay.vault.service;

import com.harshit.razorpay.common.entity.Money;
import com.harshit.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.harshit.razorpay.vault.dto.request.TokenizeRequest;
import com.harshit.razorpay.vault.dto.response.TokenizeResponse;

import java.util.Map;
import java.util.UUID;

public interface VaultService {
    TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId);

    PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails);
}
