package com.example.aswin.myapplication.helper_classes;

import android.os.AsyncTask;
import android.util.Log;

import com.example.aswin.myapplication.listener_interface.FileImportListener;
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
    private List<MoneyDonor>donorList;
    private Workbook workbook;
    private FileImportListener listener;

    public void setFileImportListener(FileImportListener importListener){
        this.listener=importListener;
    }

    public ExcelHandler() {
    }

    public void ReadStream(InputStream stream) throws IOException,IllegalStateException,NullPointerException {

        ExcelParser parser=new ExcelParser();
        parser.setStream(stream);
        parser.execute();
    }

    private class ExcelParser extends AsyncTask <String,String,String>{

        private InputStream stream;
        protected void setStream(InputStream stream){
            this.stream=stream;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                workbook=new HSSFWorkbook(stream);
            } catch (IOException e) {
                listener.OnIOException();
                return null;
            }

            List<MoneyDonor> donorList=new ArrayList<>();


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

                    Double amount=new Double(0);
                    try {
                        amount = AmountCell.getNumericCellValue();
                        donor.setName(NameCell.getStringCellValue());
                    }catch (IllegalStateException e){
                        listener.OnIllegalStateException();
                        return null;
                    }catch (NullPointerException e){
                        listener.OnNullPointerException();
                        return null;
                    }
                    donor.setAmount(amount.intValue());
                    donorList.add(donor);

                }

            }

            listener.OnImportFinished(donorList);

            try {
                stream.close();
            } catch (IOException e) {
                listener.OnIOException();
                return null;
            }


            return null;
        }
    }


}
