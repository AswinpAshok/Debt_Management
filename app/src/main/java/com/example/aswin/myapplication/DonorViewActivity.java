package com.example.aswin.myapplication;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    private ImageView editButton;
    private String prevName,prevAmount,newName,newAmount;
    private int id;
    private LinearLayout root;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_view);

        toolbar=findViewById(R.id.toolbar);
        name=findViewById(R.id.name);
        amount=findViewById(R.id.amount);
        creationDate=findViewById(R.id.creationDate);
        editButton=findViewById(R.id.editButton);
        root=findViewById(R.id.root);

        setSupportActionBar(toolbar);

        dbHelper=DBHelper.getInstance(this);
        Intent intent=getIntent();
        id=intent.getIntExtra("id",0);

        MoneyDonor donor=dbHelper.getDonorDetails(id);
        prevName=donor.getName();
        prevAmount=String.valueOf(donor.getAmount());

        name.setText(donor.getName());
        amount.setText(String.valueOf(donor.getAmount()));
        long timeStamp=Long.parseLong(donor.getCreatedDate());
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        String date=format.format(new Date(timeStamp));
        creationDate.setText("Created on "+date);


        donorCard=findViewById(R.id.donorCard);
        donorCard.setTranslationZ(35);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEditDialog();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.delete_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                createDeleteDialog();
                break;
        }

        return true;
    }

    private void createDeleteDialog() {
        final Dialog dialog=new Dialog(DonorViewActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.delete_item_dialog);
        View view=dialog.getWindow().getDecorView();
        view.setBackgroundResource(android.R.color.transparent);
        dialog.show();
        dialog.setCancelable(false);
        ImageButton closeButton=dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        Button deleteButton=dialog.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.deleteRecord(id);
                dialog.dismiss();
                Intent returnIntent=new Intent();
                returnIntent.putExtra("id",id);
                setResult(RESULT_OK,returnIntent);
                finish();
            }
        });
    }

    private void createEditDialog() {
        final Dialog dialog=new Dialog(DonorViewActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_fund_dialog);
        View view=dialog.getWindow().getDecorView();
        view.setBackgroundResource(android.R.color.transparent);
        dialog.show();
        dialog.setCancelable(false);

        TextView dialogTitle=dialog.findViewById(R.id.dialog_title);
        dialogTitle.setText("Edit details");
        ImageButton closeButton=dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        final EditText donorName=dialog.findViewById(R.id.donorName);
        final EditText donorAmount=dialog.findViewById(R.id.donorAmount);
        donorName.setText(prevName);
        donorAmount.setText(prevAmount);
        Button editFundButton=dialog.findViewById(R.id.addFundButton);
        editFundButton.setText("SAVE");
        editFundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newName=donorName.getText().toString().trim();
                newAmount=donorAmount.getText().toString().trim();
                boolean isValid=true;
                if(name.equals("")){
                    donorName.setError("Required");
                    isValid=false;
                }

                if(amount.equals("")){
                    donorAmount.setError("Required");
                    isValid=false;
                }

                if(isValid){

                    if(newName.equals(prevName)&&newAmount.equals(prevAmount)){
                        dialog.dismiss();
                    }else {
                        MoneyDonor donor=new MoneyDonor();
                        donor.setId(id);
                        donor.setAmount(Integer.parseInt(newAmount));
                        donor.setName(newName);
                        int i = dbHelper.updateDonor(donor);
                        if(i==0){
                            dialog.dismiss();
                            Snackbar.make(root,"Same record already exists!",Snackbar.LENGTH_LONG).show();
                        }else {
                            prevName=newName;
                            prevAmount=newAmount;
                            name.setText(newName);
                            amount.setText(newAmount);
                        }
                    }
                    dialog.dismiss();
//                    Snackbar.make(baseLayout,"Saved",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

}
