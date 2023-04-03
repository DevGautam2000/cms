package com.nrifintech.cms.dtos;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FeedbackStats is a class that has a totalCount, avgRating, and a list of feedbackRaingStats.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedbackStats {
    private Object totalCount;
    private Object avgRating;
    private List<FeedBackRatingStats> feedbackRaingStats;
}
