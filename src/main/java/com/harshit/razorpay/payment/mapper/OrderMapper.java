package com.harshit.razorpay.payment.mapper;

import com.harshit.razorpay.payment.dto.response.OrderResponse;
import com.harshit.razorpay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderResponse toResponse(OrderRecord orderRecord);
}
