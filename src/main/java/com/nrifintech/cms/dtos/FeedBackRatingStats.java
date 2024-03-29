package com.nrifintech.cms.dtos;

import java.math.BigInteger;

import com.nrifintech.cms.types.Feedback;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FeedBackRatingStats {
    private Feedback feedback;
    private BigInteger count;
}
