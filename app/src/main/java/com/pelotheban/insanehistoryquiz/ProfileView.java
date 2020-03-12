package com.pelotheban.insanehistoryquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

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

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class ProfileView extends AppCompatActivity {

    private TextView txtUIDX, txtProfileNameX;

    ///Firebase

   private String userUID, profileName;
   private DatabaseReference profileReference;
   private Query profileQuery;

   private FirebaseAuth mAuthPF;

   // PopUp

    private AlertDialog dialog;
    private LinearLayout loutProfileViewX;

    private String popupMenuToggle;
    private FloatingActionButton fabPopUpPFX, fabPopUpCollPFX, fabPopUpFAQminiPFtX, fabPopUpLogOutminiPFX;
    private TextView txtFAQButtonPFX, txtLogoutButtonPFX;
    private View shadeX; // to shade the background when menu out


   //Badges

    ImageView imgLongStreakBadgeX, imgTotalAnsweredBadgeX, imgTotalQuestionsBadgeX;

    String longestStreakString;
    int longestStreak;
    int longestStreakBadgeLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);//pop up

        loutProfileViewX = findViewById(R.id.loutProfileView);
        mAuthPF = FirebaseAuth.getInstance();
        //pop-up

        fabPopUpPFX = findViewById(R.id.fabPopUpPF);
        fabPopUpPFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowNewFABbasedMenu();
            }
        });

        popupMenuToggle = "Not";

        fabPopUpCollPFX = findViewById(R.id.fabPopUpCollPF);
        fabPopUpFAQminiPFtX = findViewById(R.id.fabPopUpFAQminiPF);
        fabPopUpLogOutminiPFX = findViewById(R.id.fabPopUpLogOutminiPF);

        txtFAQButtonPFX = findViewById(R.id.txtFAQButtonPF);
        txtLogoutButtonPFX = findViewById(R.id.txtLogoutButtonPF);

        shadeX = findViewById(R.id.shade);

        //// end of pop-up

        txtUIDX = findViewById(R.id.txtUID);
        txtProfileNameX = findViewById(R.id.txtProfileName);

        userUID = FirebaseAuth.getInstance().getUid();
        profileReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(userUID);
        //txtUIDX.setText(userUID);


            Random generator = new Random();
            StringBuilder randomStringBuilder = new StringBuilder();

            char tempChar;
            for (int i = 0; i < 15; i++){
                tempChar = (char) (generator.nextInt(96) + 32);
                randomStringBuilder.append(tempChar).toString();

                txtUIDX.setText(randomStringBuilder);
            }
            //return randomStringBuilder.toString();


        profileQuery = FirebaseDatabase.getInstance().getReference().child("my_users").orderByChild("user").equalTo(userUID);
        profileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userPs: dataSnapshot.getChildren()) {

                    try {
                    // if a Profile name already exists this will pull it down and set text view; if it doesn't it will throw it to catch
                    profileName = userPs.child("profilename").getValue().toString();
                    txtProfileNameX.setText(profileName);

                    } catch (Exception e) {



                        profileName = "Serbitar"; // for now generates static profile name will set to random; happens only if one does not already exist
                        txtProfileNameX.setText(profileName); // populates the text box
                        profileReference.getRef().child("profilename").setValue(profileName); //puts the generated profile name to FB so it will have it next time around

                    }

                    try {

                        longestStreakString = userPs.child("longeststreak").getValue().toString();
                        longestStreak = Integer.valueOf(longestStreakString);

                        if (longestStreak >4) {
                            imgLongStreakBadgeX.setImageResource(R.drawable.badgeone);
                        }

                        if (longestStreak >9) {
                            imgLongStreakBadgeX.setImageResource(R.drawable.badgetwo);
                        }

                        if (longestStreak >19) {
                            imgLongStreakBadgeX.setImageResource(R.drawable.badgethree);
                        }

                    } catch (Exception e) {


                    }




                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        imgLongStreakBadgeX = findViewById(R.id.imgLongStreakBadge);
        imgTotalAnsweredBadgeX = findViewById(R.id.imgTotalAnsweredBadge);
        imgTotalQuestionsBadgeX = findViewById(R.id.imgTotalQuestionsBadge);





    }


    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    private void ShowNewFABbasedMenu() {

        popupMenuToggle = "pressed";

        fabPopUpPFX.setVisibility(View.GONE);
        fabPopUpCollPFX.setVisibility(View.VISIBLE);
        fabPopUpFAQminiPFtX.setVisibility(View.VISIBLE);
        fabPopUpLogOutminiPFX.setVisibility(View.VISIBLE);

        txtFAQButtonPFX.setVisibility(View.VISIBLE);
        txtLogoutButtonPFX.setVisibility(View.VISIBLE);

        fabPopUpLogOutminiPFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenuToggle = "Not";

                fabPopUpPFX.setVisibility(View.VISIBLE);
                fabPopUpCollPFX.setVisibility(View.GONE);
                fabPopUpFAQminiPFtX.setVisibility(View.GONE);
                fabPopUpLogOutminiPFX.setVisibility(View.GONE);

                txtFAQButtonPFX.setVisibility(View.GONE);
                txtLogoutButtonPFX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

                alertDialogLogOut();

            }
        });

        shadeX.setVisibility(View.VISIBLE);

        fabPopUpCollPFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenuToggle = "Not";

                fabPopUpPFX.setVisibility(View.VISIBLE);
                fabPopUpCollPFX.setVisibility(View.GONE);
                fabPopUpFAQminiPFtX.setVisibility(View.GONE);
                fabPopUpLogOutminiPFX.setVisibility(View.GONE);

                txtFAQButtonPFX.setVisibility(View.GONE);
                txtLogoutButtonPFX.setVisibility(View.GONE);

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

                mAuthPF.signOut();

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
                Intent intent = new Intent(ProfileView.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    private void logoutSnackbar(){

        Snackbar snackbar;

        snackbar = Snackbar.make(loutProfileViewX, "Good bye", Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorPrimaryDark));

        snackbar.show();


        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);

    }




}
