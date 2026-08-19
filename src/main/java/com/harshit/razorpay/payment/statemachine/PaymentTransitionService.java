package com.harshit.razorpay.payment.statemachine;

import com.harshit.razorpay.common.enums.PaymentActor;
import com.harshit.razorpay.common.enums.PaymentEvent;
import com.harshit.razorpay.common.enums.PaymentStatus;
import com.harshit.razorpay.payment.entity.Payment;
import com.harshit.razorpay.payment.entity.PaymentTransitionLog;
import com.harshit.razorpay.payment.repository.PaymentTransitionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentTransitionService {

    private final PaymentTransitionLogRepository paymentTransitionRepository;
    private final PaymentStateMachine paymentStateMachine;

    public PaymentStatus apply(Payment payment, PaymentEvent event){
        PaymentStatus next = paymentStateMachine.transition(payment.getStatus(), event);

        PaymentTransitionLog log = PaymentTransitionLog.builder()
                .payment(payment)
                .fromStatus(payment.getStatus())
                .event(event)
                .toStatus(next)
                .actor(PaymentActor.SYSTEM)
                .occurredAt(LocalDateTime.now())
                .build();

        payment.setStatus(next);

        paymentTransitionRepository.save(log);
        return next;
    }
}
