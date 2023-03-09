package com.nrifintech.cms.dtos;

import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@RequiredArgsConstructor
public class EmailModel {
    private final String from = "cmsbatch2@gmail.com";
    @NonNull
    private List<String> to;
    @NonNull
    private String subject;
    @NonNull
    private HashMap<String,String> body;
    @NonNull
    private String templateUsed;
    private String embeddedHTML;
}
