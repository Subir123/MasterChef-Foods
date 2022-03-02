package com.example.mastercheffood.Fragments;



import static com.example.mastercheffood.Network.NetworkUtil.isNetworkAvailable;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mastercheffood.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;
import java.util.Locale;

public class PersonalDetailsFragment extends Fragment implements LocationListener{

    Toolbar myToolbar;
    EditText mobile,email,name;
    Button Continue;
    TextView personAddress;
    LocationManager locationManager;
    ProgressDialog progressDialog;

    public PersonalDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        getActivity().setTheme(R.style.Base_Theme_MaterialComponents_Light);

        View view = inflater.inflate(R.layout.fragment_personal_details, container, false);


        myToolbar = view.findViewById(R.id.search_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(myToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Fetching Your Location");
        progressDialog.setCancelable(false);
        progressDialog.show();

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                getParentFragmentManager().beginTransaction().replace(R.id.assembleOrder,new MyCartFragment()).commit();

            }
        });

        name = view.findViewById(R.id.PersonName);
        email = view.findViewById(R.id.PersonEmail);
        mobile = view.findViewById(R.id.PersonNumber);
        personAddress = view.findViewById(R.id.PersonAddress);
        Continue = view.findViewById(R.id.Continue);

        SharedPreferences preferences = getActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        if (preferences.contains("userName"))
        {
            name.setText(preferences.getString("userName",""));
        }
        if (preferences.contains("userEmail"))
        {
            email.setText(preferences.getString("userEmail",""));
        }

        if (preferences.contains("userMobile"))
        {
            mobile.setText(preferences.getString("userMobile",""));
        }



        Dexter.withContext(getContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getUserLocation();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(getContext(), "permission Denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userName = name.getText().toString().trim();
                String userEmail = email.getText().toString().trim();
                String userMobile = mobile.getText().toString().trim();
                String userAddress = personAddress.getText().toString();

                if (userName.isEmpty())
                {
                    name.requestFocus();
                    name.setError("Enter Your Name");
                    return;
                }

                if (userEmail.isEmpty())
                {
                    email.requestFocus();
                    email.setError("Enter Your Email");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
                {
                    email.requestFocus();
                    email.setError("Enter Your Email Correctly");
                    return;
                }

                if (userAddress.isEmpty())
                {
                    personAddress.requestFocus();
                    personAddress.setError("Enter Your Address");
                    return;
                }

                if (userMobile.isEmpty())
                {
                    mobile.requestFocus();
                    mobile.setError("Enter Your Mobile Number");
                    return;
                }

                if (userMobile.length()<10)
                {
                    mobile.requestFocus();
                    mobile.setError("Enter Your Mobile Number Correctly");
                    return;
                }

                String NumbersOfItems = getArguments().getString("NumbersOfItems");
                String totalPayableAmount = getArguments().getString("TotalAmount");
                String userBookedItems = getArguments().getString("bookedItems");


                OrderSummeryFragment dataFromAddToCardUserDetailsFragment = new OrderSummeryFragment();
                Bundle bundle = new Bundle();
                bundle.putString("name",userName);
                bundle.putString("email",userEmail);
                bundle.putString("mobile",userMobile);
                bundle.putString("address",personAddress.getText().toString().trim());
                bundle.putString("ItemsCount",NumbersOfItems);
                bundle.putString("totalPayableAmount",totalPayableAmount);
                dataFromAddToCardUserDetailsFragment.setArguments(bundle);

                SharedPreferences preferences = getActivity().getSharedPreferences("credentials",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("userName",userName);
                editor.putString("userEmail",userEmail);
                editor.putString("userMobile",userMobile);
                editor.putString("bookedItems",userBookedItems);
                editor.apply();


                getParentFragmentManager().beginTransaction().replace(R.id.assembleOrder, dataFromAddToCardUserDetailsFragment).commit();
            }
        });

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        checkNetwork();
        return view;
    }

    private void checkNetwork()
    {
        if (!isNetworkAvailable(getActivity(), true))
            return;
    }


    @SuppressLint("MissingPermission")
    private void getUserLocation()
    {
        try
        {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location)
    {
        try
        {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address = addresses.get(0).getAddressLine(0);

            if (address !=null && !address.isEmpty())
            {
                progressDialog.dismiss();
                personAddress.setText(address);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}