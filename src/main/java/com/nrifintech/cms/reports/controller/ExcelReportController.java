package com.nrifintech.cms.reports.controller;

import com.nrifintech.cms.reports.dto.ResourceDTO;
import com.nrifintech.cms.reports.service.ExcelService;
import com.nrifintech.cms.routes.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This class is a Spring Boot controller that handles requests to the `/excel` endpoint
 */
@CrossOrigin
@RestController
@RequestMapping(Route.Excel.prefix)
public class ExcelReportController {


    @Autowired
    private ExcelService excelService;

   /**
    * It returns a list of users in the form of an excel file.
    * 
    * @return ResponseEntity<Object>
    */
    @GetMapping(Route.Excel.getUserReports)
    public ResponseEntity<Object> getUserReports() throws IOException {
        ResourceDTO resourceDTO=excelService.getUserReports();
        return getObjectResponseEntity(resourceDTO,"users");

    }

    /**
     * It returns a ResponseEntity with the content-type set to application/vnd.ms-excel and the
     * content-disposition set to attachment; filename=filename_reports_date.xlsx
     * 
     * @param resourceDTO This is the object that contains the file that we want to download.
     * @param filename The name of the file that will be downloaded.
     * @return A ResponseEntity object is being returned.
     */
    private ResponseEntity<Object> getObjectResponseEntity(ResourceDTO resourceDTO,String filename) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        HttpHeaders httpHeaders=new HttpHeaders();
        httpHeaders.add("Content-Disposition",
                "attachment; filename="+filename+"_reports"+ dtf.format(now) +".xlsx");

        return ResponseEntity.ok()
                .contentLength(resourceDTO.getResource().contentLength())
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .headers(httpHeaders)

                .body(new InputStreamResource(resourceDTO.getResource().getInputStream()));
    }


    /**
     * It returns a list of menu items in the form of an excel file.
     * 
     * @return A ResponseEntity object is being returned.
     */
    @GetMapping(Route.Excel.getMenuReports)
    public ResponseEntity<Object> getMenuReports() throws IOException {
        ResourceDTO resourceDTO=excelService.getMenuReports();
        return getObjectResponseEntity(resourceDTO,"menus");

    }
}
