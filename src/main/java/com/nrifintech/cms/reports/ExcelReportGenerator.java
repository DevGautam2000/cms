package com.nrifintech.cms.reports;

import com.nrifintech.cms.entities.Order;
import com.nrifintech.cms.entities.User;
import org.apache.catalina.connector.Response;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelReportGenerator {

    public void generate(List<User> users) throws IOException {
        // Creating a Workbook
        Workbook workbook = new XSSFWorkbook();
        // Creating a Sheet
        Sheet sheet = workbook.createSheet("Past Orders");
        // Creating a DateTimeFormatter
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        String fileName = "past_orders_" + dtf.format(now) + ".xlsx";
        // Creating a Row for the Title
        Row titleRow = sheet.createRow(0);
        // Creating a Cell for the Title
        Cell titleCell = titleRow.createCell(0);
        // Setting the Title
        int rowNum=1;
        titleCell.setCellValue("List of the Previous Orders - " + dtf.format(now));
        Row r = sheet.createRow(rowNum++);
        r.createCell(0).setCellValue("User Name");
//        r.createCell(1).setCellValue("Order Date");
//        r.createCell(2).setCellValue("Order Id");
//        r.createCell(3).setCellValue("Item Name");
//        r.createCell(4).setCellValue("Price");
        rowNum = 3;
        for (User user : users) {
            Row row = sheet.createRow(rowNum++);
//            row.createCell(0).setCellValue(order.getCanteenUsers().getName());
//            row.createCell(1).setCellValue(order.getOrderDate().toString());
//            row.createCell(2).setCellValue(order.getOrderId());
//            row.createCell(3).setCellValue(order.getFood().getName());
//            row.createCell(4).setCellValue(order.getTotalPrice());
            row.createCell(0).setCellValue(user.getEmail());
        }
        // Setting the Column Widths
        sheet.setColumnWidth(0, 6000);
//        sheet.setColumnWidth(1, 4000);
//        sheet.setColumnWidth(2, 4000);
//        sheet.setColumnWidth(3, 6000);
//        sheet.setColumnWidth(4, 4000);
        // Writing the Workbook to the Response
        HttpServletResponse httpServletResponse = new Response();
        httpServletResponse.setContentType("application/vnd.ms-excel");
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();


        System.out.println("HI mf");

        workbook.close();
    }

}
