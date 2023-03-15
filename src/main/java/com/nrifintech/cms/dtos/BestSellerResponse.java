package com.nrifintech.cms.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BestSellerResponse {
    private String date;
    private int count;
}
