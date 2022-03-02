package com.example.mastercheffood.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.mastercheffood.Fragments.MyCartFragment;
import com.example.mastercheffood.R;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        getSupportFragmentManager().beginTransaction().replace(R.id.assembleOrder, new MyCartFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        finish();
    }

}