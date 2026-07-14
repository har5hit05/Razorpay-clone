package com.harshit.razorpay.payment.dto.request;

import com.harshit.razorpay.common.entity.Money;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Map;

public record CreateOrderRequest(

        @NotNull(message = "Amount is required")
        Money amount,

        @Size(max = 100)
        String receipt,     // order-id (known to merchant)

        Map<String, Object> notes,      //extra info like user_phone, user_address, orderId etc

        LocalDateTime expiresAt
) {
}
