package com.nrifintech.cms.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to send an email to the user after a transaction is completed
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class WalletEmailResponse {
    private String username;
    private Double currentBalance;
    private Integer moneyAdded;
    private String transactionId;
}
