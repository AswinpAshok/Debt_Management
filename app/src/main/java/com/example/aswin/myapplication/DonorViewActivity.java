package com.example.aswin.myapplication;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.aswin.myapplication.helper_classes.DBHelper;
import com.example.aswin.myapplication.model_classes.MoneyDonor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DonorViewActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CardView donorCard;
    private DBHelper dbHelper;
    private TextView name,creationDate,amount;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_view);

        toolbar=findViewById(R.id.toolbar);
        name=findViewById(R.id.name);
        amount=findViewById(R.id.amount);
        creationDate=findViewById(R.id.creationDate);

        setSupportActionBar(toolbar);

        dbHelper=DBHelper.getInstance(this);
        Intent intent=getIntent();
        int id=intent.getIntExtra("id",0);

        MoneyDonor donor=dbHelper.getDonorDetails(id);

        name.setText(donor.getName());
        amount.setText(donor.getAmount());
        long timeStamp=Long.parseLong(donor.getCreatedDate());
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        String date=format.format(new Date(timeStamp));
        creationDate.setText("Created on "+date);



        donorCard=findViewById(R.id.donorCard);
        donorCard.setTranslationZ(35);

    }
}
