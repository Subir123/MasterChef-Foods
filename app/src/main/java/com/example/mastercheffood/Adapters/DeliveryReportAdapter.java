package com.example.mastercheffood.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastercheffood.DataModel.DeliveryReportModel;
import com.example.mastercheffood.R;

import java.util.List;

public class DeliveryReportAdapter extends RecyclerView.Adapter<DeliveryReportAdapter.myviewHolder>{

    Context context;
    List<DeliveryReportModel> modelList;

    public DeliveryReportAdapter(Context context, List<DeliveryReportModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public myviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.show_pending_orders_singlerow,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull myviewHolder holder, int position) {

        holder.names.setText(modelList.get(position).getName());
        holder.emails.setText(modelList.get(position).getEmail());
        holder.mobiles.setText(modelList.get(position).getMobile());
        holder.addresses.setText(modelList.get(position).getAddress());
        holder.bookingDates.setText(modelList.get(position).getDate());
        holder.bookingTimes.setText(modelList.get(position).getBookingTime());
        holder.bookingIds.setText(modelList.get(position).getBookingId());
        holder.bookedItems.setText(modelList.get(position).getBookedItems());
        holder.amountPaid.setText(modelList.get(position).getPayableAmount());
        holder.deliveryTime.setText("Your order will be delivered within " +modelList.get(position).getDeliveryTime());


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class myviewHolder extends RecyclerView.ViewHolder {

        TextView names,emails,mobiles,addresses,bookingDates,bookingTimes,bookingIds,bookedItems,amountPaid,deliveryTime;

        public myviewHolder(@NonNull View itemView) {
            super(itemView);

            names = (TextView) itemView.findViewById(R.id.name4);
            emails = (TextView) itemView.findViewById(R.id.email4);
            mobiles = (TextView) itemView.findViewById(R.id.mobile4);
            addresses = (TextView) itemView.findViewById(R.id.address4);
            bookingDates = (TextView) itemView.findViewById(R.id.date4);
            bookingTimes = (TextView) itemView.findViewById(R.id.time4);
            bookingIds = (TextView) itemView.findViewById(R.id.id4);
            bookedItems = (TextView) itemView.findViewById(R.id.items4);
            amountPaid = (TextView) itemView.findViewById(R.id.amount4);
            deliveryTime = (TextView) itemView.findViewById(R.id.deliveryTime4);

        }
    }

}

