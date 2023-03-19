package com.nrifintech.cms.dtos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedbackStats {
    private BigInteger totalCount;
    private BigDecimal avgRating;
    private List<FeedBackRatingStats> feedbackRaingStats;
}
