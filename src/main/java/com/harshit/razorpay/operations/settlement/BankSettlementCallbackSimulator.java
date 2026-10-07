package com.harshit.razorpay.operations.settlement;

import com.harshit.razorpay.common.enums.SettlementStatus;
import com.harshit.razorpay.common.util.RandomizerUtil;
import com.harshit.razorpay.operations.entity.Settlement;
import com.harshit.razorpay.operations.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BankSettlementCallbackSimulator {

    private final SettlementRepository settlementRepository;
    private final SettlementTransactionExecutor settlementTransactionExecutor;

    @Scheduled(fixedDelayString = "5000")
    public void processCallback(){
        List<Settlement> settlements = settlementRepository.findByStatus(SettlementStatus.TRANSFER_PENDING);
        if(settlements.isEmpty()) return;

        for(Settlement settlement : settlements){
            simulateCallBack(settlement);
        }
    }

    private void simulateCallBack(Settlement settlement){
        log.info("Initiating settlement callback for settlementId: {}", settlement.getId());
        String utrNumber = "UTH_"+ RandomizerUtil.randomBase64(12);
        settlementTransactionExecutor.resolveTransfer(settlement.getId(), null, null);
    }
}
