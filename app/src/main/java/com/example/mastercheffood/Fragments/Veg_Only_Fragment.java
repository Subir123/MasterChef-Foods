package com.example.mastercheffood.Fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mastercheffood.Adapters.veg_menu_adapter;
import com.example.mastercheffood.DataModel.veg_data_model;
import com.example.mastercheffood.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class Veg_Only_Fragment extends Fragment {

    RecyclerView vegrecview;
    veg_menu_adapter vegadapter;

    public Veg_Only_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_veg__only_, container, false);

        vegrecview = (RecyclerView)view.findViewById(R.id.veg_recyclerview);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        vegMenu();

        return view;
    }

    private void vegMenu()
    {
        FirebaseRecyclerOptions<veg_data_model> myoptions =
                new FirebaseRecyclerOptions.Builder<veg_data_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("vegmenu"), veg_data_model.class)
                        .build();

        vegadapter=new veg_menu_adapter(myoptions);
        vegrecview.setAdapter(vegadapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        vegrecview.setLayoutManager(layoutManager);

    }

    @Override
    public void onStart() {
        super.onStart();
        vegadapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        vegadapter.startListening();
    }
}