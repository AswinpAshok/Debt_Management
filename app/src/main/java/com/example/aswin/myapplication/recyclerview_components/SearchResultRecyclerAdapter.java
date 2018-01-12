package com.example.aswin.myapplication.recyclerview_components;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aswin.myapplication.DonorViewActivity;
import com.example.aswin.myapplication.R;
import com.example.aswin.myapplication.model_classes.MoneyDonor;

import java.util.List;

/**
 * Created by ASWIN on 1/11/2018.
 */

public class SearchResultRecyclerAdapter extends RecyclerView.Adapter<SearchResultRecyclerAdapter.MyViewHolder> {

    private List<MoneyDonor> donorList;

    public SearchResultRecyclerAdapter(List<MoneyDonor> donorList) {
        this.donorList = donorList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_recycler_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        final MoneyDonor donor=donorList.get(position);
        holder.name.setText(donor.getName());
        holder.id.setText(String.valueOf(donor.getId()));

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.root.getContext().getApplicationContext(), DonorViewActivity.class);
                intent.putExtra("id",donor.getId());
                holder.root.getContext().getApplicationContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    protected class MyViewHolder extends RecyclerView.ViewHolder{

        protected TextView name,id;
        private LinearLayout root;

        public MyViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.donorName);
            id=itemView.findViewById(R.id.donorID);
            root=itemView.findViewById(R.id.root);
        }
    }
}
