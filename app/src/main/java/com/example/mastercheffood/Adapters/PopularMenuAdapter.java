package com.example.mastercheffood.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mastercheffood.Activities.MenuDetailsActivity;
import com.example.mastercheffood.R;


import com.bumptech.glide.Glide;
import com.example.mastercheffood.DataModel.PopularMenuModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class PopularMenuAdapter extends FirebaseRecyclerAdapter<PopularMenuModel, PopularMenuAdapter.myviewholder>
{
    public PopularMenuAdapter(@NonNull FirebaseRecyclerOptions<PopularMenuModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull PopularMenuModel model)
    {
        holder.name.setText(model.getName());
        holder.price.setText(model.getPrice());
        Glide.with(holder.img.getContext()).load(model.getPurl()).into(holder.img);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                Intent intent = new Intent(activity.getApplicationContext(), MenuDetailsActivity.class );
                intent.putExtra("name",model.getName());
                intent.putExtra("price",model.getPrice());
                intent.putExtra("details",model.getDetails());
                intent.putExtra("image",model.getPurl());
                activity.startActivity(intent);

            }
        });

        holder.addToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                Intent intent = new Intent(activity.getApplicationContext(), MenuDetailsActivity.class );
                intent.putExtra("name",model.getName());
                intent.putExtra("price",model.getPrice());
                intent.putExtra("details",model.getDetails());
                intent.putExtra("image",model.getPurl());
                activity.startActivity(intent);
            }
        });


    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_menu_singlerow,parent,false);
        return new myviewholder(view);
    }

    static class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView name,price;
        Button addToCard;

        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.img1);
            name=(TextView)itemView.findViewById(R.id.nametext);
            price=(TextView)itemView.findViewById(R.id.pricetext);
            addToCard=(Button) itemView.findViewById(R.id.add_to_cart);

        }
    }
}


