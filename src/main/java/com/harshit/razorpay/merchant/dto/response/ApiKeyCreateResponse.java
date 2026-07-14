package com.harshit.razorpay.merchant.dto.response;

import com.harshit.razorpay.common.enums.Environment;

import java.util.UUID;

public record ApiKeyCreateResponse (
        UUID id,
        String keyId,
        String keySecret,
        Environment environment
) {
}
