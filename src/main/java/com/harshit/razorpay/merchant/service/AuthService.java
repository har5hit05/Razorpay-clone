package com.harshit.razorpay.merchant.service;

import com.harshit.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.harshit.razorpay.merchant.dto.response.MerchantResponse;

public interface AuthService {
    MerchantResponse signup(MerchantSignupRequest request);
}
