package com.harshit.razorpay.payment.service;

import com.harshit.razorpay.payment.dto.request.CreateOrderRequest;
import com.harshit.razorpay.payment.dto.response.OrderResponse;
import com.harshit.razorpay.payment.dto.response.PaymentResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    OrderResponse create(UUID merchantId, CreateOrderRequest request);

    OrderResponse getById(UUID merchantId, UUID orderId);

    OrderResponse cancel(UUID merchantId, UUID orderid);

    List<PaymentResponse> listPayments(UUID merchantId, UUID orderId);
}
