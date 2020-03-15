package com.pelotheban.insanehistoryquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    private AlertDialog dialog;
    private LinearLayout loutHomePageX, loutButtonsX;

    //Buttons

    private ImageView  imgBtnPlayX, imgBtnProfileX, imgBtnLeadersX, imgBtnPlayGlowX, imgBtnProfileGlowX, imgBtnLeadersGlowX;

    private ImageView imgBtnAdminX;

    //User Buttons

    private String popupMenuToggle;
    private FloatingActionButton fabPopUpHPX, fabPopUpCollHPX, fabPopUpFAQminiHPtX, fabPopUpLogOutminiHPX;
    private TextView txtFAQButtonHPX, txtLogoutButtonHPX;
    private View shadeX; // to shade the background when menu out

    // Firebase
    private FirebaseAuth mAuth;

    private DatabaseReference userReference;
    private Query sortUsersQuery;
    private String uid;


    //game counters

    private TextView txtCoinCounterX, txtConStreakX;
    private String coinGrantToggle;

    private String coinsOwnedString;
    private double coinsOwned;

    private String consStreetString;
    private int consStreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        loutHomePageX = findViewById(R.id.loutHomePage);
        loutButtonsX = findViewById(R.id.loutButtons);

        fabPopUpHPX = findViewById(R.id.fabPopUpHP);
        fabPopUpHPX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowNewFABbasedMenu();
            }
        });

        popupMenuToggle = "Not";

        fabPopUpCollHPX = findViewById(R.id.fabPopUpCollHP);
        fabPopUpFAQminiHPtX = findViewById(R.id.fabPopUpFAQminiHP);
        fabPopUpLogOutminiHPX = findViewById(R.id.fabPopUpLogOutminiHP);

        txtFAQButtonHPX = findViewById(R.id.txtFAQButtonHP);
        txtLogoutButtonHPX = findViewById(R.id.txtLogoutButtonHP);

        shadeX = findViewById(R.id.shade);

        //game counters

        txtCoinCounterX = findViewById(R.id.hpTxtCoinCounter);
        txtConStreakX = findViewById(R.id.hpTxtConStreak);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid);

        sortUsersQuery = FirebaseDatabase.getInstance().getReference().child("my_users").orderByChild("user").equalTo(uid);
        sortUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userDs: dataSnapshot.getChildren()) {

                    try {
                        consStreetString = userDs.child("constreak").getValue().toString();
                        consStreak = Integer.valueOf(consStreetString);
                    } catch (Exception e) {

                    }


                    try {
                        coinsOwnedString = userDs.child("coins").getValue().toString();
                        coinsOwned = Double.valueOf(coinsOwnedString);

                        coinGrantToggle = userDs.child("coinsgranttoggle").getValue().toString();

                       // Toast.makeText(HomePage.this,"Toggle " + coinGrantToggle + "  coins" + coinsOwned, Toast.LENGTH_LONG).show();


                    } catch (Exception e) {

                        //Toast.makeText(Game.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    txtCoinCounterX.setText(coinsOwnedString); // IF there are any coins in the account will set the counter
                    txtConStreakX.setText(consStreetString);

                    try {
                        if (coinsOwned > 0 | coinGrantToggle.equals("yes")) {

                            // Toast.makeText(Game.this, "NOT GRANTING", Toast.LENGTH_SHORT).show();

                        } else { // setting up grant if conditions for NOT GRANTING are unmet - BUT probably never invoked because
                            // if conditions unmet that just means that the "if" goes null gets caught by try and bounced to error
                            // before invoking the else - So.... repeating this AGAIN in the catch of the if

                            // Toast.makeText(Game.this, "Granting", Toast.LENGTH_SHORT).show();

                            userReference.getRef().child("coins").setValue(80);
                            userReference.getRef().child("coinsgranttoggle").setValue("yes");
                            userReference.getRef().child("totalansweredsort").setValue(1000);
                            userReference.getRef().child("longeststreaksort").setValue(1000);
                            userReference.getRef().child("coinsownedsort").setValue(-80);
                            txtCoinCounterX.setText("80");

                        }

                    } catch (Exception e) { // this is the catch of the if above and repeating the initial coin grant query as per notes above

                        //  Toast.makeText(Game.this, "Granting", Toast.LENGTH_SHORT).show();

                        userReference.getRef().child("coins").setValue(80);
                        userReference.getRef().child("coinsgranttoggle").setValue("yes");
                        txtCoinCounterX.setText("80");
                        userReference.getRef().child("totalansweredsort").setValue(1000);
                        userReference.getRef().child("longeststreaksort").setValue(1000);
                        userReference.getRef().child("coinsownedsort").setValue(-80);


                        //Toast.makeText(Game.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //Admin Button

        imgBtnAdminX = findViewById(R.id.imgBtnAdmin);
        imgBtnAdminX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomePage.this, AdminDir.class);
                startActivity(intent);

            }
        });

        imgBtnLeadersGlowX = findViewById(R.id.btnLeadersGlow);
        imgBtnLeadersX = findViewById(R.id.btnLeaders);
        imgBtnLeadersX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imgBtnLeadersX.setVisibility(View.GONE);
                imgBtnLeadersGlowX.setVisibility(View.VISIBLE);

                CountDownTimer glowTimer = new CountDownTimer(500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        imgBtnLeadersX.setVisibility(View.VISIBLE);
                        imgBtnLeadersGlowX.setVisibility(View.GONE);
                        Intent intent = new Intent(HomePage.this, LeaderBoard.class);
                        startActivity(intent);

                    }
                }.start();



            }
        });

        imgBtnProfileGlowX = findViewById(R.id.btnProfileGlow);
        imgBtnProfileX = findViewById(R.id.btnProfile);
        imgBtnProfileX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imgBtnProfileX.setVisibility(View.GONE);
                imgBtnProfileGlowX.setVisibility(View.VISIBLE);

                CountDownTimer glowTimer = new CountDownTimer(500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        imgBtnProfileX.setVisibility(View.VISIBLE);
                        imgBtnProfileGlowX.setVisibility(View.GONE);

                        Intent intent = new Intent(HomePage.this, ProfileView.class);
                        startActivity(intent);

                    }
                }.start();



            }
        });

        imgBtnPlayGlowX = findViewById(R.id.btnPlayAgainGlow);
        imgBtnPlayX = findViewById(R.id.btnPlayAgain);
        imgBtnPlayX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imgBtnPlayX.setVisibility(View.GONE);
                imgBtnPlayGlowX.setVisibility(View.VISIBLE);


                CountDownTimer glowTimer = new CountDownTimer(500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        imgBtnPlayX.setVisibility(View.VISIBLE);
                        imgBtnPlayGlowX.setVisibility(View.GONE);

                        YoYo.with(Techniques.SlideOutLeft)
                                .delay(0)
                                .duration(100)
                                .repeat(0)
                                .playOn(imgBtnProfileX);

                        YoYo.with(Techniques.SlideOutRight)
                                .delay(0)
                                .duration(100)
                                .repeat(0)
                                .playOn(imgBtnLeadersX);

                        YoYo.with(Techniques.SlideOutDown)
                                .delay(110)
                                .duration(300)
                                .repeat(0)
                                .playOn(loutButtonsX);

                        new CountDownTimer(450, 500) {

                            public void onTick(long millisUntilFinished) {
                                // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
                            }

                            public void onFinish() {
                                Intent intent = new Intent(HomePage.this, Game.class);
                                startActivity(intent);
                                finish();
                            }
                        }.start();


                    }
                }.start();
            }
        });

        //Firebase basics




    }

    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    public void onBackPressed() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_logout, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        Button btnYesX = view.findViewById(R.id.btnYes);
        btnYesX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finishAffinity();
                System.exit(0);

            }
        });

        Button btnNoX = view.findViewById(R.id.btnNo);
        btnNoX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



    }
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    private void ShowNewFABbasedMenu() {

        popupMenuToggle = "pressed";

        fabPopUpHPX.setVisibility(View.GONE);
        fabPopUpCollHPX.setVisibility(View.VISIBLE);
        fabPopUpFAQminiHPtX.setVisibility(View.VISIBLE);
        fabPopUpLogOutminiHPX.setVisibility(View.VISIBLE);

        txtFAQButtonHPX.setVisibility(View.VISIBLE);
        txtLogoutButtonHPX.setVisibility(View.VISIBLE);

        fabPopUpLogOutminiHPX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenuToggle = "Not";

                fabPopUpHPX.setVisibility(View.VISIBLE);
                fabPopUpCollHPX.setVisibility(View.GONE);
                fabPopUpFAQminiHPtX.setVisibility(View.GONE);
                fabPopUpLogOutminiHPX.setVisibility(View.GONE);

                txtFAQButtonHPX.setVisibility(View.GONE);
                txtLogoutButtonHPX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

                alertDialogLogOut();

            }
        });

        shadeX.setVisibility(View.VISIBLE);

        fabPopUpCollHPX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenuToggle = "Not";

                fabPopUpHPX.setVisibility(View.VISIBLE);
                fabPopUpCollHPX.setVisibility(View.GONE);
                fabPopUpFAQminiHPtX.setVisibility(View.GONE);
                fabPopUpLogOutminiHPX.setVisibility(View.GONE);

                txtFAQButtonHPX.setVisibility(View.GONE);
                txtLogoutButtonHPX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

            }
        });


    }

    private void alertDialogLogOut() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_logout, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        ImageView imgIconX = view.findViewById(R.id.imgIcon);
        imgIconX.setImageDrawable(getResources().getDrawable(R.drawable.logout));

        TextView txtTitleX = view.findViewById(R.id.txtTitle);
        txtTitleX.setText("Logout");

        TextView txtMsgX = view.findViewById(R.id.txtMsg);
        txtMsgX.setText("Do you really want to Logout?");

        Button btnYesX = view.findViewById(R.id.btnYes);
        btnYesX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();

                logoutSnackbar();
                transitionBackToLogin ();
            }
        });

        Button btnNoX = view.findViewById(R.id.btnNo);
        btnNoX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }
    //Method called from LogOut to get us back to Login screen
    private void transitionBackToLogin () {

        new CountDownTimer(1000, 500) {


            public void onTick(long millisUntilFinished) {
                // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
            }

            public void onFinish() {
                Intent intent = new Intent(HomePage.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    private void logoutSnackbar(){

        Snackbar snackbar;

        snackbar = Snackbar.make(loutHomePageX, "Good bye", Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorPrimaryDark));

        snackbar.show();


        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);

    }



}
