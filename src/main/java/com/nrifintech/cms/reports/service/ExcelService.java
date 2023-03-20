package com.nrifintech.cms.reports.service;

import com.nrifintech.cms.entities.User;
import com.nrifintech.cms.reports.dto.ResourceDTO;
import com.nrifintech.cms.repositories.UserRepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class ExcelService {

    @Autowired
    private UserRepo userRepository;


    public ResourceDTO exportUsers() {
        List<User> userList= userRepository.findAll();
        Resource resource=prepareExcel(userList);
        return ResourceDTO.builder().resource(resource).mediaType(MediaType.parseMediaType
                        ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).build();
    }

    private Resource prepareExcel(List<User> userList){
        Workbook workbook=new XSSFWorkbook();
        Sheet sheet=workbook.createSheet("USERS");

        prepareHeaders(workbook,sheet,"Id","Email","Name","Records");
        populateUserData(workbook,sheet,userList);

        try(ByteArrayOutputStream byteArrayOutputStream
                    =new ByteArrayOutputStream()){

            workbook.write(byteArrayOutputStream);
            return new ByteArrayResource (byteArrayOutputStream.toByteArray());
        }catch (IOException e){
            e.printStackTrace();
            throw new RuntimeException
                    ("Error while generating excel.");
        }
    }

    private void populateUserData(Workbook workbook, Sheet sheet,
                                  List<User> userList) {

        int rowNo=1;

        for(User user:userList){
            int columnNo=0;
            Row row=sheet.createRow(rowNo);
            populateCell(sheet,row,columnNo++,
                    String.valueOf(user.getId()));
            populateCell(sheet,row,columnNo++, user.getEmail());
            populateCell(sheet,row,columnNo++, user.getEmail());
            populateCell(sheet,row,columnNo++, user.getRecords());
            rowNo++;
        }
    }

    private void populateCell(Sheet sheet,Row row,int columnNo,
                              String value){

        Cell cell=row.createCell(columnNo);
        cell.setCellValue(value);
        sheet.autoSizeColumn(columnNo);
    }

    private void prepareHeaders(Workbook workbook,
                                Sheet sheet, String... headers) {

        Row headerRow=sheet.createRow(0);
        Font font=workbook.createFont();
        font.setBold(true);
        font.setFontName("Arial");

        CellStyle cellStyle=workbook.createCellStyle();
        cellStyle.setFont(font);

        int columnNo=0;
        for(String header:headers){
            Cell headerCell=headerRow.createCell(columnNo++);
            headerCell.setCellValue(header);
            headerCell.setCellStyle(cellStyle);
        }
    }
}