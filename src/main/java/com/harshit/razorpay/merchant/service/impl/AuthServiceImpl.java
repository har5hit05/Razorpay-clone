package com.harshit.razorpay.merchant.service.impl;

import com.harshit.razorpay.common.enums.MerchantStatus;
import com.harshit.razorpay.common.enums.UserRole;
import com.harshit.razorpay.common.exception.DuplicateResourceException;
import com.harshit.razorpay.common.exception.ResourceNotFoundException;
import com.harshit.razorpay.merchant.dto.request.LoginRequest;
import com.harshit.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.harshit.razorpay.merchant.dto.response.LoginResponse;
import com.harshit.razorpay.merchant.dto.response.MerchantResponse;
import com.harshit.razorpay.merchant.entity.AppUser;
import com.harshit.razorpay.merchant.entity.Merchant;
import com.harshit.razorpay.merchant.mapper.MerchantMapper;
import com.harshit.razorpay.merchant.repository.AppUserRepository;
import com.harshit.razorpay.merchant.repository.MerchantRepository;
import com.harshit.razorpay.merchant.security.JwtUtil;
import com.harshit.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

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
                .passwordHash(passwordEncoder.encode(request.password()))       //TODO: encrypt using bcrypt
                .role(UserRole.OWNER)
                .build();

        appUserRepository.save(appUser);

//        return new MerchantResponse(merchant.getId(), merchant.getName(),
//                merchant.getEmail(), merchant.getBusinessName(),
//                merchant.getBusinessType(), merchant.getStatus());

        return merchantMapper.toResponse(merchant);
    }

    public LoginResponse login(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        AppUser appUser = appUserRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("User", request.email()));

        String token = jwtUtil.generateAccessToken(request.email(), appUser.getMerchant().getId(), appUser.getRole().toString());

        return new LoginResponse(token);
    }
}