package com.example.mastercheffood.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.mastercheffood.DataModel.OrderHistoryModel;
import com.example.mastercheffood.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.viewholder>{

    Context context;
    List<OrderHistoryModel> models;

    public OrderHistoryAdapter(Context context, List<OrderHistoryModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_singlerow,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        holder.name.setText(models.get(position).getPersonName());
        holder.items.setText(models.get(position).getItemsBooked());
        holder.bookingDate.setText(models.get(position).getCurrentDate());
        holder.amount.setText(models.get(position).getOrderAmount());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {

        TextView name,items,amount,bookingDate;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name1);
            items = (TextView) itemView.findViewById(R.id.items1);
            bookingDate = (TextView) itemView.findViewById(R.id.booking_date1);
            amount = (TextView) itemView.findViewById(R.id.amount1);
        }
    }

}

