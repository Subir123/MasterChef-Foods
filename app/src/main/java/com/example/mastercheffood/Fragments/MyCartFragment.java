package com.example.mastercheffood.Fragments;

import static com.example.mastercheffood.Network.NetworkUtil.isNetworkAvailable;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mastercheffood.Activities.HomePageActivity;
import com.example.mastercheffood.Activities.VerifyOTPActivity;
import com.example.mastercheffood.Adapters.MyCartAdapter;
import com.example.mastercheffood.DataModel.MyCartModel;
import com.example.mastercheffood.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MyCartFragment extends Fragment {

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth auth;
    Toolbar toolbar;
    RecyclerView recyclerView;
    MyCartAdapter myCartAdapter;
    List<MyCartModel> cartModelList;
    TextView overTotalAmount;
    Button proceedToOrder;
    ProgressDialog progressDialog;
    RelativeLayout relativeLayout;
    ScrollView scrollView;
    int counter = 0;
    int i=1;
    int k=1;
    String payableAmount;
    LocationManager locationManager;
    StringBuffer buffer;
    String bookedItem;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public MyCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        toolbar = view.findViewById(R.id.backPage_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Cart");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), HomePageActivity.class));
                getActivity().finish();

            }
        });

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Fetching Your Records");
        progressDialog.setCancelable(false);
        progressDialog.show();

        preferences = getContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        editor = preferences.edit();

        buffer = new StringBuffer();

        relativeLayout = (RelativeLayout) view.findViewById(R.id.empty_cart);
        scrollView = (ScrollView) view.findViewById(R.id.scroller);


        proceedToOrder = (Button) view.findViewById(R.id.proceed_To_Order);
        overTotalAmount = (TextView) view.findViewById(R.id.total_Amount);


        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mMessageReceiver, new IntentFilter("MyTotalAmount"));


        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(newMassage, new IntentFilter("RefreshData"));


        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        recyclerView = view.findViewById(R.id.OrecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        cartModelList = new ArrayList<>();
        myCartAdapter = new MyCartAdapter(getActivity(), cartModelList);
        recyclerView.setAdapter(myCartAdapter);

        checkNetwork();
        loadCardData();

        proceedToOrder.setOnClickListener(Mylistener);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);


        return view;
    }

    private void checkNetwork()
    {
        if (!isNetworkAvailable(getActivity(), true))
            return;
    }

    private void loadCardData()
    {
        firebaseFirestore.collection("AddToCart")
                .document(auth.getCurrentUser().getUid())
                .collection("CurrentUser").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    String emptyCard = "Empty";
                    editor.putString("EmptyCard",emptyCard);
                    editor.apply();
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Your cart is Empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (preferences.contains("EmptyCard"))
                    {
                        preferences.edit().remove("EmptyCard").apply();
                    }
                    progressDialog.dismiss();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists()) {
                            counter = queryDocumentSnapshots.size();
                            String documentID = documentSnapshot.getId();
                            MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
                            cartModel.setDocumentID(documentID);
                            cartModelList.add(cartModel);
                            myCartAdapter.notifyDataSetChanged();

                            buffer.append(i).append(" ) ").append(cartModel.getProductName()).append(" (Oty : ").append(cartModel.getProductQuantity()).append(")").append("\n");
                            i++;
                        }
                    }
                    if (buffer != null)
                    {
                        buffer.deleteCharAt(buffer.lastIndexOf("")-1);
                        bookedItem = buffer.toString();
                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error"+e, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public BroadcastReceiver newMassage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            buffer = new StringBuffer();
            refreshCardData();
        }
    };




    private void refreshCardData()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                firebaseFirestore.collection("AddToCart")
                        .document(auth.getCurrentUser().getUid())
                        .collection("CurrentUser").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                        {
                            if (documentSnapshot.exists()) {
                                counter = queryDocumentSnapshots.size();
                                String documentID = documentSnapshot.getId();
                                MyCartModel cartModel = documentSnapshot.toObject(MyCartModel.class);
                                cartModel.setDocumentID(documentID);
                                cartModelList.add(cartModel);
                                myCartAdapter.progress.dismiss();
                                myCartAdapter.notifyDataSetChanged();

                                buffer.append(k).append(" ) ").append(cartModel.getProductName()).append(" (Oty : ").append(cartModel.getProductQuantity()).append(")").append("\n");
                                k++;
                            }

                        }
                        if (buffer != null) {
                            buffer.deleteCharAt(buffer.lastIndexOf("")-1);
                            bookedItem = buffer.toString();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error"+e, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();

    }


    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            overTotalAmount.setText(intent.getStringExtra("toTotalAmount").toString());

            payableAmount = intent.getStringExtra("toTotalAmount").toString();

        }
    };



    private final View.OnClickListener Mylistener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                case R.id.proceed_To_Order:
                    if (preferences.contains("payableAmount")) {
                        Toast.makeText(getActivity(), "Your cart is Empty", Toast.LENGTH_SHORT).show();
                    }
                    else if (preferences.contains("EmptyCard")) {
                        Toast.makeText(getActivity(), "Your cart is Empty", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (isGPSenable()) {
                            sendData();
                        }
                    }

                    break;

                default:
                    break;
            }
        }

    };

    private boolean isGPSenable() {
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (providerEnable) {
            return true;
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle("GPS Permission")
                    .setMessage("GPS is required for this app to work.Please enable GPS")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
        return false;
    }

    private void sendData() {
        PersonalDetailsFragment dataFromOrderNowFragment = new PersonalDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TotalAmount", payableAmount);
        bundle.putString("NumbersOfItems", String.valueOf(counter));
        bundle.putString("bookedItems",bookedItem);
        dataFromOrderNowFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction().replace(R.id.assembleOrder, dataFromOrderNowFragment).commit();
    }


}