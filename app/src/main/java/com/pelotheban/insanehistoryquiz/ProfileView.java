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
import android.widget.Toast;

import java.util.Random;

public class ProfileView extends AppCompatActivity {

   // Basic set up

    private LinearLayout loutProfileViewX;

    private TextView txtUIDX, txtProfileNameX;

   ///Firebase

   private String userUID, profileName;
   private DatabaseReference profileReference;
   private Query profileQuery;

   private FirebaseAuth mAuthPF;

   // PopUp

    private AlertDialog dialog;

    private String popupMenuToggle;
    private FloatingActionButton fabPopUpPFX, fabPopUpCollPFX, fabPopUpFAQminiPFtX, fabPopUpLogOutminiPFX;
    private TextView txtFAQButtonPFX, txtLogoutButtonPFX;
    private View shadeX; // to shade the background when menu out

    // Tracker panel UI Elements

    private TextView pfTxtCoinCounterX, pfTxtConStreakX, pfTxtTotalAnswersX, pfTxtLongestStreakX, pfTxtTotalQuestionsX, pfCorrectPercentX;
    private String coinCounterString, conStreakString, totalAnswersString, longestStreakString, totalQuestionsString, correctPercentString;
    private int coinCounter, conStreak, totalAnswers, longestStreak, totalQuestions;
    Double correctPercent;

   //Badges

    ImageView imgLongStreakBadgeX, imgTotalAnsweredBadgeX, imgTotalQuestionsBadgeX;

    int longestStreakBadgeLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);//pop up

        // Basic Set Up
        loutProfileViewX = findViewById(R.id.loutProfileView);
        mAuthPF = FirebaseAuth.getInstance();

        // Tracker panel UI Elements

        pfTxtCoinCounterX = findViewById(R.id.pfTxtCoinCounter);
        pfTxtConStreakX = findViewById(R.id.pfTxtConStreak);
        pfTxtTotalAnswersX = findViewById(R.id.pfTxtTotalAnswers);
        pfTxtLongestStreakX = findViewById(R.id.pfTxtLongestStreak);
        pfTxtTotalQuestionsX = findViewById(R.id.pfTxtTotalQuestions);
        pfCorrectPercentX = findViewById(R.id.pfCorrectPercent);

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

        // This is not really used for anything anymore but keeping for now
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
                        coinCounterString = userPs.child("coins").getValue().toString();
                        coinCounter = Integer.valueOf(coinCounterString);
                    } catch (Exception e) {
                        coinCounterString = "";
                    }

                    try {
                        conStreakString = userPs.child("constreak").getValue().toString();
                        conStreak = Integer.valueOf(conStreakString);
                    } catch (Exception e) {
                        conStreakString = "";
                    }

                    try {
                        totalAnswersString = userPs.child("totalanswered").getValue().toString();
                        totalAnswers = Integer.valueOf(totalAnswersString);
                    } catch (Exception e) {
                        totalAnswersString = "";
                    }

                    try {
                        longestStreakString = userPs.child("longeststreak").getValue().toString();
                        longestStreak = Integer.valueOf(longestStreakString);
                    } catch (Exception e) {
                        longestStreakString = "";
                    }

                    try {
                        totalQuestionsString = userPs.child("totalquestions").getValue().toString();
                        totalQuestions = Integer.valueOf(totalQuestionsString);
                    } catch (Exception e) {
                        totalQuestionsString ="";
                    }

                    try {

                        correctPercent = ( (double) totalAnswers / totalQuestions)*100;
                        int correctPercent2 = (int) Math.round(correctPercent);
                        correctPercentString = correctPercent2 +"%";
                        pfCorrectPercentX.setText(correctPercentString);

                    } catch (Exception e) {
                        correctPercentString = "";
                    }

                }

                pfTxtCoinCounterX.setText(coinCounterString);
                pfTxtConStreakX.setText(conStreakString);
                pfTxtTotalAnswersX.setText(totalAnswersString);
                pfTxtLongestStreakX.setText(longestStreakString);
                pfTxtTotalQuestionsX.setText(totalQuestionsString);
                pfCorrectPercentX.setText(correctPercentString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
