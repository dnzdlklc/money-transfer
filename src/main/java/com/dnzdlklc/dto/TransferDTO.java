package com.dnzdlklc.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by denizdalkilic on 2019-11-19 @ 16:53.
 */
@Data
public class TransferDTO {
    private String typeCode;
    private Long fromAccount;
    private Long toAccount;
    private BigDecimal amount;
}
