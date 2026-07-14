package com.harshit.razorpay.operations.entity;

import com.harshit.razorpay.common.entity.BaseEntity;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class SettlementPaymentId {

    private UUID settlementId;
    private UUID paymentId;
}
