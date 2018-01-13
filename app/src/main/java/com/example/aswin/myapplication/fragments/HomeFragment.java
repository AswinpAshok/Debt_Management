package com.example.aswin.myapplication.fragments;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.example.aswin.myapplication.R;
import com.example.aswin.myapplication.helper_classes.DBHelper;
import com.example.aswin.myapplication.helper_classes.ExcelHandler;
import com.example.aswin.myapplication.listener_interface.FileImportListener;
import com.example.aswin.myapplication.model_classes.DashboardInfo;
import com.example.aswin.myapplication.model_classes.MoneyDonor;
import com.example.aswin.myapplication.recyclerview_components.TopTenRecyclerAdapter;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements FileImportListener{

    private static final int READ_REQUEST_CODE=1284;
    private static final int MY_PERMISSIONS_REQUEST_READ_STORAGE=1845;
    private String TAG="HomeFragment";
    private CoordinatorLayout baseLayout;
    private ExcelHandler excelHandler;
    private DBHelper dbHelper;
    private FloatingActionButton addButton;
    private TextView totalRecords,totalAmount;
    private CardView fileImport;
    private TopTenRecyclerAdapter adapter;
    private RecyclerView topTenRecycler;
    private ViewSwitcher switcher;
    private ProgressDialog progressDialog;

    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_home, container, false);

        baseLayout=v.findViewById(R.id.baseLayout);
        addButton=v.findViewById(R.id.addButton);
        totalAmount=v.findViewById(R.id.totalAmount);
        totalRecords=v.findViewById(R.id.totalRecords);
        fileImport=v.findViewById(R.id.fileImport);
        topTenRecycler=v.findViewById(R.id.topTenRecycler);
        switcher=v.findViewById(R.id.switcher);

        getActivity().registerReceiver(receiver, new IntentFilter("1457"));

        SnapHelper snapHelper=new LinearSnapHelper();
        snapHelper.attachToRecyclerView(topTenRecycler);

        baseLayout.post(new Runnable() {
            @Override
            public void run() {
                baseLayout.requestLayout();
            }
        });

        excelHandler=new ExcelHandler();
        excelHandler.setFileImportListener(this);

        dbHelper=DBHelper.getInstance(getActivity());
        dbHelper.setContext(getActivity());
//        dbHelper.setListener(this);

        prepareHomeScreen();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewFundDialog();
            }
        });

        fileImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

        return v;
    }

    private void prepareHomeScreen() {

        DashboardInfo info=dbHelper.getDashboardInfo();
        totalAmount.setText(info.getTotalAmount());
        totalRecords.setText(info.getTotalRecords());

        List<MoneyDonor> donorList=dbHelper.getTopTen();
        if(donorList!=null) {
            switcher.setDisplayedChild(0);
            adapter = new TopTenRecyclerAdapter(donorList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
            topTenRecycler.setLayoutManager(layoutManager);
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            topTenRecycler.setItemAnimator(itemAnimator);
            topTenRecycler.setAdapter(adapter);

        }else {
            switcher.setDisplayedChild(1);
        }


    }

    private void createNewFundDialog() {
        final Dialog dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_fund_dialog);
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

        final EditText donorName=dialog.findViewById(R.id.donorName);
        final EditText donorAmount=dialog.findViewById(R.id.donorAmount);
        Button addFundButton=dialog.findViewById(R.id.addFundButton);
        addFundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=donorName.getText().toString().trim();
                String amount=donorAmount.getText().toString().trim();
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
                    int flag=dbHelper.addDonor(name,amount);
                    dialog.dismiss();
                    if(flag==0){
                        Snackbar.make(baseLayout, "Same record already exists", Snackbar.LENGTH_SHORT).show();
                    }else {
                        Snackbar.make(baseLayout, "Saved", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
        dialog.setCancelable(false);
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
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_STORAGE);
            }
        });
        ImageButton closeButton=dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: close dialog");
                dialog.dismiss();
            }
        });
    }


    public void pickFile() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");

        getActivity().startActivityForResult(intent, READ_REQUEST_CODE);
    }


    @Override
    public void onResume() {
        if(receiver==null) {
            getActivity().registerReceiver(receiver, new IntentFilter("1457"));
        }
        prepareHomeScreen();
        super.onResume();

    }

    public void onItemDeleted() {
        Snackbar.make(baseLayout,"Deleted",Snackbar.LENGTH_LONG).show();
    }

    public void onDonorListInserted(Intent data) {
        final Uri uri;
        if (data != null) {
            uri = data.getData();
            if(uri.getPath().endsWith("xls")) {

                try {
                    excelHandler.ReadStream(getActivity().getContentResolver().openInputStream(uri));
                    progressDialog=new ProgressDialog(getActivity());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setTitle("Please wait..");
                    progressDialog.setMessage("Importing file");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                } catch (IOException e) {
                    Snackbar.make(baseLayout, "Unable to access file", Snackbar.LENGTH_LONG).show();
                }

            }else {
                Snackbar.make(baseLayout, "Unsupported file", Snackbar.LENGTH_LONG).show();
            }

        }else {
            Snackbar.make(baseLayout,"Unable to access file",Snackbar.LENGTH_LONG).show();

        }
    }


    @Override
    public void OnImportFinished(List<MoneyDonor> donors) {
        Log.d(TAG, "onImportFinished: ");
        progressDialog.dismiss();
        if(receiver==null){
            getActivity().registerReceiver(receiver,new IntentFilter("1457"));
        }
        dbHelper.insertDonorList(donors);
        Snackbar.make(baseLayout, "Successfully imported file", Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void OnIOException() {
        progressDialog.dismiss();
        Snackbar.make(baseLayout,"Unable to access file",Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void OnNullPointerException() {
        progressDialog.dismiss();
        Snackbar.make(baseLayout,"File import failed. File contents are not properly formatted",Snackbar.LENGTH_LONG).show();

    }

    @Override
    public void OnIllegalStateException() {
        Snackbar.make(baseLayout,"File import failed. File contents are not properly formatted",Snackbar.LENGTH_LONG).show();
        progressDialog.dismiss();
    }

    private BroadcastReceiver receiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: received");
            prepareHomeScreen();
        }
    };


    @Override
    public void onPause() {
//        if(receiver!=null) {
//            getActivity().unregisterReceiver(receiver);
//        }
//        receiver=null;
        super.onPause();
    }

    @Override
    public void onDestroy() {

        if(receiver!=null) {
            getActivity().unregisterReceiver(receiver);
        }
        receiver=null;
        super.onDestroy();
    }
}
