package com.pelotheban.insanehistoryquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandedAnswer extends AppCompatActivity {

   // Primary UI components
    private ImageView btnPlayAgainX, btnEAProfileX, btnLeadersX, btnPlayAgainGlowX, btnEAProfileGlowX, btnELeadersGlowX, btnTestX;
    private AlertDialog dialog;
    private String ExpandedAnswerGet, ExpAnsCategoryGet, ExpAnsEpochGet;
    private TextView txtExpandedAnswerShowX, txtExpAnsCategoryX, txtExpAnsEpochX;
    private int expAnsBacgroundNo;

    private TextView txtEACoinCounterX, txtEAConStreakX;

    private LinearLayout loutEApanelX;

    private int height2;
    private int width2;

   //Firebase

    private DatabaseReference gameReference;

    private String uid; // this is for the user account side
    private DatabaseReference userReference;
    private Query sortUsersQuery;
    private String coinsOwnedString, consStreakString;
    private int coinsOwned, consStreak;
    private FirebaseAuth mAuthEA;

    // badges

    private int newbadgetrt;

    //pop up

    String popupMenuToggle;
    FloatingActionButton fabPopUpEAX, fabPopUpCollEAX, fabPopUpFAQminiEAtX, fabPopUpLogOutminiEAX;
    TextView txtFAQButtonEAX, txtLogoutButtonEAX;
    private View shadeX; // to shade the background when menu out
    private AlertDialog dialogEA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_answer);

        //badges - happen first

        newbadgetrt = getIntent().getIntExtra("newbadgetrt", 0);
        Toast.makeText(ExpandedAnswer.this, "Your new trtbadge is:  " + newbadgetrt, Toast.LENGTH_LONG).show();

        /// sizing the display to have both the question and then the answer mostly in the center

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        double height = size.y;

        height2 = (int) Math.round(height);
        width2 = (int) Math.round(width);

        // pup up menu

        fabPopUpEAX = findViewById(R.id.fabPopUpEA);
        fabPopUpEAX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowNewFABbasedEAMenu();
            }
        });

        popupMenuToggle = "Not";

        fabPopUpCollEAX = findViewById(R.id.fabPopUpCollEA);
        fabPopUpFAQminiEAtX = findViewById(R.id.fabPopUpFAQminiEA);
        fabPopUpLogOutminiEAX = findViewById(R.id.fabPopUpLogOutminiEA);

        txtFAQButtonEAX = findViewById(R.id.txtFAQButtonEA);
        txtLogoutButtonEAX = findViewById(R.id.txtLogoutButtonEA);

        shadeX = findViewById(R.id.shade);

        ///// Initialize and populate counters and expanded answer BEGINS /////////////////////////////////////
        txtEACoinCounterX = findViewById(R.id.txtEACoinCounter);
        txtEAConStreakX = findViewById(R.id.txtEAConStreak);

        mAuthEA = FirebaseAuth.getInstance();

        uid = FirebaseAuth.getInstance().getUid();
        sortUsersQuery = FirebaseDatabase.getInstance().getReference().child("my_users").orderByChild("user").equalTo(uid);
        sortUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userDs : dataSnapshot.getChildren()) {
                    // need the try because if new account will return null

                    try {
                        coinsOwnedString = userDs.child("coins").getValue().toString();
                        coinsOwned = Integer.valueOf(coinsOwnedString);


                    } catch (Exception e) {

                        //Toast.makeText(Game.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                    try {
                        consStreakString = userDs.child("constreak").getValue().toString();
                        consStreak = Integer.valueOf(consStreakString);
                    } catch (Exception e) {

                    }

                }
                txtEACoinCounterX.setText(coinsOwnedString); // IF there are any coins in the account will set the counter
                txtEAConStreakX.setText(consStreakString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // This was old code i believe so commenting but keeping to avoid surprises for now
      // expAnsBacgroundNo = 1; //default setting for background in case we miss a category or don't have a pic for it

        ExpandedAnswerGet = getIntent().getStringExtra("iiiexpanded");
        txtExpandedAnswerShowX = findViewById(R.id.txtExpandedAnswerShow);
        txtExpandedAnswerShowX.setText(ExpandedAnswerGet);

        if (width2 > 1500) { // changes in fot for tablet and then small format phone

        txtExpandedAnswerShowX.setTextSize(30);
        } else if (height2 < 1300) {

            txtExpandedAnswerShowX.setTextSize(17);

        }

        ExpAnsCategoryGet = getIntent().getStringExtra("bbbcategory");
        txtExpAnsCategoryX = findViewById(R.id.txtExpAnsCategory);
        txtExpAnsCategoryX.setText(ExpAnsCategoryGet);

        ExpAnsEpochGet = getIntent().getStringExtra("lllepoch");
        txtExpAnsEpochX = findViewById(R.id.txtExpAnsEpoch);
        txtExpAnsEpochX.setText(ExpAnsEpochGet);

        ////////// LOGIC FOR EA PANEL CHOICE /////////////////////////////////////////
        loutEApanelX = findViewById(R.id.loutEApanel);
        if (ExpAnsCategoryGet.equals("Persian") && ExpAnsEpochGet.equals("Middle Empire")) {

            loutEApanelX.setBackgroundResource(R.drawable.permemp);
        }
        if (ExpAnsCategoryGet.equals("Greek") && ExpAnsEpochGet.equals("Hellenistic")) {

            loutEApanelX.setBackgroundResource(R.drawable.grehel);
        }
        if (ExpAnsCategoryGet.equals("Roman") && ExpAnsEpochGet.equals("Early Empire")) {

            loutEApanelX.setBackgroundResource(R.drawable.romeemp);
        }

        if (ExpAnsCategoryGet.equals("Roman") && ExpAnsEpochGet.equals("Middle Empire")) {

            loutEApanelX.setBackgroundResource(R.drawable.rommemp);
        }
        if (ExpAnsCategoryGet.equals("Roman") && ExpAnsEpochGet.equals("Late Antiquity")) {

            loutEApanelX.setBackgroundResource(R.drawable.romlatan);
        }

        if (ExpAnsCategoryGet.equals("Byzantine") && ExpAnsEpochGet.equals("Dark Ages")) {

            loutEApanelX.setBackgroundResource(R.drawable.byzdark);
        }

        if (ExpAnsCategoryGet.equals("English") && ExpAnsEpochGet.equals("Modern")) {

            loutEApanelX.setBackgroundResource(R.drawable.engmod);
        }

        if (ExpAnsCategoryGet.equals("Spanish") && ExpAnsEpochGet.equals("Enlightenment")) {

            loutEApanelX.setBackgroundResource(R.drawable.spaenl);
        }

        if (ExpAnsCategoryGet.equals("Papacy") && ExpAnsEpochGet.equals("Renaissance")) {

            loutEApanelX.setBackgroundResource(R.drawable.papren);
        }

        if (ExpAnsCategoryGet.equals("Polish") && ExpAnsEpochGet.equals("Enlightenment")) {

            loutEApanelX.setBackgroundResource(R.drawable.polenl);
        }

        ///////////// END OF LOGIC FOR EA PANEL CHOICE //////////////////////////////////


        //////// Logic for background picture based on mix of cateogy and epoch //////////////////////////
        // This was old code i believe so commenting but keeping to avoid surprises for now
//        if (ExpAnsEpochGet.equals("Hellenistic") && ExpAnsCategoryGet.equals("Roman")) {
//
//            expAnsBacgroundNo = 2;
//
//        }

        // use the No to select a pic which is then set

        ////// end of logic for background pics ///////////////////////////////////////////

        // BUTTONS FOR GOING TO DIFFERENT SCREENS BEGINS //////////////////////////////////
        btnPlayAgainGlowX = findViewById(R.id.btnPlayAgainGlow);
        btnPlayAgainX = findViewById(R.id.btnPlayAgain);
        btnPlayAgainX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnPlayAgainX.setVisibility(View.GONE);
                btnPlayAgainGlowX.setVisibility(View.VISIBLE);

                CountDownTimer glowTimer = new CountDownTimer(500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        Intent intent = new Intent(ExpandedAnswer.this, Game.class);
                        startActivity(intent);

                    }
                }.start();

            }
        });

        btnEAProfileGlowX = findViewById(R.id.btnEAProfileGlow);
        btnEAProfileX = findViewById(R.id.btnEAProfile);
        btnEAProfileX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnEAProfileX.setVisibility(View.GONE);
                btnEAProfileGlowX.setVisibility(View.VISIBLE);

                CountDownTimer glowTimer = new CountDownTimer(500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        btnEAProfileX.setVisibility(View.VISIBLE);
                        btnEAProfileGlowX.setVisibility(View.GONE);
                        Intent intent = new Intent(ExpandedAnswer.this, ProfileView.class);
                        startActivity(intent);

                    }
                }.start();



            }
        });

        btnTestX = findViewById(R.id.btnELeadersGlow);
        //for whatever reason name bthELeaders, EALeaders would not work so using this instead

        //btnELeadersGlowX.findViewById(R.id.btnELeadersGlow);
        btnLeadersX = findViewById(R.id.btnEALeaders);
        btnLeadersX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnLeadersX.setVisibility(View.GONE);
                btnTestX.setVisibility(View.VISIBLE);

                CountDownTimer glowTimer = new CountDownTimer(500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        btnLeadersX.setVisibility(View.VISIBLE);
                        btnTestX.setVisibility(View.GONE);
                        Intent intent = new Intent(ExpandedAnswer.this, LeaderBoard.class);
                        startActivity(intent);

                    }
                }.start();

            }
        });

        // BUTTONS FOR GOING TO DIFFERENT SCREENS BEGINS //////////////////////////////////

    }

    //////////////////////////////////////////// END OF ONCREATE ////////////////////////////////////////////////

    /// on back pressed given choice to log out or go back

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

    /// POP UP BEGINS /////////////////////////////////////////////////////////////////////////

    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    private void ShowNewFABbasedEAMenu() {

        popupMenuToggle = "pressed";

        fabPopUpEAX.setVisibility(View.GONE);
        fabPopUpCollEAX.setVisibility(View.VISIBLE);
        fabPopUpFAQminiEAtX.setVisibility(View.VISIBLE);
        fabPopUpLogOutminiEAX.setVisibility(View.VISIBLE);

        txtFAQButtonEAX.setVisibility(View.VISIBLE);
        txtLogoutButtonEAX.setVisibility(View.VISIBLE);

        fabPopUpLogOutminiEAX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenuToggle = "Not";

                fabPopUpEAX.setVisibility(View.VISIBLE);
                fabPopUpCollEAX.setVisibility(View.GONE);
                fabPopUpFAQminiEAtX.setVisibility(View.GONE);
                fabPopUpLogOutminiEAX.setVisibility(View.GONE);

                txtFAQButtonEAX.setVisibility(View.GONE);
                txtLogoutButtonEAX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

                alertDialogLogOut();

            }
        });

        shadeX.setVisibility(View.VISIBLE);

        fabPopUpCollEAX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenuToggle = "Not";

                fabPopUpEAX.setVisibility(View.VISIBLE);
                fabPopUpCollEAX.setVisibility(View.GONE);
                fabPopUpFAQminiEAtX.setVisibility(View.GONE);
                fabPopUpLogOutminiEAX.setVisibility(View.GONE);

                txtFAQButtonEAX.setVisibility(View.GONE);
                txtLogoutButtonEAX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

            }
        });


    }

    private void alertDialogLogOut() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_logout, null);

        dialogEA = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialogEA.show();

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

                mAuthEA.signOut();

                logoutSnackbar();
                transitionBackToLogin ();
            }
        });

        Button btnNoX = view.findViewById(R.id.btnNo);
        btnNoX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogEA.dismiss();
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
                Intent intent = new Intent(ExpandedAnswer.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    private void logoutSnackbar(){

        Snackbar snackbar;

        snackbar = Snackbar.make(loutEApanelX, "Good bye", Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorPrimaryDark));

        snackbar.show();


        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);

    }

    ////////////////////// END OF POP UP and downstream methods like log out ///////////////////////////////////

}
