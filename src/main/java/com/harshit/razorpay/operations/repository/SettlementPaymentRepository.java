package com.harshit.razorpay.operations.repository;

import com.harshit.razorpay.operations.entity.SettlementPayment;
import com.harshit.razorpay.operations.entity.SettlementPaymentId;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SettlementPaymentRepository extends JpaRepository<SettlementPayment, SettlementPaymentId> {
}
