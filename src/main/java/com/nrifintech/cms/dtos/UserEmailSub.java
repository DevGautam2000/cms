package com.nrifintech.cms.dtos;

import javax.annotation.sql.DataSourceDefinition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEmailSub {
    private int id;
    private String username;
}
