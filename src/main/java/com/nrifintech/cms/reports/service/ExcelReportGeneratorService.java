package com.nrifintech.cms.reports.service;

import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.reports.ExcelReportGenerator;
import com.nrifintech.cms.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExcelReportGeneratorService {

    @Autowired
    private UserService userService;

    public void generateUserReports() throws IOException {

        List<User> users = userService.getUsers();

        ExcelReportGenerator excelReportGenerator = new ExcelReportGenerator();


        excelReportGenerator.generate(users);

    }
}
