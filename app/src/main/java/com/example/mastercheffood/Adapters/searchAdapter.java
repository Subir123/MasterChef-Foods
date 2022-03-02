package com.example.mastercheffood.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mastercheffood.DataModel.search_data_model;
import com.example.mastercheffood.Fragments.SearchMenuDetailsFragment;
import com.example.mastercheffood.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class searchAdapter extends FirebaseRecyclerAdapter<search_data_model, searchAdapter.myviewholder> {


    public searchAdapter(@NonNull FirebaseRecyclerOptions<search_data_model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull search_data_model model) {


        holder.name.setText(model.getName());
        holder.des.setText(model.getDes());
        holder.price.setText(model.getPrice());
        Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new SearchMenuDetailsFragment(
                        model.getName(),model.getPrice(),model.getImage(),model.getDetails())).addToBackStack(null).commit();

            }
        });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_layout,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView name,price,des;
        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.menu_image);
            name=(TextView)itemView.findViewById(R.id.menuname_text);
            price=(TextView)itemView.findViewById(R.id.price_text);
            des=(TextView)itemView.findViewById(R.id.menudes_text);

        }
    }

}


