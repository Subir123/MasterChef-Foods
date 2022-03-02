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
import com.example.mastercheffood.DataModel.PopularMenuModel;
import com.example.mastercheffood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class PopularMenuViewAllAdapter extends FirebaseRecyclerAdapter<PopularMenuModel,PopularMenuViewAllAdapter.myviewholder>
{

    public PopularMenuViewAllAdapter(@NonNull FirebaseRecyclerOptions<PopularMenuModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull PopularMenuModel model) {

        holder.name.setText(model.getName());
        holder.price.setText(model.getPrice());
        Glide.with(holder.img.getContext()).load(model.getPurl()).into(holder.img);

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
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_menu_view_all_singlerow,parent,false);
        return new PopularMenuViewAllAdapter.myviewholder(view);
    }

    static class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView name,price;
        Button addToCard;

        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.pmvaImg);
            name=(TextView)itemView.findViewById(R.id.pmvanametext);
            price=(TextView)itemView.findViewById(R.id.pmvapricetext);
            addToCard=(Button) itemView.findViewById(R.id.pmva_add_to_cart);

        }
    }



}
