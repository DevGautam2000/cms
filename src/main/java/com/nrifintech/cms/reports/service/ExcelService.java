package com.nrifintech.cms.reports.service;

import com.nrifintech.cms.reports.ExcelReportGenerator;
import com.nrifintech.cms.reports.dto.ResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
/**
 * > This class is a service that provides methods to read and write Excel files
 */
public class ExcelService {

    @Autowired
    private ExcelReportGenerator.UserReport userReport;

    @Autowired
    private ExcelReportGenerator.MenuReport menuReport;

   /**
    * > This function returns a ResourceDTO object that contains a list of users and their reports
    * 
    * @return A ResourceDTO object
    */
    public ResourceDTO getUserReports(){
        return  userReport.exportUsers();
    }

   /**
    * > This function returns a ResourceDTO object that contains a list of all the menus in the
    * database
    * 
    * @return A ResourceDTO object
    */
    public ResourceDTO getMenuReports(){
        return  menuReport.exportMenus();
    }

}