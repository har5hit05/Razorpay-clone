package com.harshit.razorpay.merchant.dto.response;

import com.harshit.razorpay.common.enums.BusinessType;
import com.harshit.razorpay.common.enums.MerchantStatus;

import java.util.UUID;

public record MerchantResponse(
        UUID id,
        String name ,
        String email,
        String businessName,
        BusinessType businessType,
        MerchantStatus merchantStatus
) {

}
