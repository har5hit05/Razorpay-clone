package com.harshit.razorpay.merchant.service.impl;

import com.harshit.razorpay.common.enums.MerchantStatus;
import com.harshit.razorpay.common.enums.UserRole;
import com.harshit.razorpay.common.exception.DuplicateResourceException;
import com.harshit.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.harshit.razorpay.merchant.dto.response.MerchantResponse;
import com.harshit.razorpay.merchant.entity.AppUser;
import com.harshit.razorpay.merchant.entity.Merchant;
import com.harshit.razorpay.merchant.mapper.MerchantMapper;
import com.harshit.razorpay.merchant.repository.AppUserRepository;
import com.harshit.razorpay.merchant.repository.MerchantRepository;
import com.harshit.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request){
        if(merchantRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL", "Merchant with email already exists: "+request.email());
        }

//        Merchant merchant = Merchant.builder()
//                .businessType(request.businessType())
//                .name(request.name())
//                .email(request.email())
//                .status(MerchantStatus.PENDING_KYC)
//                .build();

        Merchant merchant = merchantMapper.toEntityFromSignUpRequest(request);
        merchant.setStatus(MerchantStatus.PENDING_KYC);

        merchant = merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(request.password())       //TODO: encrypt using bcrypt
                .role(UserRole.OWNER)
                .build();

        appUserRepository.save(appUser);

//        return new MerchantResponse(merchant.getId(), merchant.getName(),
//                merchant.getEmail(), merchant.getBusinessName(),
//                merchant.getBusinessType(), merchant.getStatus());

        return merchantMapper.toResponse(merchant);
    }
}