package com.harshit.razorpay.operations.settlement;

import com.harshit.razorpay.common.entity.Money;
import com.harshit.razorpay.common.enums.SettlementStatus;
import com.harshit.razorpay.operations.entity.Settlement;
import com.harshit.razorpay.operations.repository.SettlementRepository;
import com.harshit.razorpay.payment.api.PaymentLookupService;
import com.harshit.razorpay.payment.entity.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SettlementTransactionExecutor {

    private static final double FEE_RATE = 0.02;
    private static final double GST_RATE = 0.18;

    private final PaymentLookupService paymentLookupService;
    private final SettlementRepository settlementRepository;

    @Transactional
    public void processForMerchant(UUID merchantId, LocalDate settlementDate){

        List<Payment> unsettledPayments = paymentLookupService.findUnsettledCapturedPayments(merchantId);
        if(unsettledPayments.isEmpty()) return;

        Money gross = unsettledPayments.stream()
                .map(Payment::getAmount)
                .reduce(Money::add)
                .orElseThrow();

        int fee = Math.toIntExact(Math.round(gross.getAmountUnits() * FEE_RATE));
        int gst = Math.toIntExact(Math.round(fee * GST_RATE));
        Money feeAmount = Money.of(fee, gross.getCurrency());
        Money gstAmount = Money.of(gst, gross.getCurrency());
        Money netAmount = gross.subtract(feeAmount.subtract(gstAmount));

        Settlement settlement = Settlement.builder()
                .merchantId(merchantId)
                .grossAmount(gross)
                .feeAmount(feeAmount)
                .gstAmount(gstAmount)
                .netAmount(netAmount)
                .status(SettlementStatus.INITIATED)
                .build();

        settlementRepository.save(settlement);
    }
}
