package com.example.aswin.myapplication.recyclerview_components;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aswin.myapplication.DonorViewActivity;
import com.example.aswin.myapplication.R;
import com.example.aswin.myapplication.model_classes.MoneyDonor;

import java.util.List;

/**
 * Created by ASWIN on 1/13/2018.
 */

public class TopTenRecyclerAdapter extends RecyclerView.Adapter<TopTenRecyclerAdapter.ViewHolder> {

    private List<MoneyDonor>donorList;

    public TopTenRecyclerAdapter(List<MoneyDonor> donorList) {
        this.donorList = donorList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.top_ten_recycler_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final MoneyDonor donor=donorList.get(position);
        holder.id.setText(String.valueOf(donor.getId()));
        holder.name.setText(donor.getName());
        holder.amount.setText(String.valueOf(donor.getAmount()));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(holder.root.getContext().getApplicationContext(), DonorViewActivity.class);
                intent.putExtra("id",donor.getId());
                ((Activity)holder.root.getContext()).startActivityForResult(intent,12365);
            }
        });
    }

    @Override
    public int getItemCount() {
        return donorList.size();
    }

    public void removeItem(int deleteID) {
        for (int i=0;i<donorList.size();i++){
            MoneyDonor donor=donorList.get(i);
            if(donor.getId()==deleteID){
                donorList.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder{

        private TextView amount,name,id;
        private CardView root;

        public ViewHolder(View itemView) {
            super(itemView);
            id=itemView.findViewById(R.id.donorID);
            name=itemView.findViewById(R.id.name);
            amount=itemView.findViewById(R.id.amount);
            root=itemView.findViewById(R.id.root);
        }
    }
}
