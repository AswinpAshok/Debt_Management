package com.example.aswin.myapplication.helper_classes;

import android.util.Log;

import com.example.aswin.myapplication.model_classes.MoneyDonor;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by ASWIN on 1/10/2018.
 */

public class ExcelHandler {

    private int TOTAL_SHEETS;
    private String TAG="##Excel_HANDLER";

    public ExcelHandler() {
    }

    public List<MoneyDonor> ReadStream(InputStream stream) throws IOException {

        Workbook workbook;
        List<MoneyDonor> donorList=new ArrayList<>();
        workbook=new HSSFWorkbook(stream);

        TOTAL_SHEETS=workbook.getNumberOfSheets();

        for (int i=0;i<TOTAL_SHEETS;i++){
            Sheet sheet=workbook.getSheetAt(i);
            Iterator<Row> rowIterator = sheet.iterator();
            while (rowIterator.hasNext())
            {

                //Get the row object
                MoneyDonor donor=new MoneyDonor();
                Row row = rowIterator.next();

                Cell NameCell= row.getCell(0);
                Cell AmountCell=row.getCell(1);
                Double amount=AmountCell.getNumericCellValue();

                donor.setName(NameCell.getStringCellValue());
                donor.setAmount(String.valueOf(amount.intValue()));
                donorList.add(donor);

            }


        }


        stream.close();


        return donorList;
    }
}
