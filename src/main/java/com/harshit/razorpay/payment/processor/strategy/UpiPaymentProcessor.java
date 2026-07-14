package com.harshit.razorpay.payment.processor.strategy;

import com.harshit.razorpay.common.util.RandomizerUtil;
import com.harshit.razorpay.payment.processor.PaymentProcessor;
import com.harshit.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.harshit.razorpay.payment.processor.dto.PaymentProcessorResponse;

public class UpiPaymentProcessor implements PaymentProcessor {

    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request){
        final String VPA_CODE_FAIL = "fail@okaxis";

        String bankCode = request.methodDetails() != null ?
                request.methodDetails().get("vpa").toString() : null;

        //simulation
        if(VPA_CODE_FAIL.equals(bankCode)){
            return new PaymentProcessorResponse.Failure("BANK_REJECTED",
                    "Bank rejected the transaction registration");
        }

        String processorRef = "UPI_PROCESSOR" + RandomizerUtil.randomBase64(16);

//        String bankRef = "BANK_REF" + RandomizerUtil.randomBase64(16);

        return new PaymentProcessorResponse.Pending(processorRef);
    }
}
