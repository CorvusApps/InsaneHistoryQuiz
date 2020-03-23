package com.pelotheban.insanehistoryquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Target;

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

    // Panels

    private String panelName;

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

    private TextView txtBadgeAwardX;
    private String badgeAwardMsg;

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
        txtBadgeAwardX = findViewById(R.id.txtBadgeMessage);

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

        if (newbadgesup == 1) {
            badgeSortKey = "sup1";
            badgeAwardMsg = "You earned THE SHOPKEEPER BADGE for reaching the 125 coin Level!";
        }
        if (newbadgesup == 2) {
            badgeSortKey = "sup2";
            badgeAwardMsg = "You earned THE BOOK MERCHANT BADGE for reaching the 175 coin Level!";
        }
        if (newbadgesup == 3) {
            badgeSortKey = "sup3";
            badgeAwardMsg = "You earned THE TAX COLLECTOR BADGE for reaching the 225 coin Level!";
        }
        if (newbadgesup == 4) {
            badgeSortKey = "sup4";
            badgeAwardMsg = "You earned THE EQUITE CLASS BADGE for reaching the 500 coin Level!";
        }
        if (newbadgesup == 5) {
            badgeSortKey = "sup5";
            badgeAwardMsg = "You earned THE SENATORIAL CLASS BADGE for reaching the 1000 coin Level!";
        }

        if (newbadgestr == 1) {
            badgeSortKey = "str1";
            badgeAwardMsg = "You earned THE STUDENT BADGE for getting 3 correct answers in a row!";
        }
        if (newbadgestr == 2) {
            badgeSortKey = "str2";
            badgeAwardMsg = "You earned THE TUTOR BADGE for getting 10 correct answers in a row!";
        }
        if (newbadgestr == 3) {
            badgeSortKey = "str3";
            badgeAwardMsg = "You earned THE RECTOR BADGE for getting 20 correct answers in a row!";
        }
        if (newbadgestr == 4) {
            badgeSortKey = "str4";
            badgeAwardMsg = "You earned THE PHILOSOPHER BADGE for getting 30 correct answers in a row!";
        }
        if (newbadgestr == 5) {
            badgeSortKey = "str5";
            badgeAwardMsg = "You earned THE MASTER BADGE for getting 50 correct answers in a row!";
        }


        if (newbadgetrt == 1) {
            badgeSortKey = "trt1";
            badgeAwardMsg = "You earned THE SCRIBE BADGE for getting to a total of 5 correct answers!";
        }
        if (newbadgetrt == 2) {
            badgeSortKey = "trt2";
            badgeAwardMsg = "You earned THE LIBRARIAN BADGE for getting to a total of 25 correct answers!";
        }
        if (newbadgetrt == 3) {
            badgeSortKey = "trt3";
            badgeAwardMsg = "You earned THE MASTER LIBRARIAN BADGE for getting to a total of 100 correct answers!";
        }
        if (newbadgetrt == 4) {
            badgeSortKey = "trt4";
            badgeAwardMsg = "You earned THE CHRONICLER BADGE for getting to a total of 250 correct answers!";
        }
        if (newbadgetrt == 5) {
            badgeSortKey = "trt5";
            badgeAwardMsg = "You earned THE HISTORIAN BADGE for getting to a total of 500 correct answers!";
        }

        if (newbadgewin == 1) {
            badgeSortKey = "win1";
            badgeAwardMsg = "You earned THE COIN-FLIP BADGE for achieving a 50% correct answer level!";
        }
        if (newbadgewin == 2) {
            badgeSortKey = "win2";
            badgeAwardMsg = "You earned THE ADEPT BADGE for achieving a 60% correct answer level!";
        }
        if (newbadgewin == 3) {
            badgeSortKey = "win3";
            badgeAwardMsg = "You earned THE PRODIGY BADGE for achieving a 70% correct answer level!";
        }
        if (newbadgewin == 4) {
            badgeSortKey = "win4";
            badgeAwardMsg = "You earned THE GENIUS BADGE for achieving a 80% correct answer level!";
        }
        if (newbadgewin == 5) {
            badgeSortKey = "win5";
            badgeAwardMsg = "You earned THE INFALLIBE BADGE for achieving a 90% correct answer level!";
        }

        if (newbadgexant == 1) {
            badgeSortKey = "xant1";
            badgeAwardMsg = "You earned THE HOPLITE BADGE for getting to 5 correct answers in the ANTIQUITY category!";
        }
        if (newbadgexant == 2) {
            badgeSortKey = "xant2";
            badgeAwardMsg = "You earned THE LEGIONNAIRE BADGE for getting to 25 correct answers in the ANTIQUITY category!";
        }
        if (newbadgexant == 3) {
            badgeSortKey = "xant3";
            badgeAwardMsg = "You earned THE DECURION BADGE for getting to 50 correct answers in the ANTIQUITY category!";
        }
        if (newbadgexant == 4) {
            badgeSortKey = "xant4";
            badgeAwardMsg = "You earned THE CENTURION BADGE for getting to 100 correct answers in the ANTIQUITY category!";
        }
        if (newbadgexant == 5) {
            badgeSortKey = "xant5";
            badgeAwardMsg = "You earned THE LEGATE BADGE for getting to 200 correct answers in the ANTIQUITY category!";
        }

        if (newbadgexmed == 1) {
            badgeSortKey = "xmed1";
            badgeAwardMsg = "You earned THE SQUIRE BADGE for getting to 5 correct answers in the MEDIEVAL category!";
        }
        if (newbadgexmed == 2) {
            badgeSortKey = "xmed2";
            badgeAwardMsg = "You earned THE KNIGHT BADGE for getting to 25 correct answers in the MEDIEVAL category!";
        }
        if (newbadgexmed == 3) {
            badgeSortKey = "xmed3";
            badgeAwardMsg = "You earned THE BARON BADGE for getting to 50 correct answers in the MEDIEVAL category!";
        }
        if (newbadgexmed == 4) {
            badgeSortKey = "xmed4";
            badgeAwardMsg = "You earned THE MARQUIS BADGE for getting to 100 correct answers in the MEDIEVAL category!";
        }
        if (newbadgexmed == 5) {
            badgeSortKey = "xmed5";
            badgeAwardMsg = "You earned THE DUKE BADGE for getting to 200 correct answers in the MEDIEVAL category!";
        }

        if (newbadgexren == 1) {
            badgeSortKey = "xren1";
            badgeAwardMsg = "You earned THE TITIAN BADGE for getting to 5 correct answers in the RENAISSANCE category!";
        }
        if (newbadgexren == 2) {
            badgeSortKey = "xren2";
            badgeAwardMsg = "You earned THE DONATELLO BADGE for getting to 25 correct answers in the RENAISSANCE category!";
        }
        if (newbadgexren == 3) {
            badgeSortKey = "xren3";
            badgeAwardMsg = "You earned THE BERNINI BADGE for getting to 50 correct answers in the RENAISSANCE category!";
        }
        if (newbadgexren == 4) {
            badgeSortKey = "xren4";
            badgeAwardMsg = "You earned THE MICHELANGELO BADGE for getting to 100 correct answers in the RENAISSANCE category!";
        }
        if (newbadgexren == 5) {
            badgeSortKey = "xren5";
            badgeAwardMsg = "You earned THE DA VINCI BADGE for getting to 200 correct answers in the RENAISSANCE category!";
        }

        if (newbadgexenl == 1) {
            badgeSortKey = "xenl1";
            badgeAwardMsg = "You earned THE DESCARTES BADGE for getting to 5 correct answers in the ENLIGHTENMENT category!";
        }
        if (newbadgexenl == 2) {
            badgeSortKey = "xenl2";
            badgeAwardMsg = "You earned THE JOHN LOCKE BADGE for getting to 25 correct answers in the ENLIGHTENMENT category!";
        }
        if (newbadgexenl == 3) {
            badgeSortKey = "xenl3";
            badgeAwardMsg = "You earned THE VOLTAIRE BADGE for getting to 50 correct answers in the ENLIGHTENMENT category!";
        }
        if (newbadgexenl == 4) {
            badgeSortKey = "xenl4";
            badgeAwardMsg = "You earned THE EDWARD GIBBON BADGE for getting to 100 correct answers in the ENLIGHTENMENT category!";
        }
        if (newbadgexenl == 5) {
            badgeSortKey = "xenl5";
            badgeAwardMsg = "You earned THE ISAAC NEWTON BADGE for getting to 200 correct answers in the ENLIGHTENMENT category!";
        }

        if (newbadgexmod == 1) {
            badgeSortKey = "xmod1";
            badgeAwardMsg = "You earned THE TITIAN BADGE for getting to 5 correct answers in the MODERN HISTORY category!";
        }
        if (newbadgexmod == 2) {
            badgeSortKey = "xmod2";
            badgeAwardMsg = "You earned THE TITIAN BADGE for getting to 5 correct answers in the MODERN HISTORY category!";
        }
        if (newbadgexmod == 3) {
            badgeSortKey = "xmod3";
            badgeAwardMsg = "You earned THE TITIAN BADGE for getting to 5 correct answers in the MODERN HISTORY category!";
        }
        if (newbadgexmod == 4) {
            badgeSortKey = "xmod4";
            badgeAwardMsg = "You earned THE TITIAN BADGE for getting to 5 correct answers in the MODERN HISTORY category!";
        }
        if (newbadgexmod == 5) {
            badgeSortKey = "xmod5";
            badgeAwardMsg = "You earned THE TITIAN BADGE for getting to 5 correct answers in the MODERN HISTORY category!";
        }

        if (newbadgexcon == 1) {
            badgeSortKey = "xcon1";
            badgeAwardMsg = "You earned THE INDUSTRIAL BADGE for getting to 5 correct answers in the CONTEMPORARY HISTORY category!";
        }
        if (newbadgexcon == 2) {
            badgeSortKey = "xcon2";
            badgeAwardMsg = "You earned THE NUCLEAR BADGE for getting to 25 correct answers in the CONTEMPORARY HISTORY category!";
        }
        if (newbadgexcon == 3) {
            badgeSortKey = "xcon3";
            badgeAwardMsg = "You earned THE BIO-TECH BADGE for getting to 50 correct answers in the CONTEMPORARY HISTORY category!";
        }
        if (newbadgexcon == 4) {
            badgeSortKey = "xcon4";
            badgeAwardMsg = "You earned THE DIGITAL BADGE for getting to 100 correct answers in the CONTEMPORARY HISTORY category!";
        }
        if (newbadgexcon == 5) {
            badgeSortKey = "xcon5";
            badgeAwardMsg = "You earned THE ARTIFICIAL INTELLIGENCE BADGE for getting to 200 correct answers in the CONTEMPORARY HISTORY category!";
        }

        badgeQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(badgeSortKey);
        badgeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeSnapshot) {

                for (DataSnapshot badges : badgeSnapshot.getChildren()) {

                    loutBadgesX.setVisibility(View.VISIBLE);
                    badgeImageLink = badges.child("badgeimagelink").getValue().toString();
                    Picasso.get().load(badgeImageLink).into(imgBadgeAwardX);
                    txtBadgeAwardX.setText(badgeAwardMsg);

                    try {

                        CountDownTimer badgeAwardTimer = new CountDownTimer(7000, 1000) {
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
        if (ExpAnsCategoryGet.equals("Chinese") && ExpAnsEpochGet.equals("Dark Ages")) {
            panelName = "chidark";

        }

        if (ExpAnsCategoryGet.equals("Chinese") && ExpAnsEpochGet.equals("PreModern")) {
            panelName = "chipmod";

        }
        if (ExpAnsCategoryGet.equals("Chinese") && ExpAnsEpochGet.equals("Contemporary")) {
            panelName = "chicon";

        }
        if (ExpAnsCategoryGet.equals("Egyptian") && ExpAnsEpochGet.equals("Bronze Age")) {
            panelName = "egybro";

        }

        if (ExpAnsCategoryGet.equals("Jewish") && ExpAnsEpochGet.equals("High Middle Ages")) {
            panelName = "jewhma";

        }

        if (ExpAnsCategoryGet.equals("Jewish") && ExpAnsEpochGet.equals("Early Empire")) {
            panelName = "jeweemp";

        }

        if (ExpAnsCategoryGet.equals("Jewish") && ExpAnsEpochGet.equals("Late Antiquity")) {
            panelName = "jewlatan";

        }

        if (ExpAnsCategoryGet.equals("Jewish") && ExpAnsEpochGet.equals("WWII")) {
            panelName = "jewwwii";

        }

        if (ExpAnsCategoryGet.equals("Persian") && ExpAnsEpochGet.equals("Middle Empire")) {
            panelName = "permemp";

        }
        if (ExpAnsCategoryGet.equals("Greek") && ExpAnsEpochGet.equals("Hellenistic")) {
            panelName = "grehel";

        }
        if (ExpAnsCategoryGet.equals("Roman") && ExpAnsEpochGet.equals("Late Republic")) {
            panelName = "romlrep";

        }
        if (ExpAnsCategoryGet.equals("Roman") && ExpAnsEpochGet.equals("Early Empire")) {
            panelName = "romeemp";

        }

        if (ExpAnsCategoryGet.equals("Roman") && ExpAnsEpochGet.equals("Middle Empire")) {
            panelName = "rommemp";

        }
        if (ExpAnsCategoryGet.equals("Roman") && ExpAnsEpochGet.equals("Late Antiquity")) {
            panelName = "romlatan";

        }

        if (ExpAnsCategoryGet.equals("Carthaginian") && ExpAnsEpochGet.equals("Hellenistic")) {
            panelName = "carhel";

        }

        if (ExpAnsCategoryGet.equals("Byzantine") && ExpAnsEpochGet.equals("Late Antiquity")) {
            panelName = "byzlatan";

        }
        if (ExpAnsCategoryGet.equals("Byzantine") && ExpAnsEpochGet.equals("Dark Ages")) {
            panelName = "byzdark";

        }

        if (ExpAnsCategoryGet.equals("Goth") && ExpAnsEpochGet.equals("Late Antiquity")) {
            panelName = "gothlatan";

        }

        if (ExpAnsCategoryGet.equals("Hun") && ExpAnsEpochGet.equals("Late Antiquity")) {
            panelName = "hunlatan";

        }

        if (ExpAnsCategoryGet.equals("German") && ExpAnsEpochGet.equals("WWII")) {
            panelName = "gerwwii";

        }

        if (ExpAnsCategoryGet.equals("French") && ExpAnsEpochGet.equals("High Middle Ages")) {
            panelName = "frehma";

        }

        if (ExpAnsCategoryGet.equals("French") && ExpAnsEpochGet.equals("Crusades")) {
            panelName = "frecru";

        }

        if (ExpAnsCategoryGet.equals("English") && ExpAnsEpochGet.equals("Renaissance")) {
            panelName = "engren";

        }

        if (ExpAnsCategoryGet.equals("English") && ExpAnsEpochGet.equals("Enlightenment")) {
            panelName = "engenl";

        }

        if (ExpAnsCategoryGet.equals("English") && ExpAnsEpochGet.equals("Modern")) {
            panelName = "engmod";

        }

        if (ExpAnsCategoryGet.equals("English") && ExpAnsEpochGet.equals("Contemporary")) {
            panelName = "engcon";

        }

        if (ExpAnsCategoryGet.equals("Italian") && ExpAnsEpochGet.equals("High Middle Ages")) {
            panelName = "itahma";
        }

        if (ExpAnsCategoryGet.equals("Spanish") && ExpAnsEpochGet.equals("Enlightenment")) {
            panelName = "spaenl";

        }

        if (ExpAnsCategoryGet.equals("Papacy") && ExpAnsEpochGet.equals("High Middle Ages")) {
            panelName = "paphma";

        }

        if (ExpAnsCategoryGet.equals("Papacy") && ExpAnsEpochGet.equals("Late Middle Ages")) {
            panelName = "paplma";

        }

        if (ExpAnsCategoryGet.equals("Papacy") && ExpAnsEpochGet.equals("Renaissance")) {
            panelName = "papren";

        }

        if (ExpAnsCategoryGet.equals("Hungarian") && ExpAnsEpochGet.equals("WWII")) {
            panelName = "magwwii";

        }

        if (ExpAnsCategoryGet.equals("Polish") && ExpAnsEpochGet.equals("Enlightenment")) {
            panelName = "polenl";

        }

        if (ExpAnsCategoryGet.equals("Polish") && ExpAnsEpochGet.equals("PreModern")) {
            panelName = "polpmod";

        }

        if (ExpAnsCategoryGet.equals("Romanian") && ExpAnsEpochGet.equals("Renaissance")) {
            panelName = "romren";

        }

        if (ExpAnsCategoryGet.equals("Romanian") && ExpAnsEpochGet.equals("WWII")) {
            panelName = "romwwii";

        }

        if (ExpAnsCategoryGet.equals("Russian") && ExpAnsEpochGet.equals("WWII")) {
            panelName = "ruswwii";

        }

        if (ExpAnsCategoryGet.equals("Russian") && ExpAnsEpochGet.equals("Contemporary")) {
            panelName = "ruscon";

        }

        if (ExpAnsCategoryGet.equals("American") && ExpAnsEpochGet.equals("Enlightenment")) {
            panelName = "amenl"; // off pattern name but keepin because lazy

        }

        if (ExpAnsCategoryGet.equals("American") && ExpAnsEpochGet.equals("Civil War")) {
            panelName = "amecwar"; // off pattern name but keepin because lazy

        }
        if (ExpAnsCategoryGet.equals("American") && ExpAnsEpochGet.equals("Modern")) {
            panelName = "ammod";

        }

        if (ExpAnsCategoryGet.equals("American") && ExpAnsEpochGet.equals("Contemporary")) {
            panelName = "amcon";

        }

        if (ExpAnsCategoryGet.equals("Mexican") && ExpAnsEpochGet.equals("Rennaisance")) {
            panelName = "mexren";

        }

        if (ExpAnsCategoryGet.equals("Mexican") && ExpAnsEpochGet.equals("Modern")) {
            panelName = "mexmod";

        }

        Query panelQuery = FirebaseDatabase.getInstance().getReference().child("panels").orderByChild("panelname").equalTo(panelName);
        panelQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot panels : badgeProfSnapshot.getChildren()) {

                    try {
                        String imagePanelLink = panels.child("panelimagelink").getValue().toString();
                        //Picasso.get().load(imagePanelLink).into(loutEApanelX);
                        Picasso.get().load(imagePanelLink).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                loutEApanelX.setBackground(new BitmapDrawable(bitmap));
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });

                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        ///////////// END OF LOGIC FOR EA PANEL CHOICE //////////////////////////////////


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
