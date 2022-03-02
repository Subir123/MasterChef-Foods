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

import com.bumptech.glide.Glide;
import com.example.mastercheffood.Activities.MenuDetailsActivity;
import com.example.mastercheffood.DataModel.veg_data_model;
import com.example.mastercheffood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class veg_menu_adapter extends FirebaseRecyclerAdapter<veg_data_model, veg_menu_adapter.VegViewHolder>
{


    public veg_menu_adapter(@NonNull FirebaseRecyclerOptions<veg_data_model> options)
    {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull VegViewHolder holder, int position, @NonNull veg_data_model model)
    {

        holder.menu_name.setText(model.getItemname());
        holder.menu_description.setText(model.getItemdes());
        holder.menu_price.setText(model.getItemprice());
        Glide.with(holder.imageView.getContext()).load(model.getImgurl()).into(holder.imageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                Intent intent = new Intent(activity.getApplicationContext(), MenuDetailsActivity.class );
                intent.putExtra("name",model.getItemname());
                intent.putExtra("price",model.getItemprice());
                intent.putExtra("details",model.getDetails());
                intent.putExtra("image",model.getImgurl());
                activity.startActivity(intent);

            }
        });

        holder.addToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                Intent intent = new Intent(activity.getApplicationContext(), MenuDetailsActivity.class );
                intent.putExtra("name",model.getItemname());
                intent.putExtra("price",model.getItemprice());
                intent.putExtra("details",model.getDetails());
                intent.putExtra("image",model.getImgurl());
                activity.startActivity(intent);
            }
        });



    }

    @NonNull
    @Override
    public VegViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.veg_menu_singlerow,parent,false);
        return new VegViewHolder(view);
    }

    class VegViewHolder  extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView menu_name,menu_description,menu_price;
        Button addToCard;

        public VegViewHolder(@NonNull View itemView)
        {
            super(itemView);

            menu_name=(TextView)itemView.findViewById(R.id.veg_item_name);
            menu_description=(TextView)itemView.findViewById(R.id.veg_item_description);
            menu_price=(TextView)itemView.findViewById(R.id.veg_item_price);
            imageView=(ImageView)itemView.findViewById(R.id.veg_item_img);
            addToCard=(Button) itemView.findViewById(R.id.vegitem_add_to_cart);
        }
    }


}

