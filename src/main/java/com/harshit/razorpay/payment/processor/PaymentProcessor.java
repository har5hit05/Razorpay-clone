package com.harshit.razorpay.payment.processor;

import com.harshit.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.harshit.razorpay.payment.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);
}
