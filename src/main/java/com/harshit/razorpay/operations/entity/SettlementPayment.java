package com.harshit.razorpay.operations.entity;

import com.harshit.razorpay.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "settlement_payment")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class SettlementPayment {

    @EmbeddedId
    private SettlementPaymentId id;

    @MapsId("settlementId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "settlement_id", nullable = false)
    private Settlement settlementId;
}
