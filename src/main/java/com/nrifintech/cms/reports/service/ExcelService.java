package com.nrifintech.cms.reports.service;

import com.nrifintech.cms.reports.ExcelReportGenerator;
import com.nrifintech.cms.reports.dto.ResourceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ExcelService {

    @Autowired
    private ExcelReportGenerator.UserReport userReport;

    @Autowired
    private ExcelReportGenerator.MenuReport menuReport;

    public ResourceDTO getUserReports(){
        return  userReport.exportUsers();
    }

    public ResourceDTO getMenuReports(){
        return  menuReport.exportMenus();
    }

}