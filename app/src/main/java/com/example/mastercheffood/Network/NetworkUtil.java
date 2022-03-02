package com.example.mastercheffood.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.mastercheffood.R;

public class NetworkUtil {

    public static String networkErrorMessage = "Seems like you are not connected to \n the internet.Please try again later";
    public static boolean checkInternetConnection = true;

    public static boolean isNetworkAvailable(Context context, boolean showErrorMessage) {

        if (checkInternetConnection) {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connectivityManager != null;
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnectedOrConnecting())
                return true;
            else {
                if (showErrorMessage)
                    mymassage(context);

                return false;
            }
        } else
            return true;

    }


    private static void mymassage(Context context)
    {
        Toast.makeText(context, networkErrorMessage, Toast.LENGTH_LONG).show();

        ViewGroup parent = new ViewGroup(context) {
            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {

            }
        };

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//        final View customLayout = getLayoutInflater().inflate(R.layout.no_internet, null);
        View customLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.no_internet,parent,false);
        alertDialog.setView(customLayout);

        AlertDialog alert = alertDialog.create();
        alert.show();

        TextView textView = customLayout.findViewById(R.id.tryagain);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();
            }
        });

    }
}
