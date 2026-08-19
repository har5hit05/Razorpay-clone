package com.harshit.razorpay.merchant.service;

import com.harshit.razorpay.merchant.dto.request.LoginRequest;
import com.harshit.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.harshit.razorpay.merchant.dto.response.LoginResponse;
import com.harshit.razorpay.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {
    MerchantResponse signup(MerchantSignupRequest request);

    LoginResponse login(LoginRequest request);
}
