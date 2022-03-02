package com.example.mastercheffood.Fragments;

import static com.example.mastercheffood.Network.NetworkUtil.isNetworkAvailable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mastercheffood.Activities.OrderActivity;
import com.example.mastercheffood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class SearchMenuDetailsFragment extends Fragment {

    String name, price, image, details;
    ImageView mImage;
    TextView mName, mPrice, mDetails;
    ImageView increment, decrement,back;
    TextView quantity;
    Button addToCard;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String totalAmount;
    ProgressDialog progressDialog;

    public SearchMenuDetailsFragment() {
        // Required empty public constructor
    }

    public SearchMenuDetailsFragment(String name, String price, String image, String details) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.details = details;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_menu_details, container, false);

        quantity = (TextView) view.findViewById(R.id.setQuantity);
        increment = (ImageView) view.findViewById(R.id.plus);
        decrement = (ImageView) view.findViewById(R.id.minus);
        addToCard = (Button) view.findViewById(R.id.search_add_to_cart);



        back = (ImageView) view.findViewById(R.id.back);
        mImage = (ImageView) view.findViewById(R.id.item_img);
        mName = (TextView) view.findViewById(R.id.item_name);
        mDetails = (TextView) view.findViewById(R.id.item_details);
        mPrice = (TextView) view.findViewById(R.id.item_price);

        mName.setText(name);
        mDetails.setText(details);
        mPrice.setText(price);
        Glide.with(getContext()).load(image).into(mImage);


        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        addToCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                //Without this user can hide loader by tapping outside screen
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                addedToCard();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        checkNetwork();
        setMenuQuentity();

        return view;
    }

    private void checkNetwork()
    {
        if (!isNetworkAvailable(getActivity(), true))
            return;
    }

    private void addedToCard() {

        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        String productPrice = price;
        int price = Integer.parseInt(productPrice);


        String productQuantity = quantity.getText().toString().trim();
        int count = Integer.parseInt(productQuantity);

        int finalPrice = price * count;

        totalAmount = String.valueOf(finalPrice);

        final HashMap<String, Object> cartMap = new HashMap<>();

        cartMap.put("productName", name);
        cartMap.put("productPrice", productPrice);
        cartMap.put("productQuantity", quantity.getText().toString().trim());
        cartMap.put("totalPrice", totalAmount);
        cartMap.put("currentDate", saveCurrentDate);
        cartMap.put("currentTime", saveCurrentTime);
        cartMap.put("image",image);


        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {

                DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setContentHolder(new ViewHolder(R.layout.add_to_cart_confirmation_popup))
                        .setExpanded(true, 900)
                        .create();

                View root = dialogPlus.getHolderView();
                TextView showItemName = root.findViewById(R.id.show_product_name);
                TextView showItemPrice = (TextView) root.findViewById(R.id.show_price);
                TextView showItemQuantity = (TextView) root.findViewById(R.id.show_quantity);
                ImageView showImage = (ImageView) root.findViewById(R.id.show_product_image);
                ImageView closePopupDialog = (ImageView) root.findViewById(R.id.cross);
                Button btnGoToCart = (Button) root.findViewById(R.id.go_to_cart);

                showItemName.setText(name);
                showItemPrice.setText(totalAmount);
                showItemQuantity.setText(quantity.getText().toString().trim());
                Glide.with(getActivity()).load(image).into(showImage);

                closePopupDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPlus.dismiss();
                    }
                });

                progressDialog.dismiss();
                dialogPlus.show();
                Toast.makeText(getActivity(), "Item Added Successfully", Toast.LENGTH_LONG).show();

                btnGoToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getActivity(), OrderActivity.class));
                        dialogPlus.dismiss();
                        getActivity().finish();
                    }
                });


            }
        });


    }


    private void setMenuQuentity() {


        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(String.valueOf(quantity.getText()));
                count++;
                quantity.setText("" + count);
            }
        });


        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int count = Integer.parseInt(String.valueOf(quantity.getText()));
                if (count == 1) {
                    quantity.setText("1");
                } else {
                    count -= 1;
                    quantity.setText("" + count);
                }


            }
        });

    }

    public void onBackPressed() {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new SearchFragment()).addToBackStack(null).commit();
    }

}