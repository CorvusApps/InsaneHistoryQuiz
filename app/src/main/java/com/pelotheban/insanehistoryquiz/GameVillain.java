package com.pelotheban.insanehistoryquiz;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.PorterDuff;
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
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Random;

public class GameVillain extends AppCompatActivity implements View.OnClickListener {


    //main ui

    private int maxNumber, minRange, maxRange;
    private int randQuestionPre;
    private int randQuestion;
    private String [] questionListVillains;
    private String breaker;
    private SharedPreferences pastVillainQuestionsShared;
    private String TestQCounterString;
    private TextView txtTestQCountX;

    private ImageView imgVillainPicX;
    private TextView txtCoinVillainCounterX, txtCoinVillainAdderX;
    private int coinVillainAdder;

    private int height2;
    private int width2;

    // shared preferences to / from Expanded
    private SharedPreferences sharedVillainGameplayCounter;
    private int villainGameplayCounter;

    // buttons

    private int randAnswer;

    private LinearLayout loutShowVillainX;
    private Button btnAnswer1X, btnAnswer2X, btnAnswer3X, btnAnswer4X;
    private Button btnAnswer1XRed, btnAnswer2XRed, btnAnswer3XRed, btnAnswer4XRed;
    private Button btnAnswer1XGreen, btnAnswer2XGreen, btnAnswer3XGreen, btnAnswer4XGreen;

    String winner;

    //tiles
    private ImageView imgTile1X, imgTile2X, imgTile3X, imgTile4X, imgTile5X, imgTile6X, imgTile7X, imgTile8X, imgTile9X, imgTile10X,
                         imgTile11X, imgTile12X, imgTile13X, imgTile14X, imgTile15X, imgTile16X, imgTile17X, imgTile18X, imgTile19X, imgTile20X,
                         imgTile21X, imgTile22X, imgTile23X, imgTile24X, imgTile25X;

    private int tile1pressed, tile2pressed, tile3pressed, tile4pressed, tile5pressed, tile6pressed, tile7pressed, tile8pressed, tile9pressed,
                tile10pressed, tile11pressed, tile12pressed, tile13pressed, tile14pressed, tile15pressed, tile16pressed, tile17pressed, tile18pressed,
                 tile19pressed, tile20pressed, tile21pressed, tile22pressed, tile23pressed, tile24pressed, tile25pressed, tile26pressed;

    // Gold animation
    private ImageView imgGoldCoinsX;
    private LinearLayout loutGoldCoinsX;
    private ImageView imgCoin1X, imgCoin2X, imgCoin3X, imgCoin4X, imgCoin5X, imgCoin6X, imgCoin7X, imgCoin8X, imgCoin9X, imgCoin10X;

    //Firebase

    private DatabaseReference gameVillainReference;

    private String uid; // this is for the user account side
    private DatabaseReference userVillainReference;
    private Query sortUsersVillainQuery, villainQuestionQuery, villainsQuery;
    private String coinsOwnedVillainString;
    private int coinsOwnedVillain;
    private FirebaseAuth mAuthV;

    private String correctVillainAnsRec, wrongVillainAns1Rec, wrongVillainAns2Rec, wrongVillainAns3Rec, villainExpandedRec;
    private String imageVillainLinkRec;


    private String consStreetVillainString;
    private int consStreakVillain;

    private String longestStreakVillainString;
    private int longestStreakVillain;

    private String totalAnsweredVillainString;
    private int totalAnsweredVillain;

    private String totalQuestionsVillainString;
    private int totalQuestionsVillain;



    // Play on buttons

    private LinearLayout loutVillainGameButtonsX;
    private ImageView btnVillainProfileX, btnVillainPlayX, btnVillainLeaderX;
    private ImageView btnVillainProfileGlowX, btnVillainPlayGlowX, btnVillainLeaderGlowX;

    TextView txtSetVillainQuestion;

    // Expanded answer

    private LinearLayout loutVillainExpandedX;
    private TextView txtVillainExpandedAnswerShowX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_villain);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        double height = size.y;

        height2 = (int) Math.round(height);
        width2 = (int) Math.round(width);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // main ui

        imgVillainPicX = findViewById(R.id.imgVillainPic);
        txtCoinVillainCounterX = findViewById(R.id.txtCoinVillainCounter);
        txtCoinVillainAdderX = findViewById(R.id.txtCoinVillainAdder);
        coinVillainAdder = 30;
        txtCoinVillainAdderX.setText("+" + coinVillainAdder);



        // villain gameplay counter
        sharedVillainGameplayCounter = getSharedPreferences("villainCounter", MODE_PRIVATE);

            // set the counter back to zero right away
            villainGameplayCounter = 0;
            SharedPreferences.Editor editor = sharedVillainGameplayCounter.edit();
            editor.putInt("CounterVillainGameplay", villainGameplayCounter);
            editor.apply(); // saves the value

        // Answer buttons

        btnAnswer1X = findViewById(R.id.btnAnswer1);
        btnAnswer2X = findViewById(R.id.btnAnswer2);
        btnAnswer3X = findViewById(R.id.btnAnswer3);
        btnAnswer4X = findViewById(R.id.btnAnswer4);

        btnAnswer1XRed = findViewById(R.id.btnAnswer1Red);
        btnAnswer2XRed = findViewById(R.id.btnAnswer2Red);
        btnAnswer3XRed = findViewById(R.id.btnAnswer3Red);
        btnAnswer4XRed = findViewById(R.id.btnAnswer4Red);

        btnAnswer1XGreen = findViewById(R.id.btnAnswer1Green);
        btnAnswer2XGreen = findViewById(R.id.btnAnswer2Green);
        btnAnswer3XGreen = findViewById(R.id.btnAnswer3Green);
        btnAnswer4XGreen = findViewById(R.id.btnAnswer4Green);

        // tiles

        imgTile1X = findViewById(R.id.imgTile1);
        imgTile1X.setOnClickListener(this);
        imgTile2X = findViewById(R.id.imgTile2);
        imgTile2X.setOnClickListener(this);
        imgTile3X = findViewById(R.id.imgTile3);
        imgTile3X.setOnClickListener(this);
        imgTile4X = findViewById(R.id.imgTile4);
        imgTile4X.setOnClickListener(this);
        imgTile5X = findViewById(R.id.imgTile5);
        imgTile5X.setOnClickListener(this);
        imgTile6X = findViewById(R.id.imgTile6);
        imgTile6X.setOnClickListener(this);
        imgTile7X = findViewById(R.id.imgTile7);
        imgTile7X.setOnClickListener(this);
        imgTile8X = findViewById(R.id.imgTile8);
        imgTile8X.setOnClickListener(this);
        imgTile9X = findViewById(R.id.imgTile9);
        imgTile9X.setOnClickListener(this);
        imgTile10X = findViewById(R.id.imgTile10);
        imgTile10X.setOnClickListener(this);
        imgTile11X = findViewById(R.id.imgTile11);
        imgTile11X.setOnClickListener(this);
        imgTile12X = findViewById(R.id.imgTile12);
        imgTile12X.setOnClickListener(this);
        imgTile13X = findViewById(R.id.imgTile13);
        imgTile13X.setOnClickListener(this);
        imgTile14X = findViewById(R.id.imgTile14);
        imgTile14X.setOnClickListener(this);
        imgTile15X = findViewById(R.id.imgTile15);
        imgTile15X.setOnClickListener(this);
        imgTile16X = findViewById(R.id.imgTile16);
        imgTile16X.setOnClickListener(this);
        imgTile17X = findViewById(R.id.imgTile17);
        imgTile17X.setOnClickListener(this);
        imgTile18X = findViewById(R.id.imgTile18);
        imgTile18X.setOnClickListener(this);
        imgTile19X = findViewById(R.id.imgTile19);
        imgTile19X.setOnClickListener(this);
        imgTile20X = findViewById(R.id.imgTile20);
        imgTile20X.setOnClickListener(this);
        imgTile21X = findViewById(R.id.imgTile21);
        imgTile21X.setOnClickListener(this);
        imgTile22X = findViewById(R.id.imgTile22);
        imgTile22X.setOnClickListener(this);
        imgTile23X = findViewById(R.id.imgTile23);
        imgTile23X.setOnClickListener(this);
        imgTile24X = findViewById(R.id.imgTile24);
        imgTile24X.setOnClickListener(this);
        imgTile25X = findViewById(R.id.imgTile25);
        imgTile25X.setOnClickListener(this);

        tile1pressed = 1;
        tile2pressed = 1;
        tile3pressed = 1;
        tile4pressed = 1;
        tile5pressed = 1;
        tile6pressed = 1;
        tile7pressed = 1;
        tile8pressed = 1;
        tile9pressed = 1;
        tile10pressed = 1;
        tile11pressed = 1;
        tile12pressed = 1;
        tile13pressed = 1;
        tile14pressed = 1;
        tile15pressed = 1;
        tile16pressed = 1;
        tile17pressed = 1;
        tile18pressed = 1;
        tile19pressed = 1;
        tile20pressed = 1;
        tile21pressed = 1;
        tile22pressed = 1;
        tile23pressed = 1;
        tile24pressed = 1;
        tile25pressed = 1;

        // goldcoin animation

        loutGoldCoinsX = findViewById(R.id.loutGoldCoins);
        imgGoldCoinsX = findViewById(R.id.imgGoldCoins);
        imgCoin1X = findViewById(R.id.imgCoin1);
        imgCoin2X = findViewById(R.id.imgCoin2);
        imgCoin3X = findViewById(R.id.imgCoin3);
        imgCoin4X = findViewById(R.id.imgCoin4);
        imgCoin5X = findViewById(R.id.imgCoin5);
        imgCoin6X = findViewById(R.id.imgCoin6);
        imgCoin7X = findViewById(R.id.imgCoin7);
        imgCoin8X = findViewById(R.id.imgCoin8);
        imgCoin9X = findViewById(R.id.imgCoin9);
        imgCoin10X = findViewById(R.id.imgCoin10);

        // play buttons

        loutVillainGameButtonsX = findViewById(R.id.loutGameVillainButtons);

        btnVillainProfileX = findViewById(R.id.btnVillainProfile);
        btnVillainProfileGlowX = findViewById(R.id.btnVillainProfileGlow);
        btnVillainPlayX = findViewById(R.id.btnVillainPlay);
        btnVillainPlayGlowX = findViewById(R.id.btnVillainPlayGlow);
        btnVillainLeaderX = findViewById(R.id.btnVillainLeaders);
        btnVillainLeaderGlowX = findViewById(R.id.btnVillainLeadersGlow);

        txtSetVillainQuestion = findViewById(R.id.txtSetVillainQuestion);

        // Expanded answer
        loutVillainExpandedX = findViewById(R.id.loutVillainExpanded);
        txtVillainExpandedAnswerShowX = findViewById(R.id.txtVillainExpandedAnswerShow);

        // Firebase

        uid = FirebaseAuth.getInstance().getUid();

        userVillainReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid);

        sortUsersVillainQuery = FirebaseDatabase.getInstance().getReference().child("my_users").orderByChild("user").equalTo(uid);
        sortUsersVillainQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {

                for (DataSnapshot villainUsers : userSnapshot.getChildren()) {


                    try {
                    coinsOwnedVillainString = villainUsers.child("coins").getValue().toString();
                    coinsOwnedVillain = Integer.parseInt(coinsOwnedVillainString);

                    } catch (Exception e) {

                    }

                    try {
                        consStreetVillainString = villainUsers.child("constreak").getValue().toString();
                        consStreakVillain = Integer.valueOf(consStreetVillainString);
                    } catch (Exception e) {

                    }

                    try {
                        totalAnsweredVillainString = villainUsers.child("totalanswered").getValue().toString();
                        totalAnsweredVillain = Integer.valueOf(totalAnsweredVillainString);
                    } catch (Exception e) {

                    }

                    try {
                        longestStreakVillainString = villainUsers.child("longeststreak").getValue().toString();
                        longestStreakVillain = Integer.valueOf(longestStreakVillainString);

                        if (longestStreakVillain < consStreakVillain) {

                            longestStreakVillain = consStreakVillain;

                        }

                    } catch (Exception e) {

                    }

                    try {
                        totalQuestionsVillainString = villainUsers.child("totalquestions").getValue().toString();
                        totalQuestionsVillain = Integer.valueOf(totalQuestionsVillainString);
                    } catch (Exception e) {

                    }

                    userStatsSet();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Log.i("TIMING", "Number: " + maxNumber);
        // *** // questionList = new String[maxNumber]; // setting size of our array

        maxRange = 44; // just in case query cancels
        DatabaseReference quizVillainsRef = FirebaseDatabase.getInstance().getReference().child("values").child("villains");
        quizVillainsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String maxRangeString = dataSnapshot.getValue().toString();
                maxRange = Integer.valueOf(maxRangeString);
                Log.i("Levels", "max range:  " + maxRange);
                randQuestionSeclect();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                randQuestionSeclect();

            }
        });

        btnAnswer1X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (randAnswer == 1) {

                    winner = "yes";
                    btnAnswer1X.setVisibility(View.GONE);
                    btnAnswer1XGreen.setVisibility(View.VISIBLE);

                } else {

                    winner = "no";

                    btnAnswer1X.setVisibility(View.GONE);
                    btnAnswer1XRed.setVisibility(View.VISIBLE);

                    if (randAnswer == 2) {

                        btnAnswer2X.setVisibility(View.GONE);
                        btnAnswer2XGreen.setVisibility(View.VISIBLE);
                    }

                    if (randAnswer == 3) {
                        btnAnswer3X.setVisibility(View.GONE);
                        btnAnswer3XGreen.setVisibility(View.VISIBLE);

                    }
                    if (randAnswer == 4) {
                        btnAnswer4X.setVisibility(View.GONE);
                        btnAnswer4XGreen.setVisibility(View.VISIBLE);

                    }
                }

                CountDownTimer correctorPaintTimer = new CountDownTimer(1000, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        if (winner.equals("yes")) {
                            grantVillainCoins();
                        } else {

                            takeVillainCoins();
                        }

                        villainExpandedAnswer();
                    }
                }.start();



            }
        });

        btnAnswer2X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (randAnswer == 2) {
                    winner = "yes";

                    btnAnswer2X.setVisibility(View.GONE);
                    btnAnswer2XGreen.setVisibility(View.VISIBLE);


                } else {

                    winner = "no";

                    btnAnswer2X.setVisibility(View.GONE);
                    btnAnswer2XRed.setVisibility(View.VISIBLE);

                    if (randAnswer == 1) {

                        btnAnswer1X.setVisibility(View.GONE);
                        btnAnswer1XGreen.setVisibility(View.VISIBLE);
                    }

                    if (randAnswer == 3) {
                        btnAnswer3X.setVisibility(View.GONE);
                        btnAnswer3XGreen.setVisibility(View.VISIBLE);

                    }
                    if (randAnswer == 4) {
                        btnAnswer4X.setVisibility(View.GONE);
                        btnAnswer4XGreen.setVisibility(View.VISIBLE);

                    }

                }

                CountDownTimer correctorPaintTimer = new CountDownTimer(1000, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        if (winner.equals("yes")) {
                            grantVillainCoins();
                        } else {

                            takeVillainCoins();
                        }
                        villainExpandedAnswer();
                    }
                }.start();

            }
        });
        btnAnswer3X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (randAnswer == 3) {
                    winner = "yes";

                    btnAnswer3X.setVisibility(View.GONE);
                    btnAnswer3XGreen.setVisibility(View.VISIBLE);

                } else {

                    winner = "no";

                    btnAnswer3X.setVisibility(View.GONE);
                    btnAnswer3XRed.setVisibility(View.VISIBLE);

                    if (randAnswer == 2) {

                        btnAnswer2X.setVisibility(View.GONE);
                        btnAnswer2XGreen.setVisibility(View.VISIBLE);
                    }

                    if (randAnswer == 1) {
                        btnAnswer1X.setVisibility(View.GONE);
                        btnAnswer1XGreen.setVisibility(View.VISIBLE);

                    }
                    if (randAnswer == 4) {
                        btnAnswer4X.setVisibility(View.GONE);
                        btnAnswer4XGreen.setVisibility(View.VISIBLE);

                    }

                }

                CountDownTimer correctorPaintTimer = new CountDownTimer(1000, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        if (winner.equals("yes")) {
                            grantVillainCoins();
                        } else {

                            takeVillainCoins();
                        }
                        villainExpandedAnswer();
                    }
                }.start();

            }
        });
        btnAnswer4X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (randAnswer == 4) {
                    winner = "yes";

                    btnAnswer4X.setVisibility(View.GONE);
                    btnAnswer4XGreen.setVisibility(View.VISIBLE);

                } else {

                    winner = "no";

                    btnAnswer4X.setVisibility(View.GONE);
                    btnAnswer4XRed.setVisibility(View.VISIBLE);

                    if (randAnswer == 2) {

                        btnAnswer2X.setVisibility(View.GONE);
                        btnAnswer2XGreen.setVisibility(View.VISIBLE);
                    }

                    if (randAnswer == 3) {
                        btnAnswer3X.setVisibility(View.GONE);
                        btnAnswer3XGreen.setVisibility(View.VISIBLE);

                    }
                    if (randAnswer == 1) {
                        btnAnswer1X.setVisibility(View.GONE);
                        btnAnswer1XGreen.setVisibility(View.VISIBLE);

                    }

                }

                CountDownTimer correctorPaintTimer = new CountDownTimer(1000, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        if (winner.equals("yes")) {
                           grantVillainCoins();
                        } else {

                            takeVillainCoins();
                        }
                       villainExpandedAnswer();
                    }
                }.start();

            }
        });

    }

    private void userStatsSet() {

        txtCoinVillainCounterX.setText(coinsOwnedVillainString);

    }

    public void viallainImageSet() {

        Picasso.get().load(imageVillainLinkRec).into(imgVillainPicX);


    }

    private void buttonsSet(){

        randAnswer = new Random().nextInt(4) + 1;
        Log.i("VILLAIN", "rand: " + randAnswer);

        if (randAnswer == 1) {

            Log.i("VILLAIN", "1 correct: " + correctVillainAnsRec);

            btnAnswer1X.setText(correctVillainAnsRec);
            btnAnswer2X.setText(wrongVillainAns1Rec);
            btnAnswer3X.setText(wrongVillainAns2Rec);
            btnAnswer4X.setText(wrongVillainAns3Rec);

            btnAnswer1XRed.setText(correctVillainAnsRec);
            btnAnswer2XRed.setText(wrongVillainAns1Rec);
            btnAnswer3XRed.setText(wrongVillainAns2Rec);
            btnAnswer4XRed.setText(wrongVillainAns3Rec);

            btnAnswer1XGreen.setText(correctVillainAnsRec);


        } else if (randAnswer == 2) {
            Log.i("VILLAIN", "2 correct: " + correctVillainAnsRec);

            btnAnswer2X.setText(correctVillainAnsRec);
            btnAnswer4X.setText(wrongVillainAns1Rec);
            btnAnswer3X.setText(wrongVillainAns2Rec);
            btnAnswer1X.setText(wrongVillainAns3Rec);

            btnAnswer2XRed.setText(correctVillainAnsRec);
            btnAnswer4XRed.setText(wrongVillainAns1Rec);
            btnAnswer3XRed.setText(wrongVillainAns2Rec);
            btnAnswer1XRed.setText(wrongVillainAns3Rec);

            btnAnswer2XGreen.setText(correctVillainAnsRec);



        } else if (randAnswer == 3) {
            Log.i("VILLAIN", "3 correct: " + correctVillainAnsRec);

            btnAnswer3X.setText(correctVillainAnsRec);
            btnAnswer1X.setText(wrongVillainAns1Rec);
            btnAnswer2X.setText(wrongVillainAns2Rec);
            btnAnswer4X.setText(wrongVillainAns3Rec);

            btnAnswer3XRed.setText(correctVillainAnsRec);
            btnAnswer1XRed.setText(wrongVillainAns1Rec);
            btnAnswer2XRed.setText(wrongVillainAns2Rec);
            btnAnswer4XRed.setText(wrongVillainAns3Rec);

            btnAnswer3XGreen.setText(correctVillainAnsRec);



        } else if (randAnswer == 4) {
            Log.i("VILLAIN", "4 correct: " + correctVillainAnsRec);

            btnAnswer4X.setText(correctVillainAnsRec);
            btnAnswer3X.setText(wrongVillainAns1Rec);
            btnAnswer2X.setText(wrongVillainAns2Rec);
            btnAnswer1X.setText(wrongVillainAns3Rec);

            btnAnswer4XRed.setText(correctVillainAnsRec);
            btnAnswer3XRed.setText(wrongVillainAns1Rec);
            btnAnswer2XRed.setText(wrongVillainAns2Rec);
            btnAnswer1XRed.setText(wrongVillainAns3Rec);

            btnAnswer4XGreen.setText(correctVillainAnsRec);


        }
    }

    private void randQuestionSeclect() {



        // xxxxxx ////
        int maxNumber2 = maxRange;
        questionListVillains = new String[maxNumber2]; // setting size of our array
        Log.i("Levels", "in randQuestionSelect maxNumber2 " + maxNumber2);
        // XXXXXX ////

        //getting the array with question numbers asked to date from shared prefs as a string
        pastVillainQuestionsShared = getSharedPreferences("pastVillainQuestions", MODE_PRIVATE);
        String questionListStringified = pastVillainQuestionsShared.getString("questionListVillains", "0"); // where if no settings

        // taking the string from shared prefs and converting it back to array
        questionListVillains = questionListStringified.split(",");

        // getting the random question # for next round and setting it to string for comparisons to previously asked
        // *** // randQuestionPre = new Random().nextInt(maxNumber) + 1; // random question number to be displayed

        //Xxxxxx ///
        randQuestionPre = new Random().nextInt(maxNumber2 + 1); // [0, 60] + 20 => [20, 80]
        Log.i("Levels", "randQustionPre: " + randQuestionPre);
        //xxxx ////

        String randQuestionPreStr = String.valueOf(randQuestionPre);
        Log.i("Levels", "randquestionPreStr: " + randQuestionPreStr);

        // checking to see if gone through all questions and if so resetting the shared pref array back to zero - so the questions asked comparison starts from scratch
        if (questionListVillains.length > (maxNumber2-1)) {

            String reset = "0,";
            questionListVillains = reset.split(",");

            Log.i("Levels", "resetting question list: " + maxNumber2);

        }

        // checking to see if the random question has already been asked and no going below to get to game while if asked then getting
        // into a loop which runs until an unasked question pops up
        if (Arrays.asList(questionListVillains).contains(randQuestionPreStr)) {

            Log.i("Levels", "question repeated going to while");

            //Toast.makeText(Game.this, "FREEZING", Toast.LENGTH_SHORT).show();
            int spinner = 1;
            while (true) { // this should spin without going beyond in ASYNC way until break
                //for (int x = 0; x < 50; x++){

                // within the loop generating new random numbers and checking them agasint the array and doing so as long as not unique
                //***// randQuestionPre = new Random().nextInt(maxNumber) + 1; // random question number to be displayed
                //xxx///
                randQuestionPre = new Random().nextInt(maxNumber2 + 1);
                //xxx///
                String randQuestionPreStr2 = String.valueOf(randQuestionPre);

                if (Arrays.asList(questionListVillains).contains(randQuestionPreStr2)) {

                    spinner = spinner +1;
                }
                else { // this else is activated when the random number is NOT in the array at which point the question is set, the shared preff
                    //is updated and the action moves to the game methods

                    randQuestion = randQuestionPre;

                    Log.i("Levels", "setting randQuestion in while: " + randQuestion);

                    String randQuestionStr = String.valueOf(randQuestion); // convert the number we just generated to a string to add to our array

                    // now we add the new  question to our string

                    StringBuilder sb = new StringBuilder(); // converting array back into string using string builder
                    for (int i = 0; i < questionListVillains.length; i++) {  // create a string from playlist - so now sb is string from the array with comas between all the terms
                        sb.append(questionListVillains[i]).append(",");
                    }

                    String sbString = sb.toString(); // convert our string builder to string
                    // add the new question to the string from array

                    sbString = sbString + randQuestionStr; // adding the question that made the cut to the array

                    SharedPreferences.Editor editor = pastVillainQuestionsShared.edit();
                    editor.putString("questionListVillains", sbString);
                    editor.apply(); // saves the value

                    Toast.makeText(GameVillain.this, sbString + "***" + spinner, Toast.LENGTH_LONG).show();
                    gameStart();

                    break;
                }


            }

        } else { // so if the List so far DOES NOT HAVE the number we just generated"

            randQuestion = randQuestionPre; // set the questions number to the number we just generated (that is used in game below)
            Log.i("Levels", "setting randquestion without going into the while: " + randQuestion);
            String randQuestionStr = String.valueOf(randQuestion); // convert the number we just generated to a string to add to our array

            // now we add the new  question to our string

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < questionListVillains.length; i++) {  // create a string from playlist - so now sb is string from the array with comas between all the terms
                sb.append(questionListVillains[i]).append(",");
            }

            String sbString = sb.toString(); // convert our string builder to string
            // add the new question to the string from array

            sbString = sbString + randQuestionStr;

            SharedPreferences.Editor editor = pastVillainQuestionsShared.edit();
            editor.putString("questionListVillains", sbString);
            editor.apply(); // saves the value

            Toast.makeText(GameVillain.this, sbString, Toast.LENGTH_LONG).show();
            gameStart();

        }


    }

    private void gameStart(){

        String randomQuestionString = String.valueOf(randQuestion);
        villainsQuery = FirebaseDatabase.getInstance().getReference().child("villains").orderByChild("villainname").equalTo(randomQuestionString);
        villainsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot villainSnapshot) {

                for (DataSnapshot villainImages : villainSnapshot.getChildren()) {

                    imageVillainLinkRec = villainImages.child("villainimagelink").getValue().toString();

                    viallainImageSet();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        villainQuestionQuery = FirebaseDatabase.getInstance().getReference().child("villainquestions").orderByChild("aaavillainid").equalTo(randQuestion);
        villainQuestionQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot questionSnapshot) {

                for (DataSnapshot villainQuestions : questionSnapshot.getChildren()) {

                    correctVillainAnsRec = villainQuestions.child("bbbcorrectvillainansw").getValue().toString();
                    wrongVillainAns1Rec = villainQuestions.child("cccwrongvillainsans1").getValue().toString();
                    wrongVillainAns2Rec = villainQuestions.child("dddwrongvillainsans2").getValue().toString();
                    wrongVillainAns3Rec = villainQuestions.child("eeewrongvillainsans3").getValue().toString();
                    villainExpandedRec = villainQuestions.child("fffvillainexpanded").getValue().toString();

                    Log.i("VILLAIN", "correct: " + correctVillainAnsRec);

                    buttonsSet();


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void grantVillainCoins(){

        coinsOwnedVillain = coinsOwnedVillain + coinVillainAdder;

        userVillainReference.getRef().child("coins").setValue(coinsOwnedVillain);
        int coinsOwnedSort = - coinsOwnedVillain;
        userVillainReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);

        grantVillainCoinsAnimation();
    }

    private void takeVillainCoins() {

        txtCoinVillainAdderX.setVisibility(View.GONE);
        coinsOwnedVillain = coinsOwnedVillain -10;

        userVillainReference.getRef().child("coins").setValue(coinsOwnedVillain);
        int coinsOwnedSort = - coinsOwnedVillain;
        userVillainReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);

        takeVillainCoinsAnimation();

    }

    private void takeVillainCoinsAnimation(){

        imgGoldCoinsX.animate().scaleY(1.5f).scaleX(1.5f).setDuration(400);
        txtCoinVillainCounterX.setTextSize(45);
        txtCoinVillainCounterX.setText("-10");
        txtCoinVillainCounterX.setTextColor(getResources().getColor(R.color.red));


        CountDownTimer takeGoldTimer = new CountDownTimer(1500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                imgGoldCoinsX.animate().scaleY(1).scaleX(1).setDuration(400);
                txtCoinVillainCounterX.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                txtCoinVillainCounterX.setTextSize(30);
                String coinsOwedZ = Integer.toString(coinsOwnedVillain);
                txtCoinVillainCounterX.setText(coinsOwedZ);

                tileFlip();
            }
        }.start();



    }

    private void grantVillainCoinsAnimation(){

        imgGoldCoinsX.animate().scaleY(1.5f).scaleX(1.5f).setDuration(400);
        txtCoinVillainCounterX.setVisibility(View.GONE);

        //loutGoldCoinsX.animate().scaleY(1.15f);

        imgCoin1X.setVisibility(View.VISIBLE);
        imgCoin2X.setVisibility(View.VISIBLE);
        imgCoin3X.setVisibility(View.VISIBLE);
        imgCoin4X.setVisibility(View.VISIBLE);
        imgCoin5X.setVisibility(View.VISIBLE);
        imgCoin6X.setVisibility(View.VISIBLE);
        imgCoin7X.setVisibility(View.VISIBLE);
        imgCoin8X.setVisibility(View.VISIBLE);
        imgCoin9X.setVisibility(View.VISIBLE);
        imgCoin10X.setVisibility(View.VISIBLE);

        YoYo.with(Techniques.FadeIn)
                .delay(0)
                .duration(50)
                .repeat(0)
                .playOn(imgCoin1X);

        YoYo.with(Techniques.FadeIn)
                .delay(0)
                .duration(100)
                .repeat(0)
                .playOn(imgCoin2X);
        YoYo.with(Techniques.FadeIn)
                .delay(0)
                .duration(150)
                .repeat(0)
                .playOn(imgCoin3X);
        YoYo.with(Techniques.FadeIn)
                .delay(0)
                .duration(200)
                .repeat(0)
                .playOn(imgCoin4X);
        YoYo.with(Techniques.FadeIn)
                .delay(0)
                .duration(250)
                .repeat(0)
                .playOn(imgCoin5X);
        YoYo.with(Techniques.FadeIn)
                .delay(0)
                .duration(300)
                .repeat(0)
                .playOn(imgCoin6X);
        YoYo.with(Techniques.FadeIn)
                .delay(0)
                .duration(350)
                .repeat(0)
                .playOn(imgCoin7X);
        YoYo.with(Techniques.FadeIn)
                .delay(0)
                .duration(400)
                .repeat(0)
                .playOn(imgCoin8X);
        YoYo.with(Techniques.FadeIn)
                .delay(0)
                .duration(450)
                .repeat(0)
                .playOn(imgCoin9X);
        YoYo.with(Techniques.FadeIn)
                .delay(0)
                .duration(500)
                .repeat(0)
                .playOn(imgCoin10X);

        imgCoin1X.animate().rotation(1440).setDuration(250);
        imgCoin2X.animate().rotation(1440).setDuration(500);
        imgCoin3X.animate().rotation(1440).setDuration(750);
        imgCoin4X.animate().rotation(1440).setDuration(1000);
        imgCoin5X.animate().rotation(1440).setDuration(1250);
        imgCoin6X.animate().rotation(1440).setDuration(1500);
        imgCoin7X.animate().rotation(1440).setDuration(1750);
        imgCoin8X.animate().rotation(1440).setDuration(2000);
        imgCoin9X.animate().rotation(1440).setDuration(2250);
        imgCoin10X.animate().rotation(1440).setDuration(2500);

        CountDownTimer goldAwardTimer = new CountDownTimer(3500, 50) {

            int tickcounter = 0;

            @Override
            public void onTick(long millisUntilFinished) {



                if (millisUntilFinished <850) {

                    imgCoin10X.setVisibility(View.GONE);
                    if (tickcounter == 9) {

                        //txtCoinPaintAdderX.setText("+10");
                        tickcounter = 10;
                    }


                } else
                if (millisUntilFinished <1100) {

                    imgCoin9X.setVisibility(View.GONE);
                    if (tickcounter == 8) {

                       // txtCoinPaintAdderX.setText("+9");
                        tickcounter = 9;
                    }

                }else
                if (millisUntilFinished < 1350) {
                    imgCoin8X.setVisibility(View.GONE);
                    if (tickcounter == 7) {

                       // txtCoinPaintAdderX.setText("+8");
                        tickcounter = 8;
                    }

                } else
                if (millisUntilFinished < 1600) {

                    imgCoin7X.setVisibility(View.GONE);
                    if (tickcounter == 6) {

                      //  txtCoinPaintAdderX.setText("+7");
                        tickcounter = 7;
                    }

                } else
                if (millisUntilFinished < 1850) {
                    imgCoin6X.setVisibility(View.GONE);
                    if (tickcounter == 5) {

                       // txtCoinPaintAdderX.setText("+6");
                        tickcounter = 6;
                    }


                }

                if (millisUntilFinished < 2100) {
                    imgCoin5X.setVisibility(View.GONE);
                    if (tickcounter == 4) {

                      //  txtCoinPaintAdderX.setText("+5");
                        tickcounter = 5;
                    }


                }

                if (millisUntilFinished < 2350) {
                    imgCoin4X.setVisibility(View.GONE);
                    if (tickcounter == 3) {

                       // txtCoinPaintAdderX.setText("+4");
                        tickcounter = 4;
                    }

                } else
                if (millisUntilFinished < 2600) {

                    imgCoin3X.setVisibility(View.GONE);
                    if (tickcounter == 2) {

                      //  txtCoinPaintAdderX.setText("+3");
                        tickcounter = 3;
                    }

                } else
                if (millisUntilFinished < 2850) {
                    imgCoin2X.setVisibility(View.GONE);
                    if (tickcounter == 1) {

                     //   txtCoinPaintAdderX.setText("+2");
                        tickcounter = 2;
                    }


                }

                if (millisUntilFinished < 3100) {
                    imgCoin1X.setVisibility(View.GONE);
                    if (tickcounter == 0) {

                     //  txtCoinPaintAdderX.setText("+1");
                        tickcounter = 1;
                    }

                }

            }

            @Override
            public void onFinish() {

                loutGoldCoinsX.animate().scaleY(1);
                txtCoinVillainAdderX.setVisibility(View.GONE);
                String coinsOwedZ = Integer.toString(coinsOwnedVillain);
                txtCoinVillainCounterX.setText(coinsOwedZ);
                txtCoinVillainCounterX.setVisibility(View.VISIBLE);

                imgGoldCoinsX.animate().scaleY(1).scaleX(1).setDuration(400);

                tileFlip();

               // loutPaintGameButtonsX.setVisibility(View.VISIBLE);
                //scvAGPBX.fullScroll(View.FOCUS_DOWN);
               // Log.i("SCROLLTAG", "Final toggle = " + scrollToggle);
                //scvAGPBX.smoothScrollBy(0,2000);

//                CountDownTimer scrolltimer = new CountDownTimer(450, 50 ) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        scvAGPBX.smoothScrollBy(0,2000);
//                    }
//                }.start();
            }
        }.start();
    }

    public void tileFlip(){

        imgTile1X.animate().alpha(0f).setDuration(1000);
        imgTile1X.animate().rotationY(180).setDuration(800);

        imgTile2X.animate().alpha(0f).setDuration(1000);
        imgTile2X.animate().rotationY(180).setDuration(800);

        imgTile3X.animate().alpha(0f).setDuration(1000);
        imgTile3X.animate().rotationY(180).setDuration(800);

        imgTile4X.animate().alpha(0f).setDuration(1000);
        imgTile4X.animate().rotationY(180).setDuration(800);

        imgTile5X.animate().alpha(0f).setDuration(1000);
        imgTile5X.animate().rotationY(180).setDuration(800);

        imgTile6X.animate().alpha(0f).setDuration(1000);
        imgTile6X.animate().rotationY(180).setDuration(800);

        imgTile7X.animate().alpha(0f).setDuration(1000);
        imgTile7X.animate().rotationY(180).setDuration(800);

        imgTile8X.animate().alpha(0f).setDuration(1000);
        imgTile8X.animate().rotationY(180).setDuration(800);

        imgTile9X.animate().alpha(0f).setDuration(1000);
        imgTile9X.animate().rotationY(180).setDuration(800);

        imgTile10X.animate().alpha(0f).setDuration(1000);
        imgTile10X.animate().rotationY(180).setDuration(800);

        imgTile11X.animate().alpha(0f).setDuration(1000);
        imgTile11X.animate().rotationY(180).setDuration(800);

        imgTile12X.animate().alpha(0f).setDuration(1000);
        imgTile12X.animate().rotationY(180).setDuration(800);

        imgTile13X.animate().alpha(0f).setDuration(1000);
        imgTile13X.animate().rotationY(180).setDuration(800);

        imgTile14X.animate().alpha(0f).setDuration(1000);
        imgTile14X.animate().rotationY(180).setDuration(800);

        imgTile15X.animate().alpha(0f).setDuration(1000);
        imgTile15X.animate().rotationY(180).setDuration(800);

        imgTile16X.animate().alpha(0f).setDuration(1000);
        imgTile16X.animate().rotationY(180).setDuration(800);

        imgTile17X.animate().alpha(0f).setDuration(1000);
        imgTile17X.animate().rotationY(180).setDuration(800);

        imgTile18X.animate().alpha(0f).setDuration(1000);
        imgTile18X.animate().rotationY(180).setDuration(800);

        imgTile19X.animate().alpha(0f).setDuration(1000);
        imgTile19X.animate().rotationY(180).setDuration(800);

        imgTile20X.animate().alpha(0f).setDuration(1000);
        imgTile20X.animate().rotationY(180).setDuration(800);

        imgTile21X.animate().alpha(0f).setDuration(1000);
        imgTile21X.animate().rotationY(180).setDuration(800);

        imgTile22X.animate().alpha(0f).setDuration(1000);
        imgTile22X.animate().rotationY(180).setDuration(800);

        imgTile23X.animate().alpha(0f).setDuration(1000);
        imgTile23X.animate().rotationY(180).setDuration(800);

        imgTile24X.animate().alpha(0f).setDuration(1000);
        imgTile24X.animate().rotationY(180).setDuration(800);

        imgTile25X.animate().alpha(0f).setDuration(1000);
        imgTile25X.animate().rotationY(180).setDuration(800);

    }

    private void villainExpandedAnswer(){

        btnAnswer1X.setVisibility(View.GONE);
        btnAnswer2X.setVisibility(View.GONE);
        btnAnswer3X.setVisibility(View.GONE);
        btnAnswer4X.setVisibility(View.GONE);

        btnAnswer1XGreen.setVisibility(View.GONE);
        btnAnswer2XGreen.setVisibility(View.GONE);
        btnAnswer3XGreen.setVisibility(View.GONE);
        btnAnswer4XGreen.setVisibility(View.GONE);

        btnAnswer1XRed.setVisibility(View.GONE);
        btnAnswer2XRed.setVisibility(View.GONE);
        btnAnswer3XRed.setVisibility(View.GONE);
        btnAnswer4XRed.setVisibility(View.GONE);

        loutVillainGameButtonsX.setVisibility(View.VISIBLE);
        btnVillainProfileX.setVisibility(View.VISIBLE);
        btnVillainPlayX.setVisibility(View.VISIBLE);
        btnVillainLeaderX.setVisibility(View.VISIBLE);
        txtSetVillainQuestion.setVisibility(View.GONE);

        btnVillainProfileX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnVillainProfileX.setVisibility(View.GONE);
                btnVillainProfileGlowX.setVisibility(View.VISIBLE);

                CountDownTimer glowTimer = new CountDownTimer(500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        Intent intent = new Intent(GameVillain.this, ProfileView.class);
                        startActivity(intent);
                        btnVillainProfileX.setVisibility(View.VISIBLE);
                        btnVillainProfileGlowX.setVisibility(View.GONE);

                    }
                }.start();

            }
        });

        btnVillainPlayX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnVillainPlayX.setVisibility(View.GONE);
                btnVillainPlayGlowX.setVisibility(View.VISIBLE);

                CountDownTimer glowTimer = new CountDownTimer(500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        Intent intent = new Intent(GameVillain.this, Game.class);
                        startActivity(intent);

                    }
                }.start();

            }
        });

        btnVillainLeaderX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnVillainLeaderX.setVisibility(View.GONE);
                btnVillainLeaderGlowX.setVisibility(View.VISIBLE);

                CountDownTimer glowTimer = new CountDownTimer(500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        Intent intent = new Intent(GameVillain.this, LeaderBoard.class);
                        startActivity(intent);
                        btnVillainLeaderX.setVisibility(View.VISIBLE);
                        btnVillainLeaderGlowX.setVisibility(View.GONE);

                    }
                }.start();

            }
        });

        loutVillainExpandedX.setVisibility(View.VISIBLE);

        if (width2 > 1500) { // changes in fot for tablet and then small format phone
            Log.i("EXPTAG", "width = " + width2);

            txtVillainExpandedAnswerShowX.setTextSize(30);
            txtVillainExpandedAnswerShowX.setText(correctVillainAnsRec + " \n" + "------- \n" + villainExpandedRec);


        } else if (height2 < 1300) {

            Log.i("EXPTAG", "height = " + height2);
            txtVillainExpandedAnswerShowX.setTextSize(17);
            txtVillainExpandedAnswerShowX.setText(correctVillainAnsRec + " \n" + "------- \n" + villainExpandedRec);

        } else {

            txtVillainExpandedAnswerShowX.setText(correctVillainAnsRec + " \n" + "------- \n" + villainExpandedRec);

        }

    }


    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    public void onBackPressed(){

        // will ultimately get this to a dialog with exit the question but lose 10 point

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.imgTile1:

                if (coinVillainAdder > 4) {

                    if (tile1pressed == 1) {
                        imgTile1X.animate().alpha(0f).setDuration(1000);
                        imgTile1X.animate().rotationY(180).setDuration(800);
                        tile1pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();

                }

                break;

            case R.id.imgTile2:

                if (coinVillainAdder > 4) {

                    if (tile2pressed == 1) {
                        imgTile2X.animate().alpha(0f).setDuration(1000);
                        imgTile2X.animate().rotationY(180).setDuration(800);
                        tile2pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();

                }

                break;

            case R.id.imgTile3:

                if (coinVillainAdder > 4) {

                    if (tile3pressed == 1) {
                        imgTile3X.animate().alpha(0f).setDuration(1000);
                        imgTile3X.animate().rotationY(180).setDuration(800);
                        tile3pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();

                }

                break;

            case R.id.imgTile4:

                if (coinVillainAdder > 4) {

                    if (tile4pressed == 1) {
                        imgTile4X.animate().alpha(0f).setDuration(1000);
                        imgTile4X.animate().rotationY(180).setDuration(800);
                        tile4pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();

                }

                break;

            case R.id.imgTile5:

                if (coinVillainAdder > 4) {

                    if (tile5pressed == 1) {
                        imgTile5X.animate().alpha(0f).setDuration(1000);
                        imgTile5X.animate().rotationY(180).setDuration(800);
                        tile5pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();

                }

                break;

            case R.id.imgTile6:

                if (coinVillainAdder > 4) {

                    if (tile6pressed == 1) {
                        imgTile6X.animate().alpha(0f).setDuration(1000);
                        imgTile6X.animate().rotationY(180).setDuration(800);
                        tile6pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();

                }

                break;

            case R.id.imgTile7:

                if (coinVillainAdder > 4) {

                    if (tile7pressed == 1) {
                        imgTile7X.animate().alpha(0f).setDuration(1000);
                        imgTile7X.animate().rotationY(180).setDuration(800);
                        tile7pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();

                }

                break;

            case R.id.imgTile8:

                if (coinVillainAdder > 4) {

                    if (tile8pressed == 1) {
                        imgTile8X.animate().alpha(0f).setDuration(1000);
                        imgTile8X.animate().rotationY(180).setDuration(800);
                        tile8pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();

                }

                break;

            case R.id.imgTile9:

                if (coinVillainAdder > 4) {

                    if (tile9pressed == 1) {
                        imgTile9X.animate().alpha(0f).setDuration(1000);
                        imgTile9X.animate().rotationY(180).setDuration(800);
                        tile9pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();

                }

                break;

            case R.id.imgTile10:

                if (coinVillainAdder > 4) {

                    if (tile10pressed == 1) {
                        imgTile10X.animate().alpha(0f).setDuration(1000);
                        imgTile10X.animate().rotationY(180).setDuration(800);
                        tile10pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();

                }

                break;

            case R.id.imgTile11:

                if (coinVillainAdder > 4) {

                    if (tile11pressed == 1) {
                        imgTile11X.animate().alpha(0f).setDuration(1000);
                        imgTile11X.animate().rotationY(180).setDuration(800);
                        tile11pressed = 2;
                        coinVillainAdder = coinVillainAdder -2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

            case R.id.imgTile12:

                if (coinVillainAdder > 4) {

                    if (tile12pressed == 1) {
                        imgTile12X.animate().alpha(0f).setDuration(1000);
                        imgTile12X.animate().rotationY(180).setDuration(800);
                        tile12pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

            case R.id.imgTile13:

                if (coinVillainAdder > 4) {

                    if (tile13pressed == 1) {
                        imgTile13X.animate().alpha(0f).setDuration(1000);
                        imgTile13X.animate().rotationY(180).setDuration(800);
                        tile13pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }



                break;

            case R.id.imgTile14:

                if (coinVillainAdder > 4) {

                    if (tile14pressed == 1) {
                        imgTile14X.animate().alpha(0f).setDuration(1000);
                        imgTile14X.animate().rotationY(180).setDuration(800);
                        tile14pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

            case R.id.imgTile15:

                if (coinVillainAdder > 4) {

                    if (tile15pressed == 1) {
                        imgTile15X.animate().alpha(0f).setDuration(1000);
                        imgTile15X.animate().rotationY(180).setDuration(800);
                        tile15pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

            case R.id.imgTile16:

                if (coinVillainAdder > 4) {

                    if (tile16pressed == 1) {
                        imgTile16X.animate().alpha(0f).setDuration(1000);
                        imgTile16X.animate().rotationY(180).setDuration(800);
                        tile16pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

            case R.id.imgTile17:

                if (coinVillainAdder > 4) {

                    if (tile17pressed == 1) {
                        imgTile17X.animate().alpha(0f).setDuration(1000);
                        imgTile17X.animate().rotationY(180).setDuration(800);
                        tile17pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

            case R.id.imgTile18:

                if (coinVillainAdder > 4) {

                    if (tile18pressed == 1) {
                        imgTile18X.animate().alpha(0f).setDuration(1000);
                        imgTile18X.animate().rotationY(180).setDuration(800);
                        tile18pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

            case R.id.imgTile19:

                if (coinVillainAdder > 4) {

                    if (tile19pressed == 1) {
                        imgTile19X.animate().alpha(0f).setDuration(1000);
                        imgTile19X.animate().rotationY(180).setDuration(800);
                        tile19pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

            case R.id.imgTile20:

                if (coinVillainAdder > 4) {

                    if (tile20pressed == 1) {
                        imgTile20X.animate().alpha(0f).setDuration(1000);
                        imgTile20X.animate().rotationY(180).setDuration(800);
                        tile20pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

            case R.id.imgTile21:

                if (coinVillainAdder > 4) {

                    if (tile21pressed == 1) {
                        imgTile21X.animate().alpha(0f).setDuration(1000);
                        imgTile21X.animate().rotationY(180).setDuration(800);
                        tile21pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

            case R.id.imgTile22:

                if (coinVillainAdder > 4) {

                    if (tile22pressed == 1) {
                        imgTile22X.animate().alpha(0f).setDuration(1000);
                        imgTile22X.animate().rotationY(180).setDuration(800);
                        tile22pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

            case R.id.imgTile23:

                if (coinVillainAdder > 4) {

                    if (tile23pressed == 1) {
                        imgTile23X.animate().alpha(0f).setDuration(1000);
                        imgTile23X.animate().rotationY(180).setDuration(800);
                        tile23pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

            case R.id.imgTile24:

                if (coinVillainAdder > 4) {

                    if (tile24pressed == 1) {
                        imgTile24X.animate().alpha(0f).setDuration(1000);
                        imgTile24X.animate().rotationY(180).setDuration(800);
                        tile24pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

            case R.id.imgTile25:

                if (coinVillainAdder > 4) {

                    if (tile25pressed == 1) {
                        imgTile25X.animate().alpha(0f).setDuration(1000);
                        imgTile25X.animate().rotationY(180).setDuration(800);
                        tile25pressed = 2;
                        coinVillainAdder = coinVillainAdder - 2;
                        txtCoinVillainAdderX.setText("+" + coinVillainAdder);
                    }

                } else {

                    maxTile();
                }

                break;

        }

    }

    private void maxTile (){

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_tile, null);

        final Dialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        CountDownTimer tileDialogTimer = new CountDownTimer(1000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                dialog.dismiss();

            }
        }.start();


    }
}
