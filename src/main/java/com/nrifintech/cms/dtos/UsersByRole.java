package com.nrifintech.cms.dtos;

import com.nrifintech.cms.types.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UsersByRole is a class that has a Role and a count of users with that role.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsersByRole {
    Role role;
    Object count;
}
