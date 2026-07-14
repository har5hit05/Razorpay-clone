package com.harshit.razorpay.merchant.mapper;

import com.harshit.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.harshit.razorpay.merchant.dto.response.MerchantResponse;
import com.harshit.razorpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {
    Merchant toEntityFromSignUpRequest(MerchantSignupRequest request);

    MerchantResponse toResponse(Merchant merchant);
}
