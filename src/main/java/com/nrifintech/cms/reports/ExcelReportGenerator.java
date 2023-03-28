package com.nrifintech.cms.reports;

import com.nrifintech.cms.entities.*;
import com.nrifintech.cms.reports.dto.ResourceDTO;
import com.nrifintech.cms.repositories.MenuRepo;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.types.ItemType;
import com.nrifintech.cms.types.TransactionType;
import org.apache.catalina.connector.Response;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelReportGenerator {

    @Service
    @Transactional
    public static class UserReport {
        @Autowired
        private UserRepo userRepository;


        public ResourceDTO exportUsers() {
            List<User> userList = userRepository.findAll();
            Resource resource = prepareExcel(userList);
            return ResourceDTO.builder().resource(resource).mediaType(MediaType.parseMediaType
                    ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).build();
        }

        private Resource prepareExcel(List<User> userList) {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("USERS");


            prepareHeaders(workbook, sheet, "Id", "Email", "Role", "Status", "Created At", "Total Orders", "Total Veg Item", "Total Non Veg Item", "Wallet Balance", "Total transactions",
                    "Total Deposits", "Total Withdrawals", "Total deposited", "Total expense");
            populateUserData(workbook, sheet, userList);


            try (ByteArrayOutputStream byteArrayOutputStream
                         = new ByteArrayOutputStream()) {

                workbook.write(byteArrayOutputStream);
                return new ByteArrayResource(byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException
                        ("Error while generating excel.");
            }
        }

        private void populateUserData(Workbook workbook, Sheet sheet, List<User> users) {

            int rowNo = 1;

            for (User obj : users) {

                int columnNo = 0;
                Row row = sheet.createRow(rowNo);
                populateCell(sheet, row, columnNo++, String.valueOf(obj.getId()));
                populateCell(sheet, row, columnNo++, obj.getEmail());
                populateCell(sheet, row, columnNo++, obj.getRole().toString());
                populateCell(sheet, row, columnNo++, obj.getStatus().toString());
                populateCell(sheet, row, columnNo++, obj.getCreated().toString());
                populateCell(sheet, row, columnNo++, String.valueOf(obj.getRecords().size()));

                long veg = 0, nonVeg = 0;
                for (Order order : obj.getRecords()) {
                    veg += order.getCartItems().stream().filter(i -> i.getItemType().equals(ItemType.Veg)).count();
                    nonVeg += order.getCartItems().stream().filter(i -> i.getItemType().equals(ItemType.NonVeg)).count();
                }


                populateCell(sheet, row, columnNo++, String.valueOf(veg));
                populateCell(sheet, row, columnNo++, String.valueOf(nonVeg));

                if (obj.getWallet() != null) {
                    populateCell(sheet, row, columnNo++, String.valueOf(obj.getWallet().getBalance()));
                    populateCell(sheet, row, columnNo++, String.valueOf(obj.getWallet().getTransactions().size()));

                    int deposits = (int) obj.getWallet().getTransactions().stream().filter(
                            t -> t.getType().equals(TransactionType.Deposit)
                    ).count();

                    double depositedAmount = 0d, expenseAmount = 0d;
                    for (Transaction t : obj.getWallet().getTransactions()) {
                        if (t.getType().equals(TransactionType.Deposit))
                            depositedAmount += t.getAmount();
                        else expenseAmount += t.getAmount();
                    }


                    populateCell(sheet, row, columnNo++, String.valueOf(deposits));
                    populateCell(sheet, row, columnNo++, String.valueOf(obj.getWallet().getTransactions().size() - deposits));
                    populateCell(sheet, row, columnNo++, String.valueOf(depositedAmount));
                    populateCell(sheet, row, columnNo++, String.valueOf(expenseAmount));
                } else {
                    populateCell(sheet, row, columnNo++, "NA");
                    populateCell(sheet, row, columnNo++, "NA");
                    populateCell(sheet, row, columnNo++, "NA");
                    populateCell(sheet, row, columnNo++, "NA");
                    populateCell(sheet, row, columnNo++, "NA");
                    populateCell(sheet, row, columnNo++, "NA");
                }
                rowNo++;
            }
        }

        private void populateOrderData(Workbook workbook, Sheet sheet,
                                       List<Order> orders) {

            int rowNo = 1;

            for (Order obj : orders) {

                int columnNo = 0;
                Row row = sheet.createRow(rowNo);
                populateCell(sheet, row, columnNo++, String.valueOf((obj.getId())));
                populateCell(sheet, row, columnNo++, obj.getStatus().toString());
                populateCell(sheet, row, columnNo++, String.valueOf(obj.getCartItems().size()));
                populateCell(sheet, row, columnNo++, obj.getFeedBack().toString());
                populateCell(sheet, row, columnNo, obj.getOrderPlaced().toString());

                rowNo++;
            }
        }

        private void populateCell(Sheet sheet, Row row, int columnNo,
                                  String value) {

            Cell cell = row.createCell(columnNo);
            cell.setCellValue(value);
            sheet.autoSizeColumn(columnNo);

        }

        private void populateCell(Sheet sheet, Row row, int columnNo,
                                  String value, Hyperlink link) {

            Cell cell = row.createCell(columnNo);
            cell.setCellValue(value);
            sheet.autoSizeColumn(columnNo);


            cell.setHyperlink(link);
//        cell.setCellStyle();

        }

        private void prepareHeaders(Workbook workbook,
                                    Sheet sheet, String... headers) {

            Row headerRow = sheet.createRow(0);
            Font font = workbook.createFont();
            font.setBold(true);
//        font.setFontName("Arial");

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);

            int columnNo = 0;
            for (String header : headers) {
                Cell headerCell = headerRow.createCell(columnNo++);
                headerCell.setCellValue(header);
                headerCell.setCellStyle(cellStyle);
            }
        }
    }


    @Service
    @Transactional
    public static class MenuReport {
        @Autowired
        private MenuRepo menuRepo;


        public ResourceDTO exportMenus() {
            List<Menu> menus = menuRepo.findAll();
            Resource resource = prepareExcel(menus);
            return ResourceDTO.builder().resource(resource).mediaType(MediaType.parseMediaType
                    ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).build();
        }

        private Resource prepareExcel(List<Menu> menus) {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("MENUS");


            prepareHeaders(workbook, sheet,"Id","Approval","For Date","Menu Type","Total items",
                    "Total Veg Items","Total Non Veg Items");
            populateMenuData(workbook, sheet, menus);


            try (ByteArrayOutputStream byteArrayOutputStream
                         = new ByteArrayOutputStream()) {

                workbook.write(byteArrayOutputStream);
                return new ByteArrayResource(byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                throw new RuntimeException
                        ("Error while generating excel.");
            }
        }

        private void populateMenuData(Workbook workbook, Sheet sheet, List<Menu> menus) {

            int rowNo = 1;

            for (Menu obj : menus) {

                int columnNo = 0;
                Row row = sheet.createRow(rowNo);
                populateCell(sheet, row, columnNo++, String.valueOf(obj.getId()));
                populateCell(sheet, row, columnNo++, String.valueOf(obj.getApproval()));
                populateCell(sheet, row, columnNo++, obj.getDate().toString());
                populateCell(sheet, row, columnNo++, obj.getMenuType().toString());
                populateCell(sheet, row, columnNo++, String.valueOf(obj.getItems().size()));


                int veg =0,nonVeg=0;

                for (Item i :
                        obj.getItems()) {
                    if(i.getItemType().equals(ItemType.Veg))
                        veg++;
                    else nonVeg++;
                }

                populateCell(sheet, row, columnNo++, String.valueOf(veg));
                populateCell(sheet, row, columnNo++, String.valueOf(nonVeg));

                rowNo++;
            }
        }


        private void populateCell(Sheet sheet, Row row, int columnNo,
                                  String value) {

            Cell cell = row.createCell(columnNo);
            cell.setCellValue(value);
            sheet.autoSizeColumn(columnNo);

        }

        private void prepareHeaders(Workbook workbook,
                                    Sheet sheet, String... headers) {

            Row headerRow = sheet.createRow(0);
            Font font = workbook.createFont();
            font.setBold(true);
//        font.setFontName("Arial");

            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);

            int columnNo = 0;
            for (String header : headers) {
                Cell headerCell = headerRow.createCell(columnNo++);
                headerCell.setCellValue(header);
                headerCell.setCellStyle(cellStyle);
            }
        }
    }
}
