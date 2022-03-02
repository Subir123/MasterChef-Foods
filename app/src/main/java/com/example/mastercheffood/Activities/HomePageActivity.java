package com.example.mastercheffood.Activities;



import static com.example.mastercheffood.Network.NetworkUtil.isNetworkAvailable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mastercheffood.Adapters.DeliveryReportAdapter;
import com.example.mastercheffood.Adapters.OrderHistoryAdapter;
import com.example.mastercheffood.Adapters.SliderAdapter;
import com.example.mastercheffood.Adapters.PopularMenuAdapter;
import com.example.mastercheffood.DataModel.DeliveryReportModel;
import com.example.mastercheffood.DataModel.OrderHistoryModel;
import com.example.mastercheffood.DataModel.ShowUserProfileModel;
import com.example.mastercheffood.DataModel.SliderDataModel;
import com.example.mastercheffood.DataModel.PopularMenuModel;
import com.example.mastercheffood.Fragments.Nonveg_Only_Fragment;
import com.example.mastercheffood.Fragments.Veg_Only_Fragment;
import com.example.mastercheffood.R;
import com.example.mastercheffood.Adapters.ShowUserProfileAdapter;
import com.facebook.Profile;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nex3z.notificationbadge.NotificationBadge;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePageActivity extends AppCompatActivity {

    private ArrayList<SliderDataModel> sliderDataArrayList;
    private SliderAdapter myadapter;
    private SliderView sliderView;
    GoogleSignInAccount account;
    GoogleSignInClient mGoogleSignInClient;
    CircleImageView profileImage;
    TextView profile_Name,viewPopularMenu;
    NotificationBadge notificationBadge;
    int counter = 0;
    ImageView shoppingCart;
    FirebaseAuth auth;
    FirebaseFirestore db;
    Button btnsearch;
    PopularMenuAdapter adapter;
    RecyclerView recview;
    TextView bestMenu;
    Switch aSwitch;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        notificationBadge = (NotificationBadge) findViewById(R.id.badge);
        shoppingCart = (ImageView)  findViewById(R.id.cart_icon);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        MaterialToolbar toolbar = findViewById(R.id.topAppbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                drawerLayout.closeDrawer(GravityCompat.START);

                switch (item.getItemId())
                {
                    case R.id.nav_about:
                        showAboutUsDetails();
                        break;

                    case R.id.nav_profile:
                        showUserProfile();
                        break;

                    case R.id.my_cart:
                        startActivity(new Intent(getApplicationContext(), OrderActivity.class));
                        break;

                    case R.id.nav_Orders:
                        showOrder();

                    case R.id.nav_history:
                        showOrderHistory();
                        break;

                    case R.id.nav_logout:
                        signOut();
                        break;


                    default:
                        break;
                }
                return true;
            }
        });

        View headerView = navigationView.getHeaderView(0);
        profileImage = (CircleImageView) headerView.findViewById(R.id.profile_image);
        profile_Name = (TextView) headerView.findViewById(R.id.profile_name);

        account = GoogleSignIn.getLastSignedInAccount(this);

        btnsearch = (Button) findViewById(R.id.searchbar);
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SearchActivity.class));
            }
        });

        viewPopularMenu = (TextView) findViewById(R.id.view_all);
        viewPopularMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PopulerMenuViewAllActivity.class));
            }
        });

        sliderDataArrayList = new ArrayList<>();
        sliderView = findViewById(R.id.slider);

        recview = (RecyclerView) findViewById(R.id.recview);

        checkNetwork();
        getUserDetails();
        loadNotificatinCount();
        loadSliderImages();
        loadPopulerMenu();
        veg_Nonveg_Menu_toolbar();
        loadVegMenu();

    }

    private void checkNetwork()
    {
        if (!isNetworkAvailable(HomePageActivity.this, true))
            return;
    }

    private void showOrder()
    {
        DialogPlus dialogPlus = DialogPlus.newDialog(HomePageActivity.this)
                .setContentHolder(new ViewHolder(R.layout.pending_orders_popup_dialog))
                .setExpanded(true,900)
                .create();

        View view = dialogPlus.getHolderView();
        ImageView closePopupDialog4 = (ImageView) view.findViewById(R.id.close4);
        RecyclerView recyclerView4 = view.findViewById(R.id.recyclerView4);

        recyclerView4.setLayoutManager(new LinearLayoutManager(HomePageActivity.this));
        List<DeliveryReportModel> deliverymodel = new ArrayList<>();
        DeliveryReportAdapter adapter = new DeliveryReportAdapter(HomePageActivity.this,deliverymodel);
        recyclerView4.setAdapter(adapter);

        db.collection("DeliveryReport").document(auth.getCurrentUser().getUid())
                .collection("Orders").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty())
                {
                    Toast.makeText(HomePageActivity.this, "Your didn't booked any items yet ", Toast.LENGTH_LONG).show();
                }
                else
                {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists())
                        {
                            String documentID = documentSnapshot.getId();
                            DeliveryReportModel reportModel = documentSnapshot.toObject(DeliveryReportModel.class);
                            reportModel.setDocumentID(documentID);
                            deliverymodel.add(reportModel);
                            adapter.notifyDataSetChanged();
                        }}
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        closePopupDialog4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
            }
        });

        dialogPlus.show();
    }

    private void showUserProfile()
    {
        DialogPlus profileDialog = DialogPlus.newDialog(HomePageActivity.this)
                .setContentHolder(new ViewHolder(R.layout.show_user_profile_popup_dialog))
                .setExpanded(true, 1000)
                .create();

        View root2 = profileDialog.getHolderView();
        ImageView closePopupDialog2 = (ImageView) root2.findViewById(R.id.close2);
        RecyclerView recyclerView2 = root2.findViewById(R.id.recyclerView2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(HomePageActivity.this));
        List<ShowUserProfileModel> models = new ArrayList<>();
        ShowUserProfileAdapter userProfileAdapter = new ShowUserProfileAdapter(HomePageActivity.this,models);
        recyclerView2.setAdapter(userProfileAdapter);

        db.collection("PersonalDetails")
                .document(auth.getCurrentUser().getUid())
                .collection("Details").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty())
                {
                    Toast.makeText(HomePageActivity.this, "Your Profile is Empty", Toast.LENGTH_LONG).show();
                }
                else
                {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists())
                        {
                            String documentID = documentSnapshot.getId();
                            ShowUserProfileModel model = documentSnapshot.toObject(ShowUserProfileModel.class);
                            model.setDocumentID(documentID);
                            models.add(model);
                            userProfileAdapter.notifyDataSetChanged();

                        }}
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        closePopupDialog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileDialog.dismiss();
            }
        });

        profileDialog.show();
    }

    private void showOrderHistory()
    {
        DialogPlus dialog = DialogPlus.newDialog(HomePageActivity.this)
                .setContentHolder(new ViewHolder(R.layout.order_history_popup_dialog))
                .setExpanded(true, 1000)
                .create();

        View root1 = dialog.getHolderView();
        ImageView closePopupDialog1 = (ImageView) root1.findViewById(R.id.close1);

        RecyclerView recyclerView = root1.findViewById(R.id.recyclerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomePageActivity.this));
        List<OrderHistoryModel> models = new ArrayList<>();
        OrderHistoryAdapter historyAdapter = new OrderHistoryAdapter(HomePageActivity.this,models);
        recyclerView.setAdapter(historyAdapter);

        db.collection("OrderHistory")
                .document(auth.getCurrentUser().getUid())
                .collection("History").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty())
                {
                    Toast.makeText(HomePageActivity.this, "No order history available", Toast.LENGTH_LONG).show();
                }
                else
                {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        if (documentSnapshot.exists())
                        {
                            String documentID = documentSnapshot.getId();
                            OrderHistoryModel model = documentSnapshot.toObject(OrderHistoryModel.class);
                            model.setDocumentID(documentID);
                            models.add(model);
                            historyAdapter.notifyDataSetChanged();

                        }}
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        closePopupDialog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void showAboutUsDetails()
    {
        DialogPlus dialogPlus = DialogPlus.newDialog(HomePageActivity.this)
                .setContentHolder(new ViewHolder(R.layout.aboutus_popup_dialog))
                .setGravity(Gravity.CENTER)
                .setExpanded(true, 600)
                .setOnBackPressListener(new OnBackPressListener() {
                    @Override
                    public void onBackPressed(DialogPlus dialogPlus) {
                        dialogPlus.dismiss();
                    }
                })
                .create();

        View root = dialogPlus.getHolderView();
        ImageView closePopupDialog = (ImageView) root.findViewById(R.id.close);

        closePopupDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPlus.dismiss();
            }
        });

        dialogPlus.show();
    }

    private void loadNotificatinCount()
    {
        db.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("CurrentUser").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots)
                {
                    counter = queryDocumentSnapshots.size();
                    notificationBadge.setNumber(counter);
                }

            }
        });

        notificationBadge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OrderActivity.class));
                finish();
            }
        });

        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), OrderActivity.class));
                finish();
            }
        });
    }

    private void loadSliderImages() {
        // getting data from our collection and after
        // that calling a method for on success listener.
        db.collection("Slider").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                // inside the on success method we are running a for loop
                // and we are getting the data from Firebase Firestore
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    // after we get the data we are passing inside our object class.
                    SliderDataModel sliderData = documentSnapshot.toObject(SliderDataModel.class);
                    SliderDataModel model = new SliderDataModel();
                    model.setTitle(sliderData.getTitle());

                    // below line is use for setting our
                    // image url for our modal class.
                    model.setImgUrl(sliderData.getImgUrl());

                    // after that we are adding that
                    // data inside our array list.
                    sliderDataArrayList.add(model);

                    // after adding data to our array list we are passing
                    // that array list inside our adapter class.
                    myadapter = new SliderAdapter(HomePageActivity.this, sliderDataArrayList);

                    // belows line is for setting adapter
                    // to our slider view
                    sliderView.setSliderAdapter(myadapter);

                    sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);

                    sliderView.setIndicatorSelectedColor(Color.WHITE);
                    sliderView.setIndicatorUnselectedColor(Color.GRAY);

                    // below line is for setting animation to our slider.
                    sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

                    // below line is for setting auto cycle duration.
                    sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

                    // below line is for setting
                    // scroll time animation
                    sliderView.setScrollTimeInSec(5);

                    // below line is for setting auto
                    // cycle animation to our slider
                    sliderView.setAutoCycle(true);

                    // below line is use to start
                    // the animation of our slider view.
                    sliderView.startAutoCycle();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // if we get any error from Firebase we are
                // displaying a toast message for failure
                Toast.makeText(HomePageActivity.this, "Fail to load slider data..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadPopulerMenu() {
        FirebaseRecyclerOptions<PopularMenuModel> options =
                new FirebaseRecyclerOptions.Builder<PopularMenuModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("PopulerMenu"), PopularMenuModel.class)
                        .build();

        adapter = new PopularMenuAdapter(options);
        recview.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recview.setLayoutManager(layoutManager);
        adapter.startListening();
    }

    private void veg_Nonveg_Menu_toolbar()
    {
        bestMenu = (TextView) findViewById(R.id.bestmenu);
        aSwitch = (Switch) findViewById(R.id.menu_change_switch);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (aSwitch.isChecked()) {
                    aSwitch.setText("Nonveg Only");
                    aSwitch.setTextColor(Color.RED);

                    FragmentManager nonvegfragmentManager = getSupportFragmentManager();
                    FragmentTransaction nonvegtransaction = nonvegfragmentManager.beginTransaction();
                    nonvegtransaction.replace(R.id.fragment_container2, new Nonveg_Only_Fragment());
                    nonvegtransaction.addToBackStack(null);
                    nonvegtransaction.commit();

                } else {
                    aSwitch.setText("Veg Only");
                    loadVegMenu();
                }
            }
        });
    }

    private void loadVegMenu() {
        aSwitch.setTextColor(Color.GRAY);
        FragmentManager vegfragmentManager = getSupportFragmentManager();
        FragmentTransaction vegtransaction = vegfragmentManager.beginTransaction();
        vegtransaction.replace(R.id.fragment_container2, new Veg_Only_Fragment());
        vegtransaction.addToBackStack(null);
        vegtransaction.commit();
    }

    private void getUserDetails()
    {
        if (account != null) {
            profile_Name.setText(account.getDisplayName());
            Glide.with(this).load(account.getPhotoUrl()).into(profileImage);
        }
        else if(Profile.getCurrentProfile() != null)
        {
            Profile profile = Profile.getCurrentProfile();
            String facebook_name = profile.getName();

            String profile_image = profile.getProfilePictureUri(60,60).toString();

            profile_Name.setText(facebook_name);
            Glide.with(this).load(profile_image).into(profileImage);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNotificatinCount();
    }

    private void signOut()
    {
        if (account != null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            mGoogleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(getApplicationContext(), "Successfully SignOut", Toast.LENGTH_SHORT).show();

                            SharedPreferences preferences = getApplicationContext().getSharedPreferences("credentials",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove("payableAmount");
                            editor.remove("bookedItems");
                            editor.apply();

                            startActivity(new Intent(getApplicationContext(), LoginPageActivity.class));
                            finish();
                        }
                    });
        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();

            SharedPreferences preferences = getApplicationContext().getSharedPreferences("credentials", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("payableAmount");
            editor.remove("bookedItems");
            editor.apply();

            startActivity(new Intent(getApplicationContext(), LoginPageActivity.class));
            finish();
        }
    }
}