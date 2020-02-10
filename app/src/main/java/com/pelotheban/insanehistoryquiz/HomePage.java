package com.pelotheban.insanehistoryquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomePage extends AppCompatActivity {

    AlertDialog dialog;
    ConstraintLayout loutHomePageX;

    //Admin Button

    ImageView imgBtnAdminX;

    //User Buttons

    String popupMenuToggle;
    FloatingActionButton fabPopUpHPX, fabPopUpCollHPX, fabPopUpFAQminiHPtX, fabPopUpLogOutminiHPX;
    TextView txtFAQButtonHPX, txtLogoutButtonHPX;
    private View shadeX; // to shade the background when menu out

    // Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        loutHomePageX = findViewById(R.id.loutHomePage);

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

        //Admin Button

        imgBtnAdminX = findViewById(R.id.imgBtnAdmin);
        imgBtnAdminX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomePage.this, AdminDir.class);
                startActivity(intent);

            }
        });

        //Firebase basics

        mAuth = FirebaseAuth.getInstance();

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
        txtMsgX.setText("Do you really want to Logout from Corvus?");

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
