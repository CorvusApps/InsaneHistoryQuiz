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
import com.squareup.picasso.Picasso;

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

    private int newbadgesup, newbadgestr, newbadgetrt, newbadgewin;
    private int newbadgexant, newbadgexmed, newbadgexren, newbadgexenl, newbadgexmod, newbadgexcon;
    private LinearLayout loutBadgesX;
    private ImageView imgBadgeAwardX;

    private DatabaseReference badgeReference;
    private Query badgeQuery;
    private String badgeImageLink;
    private String badgeSortKey;

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

        /////////////////badges - happen first //////////////////////////////////
        loutBadgesX = findViewById(R.id.loutBadges);
        imgBadgeAwardX = findViewById(R.id.imgBadgeAward);

        newbadgesup = getIntent().getIntExtra("newbadgesup", 0);
        newbadgestr = getIntent().getIntExtra("newbadgestr", 0);
        newbadgetrt = getIntent().getIntExtra("newbadgetrt", 0);
        newbadgewin = getIntent().getIntExtra("newbadgewin", 0);

        newbadgexant = getIntent().getIntExtra("newbadgexant", 0);
        newbadgexmed = getIntent().getIntExtra("newbadgexmed", 0);
        newbadgexren = getIntent().getIntExtra("newbadgexren", 0);
        newbadgexenl = getIntent().getIntExtra("newbadgexenl", 0);
        newbadgexmod = getIntent().getIntExtra("newbadgexmod", 0);
        newbadgexcon = getIntent().getIntExtra("newbadgexcon", 0);

        // this assigns the badge to award assuming there are any

        if (newbadgesup == 1){
            badgeSortKey = "sup1";
        }
        if (newbadgesup == 2){
            badgeSortKey = "sup2";
        }
        if (newbadgesup == 3){
            badgeSortKey = "sup3";
        }
        if (newbadgesup == 4){
            badgeSortKey = "sup4";
        }
        if (newbadgesup == 5){
            badgeSortKey = "sup5";
        }

        if (newbadgestr == 1){
            badgeSortKey = "str1";
        }
        if (newbadgestr == 2){
            badgeSortKey = "str2";
        }
        if (newbadgestr == 3){
            badgeSortKey = "str3";
        }
        if (newbadgestr == 4){
            badgeSortKey = "str4";
        }
        if (newbadgestr == 5){
            badgeSortKey = "str5";
        }


        if (newbadgetrt == 1){
            badgeSortKey = "trt1";
        }
        if (newbadgetrt == 2){
            badgeSortKey = "trt2";
        }
        if (newbadgetrt == 3){
            badgeSortKey = "trt3";
        }
        if (newbadgetrt == 4){
            badgeSortKey = "trt4";
        }
        if (newbadgetrt == 5){
            badgeSortKey = "trt5";
        }

        if (newbadgewin == 1){
            badgeSortKey = "win1";
        }
        if (newbadgewin == 2){
            badgeSortKey = "win2";
        }
        if (newbadgewin == 3){
            badgeSortKey = "win3";
        }
        if (newbadgewin == 4){
            badgeSortKey = "win4";
        }
        if (newbadgewin == 5){
            badgeSortKey = "win5";
        }

        if (newbadgexant == 1){
            badgeSortKey = "xant1";
        }
        if (newbadgexant == 2){
            badgeSortKey = "xant2";
        }
        if (newbadgexant == 3){
            badgeSortKey = "xant3";
        }
        if (newbadgexant == 4){
            badgeSortKey = "xant4";
        }
        if (newbadgexant == 5){
            badgeSortKey = "xant5";
        }

        if (newbadgexmed == 1){
            badgeSortKey = "xmed1";
        }
        if (newbadgexmed == 2){
            badgeSortKey = "xmed2";
        }
        if (newbadgexmed == 3){
            badgeSortKey = "xmed3";
        }
        if (newbadgexmed == 4){
            badgeSortKey = "xmed4";
        }
        if (newbadgexmed == 5){
            badgeSortKey = "xmed5";
        }

        if (newbadgexren == 1){
            badgeSortKey = "xren1";
        }
        if (newbadgexren == 2){
            badgeSortKey = "xren2";
        }
        if (newbadgexren == 3){
            badgeSortKey = "xren3";
        }
        if (newbadgexren == 4){
            badgeSortKey = "xren4";
        }
        if (newbadgexren == 5){
            badgeSortKey = "xren5";
        }

        if (newbadgexenl == 1){
            badgeSortKey = "xenl1";
        }
        if (newbadgexenl == 2){
            badgeSortKey = "xenl2";
        }
        if (newbadgexenl == 3){
            badgeSortKey = "xenl3";
        }
        if (newbadgexenl == 4){
            badgeSortKey = "xenl4";
        }
        if (newbadgexenl == 5){
            badgeSortKey = "xenl5";
        }

        if (newbadgexmod == 1){
            badgeSortKey = "xmod1";
        }
        if (newbadgexmod == 2){
            badgeSortKey = "xmod2";
        }
        if (newbadgexmod == 3){
            badgeSortKey = "xmod3";
        }
        if (newbadgexmod == 4){
            badgeSortKey = "xmod4";
        }
        if (newbadgexmod == 5){
            badgeSortKey = "xmod5";
        }

        if (newbadgexcon == 1){
            badgeSortKey = "xcon1";
        }
        if (newbadgexcon == 2){
            badgeSortKey = "xcon2";
        }
        if (newbadgexcon == 3){
            badgeSortKey = "xcon3";
        }
        if (newbadgexcon == 4){
            badgeSortKey = "xcon4";
        }
        if (newbadgexcon == 5){
            badgeSortKey = "xcon5";
        }

        badgeQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(badgeSortKey);
        badgeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeSnapshot) {

                for (DataSnapshot badges : badgeSnapshot.getChildren()) {

                    loutBadgesX.setVisibility(View.VISIBLE);
                    badgeImageLink = badges.child("badgeimagelink").getValue().toString();
                    Picasso.get().load(badgeImageLink).into(imgBadgeAwardX);

                    try {

                        CountDownTimer badgeAwardTimer = new CountDownTimer(5000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                            }

                            @Override
                            public void onFinish() {
                            loutBadgesX.setVisibility(View.GONE);
                            }
                        }.start();

                    } catch (Exception e) {
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


       // Toast.makeText(ExpandedAnswer.this, "Your new trtbadge is:  " + newbadgetrt, Toast.LENGTH_LONG).show();

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
