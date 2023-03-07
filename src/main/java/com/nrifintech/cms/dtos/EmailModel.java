package com.nrifintech.cms.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmailModel {
    private final String from = "cmsbatch2@gmail.com";
    private String to;
    private String subject;
    private String body;
    private String time;
}
