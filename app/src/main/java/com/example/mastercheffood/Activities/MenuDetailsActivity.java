package com.example.mastercheffood.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.mastercheffood.Fragments.AddToCardFragment;
import com.example.mastercheffood.R;

public class MenuDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_details);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        getSupportFragmentManager().beginTransaction().replace(R.id.rapper, new AddToCardFragment()).commit();
    }

}