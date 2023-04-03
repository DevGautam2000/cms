package com.nrifintech.cms.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AnalyticsResponseDate is a class that has two fields: date and value
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AnalyticsResponseDate {
    private String date;
    private Object value;
}
