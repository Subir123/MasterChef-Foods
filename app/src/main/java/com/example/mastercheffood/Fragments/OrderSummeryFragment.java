package com.example.mastercheffood.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mastercheffood.Activities.RazorpayActivity;
import com.example.mastercheffood.R;



public class OrderSummeryFragment extends Fragment {

    Toolbar toolbar;
    TextView name,email,mobile,address,items,price,payableAmount,bookedItems;
    Button pay;
    String amount;

    public OrderSummeryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_summery, container, false);

        toolbar = view.findViewById(R.id.Utoolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        name = view.findViewById(R.id.Uname);
        email = view.findViewById(R.id.Uemail);
        mobile = view.findViewById(R.id.Umobile);
        address = view.findViewById(R.id.Uaddress);
        items = view.findViewById(R.id.Uitem);
        price = view.findViewById(R.id.Uprice);
        pay = view.findViewById(R.id.pay);
        payableAmount = view.findViewById(R.id.payable);
        bookedItems = view.findViewById(R.id.booked_items);


        int total = 50 + Integer.parseInt(getArguments().getString("totalPayableAmount"));
        amount = String.valueOf(total);


        name.setText(getArguments().getString("name"));
        email.setText(getArguments().getString("email"));
        mobile.setText(getArguments().getString("mobile"));
        address.setText(getArguments().getString("address"));
        items.setText(getArguments().getString("ItemsCount"));
        price.setText(getArguments().getString("totalPayableAmount"));
        payableAmount.setText(amount);


        SharedPreferences preferences = getActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        if (preferences.contains("bookedItems"))
        {
            bookedItems.setText(preferences.getString("bookedItems",""));
        }


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent dataFromOrderSummery = new Intent(getActivity(), RazorpayActivity.class);
                dataFromOrderSummery.putExtra("username",getArguments().getString("name"));
                dataFromOrderSummery.putExtra("useremail",getArguments().getString("email"));
                dataFromOrderSummery.putExtra("usermobile",getArguments().getString("mobile"));
                dataFromOrderSummery.putExtra("useraddress",getArguments().getString("address"));
                dataFromOrderSummery.putExtra("orderAmount",getArguments().getString("totalPayableAmount"));
                dataFromOrderSummery.putExtra("totalAmountToPay",amount);
                startActivity(dataFromOrderSummery);
                getActivity().finish();

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getParentFragmentManager().beginTransaction().replace(R.id.assembleOrder, new PersonalDetailsFragment()).commit();
            }
        });

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        return view;
    }


}