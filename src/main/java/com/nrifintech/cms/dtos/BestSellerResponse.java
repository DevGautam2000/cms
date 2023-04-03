package com.nrifintech.cms.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is a POJO that represents the response from the best seller API.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BestSellerResponse {
    private String name;
    private String imagePath;
    private Object count;
}
