package com.harshit.razorpay.operations.entity;

import com.harshit.razorpay.common.entity.BaseEntity;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.UUID;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class SettlementPaymentId {

    private UUID settlementId;
    private UUID paymentId;
}
