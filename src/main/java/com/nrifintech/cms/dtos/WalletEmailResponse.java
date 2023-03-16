package com.nrifintech.cms.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class WalletEmailResponse {
    private String username;
    private Double currentBalance;
    private Integer moneyAdded;
    private String transactionId;
}
