package com.example.aswin.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.aswin.myapplication.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {


    private FragmentManager manager;
    private Toolbar toolbar;
    private HomeFragment fragment;
    private static final int READ_REQUEST_CODE=1284;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=findViewById(R.id.toolbar);

        manager=getSupportFragmentManager();
        fragment=new HomeFragment();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.fragmentHolder,fragment);
        transaction.commit();

        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        searchView.setIconifiedByDefault(false);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {

                MenuItem searchMenuItem = menu.findItem(R.id.action_search);
                if (searchMenuItem != null) {
                    searchMenuItem.collapseActionView();
                }
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case 12365:
                if(resultCode==RESULT_OK) {
                    fragment.onItemDeleted();
                }
                break;
            case READ_REQUEST_CODE:
                if(resultCode==RESULT_OK) {
                    fragment.onDonorListInserted(data);
                }
                break;
        }
    }
}
