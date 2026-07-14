package com.harshit.razorpay.merchant.dto.request;

import com.harshit.razorpay.common.enums.Environment;

public record CreateApiKeyRequest(
        Environment environment
) {
}
