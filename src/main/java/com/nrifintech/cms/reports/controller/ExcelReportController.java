package com.nrifintech.cms.reports.controller;

import com.nrifintech.cms.reports.dto.ResourceDTO;
import com.nrifintech.cms.reports.service.ExcelReportGeneratorService;
import com.nrifintech.cms.reports.service.ExcelService;
import com.nrifintech.cms.types.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/excel/")
public class ExcelReportController {

    @Autowired
    private ExcelService excelService;
    @GetMapping("/getuserreports")
    public ResponseEntity<Object> getUserReports() throws IOException {
        ResourceDTO resourceDTO=excelService.exportUsers();

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-Disposition",
                "attachment; filename="+"User.xlsx");

        return ResponseEntity.ok()
                .contentLength(resourceDTO.getResource().contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .headers(httpHeaders)

                .body(new InputStreamResource(resourceDTO.getResource().getInputStream()));

    }
}
