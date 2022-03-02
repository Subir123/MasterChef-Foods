package com.example.mastercheffood.Activities;

import static com.example.mastercheffood.Network.NetworkUtil.isNetworkAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mastercheffood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class RazorpayActivity extends AppCompatActivity implements PaymentResultListener {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String name,email,mobile,address,orderAmount,totalAmount,Itemsbooked,bookingId;
    String saveCurrentDate, saveCurrentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_razorpay);

        Checkout.preload(getApplicationContext());

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);

        name = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("useremail");
        mobile = getIntent().getStringExtra("usermobile");
        address = getIntent().getStringExtra("useraddress");
        orderAmount = getIntent().getStringExtra("orderAmount");
        totalAmount = getIntent().getStringExtra("totalAmountToPay");
        Itemsbooked = preferences.getString("bookedItems","");

        checkNetwork();
        makepayment();
    }

    private void checkNetwork()
    {
        if (!isNetworkAvailable(RazorpayActivity.this, true))
            return;
    }

    private void makepayment()
    {
        int total = Integer.parseInt(getIntent().getStringExtra("totalAmountToPay"))*100;
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_rdm4YsnmpedH3T");

        checkout.setImage(R.drawable.logo);
        final Activity activity = this;

        try {
            JSONObject options = new JSONObject();

            options.put("name", "MasterChef Foods");
            options.put("description", "MasterChef Foods Ordering App");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            // options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount",String.valueOf(total) );  //amount X 100
            options.put("prefill.email", getIntent().getStringExtra("useremail"));
            options.put("prefill.contact",getIntent().getStringExtra("usermobile"));
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);

        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        bookingId = s;
        deletePersonalDetails();

    }

    private void deletePersonalDetails()
    {
        firestore.collection("PersonalDetails")
                .document(auth.getCurrentUser().getUid())
                .collection("Details").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty())
                {
                    savePersonalDetails();
                }
                else
               {
                   for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                   firestore.collection("PersonalDetails")
                           .document(auth.getCurrentUser().getUid())
                           .collection("Details").document(documentSnapshot.getId())
                           .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                       }
                   });
               }

                   savePersonalDetails();
               }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    private void savePersonalDetails()
    {
        final HashMap<String,Object> personalDetails = new HashMap<>();
        personalDetails.put("personName",name);
        personalDetails.put("personEmail",email);
        personalDetails.put("personMobile",mobile);
        personalDetails.put("personAddress",address);

        firestore.collection("PersonalDetails")
                .document(auth.getCurrentUser().getUid())
                .collection("Details").add(personalDetails).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error!!!"+e, Toast.LENGTH_SHORT).show();
            }
        });

        saveOrder();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(getApplicationContext(), "Payment Failed and cause is :"+s, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        finish();
    }

    private void saveOrder()
    {

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> orderData = new HashMap<>();

        orderData.put("personName",name);
        orderData.put("personEmail",email);
        orderData.put("personMobile",mobile);
        orderData.put("personAddress",address);
        orderData.put("orderAmount",orderAmount);
        orderData.put("totalPayableAmount",totalAmount);
        orderData.put("currentDate", saveCurrentDate);
        orderData.put("currentTime", saveCurrentTime);
        orderData.put("itemsBooked",Itemsbooked);

        firestore.collection("OrderSummery")
                .document(auth.getCurrentUser().getUid())
                .collection("Order").add(orderData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error!!!"+e, Toast.LENGTH_SHORT).show();
            }
        });

        final HashMap<String,Object> orderHistory = new HashMap<>();
        orderHistory.put("personName",name);
        orderHistory.put("itemsBooked",Itemsbooked);
        orderHistory.put("currentDate", saveCurrentDate);
        orderHistory.put("orderAmount",orderAmount);

        firestore.collection("OrderHistory")
                .document(auth.getCurrentUser().getUid())
                .collection("History").add(orderHistory).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error!!!"+e, Toast.LENGTH_SHORT).show();
            }
        });

        deleteCartItems();
    }

    private void deleteCartItems()
    {
        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful())
                {
                    for (final QueryDocumentSnapshot document : task.getResult())
                    {
                        if (document.exists())
                        {
                            firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                                    .collection("CurrentUser").document(document.getId())
                                    .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

        showOrderConfirmation();
    }

    private void showOrderConfirmation()
    {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
        calForDate.add(Calendar.HOUR,1);

        String deliveryTime = currentTime.format(calForDate.getTime());

        final HashMap<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("email",email);
        map.put("mobile",mobile);
        map.put("address",address);
        map.put("date",saveCurrentDate);
        map.put("bookingTime",saveCurrentTime);
        map.put("deliveryTime",deliveryTime);
        map.put("bookingId",bookingId);
        map.put("bookedItems",Itemsbooked);
        map.put("orderAmount",orderAmount);
        map.put("payableAmount",totalAmount);

        firestore.collection("DeliveryReport").document(auth.getCurrentUser().getUid())
                .collection("Orders").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                DialogPlus dialogPlus = DialogPlus.newDialog(RazorpayActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.order_confirmation_popup))
                        .setExpanded(true, 900)
                        .setOnBackPressListener(new OnBackPressListener() {
                            @Override
                            public void onBackPressed(DialogPlus dialogPlus) {
                                dialogPlus.dismiss();
                                startActivity(new Intent(RazorpayActivity.this, HomePageActivity.class));
                                finish();
                            }
                        }).create();

                View root = dialogPlus.getHolderView();
                ImageView closePopupDialog = (ImageView) root.findViewById(R.id.close3);
                TextView name1 = (TextView) root.findViewById(R.id.name3);
                TextView bookingDate1 = (TextView) root.findViewById(R.id.booking_date3);
                TextView bookingTime1 = (TextView) root.findViewById(R.id.booking_time3);
                TextView bookingTime2 = (TextView) root.findViewById(R.id.time_at);
                TextView deliveryTime1 = (TextView) root.findViewById(R.id.time_within);
                TextView bookingId1 = (TextView) root.findViewById(R.id.booking_id3);
                TextView bookingItems1 = (TextView) root.findViewById(R.id.items3);
                TextView bookingAmount1 = (TextView) root.findViewById(R.id.amount3);

                name1.setText(name);
                bookingDate1.setText(saveCurrentDate);
                bookingTime1.setText(saveCurrentTime);
                bookingTime2.setText(saveCurrentTime);
                deliveryTime1.setText(deliveryTime);
                bookingId1.setText(bookingId);
                bookingItems1.setText(Itemsbooked);
                bookingAmount1.setText(orderAmount);

                closePopupDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPlus.dismiss();
                        startActivity(new Intent(RazorpayActivity.this, HomePageActivity.class));
                        finish();
                    }
                });

                dialogPlus.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

}