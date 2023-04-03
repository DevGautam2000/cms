package com.nrifintech.cms.dtos;

import java.util.HashMap;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * It's a model class that holds the data for an email
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@RequiredArgsConstructor
public class EmailModel {

    private static final String FROM = "cmsnriftb2@gmail.com";
    @NonNull
    private List<String> to;
    @NonNull
    private String subject;
    @NonNull
    private HashMap<String,String> body;
    @NonNull
    private String templateUsed;
    private String embeddedHTML;

    public String getFrom() {
        return EmailModel.FROM;
    }
}
