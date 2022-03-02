package com.example.mastercheffood.Fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mastercheffood.Adapters.nonveg_menu_adapter;
import com.example.mastercheffood.DataModel.nonveg_data_model;
import com.example.mastercheffood.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;


public class Nonveg_Only_Fragment extends Fragment {

    RecyclerView nonvegrecview;
    nonveg_menu_adapter nonvegadapter;

    public Nonveg_Only_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nonveg__only_, container, false);

        nonvegrecview = (RecyclerView)view.findViewById(R.id.nonveg_recyclerview);
        nonvegMenu();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        return view;
    }

    private void nonvegMenu()
    {
        FirebaseRecyclerOptions<nonveg_data_model> myoptions =
                new FirebaseRecyclerOptions.Builder<nonveg_data_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("NonvegMenu"), nonveg_data_model.class)
                        .build();

        nonvegadapter = new nonveg_menu_adapter(myoptions);
        nonvegrecview.setAdapter(nonvegadapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(),2);
        nonvegrecview.setLayoutManager(layoutManager);

    }

    @Override
    public void onStart() {
        super.onStart();
        nonvegadapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        nonvegadapter.startListening();
    }


}