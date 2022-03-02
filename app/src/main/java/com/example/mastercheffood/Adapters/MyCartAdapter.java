package com.example.mastercheffood.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.mastercheffood.DataModel.MyCartModel;
import com.example.mastercheffood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.MyViewholder> {

    Context context;
    List<MyCartModel> cartModelList;
    int grandTotal;
    String finalAmount;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    public ProgressDialog progress;


    public MyCartAdapter(Context context, List<MyCartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

    }

    @NonNull
    @Override
    public MyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_singlerow, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewholder holder, int position) {

        holder.name.setText(cartModelList.get(position).getProductName());
        holder.price.setText(cartModelList.get(position).getProductPrice());
        holder.quantity.setText(cartModelList.get(position).getProductQuantity());
        holder.total.setText(cartModelList.get(position).getTotalPrice());
        Glide.with(holder.productImage.getContext()).load(cartModelList.get(position).getImage()).into(holder.productImage);
        holder.quantityValue.setText(cartModelList.get(position).getProductQuantity());


        holder.spinner.setOnClickListener(new View.OnClickListener() {
            final EditText editText = new EditText(context);
            @Override
            public void onClick(View v) {
                if (editText.getParent() != null){
                    ((ViewGroup)editText.getParent()).removeView(editText);
                }

                AlertDialog.Builder alartbuilder = new AlertDialog.Builder(context);
                alartbuilder.setTitle("Enter Quantity");
                editText.setHint("Quantity");
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                alartbuilder.setView(editText);
                alartbuilder.setCancelable(true);


                alartbuilder.setPositiveButton("APPLY", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        if (editText.getText().toString().trim().isEmpty())
                        {
                            Toast.makeText(context, "Quantity can't be empty", Toast.LENGTH_LONG).show();
                            dialogInterface.dismiss();
                        }
                        else {
                            String editTextValue =  editText.getText().toString().trim();
                            int count = Integer.parseInt(String.valueOf(editText.getText().toString().trim()));
                            if (count >= 1)
                            {
                                updateQuantity();
                            }
                            else
                            {
                                Toast.makeText(context, "Quantity can't be "+editTextValue, Toast.LENGTH_LONG).show();
                                dialogInterface.dismiss();
                            }
                        }}
                });

                alartbuilder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog dialog1 = alartbuilder.create();

                dialog1.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog1.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                        dialog1.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.BLACK);
                    }
                });

                dialog1.show();

            }

            public void updateQuantity()
            {

                String saveCurrentDate, saveCurrentTime;

                Calendar calForDate = Calendar.getInstance();

                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());


                SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");
                saveCurrentTime = currentTime.format(calForDate.getTime());

                String productPrice = cartModelList.get(holder.getAdapterPosition()).getProductPrice();
                int price = Integer.parseInt(productPrice);

                String productQuantity = editText.getText().toString().trim();
                int counts = Integer.parseInt(productQuantity);

                int finalPrice = price * counts;

                String totalAmount = String.valueOf(finalPrice);

                final HashMap<String, Object> cartMap = new HashMap<>();

                cartMap.put("productName", cartModelList.get(holder.getAdapterPosition()).getProductName());
                cartMap.put("productPrice", productPrice);
                cartMap.put("productQuantity", productQuantity);
                cartMap.put("totalPrice", totalAmount);
                cartMap.put("currentDate", saveCurrentDate);
                cartMap.put("currentTime", saveCurrentTime);
                cartMap.put("image",cartModelList.get(holder.getAdapterPosition()).getImage());


                firestore.collection("AddToCart")
                        .document(auth.getCurrentUser().getUid())
                        .collection("CurrentUser")
                        .document(cartModelList.get(holder.getAdapterPosition()).getDocumentID())
                        .update(cartMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful())
                                {
                                    cartModelList.clear();
                                    progress = new ProgressDialog(context);
                                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progress.setMessage("Please Wait");
                                    progress.setCancelable(false);
                                    progress.show();
                                    Intent intent2 = new Intent("RefreshData");
                                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent2);
                                    grandTotal = 0;
                                    notifyDataSetChanged();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error :"+e, Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove Item");
                builder.setMessage("Are you sure you want to remove this item?");

                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteCartItem();


                    }
                });

                builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLUE);
                        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.BLACK);
                    }
                });

                dialog.show();


            }

            private void deleteCartItem() {
                firestore.collection("AddToCart")
                        .document(auth.getCurrentUser().getUid())
                        .collection("CurrentUser")
                        .document(cartModelList.get(holder.getAdapterPosition()).getDocumentID())
                        .delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful())
                                {
                                    cartModelList.remove(holder.getAbsoluteAdapterPosition());
                                    Toast.makeText(context, "Item Deleted from Cart", Toast.LENGTH_LONG).show();

                                    if (cartModelList.size() == 0)
                                    {
                                        Toast.makeText(context, "Your cart is Empty", Toast.LENGTH_LONG).show();
                                        finalAmount = "0.00";
                                        Intent intent1 = new Intent("MyTotalAmount");
                                        intent1.putExtra("toTotalAmount", finalAmount);
                                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);

                                        SharedPreferences preferences = context.getSharedPreferences("credentials",Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("payableAmount",finalAmount);
                                        editor.apply();
                                    }

                                    grandTotal = 0;
                                    notifyDataSetChanged();
                                    notifyItemRemoved(holder.getPosition());

                                }
                                else
                                {
                                    Toast.makeText(context, "Error" + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }

                            }
                        });

            }
        });


        //Pass total amount to OrderNowFragment
        grandTotal += Integer.parseInt(cartModelList.get(position).getTotalPrice());
        finalAmount = String.valueOf(grandTotal);
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("toTotalAmount", finalAmount);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

        updateCartView();

    }



    private void updateCartView()
    {
        if (cartModelList.size() > 0)
        {
            SharedPreferences preferences = context.getSharedPreferences("credentials",Context.MODE_PRIVATE);
            if (preferences.contains("payableAmount"))
            {
                preferences.edit().remove("payableAmount").apply();
            }
        }
    }


    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public static class MyViewholder extends RecyclerView.ViewHolder {
        TextView name, price, quantity,total;
        ImageView deleteItem,productImage,spinner;
        TextView quantityValue;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.product_name);
            price = (TextView) itemView.findViewById(R.id.product_price);
            quantity = (TextView) itemView.findViewById(R.id.product_quantity);
            total = (TextView) itemView.findViewById(R.id.total_price);
            deleteItem = (ImageView) itemView.findViewById(R.id.delete);
            productImage = (ImageView) itemView.findViewById(R.id.productImage);
            quantityValue = (TextView) itemView.findViewById(R.id.quantity_value);
            spinner = (ImageView) itemView.findViewById(R.id.spinner_image);
        }
    }
}

