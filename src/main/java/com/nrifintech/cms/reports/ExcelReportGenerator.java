package com.nrifintech.cms.reports;

import com.nrifintech.cms.entities.*;
import com.nrifintech.cms.reports.dto.ResourceDTO;
import com.nrifintech.cms.repositories.MenuRepo;
import com.nrifintech.cms.repositories.UserRepo;
import com.nrifintech.cms.types.ItemType;
import com.nrifintech.cms.types.TransactionType;
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

/**
 * It's a class that generates excel reports for users and menus
 */
public class ExcelReportGenerator {

  /**
   * It's a service class that exports users to an excel file
   */
    @Service
    @Transactional
    public static class UserReport {
        @Autowired
        private UserRepo userRepository;


       /**
        * It takes a list of users, converts it to an excel file, and returns it as a ResourceDTO
        * 
        * @return A ResourceDTO object.
        */
        public ResourceDTO exportUsers() {
            List<User> userList = userRepository.findAll();
            Resource resource = prepareExcel(userList);
            return ResourceDTO.builder().resource(resource).mediaType(MediaType.parseMediaType
                    ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).build();
        }

        /**
         * It takes a list of users and returns a resource object which can be used to download the
         * excel file
         * 
         * @param userList List of users to be exported
         * @return A resource object is being returned.
         */
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

        /**
         * It takes a list of users, iterates over it, and populates the data in the excel sheet
         * 
         * @param workbook The workbook object that we created in the previous step.
         * @param sheet The sheet in which the data is to be populated.
         * @param users List of users to be exported
         */
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

      
        /**
         * It creates a cell in the row, sets the value of the cell to the value passed in, and then
         * auto-sizes the column to fit the cell
         * 
         * @param sheet The sheet to which the row is to be added.
         * @param row The row to populate
         * @param columnNo The column number in the row.
         * @param value The value to be populated in the cell
         */
        private void populateCell(Sheet sheet, Row row, int columnNo,
                                  String value) {

            Cell cell = row.createCell(columnNo);
            cell.setCellValue(value);
            sheet.autoSizeColumn(columnNo);

        }

        /**
         * It creates a row, creates a font, creates a cell style, and then creates a cell for each
         * header
         * 
         * @param workbook The workbook object that we created in the previous step.
         * @param sheet The sheet to which the headers are to be added.
         */
        private void prepareHeaders(Workbook workbook,
                                    Sheet sheet, String... headers) {

            Row headerRow = sheet.createRow(0);
            Font font = workbook.createFont();
            font.setBold(true);

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


   // It's a service class that exports users to an excel file
    @Service
    @Transactional
    public static class MenuReport {
        @Autowired
        private MenuRepo menuRepo;


        /**
         * > It takes a list of menus, converts it to an excel file, and returns a ResourceDTO object
         * containing the excel file
         * 
         * @return A ResourceDTO object is being returned.
         */
        public ResourceDTO exportMenus() {
            List<Menu> menus = menuRepo.findAll();
            Resource resource = prepareExcel(menus);
            return ResourceDTO.builder().resource(resource).mediaType(MediaType.parseMediaType
                    ("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")).build();
        }

        /**
         * It creates a workbook, creates a sheet, creates headers, populates data and returns a
         * resource
         * 
         * @param menus List of Menu objects
         * @return A ByteArrayResource
         */
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

        /**
         * It takes a list of Menu objects, iterates over it, and populates the data in the excel sheet
         * 
         * @param workbook The workbook object that we created in the previous step.
         * @param sheet The sheet in which the data is to be populated.
         * @param menus List of Menu objects to be exported
         */
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


        /**
         * It creates a cell in the row, sets the value of the cell to the value passed in, and then
         * auto sizes the column
         * 
         * @param sheet The sheet to which the row is to be added.
         * @param row The row to populate
         * @param columnNo The column number in the row.
         * @param value The value to be populated in the cell
         */
        private void populateCell(Sheet sheet, Row row, int columnNo,
                                  String value) {

            Cell cell = row.createCell(columnNo);
            cell.setCellValue(value);
            sheet.autoSizeColumn(columnNo);

        }

        /**
         * It creates a row, creates a font, creates a cell style, and then creates a cell for each
         * header
         * 
         * @param workbook The workbook object that we created earlier.
         * @param sheet The sheet to which the headers are to be added.
         */
        private void prepareHeaders(Workbook workbook,
                                    Sheet sheet, String... headers) {

            Row headerRow = sheet.createRow(0);
            Font font = workbook.createFont();
            font.setBold(true);


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
