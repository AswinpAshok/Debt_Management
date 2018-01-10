package com.example.aswin.myapplication.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.example.aswin.myapplication.R;
import com.example.aswin.myapplication.helper_classes.DBHelper;
import com.example.aswin.myapplication.helper_classes.ExcelHandler;
import com.example.aswin.myapplication.model_classes.MoneyDonor;

import java.io.IOException;
import java.util.List;

import jxl.read.biff.BiffException;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final int READ_REQUEST_CODE=1284;
    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE=1845;
    private String TAG="HomeFragment";
    private CoordinatorLayout baseLayout;
    private ExcelHandler excelHandler;
    private DBHelper dbHelper;

    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);

        baseLayout=v.findViewById(R.id.baseLayout);

        setHasOptionsMenu(true);
        excelHandler=new ExcelHandler();
        dbHelper=DBHelper.getInstance(getActivity());

        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu,menu);
//        super.onCreateOptionsMenu();
//        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.action_open : checkPermission();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case MY_PERMISSIONS_REQUEST_READ_STORAGE :
                Log.d(TAG, "onRequestPermissionsResult: ");
                if(grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d(TAG, "onRequestPermissionsResult: calling pikfile");
                    pickFile();
                }else {
                    Snackbar.make(baseLayout,"Permission denied",Snackbar.LENGTH_SHORT).show();
                    Log.d(TAG, "onRequestPermissionsResult: permission denied");
                }

        }
    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                show_permission_Req_dialog();
            } else {

               show_permission_Req_dialog();
            }

        }else {

            pickFile();
        }
    }

    public void show_permission_Req_dialog(){
        final Dialog dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.storage_permission_dialog_layout);
        View v = dialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        dialog.show();

        Button grantButton=dialog.findViewById(R.id.grantButton);
        grantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                Log.d(TAG, "onClick: "+shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE));
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_STORAGE);
//                Log.d(TAG, "onClick: req permission");
            }
        });
    }


    public void pickFile() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                Log.i(TAG, "Uri: " + uri.toString());
                try {
                    List<MoneyDonor>donorList=excelHandler.ReadStream(getActivity().getContentResolver().openInputStream(uri));
                    dbHelper.insertDonorList(donorList);

                } catch (IOException e) {

                    Snackbar.make(baseLayout,"Unable to access file",Snackbar.LENGTH_LONG).show();
                } catch (BiffException e) {

                    Snackbar.make(baseLayout,"Unsupported file",Snackbar.LENGTH_LONG).show();
                }

            }else {
                Snackbar.make(baseLayout,"Unable to access file",Snackbar.LENGTH_LONG).show();

            }
        }
    }
}
