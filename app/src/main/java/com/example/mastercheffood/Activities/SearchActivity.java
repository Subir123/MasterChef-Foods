package com.example.mastercheffood.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.mastercheffood.Fragments.SearchFragment;
import com.example.mastercheffood.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new SearchFragment()).commit();

    }
}