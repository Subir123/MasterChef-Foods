package com.example.mastercheffood.Activities;

import static com.example.mastercheffood.Network.NetworkUtil.isNetworkAvailable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mastercheffood.Adapters.PopularMenuViewAllAdapter;
import com.example.mastercheffood.DataModel.PopularMenuModel;
import com.example.mastercheffood.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import java.util.Objects;


public class PopulerMenuViewAllActivity extends AppCompatActivity {

    PopularMenuViewAllAdapter adapter;
    RecyclerView recview;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_populer_menu_view_all);

        toolbar = (Toolbar) findViewById(R.id.pmva_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                finish();

            }
        });

        recview = (RecyclerView) findViewById(R.id.popularMenuRecview);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        checkNetwork();
        loadCardData();
    }

    private void loadCardData()
    {
        FirebaseRecyclerOptions<PopularMenuModel> options =
                new FirebaseRecyclerOptions.Builder<PopularMenuModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PopulerMenu"), PopularMenuModel.class)
                        .build();

        adapter = new PopularMenuViewAllAdapter(options);
        recview.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recview.setLayoutManager(layoutManager);
        adapter.startListening();
    }

    private void checkNetwork()
    {
        if (!isNetworkAvailable(PopulerMenuViewAllActivity.this, true))
            return;
    }
}