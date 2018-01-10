package com.example.aswin.myapplication.helper_classes;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.aswin.myapplication.model_classes.MoneyDonor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * Created by ASWIN on 1/10/2018.
 */

public class ExcelHandler {

    private int TOTAL_SHEETS;

    public ExcelHandler() {
    }

    public List<MoneyDonor> ReadStream(InputStream is) throws IOException, BiffException {

        Workbook workbook;
        List<MoneyDonor> donorList=new ArrayList<>();
        workbook=Workbook.getWorkbook(is);
        this.TOTAL_SHEETS=workbook.getNumberOfSheets();

        for (int i=0;i<TOTAL_SHEETS;i++){
            Sheet sheet=workbook.getSheet(i);
            for (int k=0;k<sheet.getRows();k++){
                Cell nameCell = sheet.getCell(0,k);
                Cell amountCell=sheet.getCell(1,k);
                MoneyDonor donor=new MoneyDonor();
                donor.setName(nameCell.getContents());
                donor.setAmount(amountCell.getContents());
                donorList.add(donor);
            }
        }
        return donorList;
    }
}
