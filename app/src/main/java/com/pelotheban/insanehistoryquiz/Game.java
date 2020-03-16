package com.pelotheban.insanehistoryquiz;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.CountDownTimer;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Random;
import java.util.Timer;

public class Game extends AppCompatActivity implements RewardedVideoAdListener {

    // Game play UI elements and variables

    private int randQuestion;
    private String displayAnswer, displayAnswer2, displayAnswer3, displayAnswer4, displayAnswer5; // will be randomly assigned to the answer options in each question
    private int answerCounter; // scrolls through how many answers options to a question the player has seen
    private int randAnswer;
    private ImageView btnCorrectX, btnWrongX, btnWrongGlowX;

    private String ExpandedAnswerPut, ExpAnsCategoryPut, ExpAnsEpochPut; // making this class variable so can go to expanded answer screen
    private String era; // pulls in era so we know which counter to grow when user answers question correctly

    private LinearLayout loutGameQuestionX;
    private LinearLayout loutGameAnswerDisplayX;

    private TextView txtGameQuestionX, txtGameAnswerDisplayX, txtGameExpandedAnswerX;

    private CountDownTimer countDownTimer;

        // corrector view elements

        private View shadeX;
        private ImageView imgCorrectorCheckX, imgCorrectorXmarkX, imgCorrectorTimeoutX;
        private LinearLayout loutCoinAwardX;
        private ImageView imgCoinAwardX;
        private TextView txtCoinAwardX;
        private int rightAnswerTicker;

    //.............//// various score counters

    private TextView txtCoinCounterX, txtConStreakX;
    private String coinGrantToggle;

    private String coinsOwnedString;
    private int coinsOwned;

    private String consStreetString;
    private int consStreak;

    private String longestStreakString;
    private int longestStreak;

    private String totalAnsweredString;
    private int totalAnswered;

    private String totalQuestionsString;
    private int totalQuestions;

    private String eraAnsweredAntiquityString;
    private String eraAnsweredMiddleAgesString;
    private String eraAnsweredRenaissanceString;
    private String eraAnsweredEnlightenmentString;
    private String eraAnsweredEarlyModernString;
    private String eraAnsweredModernString;

    private int eraAnsweredAntiquity;
    private int eraAnsweredMiddleAges;
    private int eraAnsweredRenaissance;
    private int eraAnsweredEnlightenment;
    private int eraAnsweredEarlyModern;
    private int eraAnsweredModern;

    private TextView txtTimerX;
    private ImageView imgHPTimerX, imgHPTimer2X, imgHPTimer3X, imgHPTimer4X, imgHPTimer5X;

    private int timersetting, ticksetting;  // want different setting depending on whether first showing question or just next answer
    private int tickerToggle;




    // Firebase

    private DatabaseReference gameReference;
    private Query sortGameQueryQuestions;

    private String uid; // this is for the user account side
    private DatabaseReference userReference;
    private Query sortUsersQuery;


    String GameCorrectAnswerY;
    String test;

    // variables for UI placements like centering the displays

    private int height2;
    private int width2;

    // adMob

    RewardedVideoAd mRewardedAdGameScreenCoins;
    TextView txtAdMessageX;
    int adMobToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Firebase user

        uid = FirebaseAuth.getInstance().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid);
        userReference.getRef().child("user").setValue(uid);

        //ad mob initialize
        adMobToggle = 0;

        MobileAds.initialize(this, "ca-app-pub-1744081621312112~9212279801");
        mRewardedAdGameScreenCoins = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedAdGameScreenCoins.setRewardedVideoAdListener(this);
        mRewardedAdGameScreenCoins.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());

        txtAdMessageX = findViewById(R.id.txtAdMessage);

        shadeX = findViewById(R.id.shadeCor);

        // Game play
        answerCounter = 1;
        randQuestion = new Random().nextInt(16) + 1; // random question number to be displayed

        //...... displayed outputs
        loutGameQuestionX = findViewById(R.id.loutGameQuestion);
        loutGameAnswerDisplayX = findViewById(R.id.loutGameAnswerDisplay);

        txtGameQuestionX = findViewById(R.id.txtGameQuestion);
        txtGameAnswerDisplayX = findViewById(R.id.txtGameAnswerDisplay);
        txtCoinCounterX = findViewById(R.id.txtCoinCounter);
        txtConStreakX = findViewById(R.id.txtConStreak);


        imgCorrectorCheckX = findViewById(R.id.imgCorrectorCheck);
        imgCorrectorXmarkX = findViewById(R.id.imgCorrectorXmark);
        imgCorrectorTimeoutX = findViewById(R.id.imgCorrectorTimeout);
        loutCoinAwardX = findViewById(R.id.loutCoinAward);
        imgCoinAwardX = findViewById(R.id.imgCoinAward);
        txtCoinAwardX = findViewById(R.id.txtCoinAward);
        rightAnswerTicker = 0;

        // timesettings

        txtTimerX = findViewById(R.id.txtTimer);
        imgHPTimerX = findViewById(R.id.imgHPTimer);
        imgHPTimer2X = findViewById(R.id.imgHPTimer2);
        imgHPTimer3X = findViewById(R.id.imgHPTimer3);
        imgHPTimer4X = findViewById(R.id.imgHPTimer4);
        imgHPTimer5X = findViewById(R.id.imgHPTimer5);

        timersetting = 10000;
        ticksetting = 5000;
        tickerToggle = 1;


        /// sizing the display to have both the question and then the answer mostly in the center

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        double height = size.y;

        height2 = (int) Math.round(height);
        width2 = (int) Math.round(width);


        ////////////// Query to pull in all the user variables like counters of questions answered etc.

        sortUsersQuery = FirebaseDatabase.getInstance().getReference().child("my_users").orderByChild("user").equalTo(uid);
        sortUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot userDs : dataSnapshot.getChildren()) {
                    // need the try because if new account will return null

                    try {
                        consStreetString = userDs.child("constreak").getValue().toString();
                        consStreak = Integer.valueOf(consStreetString);
                    } catch (Exception e) {

                    }

                    try {
                        totalAnsweredString = userDs.child("totalanswered").getValue().toString();
                        totalAnswered = Integer.valueOf(totalAnsweredString);
                    } catch (Exception e) {

                    }

                    try {
                        longestStreakString = userDs.child("longeststreak").getValue().toString();
                        longestStreak = Integer.valueOf(longestStreakString);

                        if (longestStreak < consStreak) {

                            longestStreak = consStreak;

                        }

                    } catch (Exception e) {

                    }

                    try {
                        totalQuestionsString = userDs.child("totalquestions").getValue().toString();
                        totalQuestions = Integer.valueOf(totalQuestionsString);
                    } catch (Exception e) {

                    }

                    try {
                        eraAnsweredAntiquityString = userDs.child("eraansantiquity").getValue().toString();
                        eraAnsweredAntiquity = Integer.valueOf(eraAnsweredAntiquityString);

                    } catch (Exception e) {

                    }

                    try {
                        eraAnsweredMiddleAgesString = userDs.child("eraansmiddle").getValue().toString();
                        eraAnsweredMiddleAges = Integer.valueOf(eraAnsweredMiddleAgesString);

                    } catch (Exception e) {

                    }

                    try {
                        eraAnsweredRenaissanceString = userDs.child("eraansrenaissance").getValue().toString();
                        eraAnsweredRenaissance = Integer.valueOf(eraAnsweredRenaissanceString);

                    } catch (Exception e) {

                    }

                    try {
                        eraAnsweredEnlightenmentString = userDs.child("eraanselight").getValue().toString();
                        eraAnsweredEnlightenment = Integer.valueOf(eraAnsweredEnlightenmentString);

                    } catch (Exception e) {

                    }

                    try {
                        eraAnsweredEarlyModernString = userDs.child("eraansearlymod").getValue().toString();
                        eraAnsweredEarlyModern = Integer.valueOf(eraAnsweredEarlyModernString);

                    } catch (Exception e) {

                    }

                    try {
                        eraAnsweredModernString = userDs.child("eraansmodern").getValue().toString();
                        eraAnsweredModern = Integer.valueOf(eraAnsweredModernString);

                    } catch (Exception e) {

                    }


                    try {
                        coinsOwnedString = userDs.child("coins").getValue().toString();
                        coinsOwned = Integer.valueOf(coinsOwnedString);

                        coinGrantToggle = userDs.child("coinsgranttoggle").getValue().toString();

                       // Toast.makeText(Game.this, "query complete", Toast.LENGTH_SHORT).show();




                    } catch (Exception e) {

                        //Toast.makeText(Game.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }

                txtCoinCounterX.setText(coinsOwnedString); // IF there are any coins in the account will set the counter
                txtConStreakX.setText(consStreetString);

                try {
                    if (coinsOwned > 0 | coinGrantToggle.equals("yes")) {

                        if (coinsOwned<1) {

                            gotoAd();

                        }

                    } else { // setting up grant if conditions for NOT GRANTING are unmet - BUT probably never invoked because
                        // if conditions unmet that just means that the "if" goes null gets caught by try and bounced to error
                        // before invoking the else - So.... repeating this AGAIN in the catch of the if

                        // THE GRANT IS OBSOLETE AS WE NOW DO IT ON HOME PAGE ON FIRST LOGIN BUT KEEPING HERE AS BACK STOP
                            // IN THE CURRENT GRANT also setting up initial sort value defaults but no bothering to add here as this section is unlikely to ever be used

                        userReference.getRef().child("coins").setValue(80);
                        userReference.getRef().child("coinsgranttoggle").setValue("yes");

                    }

                } catch (Exception e) { // this is the catch of the if above and repeating the initial coin grant query as per notes above



                    userReference.getRef().child("coins").setValue(80);
                    userReference.getRef().child("coinsgranttoggle").setValue("yes");


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });


        //// END OF use value pull in section //////////////////////////////////////////////////////////////////////////////////



        // Firebase game SECTION BEGINS /////////////////////////////////////////////////////////////////////////////////////////

        gameReference = FirebaseDatabase.getInstance().getReference().child("questions");
        sortGameQueryQuestions = gameReference.orderByChild("aaaqno").equalTo(randQuestion);

        // First firebase pull is on create showing the question and first answer (will add a time delay on the answer)

        sortGameQueryQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot gameQs : dataSnapshot.getChildren()) {

                    // pull in to display in question screen
                    String GameQuestionZ = gameQs.child("cccquestion").getValue().toString();
                    txtGameQuestionX.setText(GameQuestionZ);

                    String GameCorrectAnswerZ = gameQs.child("dddcorrectansw").getValue().toString();
                    String GameWrongAnswer1Z = gameQs.child("eeewrongans1").getValue().toString();
                    String GameWrongAnswer2Z = gameQs.child("fffwrongans2").getValue().toString();
                    String GameWrongAnswer3Z = gameQs.child("gggwrongans3").getValue().toString();
                    String GameWrongAnswer4Z = gameQs.child("hhhwrongans4").getValue().toString();

                    //pull in to put to exapanded answer screen
                    ExpandedAnswerPut = gameQs.child("iiiexpanded").getValue().toString();
                    ExpAnsCategoryPut = gameQs.child("bbbcategory").getValue().toString();
                    ExpAnsEpochPut = gameQs.child("lllepoch").getValue().toString();

                    // pull to figure out which counter to grow when user answers the question
                    era = gameQs.child("mmmera").getValue().toString();

                    randAnswer = new Random().nextInt(5) + 1;

                    if (randAnswer == 1) {

                        displayAnswer = GameCorrectAnswerZ;

                    } else if (randAnswer == 2) {

                        displayAnswer = GameWrongAnswer1Z;


                    } else if (randAnswer == 3) {

                        displayAnswer = GameWrongAnswer4Z;


                    } else if (randAnswer == 4) {

                        displayAnswer = GameWrongAnswer3Z;


                    } else if (randAnswer == 5) {

                        displayAnswer = GameWrongAnswer1Z;
                    }


                    if (answerCounter == 1) {
                        txtGameAnswerDisplayX.setText(displayAnswer);

//                    } else if (answerCounter == 2) {
//                        txtGameAnswerDisplayX.setText(displayAnswer2);

                        // probably don't need the above because there is no value in 2 but keeping for now just in case
                    } else {

                        Toast.makeText(Game.this, "Out of numbers", Toast.LENGTH_SHORT).show();

                    }
                }

                startTimer();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // FIREBASE GAME SECTION continues - buttons and resulting right / wrong logic and scores adjustments
        btnCorrectX = findViewById(R.id.btnCorrect);
        btnCorrectX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopTimer(); // stop the clock as soon as button pressed to avoid ASYNC issues

                // pressed button effect with timer below for ensuring effect seen
                btnCorrectX.setVisibility(View.GONE);
                ImageView btnCorrectGlowX = findViewById(R.id.btnCorrectGlow);
                btnCorrectGlowX.setVisibility(View.VISIBLE);

                CountDownTimer glowTimer = new CountDownTimer(550, 50) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                if (randAnswer == answerCounter) { // this means the correct answer was shown and User pressed CORRECT

                    // First showing the user the implications of getting correct

                    shadeX.setVisibility(View.VISIBLE);
                    imgCorrectorCheckX.setVisibility(View.VISIBLE);
                    loutCoinAwardX.setVisibility(View.VISIBLE);
                    int coinAwardNo = 5 - rightAnswerTicker; // total award for correct is 5; this subtracts points already added for correctly guessing wrong answers
                    txtCoinAwardX.setText("+" + coinAwardNo);

                    CountDownTimer correctorTimer = new CountDownTimer(1500, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() { // adjusting all counters - coins, streaks, category records, etc; writing to Firebase right away

                            coinsOwned = coinsOwned + 5 - rightAnswerTicker;
                            String coinsOwnedZ = Integer.toString(coinsOwned);
                            txtCoinCounterX.setText(coinsOwnedZ);
                            userReference.getRef().child("coins").setValue(coinsOwned);
                            int coinsOwnedSort = - coinsOwned;
                            userReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);

                            consStreak = consStreak + 1;
                            String conStreakZ = Integer.toString(consStreak);
                            txtConStreakX.setText(conStreakZ);
                            userReference.getRef().child("constreak").setValue(consStreak);

                            totalAnswered = totalAnswered + 1;
                            userReference.getRef().child("totalanswered").setValue(totalAnswered);
                            int totalAnsweredSort = -totalAnswered;
                            userReference.getRef().child("totalansweredsort").setValue(totalAnsweredSort);

                            if (longestStreak < consStreak) {

                                longestStreak = consStreak;

                            }
                            userReference.getRef().child("longeststreak").setValue(longestStreak);
                            int longestStreakSort = -longestStreak;
                            userReference.getRef().child("longeststreaksort").setValue(longestStreakSort);


                            totalQuestions = totalQuestions + 1;
                            userReference.getRef().child("totalquestions").setValue(totalQuestions);

                            /// THESE HAVE TO BE ADJUSTED TO the final era settings
                            if (era.equals("Antiquity")) {
                                eraAnsweredAntiquity = eraAnsweredAntiquity + 1;
                                userReference.getRef().child("eraansantiquity").setValue(eraAnsweredAntiquity);
                            }

                            if (era.equals("Middle Ages")) {
                                eraAnsweredMiddleAges = eraAnsweredMiddleAges + 1;
                                userReference.getRef().child("eraansmiddle").setValue(eraAnsweredMiddleAges);
                            }

                            if (era.equals("Renaissance")) {
                                eraAnsweredRenaissance = eraAnsweredRenaissance + 1;
                                userReference.getRef().child("eraansrenaissance").setValue(eraAnsweredRenaissance);
                            }

                            if (era.equals("Enlightenment")) {
                                eraAnsweredEnlightenment = eraAnsweredEnlightenment + 1;
                                userReference.getRef().child("eraanselight").setValue(eraAnsweredEnlightenment);
                            }

                            if (era.equals("Early Modern")) {
                                eraAnsweredEarlyModern = eraAnsweredEarlyModern + 1;
                                userReference.getRef().child("eraansearlymod").setValue(eraAnsweredEarlyModern);
                            }

                            if (era.equals("Modern")) {
                                eraAnsweredModern = eraAnsweredModern + 1;
                                userReference.getRef().child("eraansmodern").setValue(eraAnsweredModern);
                            }

                            // takes user to expanded answer page carrying over values needed to determing right screen to use and the actual expanded answer
                            Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                            intent.putExtra("iiiexpanded", ExpandedAnswerPut);
                            intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                            intent.putExtra("lllepoch", ExpAnsEpochPut);
                            startActivity(intent);

                        }
                    }.start();


                } else {

                    // This else basically means that the user pressed correct but the answer was not correct - ie they got it wrong
                    // section works just like the section for correct just with different implications - see above for explanations

                    shadeX.setVisibility(View.VISIBLE);
                    imgCorrectorXmarkX.setVisibility(View.VISIBLE);
                    loutCoinAwardX.setVisibility(View.VISIBLE);
                    txtCoinAwardX.setText("-10");

                    CountDownTimer correctorTimer = new CountDownTimer(1500, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {

                            coinsOwned = coinsOwned - 10;
                            String coinsOwedZ = Integer.toString(coinsOwned);
                            txtCoinCounterX.setText(coinsOwedZ);
                            userReference.getRef().child("coins").setValue(coinsOwned);
                            int coinsOwnedSort = - coinsOwned;
                            userReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);

                            consStreak = 0;
                            String conStreakZ = Integer.toString(consStreak);
                            txtConStreakX.setText(conStreakZ);
                            userReference.getRef().child("constreak").setValue(consStreak);

                            totalQuestions = totalQuestions + 1;
                            userReference.getRef().child("totalquestions").setValue(totalQuestions);

                            if (coinsOwned < 1) { // if the wrong answer drops user to 0 or fewer coins go to reward add first being shown the message to this effect for a few seconds

                                imgCorrectorXmarkX.setVisibility(View.GONE);
                                txtAdMessageX.setVisibility(View.VISIBLE);

                                CountDownTimer admsgTimer = new CountDownTimer(2000, 500) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        if (mRewardedAdGameScreenCoins.isLoaded()) {
                                            mRewardedAdGameScreenCoins.show();
                                        }

                                    }
                                }.start();


                            } else { // got it wrong but has enough coins to continue playing so off to expanded answer

                                Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                                intent.putExtra("iiiexpanded", ExpandedAnswerPut);
                                intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                                intent.putExtra("lllepoch", ExpAnsEpochPut);
                                startActivity(intent);

                            }

                        }
                    }.start();


                }

                    }
                }.start();
            }
        });

        btnWrongGlowX = findViewById(R.id.btnWrongGlow);
        btnWrongX = findViewById(R.id.btnWrong);
        btnWrongX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopTimer();

                btnWrongX.setVisibility(View.GONE);
                btnWrongGlowX.setVisibility(View.VISIBLE);

                CountDownTimer glowTimer = new CountDownTimer(550, 50) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {



                if (randAnswer == answerCounter) { // means user pressed wrong when the answer was right - so user was wrong

                    // sections work as above - see annotations there for explanations

                    shadeX.setVisibility(View.VISIBLE);
                    imgCorrectorXmarkX.setVisibility(View.VISIBLE);
                    loutCoinAwardX.setVisibility(View.VISIBLE);
                    txtCoinAwardX.setText("-10");

                    CountDownTimer correctorTimer = new CountDownTimer(1500, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {

                            coinsOwned = coinsOwned - 10;
                            String coinsOwedZ = Integer.toString(coinsOwned);
                            txtCoinCounterX.setText(coinsOwedZ);
                            userReference.getRef().child("coins").setValue(coinsOwned);
                            int coinsOwnedSort = - coinsOwned;
                            userReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);

                            consStreak = 0;
                            String conStreakZ = Integer.toString(consStreak);
                            txtConStreakX.setText(conStreakZ);
                            userReference.getRef().child("constreak").setValue(consStreak);

                            totalQuestions = totalQuestions + 1;
                            userReference.getRef().child("totalquestions").setValue(totalQuestions);


                            if (coinsOwned < 1) {

                                imgCorrectorXmarkX.setVisibility(View.GONE);
                                txtAdMessageX.setVisibility(View.VISIBLE);

                                CountDownTimer admsgTimer = new CountDownTimer(2000, 500) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {

                                    }

                                    @Override
                                    public void onFinish() {
                                        if (mRewardedAdGameScreenCoins.isLoaded()) {
                                            mRewardedAdGameScreenCoins.show();
                                        }

                                    }
                                }.start();


                            } else {

                                Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                                intent.putExtra("iiiexpanded", ExpandedAnswerPut);
                                intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                                intent.putExtra("lllepoch", ExpAnsEpochPut);
                                startActivity(intent);

                            }

                        }
                    }.start();


                } else { //the else here means that the user guessed that correctly that answer shown was wrong - will get 1 coin which is
                            //then deducted from 5 max he can get for identifying the correct response.


                    stopTimer(); // need to start and reset timer as not entering the oncreate  and need to transition from one answer to next
                    // not sure why i commented this out. but somehow it works and the toast counting down the timer disppears when the button is pressed
                    // MAY NEED TO REVISIT - actually uncommented it for longr term trial


                    shadeX.setVisibility(View.VISIBLE);
                    imgCorrectorCheckX.setVisibility(View.VISIBLE);
                    loutCoinAwardX.setVisibility(View.VISIBLE);
                    txtCoinAwardX.setText("+1");
                    rightAnswerTicker = rightAnswerTicker + 1;


                    CountDownTimer correctorTimer = new CountDownTimer(1500, 500) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {

                            btnWrongGlowX.setVisibility(View.GONE);
                            shadeX.setVisibility(View.GONE);
                            imgCorrectorCheckX.setVisibility(View.GONE);
                            loutCoinAwardX.setVisibility(View.GONE);
                            btnWrongX.setVisibility(View.VISIBLE);


                            coinsOwned = coinsOwned + 1;
                            String coinsOwnedZ = Integer.toString(coinsOwned);
                            txtCoinCounterX.setText(coinsOwnedZ);
                            userReference.getRef().child("coins").setValue(coinsOwned);
                            int coinsOwnedSort = - coinsOwned;
                            userReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);

                            timersetting = 6000;
                            ticksetting = 1000;
                            imgHPTimer2X.setVisibility(View.GONE);
                            imgHPTimer3X.setVisibility(View.GONE);
                            imgHPTimer4X.setVisibility(View.GONE);
                            imgHPTimer5X.setVisibility(View.GONE);

                            startTimer();
                            nextQuestions();

                        }
                    }.start();


                }

                    }
                }.start();

            }
        });

        // END OF BUTTON RIGHT / WRONG logic section //////////////////////////////////////////////////////////


    }  // END OF ON CREATE ///////////////////////////////////////////////////////////////////////////////////

    private void nextQuestions (){

        answerCounter = answerCounter + 1;
        imgHPTimerX.setVisibility(View.VISIBLE);

        //This second query is probably unnecessary as could have gotten all these variables and sequences in on create only ran the
        // what to display ifs here without further firebase access

        sortGameQueryQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot gameQs: dataSnapshot.getChildren()) {


                    String GameCorrectAnswerZ = gameQs.child("dddcorrectansw").getValue().toString();
                    String GameWrongAnswer1Z = gameQs.child("eeewrongans1").getValue().toString();
                    String GameWrongAnswer2Z = gameQs.child("fffwrongans2").getValue().toString();
                    String GameWrongAnswer3Z = gameQs.child("gggwrongans3").getValue().toString();
                    String GameWrongAnswer4Z = gameQs.child("hhhwrongans4").getValue().toString();

                    /// set out sequences in which answers will be shown for each randanswer1
                    if(randAnswer == 1){

                        displayAnswer = GameCorrectAnswerZ;
                        displayAnswer2 = GameWrongAnswer1Z;
                        displayAnswer3 = GameWrongAnswer4Z;
                        displayAnswer4 = GameWrongAnswer2Z;
                        displayAnswer5 = GameWrongAnswer3Z;


                    }else if(randAnswer == 2){

                        displayAnswer = GameWrongAnswer1Z;
                        displayAnswer2 = GameCorrectAnswerZ;
                        displayAnswer3 = GameWrongAnswer2Z;
                        displayAnswer4 = GameWrongAnswer3Z;
                        displayAnswer5 = GameWrongAnswer4Z;

                    }else if(randAnswer == 3){

                        displayAnswer = GameWrongAnswer4Z;
                        displayAnswer2 = GameWrongAnswer3Z;
                        displayAnswer3 = GameCorrectAnswerZ;
                        displayAnswer4 = GameWrongAnswer1Z;
                        displayAnswer5 = GameWrongAnswer2Z;

                    }else if(randAnswer == 4){

                        displayAnswer = GameWrongAnswer3Z;
                        displayAnswer2 = GameWrongAnswer4Z;
                        displayAnswer3 = GameWrongAnswer2Z;
                        displayAnswer4 = GameCorrectAnswerZ;
                        displayAnswer5 = GameWrongAnswer1Z;

                    }else if(randAnswer == 5){

                        displayAnswer = GameWrongAnswer1Z;
                        displayAnswer2 = GameWrongAnswer2Z;;
                        displayAnswer3 = GameWrongAnswer4Z;
                        displayAnswer4 = GameWrongAnswer3Z;
                        displayAnswer5 = GameCorrectAnswerZ;

                    }

                    // executes showing the different answers as the user gests things right on previous question
                    if (answerCounter == 1) {
                        txtGameAnswerDisplayX.setText(displayAnswer);

                    } else if (answerCounter == 2) {
                        txtGameAnswerDisplayX.setText(displayAnswer2);
                    } else if (answerCounter == 3) {
                        txtGameAnswerDisplayX.setText(displayAnswer3);
                    } else if (answerCounter == 4) {
                        txtGameAnswerDisplayX.setText(displayAnswer4);
                    } else if (answerCounter == 5) {
                        txtGameAnswerDisplayX.setText(displayAnswer5);
                    } else {

                        Toast.makeText(Game.this, "Out of numbers", Toast.LENGTH_SHORT).show();

                    }

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // this is the main timer logic for the 10 then 5 seconds a user has to answer the question
    public void startTimer(){

        // will be called at every 1500 milliseconds i.e. every 1.5 second.
        countDownTimer = new CountDownTimer(timersetting, 1000) {
            public void onTick(long millisUntilFinished) {

                    double millisleftRough = millisUntilFinished / 1000;

                    int millisleftFinal = (int) Math.round(millisleftRough);

                   // Toast.makeText(Game.this, "M = " + millisleftFinal, Toast.LENGTH_SHORT).show();

                    String timerCountString = millisleftFinal + "";
                    txtTimerX.setText(timerCountString);  // converting timer to string and setting the counter  which starts of as GONE
                if (ticksetting == 5000 & millisUntilFinished < 6000 & tickerToggle == 1) {  // making the counter  and hourglass visible at 5 seconds

                    tickerToggle = 2;
                    txtTimerX.setVisibility(View.VISIBLE);
                    imgHPTimerX.setVisibility(View.VISIBLE);


                    if (width2 > 1500) { // empty for now but there if we need to make graphics changes for tablets


                    } else if (height2 < 1300) {  // slight graphics changes for small lower res phones

                       loutGameQuestionX.animate().translationY(-30).setDuration(300);

                        txtGameAnswerDisplayX.setTextSize(16);
                        loutGameAnswerDisplayX.animate().translationY(-40).setDuration(1);

                    } else {

                    }
                    // for all screen sizes fading in the answer banner
                   loutGameAnswerDisplayX.setVisibility(View.VISIBLE);

                    YoYo.with(Techniques.FadeIn)
                            .delay(0)
                            .duration(2000)
                            .repeat(0)
                            .playOn(loutGameAnswerDisplayX);

                }

                // toggling through the different hourglass positions

                if (millisUntilFinished <1000) {

                    imgHPTimer5X.setVisibility(View.GONE);
                    imgHPTimerX.setVisibility(View.VISIBLE);
                } else
                if (millisUntilFinished <2000) {

                    imgHPTimer4X.setVisibility(View.GONE);
                    imgHPTimer5X.setVisibility(View.VISIBLE);
                }else
                if (millisUntilFinished < 3000) {
                    imgHPTimer3X.setVisibility(View.GONE);
                    imgHPTimer4X.setVisibility(View.VISIBLE);
                } else
                if (millisUntilFinished < 4000) {

                    imgHPTimer2X.setVisibility(View.GONE);
                    imgHPTimer3X.setVisibility(View.VISIBLE);
                } else
                if (millisUntilFinished < 5000) {


                    imgHPTimerX.setVisibility(View.GONE);
                    imgHPTimer2X.setVisibility(View.VISIBLE);
                }

            }

            public void onFinish() { // WHEN TIMING OUT:
                // other than the toast treats this as a wrong answer, makes streak deductions and sends to expanded answer

                btnCorrectX.setVisibility(View.GONE);
                btnWrongX.setVisibility(View.GONE);
                shadeX.setVisibility(View.VISIBLE);
                imgCorrectorXmarkX.setVisibility(View.VISIBLE);
                imgCorrectorTimeoutX.setVisibility(View.VISIBLE);
                loutCoinAwardX.setVisibility(View.VISIBLE);
                txtCoinAwardX.setText("-10");

                CountDownTimer correctorTimer = new CountDownTimer(1500, 500) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {


                        coinsOwned = coinsOwned - 10;
                        String coinsOwedZ = Integer.toString(coinsOwned);
                        txtCoinCounterX.setText(coinsOwedZ);
                        userReference.getRef().child("coins").setValue(coinsOwned);
                        int coinsOwnedSort = - coinsOwned;
                        userReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);

                        consStreak = 0;
                        String conStreakZ = Integer.toString(consStreak);
                        txtConStreakX.setText(conStreakZ);
                        userReference.getRef().child("constreak").setValue(consStreak);

                        if (coinsOwned < 1) {

                            imgCorrectorXmarkX.setVisibility(View.GONE);
                            imgCorrectorTimeoutX.setVisibility(View.GONE);
                            txtAdMessageX.setVisibility(View.VISIBLE);

                            CountDownTimer admsgTimer = new CountDownTimer(2000, 500) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {
                                    if (mRewardedAdGameScreenCoins.isLoaded()) {
                                        mRewardedAdGameScreenCoins.show();
                                    }

                                }
                            }.start();


                        } else {


                            Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                            intent.putExtra("iiiexpanded", ExpandedAnswerPut);
                            intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                            intent.putExtra("lllepoch", ExpAnsEpochPut);
                            startActivity(intent);

                        }

                    }
                }.start();


            }
        }.start();


    }

    public void stopTimer() {

        countDownTimer.cancel();

    }

    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    public void onBackPressed(){

        // just leaving this blank so back press should just be a nothing

    }


    ///////////////// AD-MOB BEGINS /////////////////////////////////////////////////////////////
    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

        mRewardedAdGameScreenCoins.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdClosed() {

        // if user closes add the display defaults back to the screen showing the message they need to watch and then it times out and puts up the ad again
        CountDownTimer rewatchTimer = new CountDownTimer(3000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (mRewardedAdGameScreenCoins.isLoaded()) {
                    mRewardedAdGameScreenCoins.show();
                }
            }
        }.start();

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

        coinsOwned = coinsOwned + 100;
        String coinsOwnedZ = Integer.toString(coinsOwned);
        txtCoinCounterX.setText(coinsOwnedZ);
        userReference.getRef().child("coins").setValue(coinsOwned);
        int coinsOwnedSort = - coinsOwned;
        userReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);

        if (adMobToggle == 1) { // toggles is 1 when sent here from oncreate vs. a wrong answer so restarting game activity instead of going to expanded answer

            shadeX.setVisibility(View.GONE);
            txtAdMessageX.setVisibility(View.GONE);

            finish();
            startActivity(getIntent());

        } else {

            Intent intent = new Intent(Game.this, ExpandedAnswer.class);
            intent.putExtra("iiiexpanded", ExpandedAnswerPut);
            intent.putExtra("bbbcategory", ExpAnsCategoryPut);
            intent.putExtra("lllepoch", ExpAnsEpochPut);
            startActivity(intent);

        }


    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoCompleted() {

    }

    // sent here if have 0 or less coins on-create... needed to signal to onrewared with the admobtoggle on where to send user after ad watched
    public void gotoAd() { // could probably put the toggle right in on-create and even if sending here avoid this if statement but some extra protection so leaving...

        if (coinsOwned < 1) {

           // Toast.makeText(Game.this, coinsOwned+"", Toast.LENGTH_SHORT).show();

            adMobToggle = 1;

            shadeX.setVisibility(View.VISIBLE);
            txtAdMessageX.setVisibility(View.VISIBLE);

            CountDownTimer admsgTimer = new CountDownTimer(2000, 500) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if (mRewardedAdGameScreenCoins.isLoaded()) {
                        mRewardedAdGameScreenCoins.show();
                    } else {

                        CountDownTimer slowaddLoad = new CountDownTimer(5000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                if (mRewardedAdGameScreenCoins.isLoaded()) {
                                    mRewardedAdGameScreenCoins.show();
                                }

                            }
                        }.start();

                    }

                }
            }.start();


        }


    }
}
