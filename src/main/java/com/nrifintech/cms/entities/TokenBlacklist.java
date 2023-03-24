package com.nrifintech.cms.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TokenBlacklist {
    @Id
    private String token;

        
}
