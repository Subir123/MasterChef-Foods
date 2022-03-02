package com.example.mastercheffood.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static com.example.mastercheffood.Network.NetworkUtil.isNetworkAvailable;

import android.os.Environment;
import android.os.StrictMode;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;


public class AddToCardFragment extends Fragment {

    String name, price, image, details;
    ImageView mImage;
    TextView mName, mPrice, mDetails;
    ImageView increment, decrement, back,share;
    TextView quantity;
    Button addToCard;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String totalAmount;
    ProgressDialog progressDialog;

    public AddToCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_to_card, container, false);

        quantity = (TextView) view.findViewById(R.id.quantity);
        increment = (ImageView) view.findViewById(R.id.increment);
        decrement = (ImageView) view.findViewById(R.id.decrement);


        back = (ImageView) view.findViewById(R.id.Pback);
        share = (ImageView) view.findViewById(R.id.Pshare);
        mImage = (ImageView) view.findViewById(R.id.menu_item_img);
        mName = (TextView) view.findViewById(R.id.menu_item_name);
        mDetails = (TextView) view.findViewById(R.id.menu_item_details);
        mPrice = (TextView) view.findViewById(R.id.menu_item_price);
        addToCard = (Button) view.findViewById(R.id.Padd_to_cart);



        name = getActivity().getIntent().getStringExtra("name").toString();
        price = getActivity().getIntent().getStringExtra("price").toString();
        details = getActivity().getIntent().getStringExtra("details").toString();
        image = getActivity().getIntent().getStringExtra("image").toString();


        mName.setText(name);
        mPrice.setText(price);
        mDetails.setText(details);
        Glide.with(this).load(image).into(mImage);

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

//////////////////////////////////////////////////////////////////////////////////////////////

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareScreenShot();
            }
        });
///////////////////////////////////////////////////////////////////////////////////////////////
        checkNetwork();
        setMenuQuantity();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        return view;
    }


    private void checkNetwork()
    {
        if (!isNetworkAvailable(getActivity(), true))
            return;
    }

    private void shareScreenShot()
    {
        ActivityCompat.requestPermissions(getActivity(),new String[] {WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); // To run this app on above api level 24 we have too add this line//
        StrictMode.setVmPolicy(builder.build());

        View view = getActivity().getWindow().getDecorView().getRootView();
        view.setDrawingCacheEnabled(true);

        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm a");
        String time = currentTime.format(Calendar.getInstance().getTime());

        String filePath = Environment.getExternalStorageDirectory()+"/Download/"+name+".jpg"+" - "+time;

        File fileScreenshot = new File(filePath);

        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(fileScreenshot);
            bitmap.compress(Bitmap.CompressFormat.JPEG,90,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(getContext(), "ScreenShot Taken Successfully..", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(fileScreenshot);
        intent.setDataAndType(uri,"image/jpeg");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    private void addedToCard() {

        String saveCurrentDate, saveCurrentTime;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());


        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());


        String productPrice = getActivity().getIntent().getStringExtra("price").toString();
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error :"+e, Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

    private void setMenuQuantity() {


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
}