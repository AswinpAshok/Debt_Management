package com.example.aswin.myapplication;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ViewSwitcher;

import com.example.aswin.myapplication.helper_classes.DBHelper;
import com.example.aswin.myapplication.model_classes.MoneyDonor;
import com.example.aswin.myapplication.recyclerview_components.SearchResultRecyclerAdapter;

import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private Toolbar toolbar;
    private ViewSwitcher switcher;
    private RecyclerView resultRecycler;
    private SearchResultRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        toolbar=findViewById(R.id.toolbar);
        switcher=findViewById(R.id.switcher);
        resultRecycler=findViewById(R.id.searchResultRecycler);

        setSupportActionBar(toolbar);

        dbHelper=DBHelper.getInstance(getApplicationContext());
        handleIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
            Log.d("####", "handleIntent: "+query);
            List<MoneyDonor> donorList= dbHelper.searchDonors(query);
            if(donorList!=null){
                switcher.setDisplayedChild(0);
                Log.d("###", "handleIntent: "+donorList);

                adapter=new SearchResultRecyclerAdapter(donorList);
                RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
                RecyclerView.ItemAnimator itemAnimator=new DefaultItemAnimator();
                resultRecycler.setLayoutManager(layoutManager);
                resultRecycler.setItemAnimator(itemAnimator);
                resultRecycler.setAdapter(adapter);

            }else {
                switcher.setDisplayedChild(1);
                Log.d("####", "handleIntent: no results");
            }
        }
    }
}
