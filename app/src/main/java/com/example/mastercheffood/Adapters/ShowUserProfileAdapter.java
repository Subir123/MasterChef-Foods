package com.example.mastercheffood.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastercheffood.DataModel.ShowUserProfileModel;
import com.example.mastercheffood.R;

import java.util.List;

public class ShowUserProfileAdapter extends RecyclerView.Adapter<ShowUserProfileAdapter.viewholder>{

    Context context;
    List<ShowUserProfileModel> modelList;

    public ShowUserProfileAdapter(Context context, List<ShowUserProfileModel> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.show_user_profile_singlerow,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        holder.name.setText(modelList.get(position).getPersonName());
        holder.email.setText(modelList.get(position).getPersonEmail());
        holder.mobile.setText(modelList.get(position).getPersonMobile());
        holder.address.setText(modelList.get(position).getPersonAddress());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {

        TextView name,email,mobile,address;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name2);
            email = (TextView) itemView.findViewById(R.id.email2);
            mobile = (TextView) itemView.findViewById(R.id.mobile2);
            address = (TextView) itemView.findViewById(R.id.address2);

        }
    }

}

