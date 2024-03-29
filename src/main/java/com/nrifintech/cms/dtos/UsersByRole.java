package com.nrifintech.cms.dtos;

import java.math.BigInteger;

import com.nrifintech.cms.types.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsersByRole {
    Role role;
    BigInteger count;
}
