package com.example.mastercheffood.Fragments;

import static com.example.mastercheffood.Network.NetworkUtil.isNetworkAvailable;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import com.example.mastercheffood.Adapters.searchAdapter;
import com.example.mastercheffood.DataModel.search_data_model;
import com.example.mastercheffood.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SearchFragment extends Fragment {

    SearchView searchView;
    RecyclerView recyclerView;
    searchAdapter adapter;
    DatabaseReference mUserDatabase;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = (SearchView) view.findViewById(R.id.search);
        recyclerView = (RecyclerView) view.findViewById(R.id.search_resule);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mUserDatabase = FirebaseDatabase.getInstance().getReference("Search");

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        checkNetwork();
        searchMenu();

        return view;
    }

    private void checkNetwork()
    {
        if (!isNetworkAvailable(getActivity(), true))
            return;
    }

    private void searchMenu() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                processSearch(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                processSearch(query);
                return false;
            }
        });
    }


    private void processSearch(String str) {
        FirebaseRecyclerOptions<search_data_model> options =
                new FirebaseRecyclerOptions.Builder<search_data_model>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Search").orderByChild("name").startAt(str.toUpperCase()).endAt(str.toLowerCase() + "\uf8ff"), search_data_model.class)
                        .build();

        adapter = new searchAdapter(options);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

}