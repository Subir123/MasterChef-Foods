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
import com.example.mastercheffood.R;
import com.example.mastercheffood.DataModel.nonveg_data_model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class nonveg_menu_adapter extends FirebaseRecyclerAdapter<nonveg_data_model, nonveg_menu_adapter.NonvegViewHolder>
{

    public nonveg_menu_adapter(@NonNull FirebaseRecyclerOptions<nonveg_data_model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NonvegViewHolder holder, int position, @NonNull nonveg_data_model model)
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
    public NonvegViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.nonveg_menu_singlerow,parent,false);
        return new NonvegViewHolder(view);
    }

    class NonvegViewHolder  extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView menu_name,menu_description,menu_price;
        Button addToCard;

        public NonvegViewHolder(@NonNull View itemView)
        {
            super(itemView);

            menu_name=(TextView)itemView.findViewById(R.id.nonveg_item_name);
            menu_description=(TextView)itemView.findViewById(R.id.nonveg_item_description);
            menu_price=(TextView)itemView.findViewById(R.id.nonveg_item_price);
            imageView=(ImageView)itemView.findViewById(R.id.nonveg_item_img);
            addToCard=(Button) itemView.findViewById(R.id.nonvegitem_add_to_cart);

        }
    }

}
