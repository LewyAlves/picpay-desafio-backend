package com.picpay.api.dto;

import java.math.BigDecimal;

public record TransactionDto(
    BigDecimal value,
    Long sanderId,
    Long receiverId
) {
}
