package com.harshit.razorpay.common.audit;

import com.harshit.razorpay.merchant.security.MerchantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuditAwareImpl implements AuditorAware<String> {

    private final MerchantContext merchantContext;

    @Override
    public Optional<String> getCurrentAuditor() {

        try {
            String keyId = merchantContext.getKeyId();
            if(keyId != null && !keyId.isBlank()) return Optional.of(keyId);

            UUID merchantId = merchantContext.getMerchantId();

            if(merchantId != null){
                return Optional.of("merchant_id " + merchantContext.getMerchantId());
            }

        } catch (Exception ignored) {
        }

        return Optional.of("SYSTEM");
    }
}
