package com.harshit.razorpay.merchant.service;

import com.harshit.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.harshit.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.harshit.razorpay.merchant.dto.response.ApiKeyResponse;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse create(UUID merchantId, @Valid CreateApiKeyRequest request);

    List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);
}
