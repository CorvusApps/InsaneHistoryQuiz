package com.pelotheban.insanehistoryquiz;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdListener;
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
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;

public class Game extends AppCompatActivity implements RewardedVideoAdListener {

    // Game play UI elements and variables

    private int maxNumber;
    private int randQuestionPre;
    private int randQuestion;
    private String [] questionList;
    private String breaker;
    private SharedPreferences pastQuestionsShared;
    private String TestQCounterString;
    private TextView txtTestQCountX;

    private String displayAnswer, displayAnswer2, displayAnswer3, displayAnswer4, displayAnswer5; // will be randomly assigned to the answer options in each question
    private int answerCounter; // scrolls through how many answers options to a question the player has seen
    private int randAnswer;

    private ImageView btnCorrectX, btnWrongX, btnWrongGlowX;

    private String ExpandedAnswerPut, ExpAnsCategoryPut, ExpAnsEpochPut, ExpCorrectAnsPut; // making this class variable so can go to expanded answer screen
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
    private String eraAnsweredContemporaryString;

    private int eraAnsweredAntiquity;
    private int eraAnsweredMiddleAges;
    private int eraAnsweredRenaissance;
    private int eraAnsweredEnlightenment;
    private int eraAnsweredEarlyModern;
    private int eraAnsweredModern;
    private int eraAnsweredContemporary;

            //// badges starte

            private String badgesupString, badgestrString, badgetrtString, badgewinString;
            private int badgesup, badgestr, badgetrt, badgewin; // badge level already in firebase
            private int badgesuplev, badgestrlev, badgetrtlev, badgewinlev; // badge level user is entitled to based on latest right answer
            private int newbadgesup, newbadgestr, newbadgetrt, newbadgewin; // badge level that will get written to FB (only a number if bigger than current)

            private String badgexantString, badgexmedString, badgexrenString, badgexenlString, badgexmodString, badgexconString;
            private int badgexant, badgexmed, badgexren, badgexenl, badgexmod, badgexcon;
            private int badgexantlev, badgexmedlev, badgexrenlev, badgexenllev, badgexmodlev, badgexconlev;
            private int newbadgexant, newbadgexmed, newbadgexren, newbadgexenl, newbadgexmod, newbadgexcon;

            private int badgeAwardedToggle;


            //// badges end

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

    int sentFromQuery; // needed to make sure that we don't keep sending from query to the test for coin sufficiency method

    RewardedVideoAd mRewardedAdGameScreenCoins;
    TextView txtAdMessageX;
    int adMobToggle;

    InterstitialAd mInterstitialGame;
    int mAdvertCounterGame;
    private SharedPreferences sharedAdvertCounterGame;


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
        sentFromQuery = 1;

        Log.i ("TIMING" , "On-create = " + adMobToggle);

        MobileAds.initialize(this, "ca-app-pub-1744081621312112~9212279801");
            /// Reward video

        mRewardedAdGameScreenCoins = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedAdGameScreenCoins.setRewardedVideoAdListener(this);
        mRewardedAdGameScreenCoins.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());

        txtAdMessageX = findViewById(R.id.txtAdMessage);

            /// Interstitial
        sharedAdvertCounterGame = getSharedPreferences("adSettingGame", MODE_PRIVATE);
        mAdvertCounterGame = sharedAdvertCounterGame.getInt("CounterGame", 0); // where if no settings

        mInterstitialGame = new InterstitialAd(Game.this);
        mInterstitialGame.setAdUnitId(getString(R.string.test_interstitial_ad));
        //mInterstitialAdCoinList.setAdUnitId(getString(R.string.coinlist_interstitial_ad)); // still need to generate
        mInterstitialGame.loadAd(new AdRequest.Builder().build());

        mInterstitialGame.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("INTERSTITIAL", "in the ONCREATE onloaded");

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdOpened() {

            }

            @Override
            public void onAdClicked() {


            }

            @Override
            public void onAdLeftApplication() {
                Log.i("INTERSTITIAL", "in the adleft app");
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {


            }
        });

        shadeX = findViewById(R.id.shadeCor);

        // Game play

        answerCounter = 1;


        //...... displayed outputs
        loutGameQuestionX = findViewById(R.id.loutGameQuestion);
        loutGameAnswerDisplayX = findViewById(R.id.loutGameAnswerDisplay);

        txtGameQuestionX = findViewById(R.id.txtGameQuestion);
        txtGameAnswerDisplayX = findViewById(R.id.txtGameAnswerDisplay);
        txtCoinCounterX = findViewById(R.id.txtCoinCounter);
        txtConStreakX = findViewById(R.id.txtConStreak);
        txtTestQCountX = findViewById(R.id.txtTestQCounter);


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

        timersetting = 15000;
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

        // Dynamic maxNumber


//        DatabaseReference quizQuestionsRef = FirebaseDatabase.getInstance().getReference().child("values").child("quizquestions");
//        //Log.i("QUIZQ", quizQuestionsRef.toString());
//        quizQuestionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//               String maxNumberString = dataSnapshot.getValue().toString();
//                maxNumber = Integer.valueOf(maxNumberString);
//
//                //Log.i("QUIZQ", "String:  " + maxNumberString);
//                randQuestionSeclect();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        ////////////// Query to pull in all the user variables like counters of questions answered etc.

        //seems to be a danger that if the pull does not work because internet slow for example but user plays question and internet ok and then writes back it will write
        // from zero value and delete earlier user score. To minimize this trying to use a non-single even listener

        sortUsersQuery = FirebaseDatabase.getInstance().getReference().child("my_users").orderByChild("user").equalTo(uid);
        sortUsersQuery.addValueEventListener(new ValueEventListener() {
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
                        eraAnsweredModernString = userDs.child("eraansmodern").getValue().toString();
                        eraAnsweredModern = Integer.valueOf(eraAnsweredModernString);

                    } catch (Exception e) {

                    }
                    try {
                        eraAnsweredContemporaryString = userDs.child("eraanscontem").getValue().toString();
                        eraAnsweredContemporary = Integer.valueOf(eraAnsweredContemporaryString);

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

                    //// CURRENT badge checks - get value in firebase for later comparison to what uwer should have after last right answer

                    try {
                        badgesupString = userDs.child("badgesup").getValue().toString();
                        badgesup = Integer.valueOf(badgesupString);
                    } catch (Exception e) {
                    }
                    try {
                        badgestrString = userDs.child("badgestr").getValue().toString();
                        badgestr = Integer.valueOf(badgestrString);
                    } catch (Exception e) {
                    }
                    try {
                        badgetrtString = userDs.child("badgetrt").getValue().toString();
                        badgetrt = Integer.valueOf(badgetrtString);
                    } catch (Exception e) {
                    }
                    try {
                        badgewinString = userDs.child("badgewin").getValue().toString();
                        badgewin = Integer.valueOf(badgewinString);
                    } catch (Exception e) {
                    }

                    try {
                        badgexantString = userDs.child("badgexant").getValue().toString();
                        badgexant = Integer.valueOf(badgexantString);
                    } catch (Exception e) {
                    }
                    try {
                        badgexmedString = userDs.child("badgexmed").getValue().toString();
                        badgexmed = Integer.valueOf(badgexmedString);
                    } catch (Exception e) {
                    }
                    try {
                        badgexrenString = userDs.child("badgexren").getValue().toString();
                        badgexren = Integer.valueOf(badgexrenString);
                    } catch (Exception e) {
                    }
                    try {
                        badgexenlString = userDs.child("badgexenl").getValue().toString();
                        badgexenl = Integer.valueOf(badgexenlString);
                    } catch (Exception e) {
                    }
                    try {
                        badgexmodString = userDs.child("badgexmod").getValue().toString();
                        badgexmod = Integer.valueOf(badgexmodString);
                    } catch (Exception e) {
                    }
                    try {
                        badgexconString = userDs.child("badgexcon").getValue().toString();
                        badgexcon = Integer.valueOf(badgexconString);
                    } catch (Exception e) {
                    }



                   Log.i ("TIMING" , "coinGrantToggle in query: " + coinGrantToggle);
                    if (sentFromQuery == 1) { // only go to check if we have coins once not on every data change
                        gotoCoinCountSet();

                    }
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        //// END OF use value pull in section //////////////////////////////////////////////////////////////////////////////////

        // Firebase game SECTION BEGINS /////////////////////////////////////////////////////////////////////////////////////////



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

                            // adds one to the counter and its shared pref for interstitial ads
                            Log.i("INTERSTITIAL", "Counter on Right = " + mAdvertCounterGame);
                            mAdvertCounterGame = mAdvertCounterGame +1;
                            SharedPreferences.Editor editor = sharedAdvertCounterGame.edit();
                            editor.putInt("CounterGame", mAdvertCounterGame); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
                            editor.apply(); // saves the value

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

                            if (era.equals("Contemporary")) {
                                eraAnsweredContemporary = eraAnsweredContemporary + 1;
                                userReference.getRef().child("eraanscontem").setValue(eraAnsweredContemporary);
                            }

                            badgeAward();



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

                            // adds one to the counter and its shared pref for interstitial ads
                            Log.i("INTERSTITIAL", "Counter on Wrong = " + mAdvertCounterGame);
                            mAdvertCounterGame = mAdvertCounterGame +1;
                            SharedPreferences.Editor editor = sharedAdvertCounterGame.edit();
                            editor.putInt("CounterGame", mAdvertCounterGame); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
                            editor.apply(); // saves the value


                            if (coinsOwned < 1) { // if the wrong answer drops user to 0 or fewer coins go to reward add first being shown the message to this effect for a few seconds

                                imgCorrectorXmarkX.setVisibility(View.GONE);
                                txtAdMessageX.setVisibility(View.VISIBLE);

                                // resets the counter for interstitial ads
                                mAdvertCounterGame = 0;
                                SharedPreferences.Editor editor2 = sharedAdvertCounterGame.edit();
                                editor2.putInt("CounterGame", mAdvertCounterGame); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
                                editor2.apply(); // saves the value
                                Log.i("INTERSTITIAL", "Counter on reset = " + mAdvertCounterGame);

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


                            } else if (mAdvertCounterGame > 9) {

                                Log.i("INTERSTITIAL", "going to intesrtitial = " + mAdvertCounterGame);

                                interstitialAdvert();


                            } else { // got it wrong but has enough coins to continue playing so off to expanded answer

                                Log.i("INTERSTITIAL", "IN THE LAST ELSE FROM WRONG CORRECT " + mAdvertCounterGame);

                                Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                                intent.putExtra("iiiexpanded", ExpandedAnswerPut);
                                intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                                intent.putExtra("lllepoch", ExpAnsEpochPut);
                                intent.putExtra("dddcorrectansw", ExpCorrectAnsPut);
                                finish();
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

                            // adds one to the counter and its shared pref for interstitial ads
                            Log.i("INTERSTITIAL", "Counter on Wrong = " + mAdvertCounterGame);
                            mAdvertCounterGame = mAdvertCounterGame +1;
                            SharedPreferences.Editor editor = sharedAdvertCounterGame.edit();
                            editor.putInt("CounterGame", mAdvertCounterGame); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
                            editor.apply(); // saves the value


                            if (coinsOwned < 1) {

                                imgCorrectorXmarkX.setVisibility(View.GONE);
                                txtAdMessageX.setVisibility(View.VISIBLE);

                                // resets the counter for interstitial ads
                                mAdvertCounterGame = 0;
                                SharedPreferences.Editor editor2 = sharedAdvertCounterGame.edit();
                                editor2.putInt("CounterGame", mAdvertCounterGame); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
                                editor2.apply(); // saves the value
                                Log.i("INTERSTITIAL", "Counter on reset = " + mAdvertCounterGame);

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


                            } else if (mAdvertCounterGame > 9) {

                                Log.i("INTERSTITIAL", "going to intesrtitial = " + mAdvertCounterGame);

                                interstitialAdvert();


                            } else {
                                Log.i("INTERSTITIAL", "in the last else = " + mAdvertCounterGame);

                                Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                                intent.putExtra("iiiexpanded", ExpandedAnswerPut);
                                intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                                intent.putExtra("lllepoch", ExpAnsEpochPut);
                                intent.putExtra("dddcorrectansw", ExpCorrectAnsPut);
                                finish();
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

//                            btnWrongGlowX.setVisibility(View.GONE);
//                            shadeX.setVisibility(View.GONE);
//                            imgCorrectorCheckX.setVisibility(View.GONE);
//                            loutCoinAwardX.setVisibility(View.GONE);
//                            btnWrongX.setVisibility(View.VISIBLE);


                            coinsOwned = coinsOwned + 1;
                            String coinsOwnedZ = Integer.toString(coinsOwned);
                            txtCoinCounterX.setText(coinsOwnedZ);
                            userReference.getRef().child("coins").setValue(coinsOwned);
                            int coinsOwnedSort = - coinsOwned;
                            userReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);

                            timersetting = 8000;
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

        // this is at the end so can be sure that userQuery finished and can only send to question generation if have coins
        // at the same time already sending to adMob but this way avoids have game and timer in background which fucks things up
//        DatabaseReference quizQuestionsRef = FirebaseDatabase.getInstance().getReference().child("values").child("quizquestions");
//        Log.i("TIMING", quizQuestionsRef.toString());
//        quizQuestionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                String maxNumberString = dataSnapshot.getValue().toString();
//                maxNumber = Integer.valueOf(maxNumberString);
//
//                 Log.i("TIMING", "String:  " + maxNumberString);
//
//                 //prolly need to get rid of this if - since the ongoing query should get at this but then what happens if it is less
//                // actually just generating max number here but going to randQuestions from the coin query - keeping old code for ref below
//                if (coinsOwned > 1 ) {  // && sentFromQuery == 1
//                    Log.i("TIMING", "coins " + coinsOwned);
//                   // randQuestionSeclect();
//                }
//
//                Log.i("TIMING", "did not detect coins? " +  coinsOwned);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


    }  // END OF ON CREATE ///////////////////////////////////////////////////////////////////////////////////

    private void gotoCoinCountSet() {

        // moved this section OUT of the userquery because it doesn't need to be there AND because it is now constantly updating the query
        // this is toggle is going crazy and looping admob - hopefully out here it is good
        // needed seperate function protected by toggle so this doesn't get called multiple times while still getting called because if just
        // sitting in oncreate below query gets fucked by async

        sentFromQuery = 2;
       // Log.i("TIMING", "Before setting coin counter  toggle" + adMobToggle);
       // Log.i("TIMING", "Before setting coin counter  coinsOwnedString" + coinsOwnedString);

        txtCoinCounterX.setText(coinsOwnedString); // IF there are any coins in the account will set the counter
        txtConStreakX.setText(consStreetString);

        try {
            if (coinsOwned > 0 | coinGrantToggle.equals("yes")) {

                Log.i ("TIMING" , "coinGrantToggle in if: " + coinGrantToggle);

                if (coinsOwned<1) {

                    adMobToggle = 1;
                    Log.i("TIMING", "withinTXTcoincounter " + adMobToggle);
                    gotoAd();
                } else {

                    Log.i ("TIMING" , "in the new else coins: " + coinsOwned);
                    // hoping that this here now always takes us to questions versus hanging when coins were not detected when it was in the on create
                    // but need to go to maxnumber generation first because that also asyncs out sometimes
                   // randQuestionSeclect();
                    maxNumberGenerate();

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

    private void  maxNumberGenerate() {

        DatabaseReference quizQuestionsRef = FirebaseDatabase.getInstance().getReference().child("values").child("quizquestions");
        Log.i("TIMING", quizQuestionsRef.toString());
        quizQuestionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String maxNumberString = dataSnapshot.getValue().toString();
                maxNumber = Integer.valueOf(maxNumberString);

                Log.i("TIMING", "String:  " + maxNumberString);

                //prolly need to get rid of this if - since the ongoing query should get at this but then what happens if it is less
                // actually just generating max number here but going to randQuestions from the coin query - keeping old code for ref below
                if (coinsOwned > 1 ) {  // && sentFromQuery == 1
                    Log.i("TIMING", "coins " + coinsOwned);
                    // randQuestionSeclect();
                }

                Log.i("TIMING", "did not detect coins? " +  coinsOwned);
                randQuestionSeclect();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void randQuestionSeclect() {

        Log.i("TIMING", "Number: " + maxNumber);
        questionList = new String[maxNumber]; // setting size of our array

        //getting the array with question numbers asked to date from shared prefs as a string
        pastQuestionsShared = getSharedPreferences("pastQuestions", MODE_PRIVATE);
        String questionListStringified = pastQuestionsShared.getString("questionList", "0"); // where if no settings

        // taking the string from shared prefs and converting it back to array
        questionList = questionListStringified.split(",");

        // getting the random question # for next round and setting it to string for comparisons to previously asked
        randQuestionPre = new Random().nextInt(maxNumber) + 1; // random question number to be displayed
        String randQuestionPreStr = String.valueOf(randQuestionPre);

        // checking to see if gone through all questions and if so resetting the shared pref array back to zero - so the questions asked comparison starts from scratch
        if (questionList.length > (maxNumber-1)) {

            String reset = "0,";
            questionList = reset.split(",");

        }

        // checking to see if the random question has already been asked and no going below to get to game while if asked then getting
        // into a loop which runs until an unasked question pops up
        if (Arrays.asList(questionList).contains(randQuestionPreStr)) {

            Log.i("TIMING", "question repeated going to while");

            //Toast.makeText(Game.this, "FREEZING", Toast.LENGTH_SHORT).show();
            int spinner = 1;
            while (true) { // this should spin without going beyond in ASYNC way until break
                //for (int x = 0; x < 50; x++){

                // within the loop generating new random numbers and checking them agasint the array and doing so as long as not unique
                randQuestionPre = new Random().nextInt(maxNumber) + 1; // random question number to be displayed
                String randQuestionPreStr2 = String.valueOf(randQuestionPre);

                if (Arrays.asList(questionList).contains(randQuestionPreStr2)) {

                    spinner = spinner +1;
                }
                else { // this else is activated when the random number is NOT in the array at which point the question is set, the shared preff
                    //is updated and the action moves to the game methods

                    randQuestion = randQuestionPre;

                    String randQuestionStr = String.valueOf(randQuestion); // convert the number we just generated to a string to add to our array

                    // now we add the new  question to our string

                    StringBuilder sb = new StringBuilder(); // converting array back into string using string builder
                    for (int i = 0; i < questionList.length; i++) {  // create a string from playlist - so now sb is string from the array with comas between all the terms
                        sb.append(questionList[i]).append(",");
                    }

                    String sbString = sb.toString(); // convert our string builder to string
                    // add the new question to the string from array

                    sbString = sbString + randQuestionStr; // adding the question that made the cut to the array

                    SharedPreferences.Editor editor = pastQuestionsShared.edit();
                    editor.putString("questionList", sbString);
                    editor.apply(); // saves the value

                    Toast.makeText(Game.this, sbString + "***" + spinner, Toast.LENGTH_LONG).show();
                    gameStart();

                    break;
                }


            }

        } else { // so if the List so far DOES NOT HAVE the number we just generated"

            randQuestion = randQuestionPre; // set the questions number to the number we just generated (that is used in game below)

            String randQuestionStr = String.valueOf(randQuestion); // convert the number we just generated to a string to add to our array

            // now we add the new  question to our string

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < questionList.length; i++) {  // create a string from playlist - so now sb is string from the array with comas between all the terms
                sb.append(questionList[i]).append(",");
            }

            String sbString = sb.toString(); // convert our string builder to string
            // add the new question to the string from array

            sbString = sbString + randQuestionStr;

            SharedPreferences.Editor editor = pastQuestionsShared.edit();
            editor.putString("questionList", sbString);
            editor.apply(); // saves the value

            Toast.makeText(Game.this, sbString, Toast.LENGTH_LONG).show();
            gameStart();

        }


    }

    private void gameStart() {

        Log.i("TIMING", "Starting game");

        gameReference = FirebaseDatabase.getInstance().getReference().child("questions");
        sortGameQueryQuestions = gameReference.orderByChild("aaaqno").equalTo(randQuestion);

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

                    TestQCounterString = gameQs.child("aaaqno").getValue().toString();
                    txtTestQCountX.setText(TestQCounterString);

                    //pull in to put to exapanded answer screen
                    ExpandedAnswerPut = gameQs.child("iiiexpanded").getValue().toString();
                    ExpAnsCategoryPut = gameQs.child("bbbcategory").getValue().toString();
                    ExpAnsEpochPut = gameQs.child("lllepoch").getValue().toString();
                    ExpCorrectAnsPut = gameQs.child("dddcorrectansw").getValue().toString();


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

                Log.i("TIMING", "About to start timer");
                startTimer();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void badgeAward() {

        badgeAwardedToggle = 1;
        // this toggle gets set to 2 below whenever we AWARD a badge and WRITE to Firebase
        // set to 2 the toggle prevents from any other simultaneously elegible badges to be awarded and saves them for next time
        // this way only one badge at a time gets sent to Expanded Answer screen for showing to the user

       //// checks to see what level of badge the user is entitled to now
        if (coinsOwned > 999) {
            badgesuplev = 5;
        } else if (coinsOwned > 499) {
            badgesuplev = 4;
        } else if (coinsOwned > 224) {
            badgesuplev = 3;
        } else if (coinsOwned > 174) {
            badgesuplev = 2;
        } else if (coinsOwned > 124) {
            badgesuplev = 1;
        } else {
            badgesuplev = 0;
        }

        if (longestStreak > 49) {
            badgestrlev = 5;
        } else if (longestStreak > 29) {
            badgestrlev = 4;
        } else if (longestStreak > 19) {
            badgestrlev = 3;
        } else if (longestStreak > 9) {
            badgestrlev = 2;
        } else if (longestStreak > 2) {
            badgestrlev = 1;
        } else {
            badgestrlev = 0;
        }


        if (totalAnswered > 499) {
            badgetrtlev = 5;
        } else if (totalAnswered > 249) {
            badgetrtlev = 4;
        } else if (totalAnswered > 99) {
            badgetrtlev = 3;
        } else if (totalAnswered > 24) {
            badgetrtlev = 2;
        } else if (totalAnswered > 4) {
            badgetrtlev = 1;
        } else {
            badgetrtlev = 0;
        }


        if (totalQuestions > 50){ // for winning % do it only if answered a critical mass of 50 questions at least
            double winrate = ((double)totalAnswered) / (int)totalQuestions;

            if (winrate > .89) {
                badgewinlev = 5;
            } else if (winrate > .79) {
                badgewinlev = 4;
            } else if (winrate > .69) {
                badgewinlev = 3;
            } else if (winrate > .59) {
                badgewinlev = 2;
            } else if (winrate > .49) {
                badgewinlev = 1;
            } else {
                badgewinlev = 0;
            }

        }


        if (eraAnsweredAntiquity > 199) {
            badgexantlev = 5;
        } else if (eraAnsweredAntiquity > 99) {
            badgexantlev = 4;
        } else if (eraAnsweredAntiquity > 49) {
            badgexantlev = 3;
        } else if (eraAnsweredAntiquity > 24) {
            badgexantlev = 2;
        } else if (eraAnsweredAntiquity > 4) {
            badgexantlev = 1;
        } else {
            badgexantlev = 0;
        }

        if (eraAnsweredMiddleAges > 199) {
            badgexmedlev = 5;
        } else if (eraAnsweredMiddleAges > 99) {
            badgexmedlev = 4;
        } else if (eraAnsweredMiddleAges > 49) {
            badgexmedlev = 3;
        } else if (eraAnsweredMiddleAges > 24) {
            badgexmedlev = 2;
        } else if (eraAnsweredMiddleAges > 4) {
            badgexmedlev = 1;
        } else {
            badgexmedlev = 0;
        }

        if (eraAnsweredRenaissance > 199) {
            badgexrenlev = 5;
        } else if (eraAnsweredRenaissance > 99) {
            badgexrenlev = 4;
        } else if (eraAnsweredRenaissance > 49) {
            badgexrenlev = 3;
        } else if (eraAnsweredRenaissance > 24) {
            badgexrenlev = 2;
        } else if (eraAnsweredRenaissance > 4) {
            badgexrenlev = 1;
        } else {
            badgexrenlev = 0;
        }

        if (eraAnsweredEnlightenment > 199) {
            badgexenllev = 5;
        } else if (eraAnsweredEnlightenment > 99) {
            badgexenllev = 4;
        } else if (eraAnsweredEnlightenment > 49) {
            badgexenllev = 3;
        } else if (eraAnsweredEnlightenment > 24) {
            badgexenllev = 2;
        } else if (eraAnsweredEnlightenment > 4) {
            badgexenllev = 1;
        } else {
            badgexenllev = 0;
        }

        if (eraAnsweredModern > 199) {
            badgexmodlev = 5;
        } else if (eraAnsweredModern > 99) {
            badgexmodlev = 4;
        } else if (eraAnsweredModern > 49) {
            badgexmodlev = 3;
        } else if (eraAnsweredModern > 24) {
            badgexmodlev = 2;
        } else if (eraAnsweredModern > 4) {
            badgexmodlev = 1;
        } else {
            badgexmodlev = 0;
        }

        if (eraAnsweredContemporary > 199) {
            badgexconlev = 5;
        } else if (eraAnsweredContemporary > 99) {
            badgexconlev = 4;
        } else if (eraAnsweredContemporary > 49) {
            badgexconlev = 3;
        } else if (eraAnsweredContemporary > 24) {
            badgexconlev = 2;
        } else if (eraAnsweredContemporary > 4) {
            badgexconlev = 1;
        } else {
            badgexconlev = 0;
        }

        /// checks to see if current badge level is more than in Firebase and if so write that new level and pass on to expanded answer
        try {
            if (badgesuplev > badgesup) {
                newbadgesup = badgesuplev;
                userReference.getRef().child("badgesup").setValue(newbadgesup);
                badgeAwardedToggle = 2;
            }
        } catch (Exception e) {
        }
        try {
            if (badgeAwardedToggle == 1) {
                if (badgestrlev > badgestr) {
                    newbadgestr = badgestrlev;
                    userReference.getRef().child("badgestr").setValue(newbadgestr);
                    badgeAwardedToggle = 2;
                }
            }
        } catch (Exception e) {
        }
        try {
            if (badgeAwardedToggle == 1) {
                if (badgetrtlev > badgetrt) {
                    newbadgetrt = badgetrtlev;
                    userReference.getRef().child("badgetrt").setValue(newbadgetrt);
                    badgeAwardedToggle = 2;
                }
            }
        } catch (Exception e) {
        }
        try {
            if (badgeAwardedToggle == 1) {
                if (badgewinlev > badgewin) {
                    newbadgewin = badgewinlev;
                    userReference.getRef().child("badgewin").setValue(newbadgewin);
                    badgeAwardedToggle = 2;
                }
            }
        } catch (Exception e) {
        }

        try {
            if (badgeAwardedToggle == 1) {
                if (badgexantlev > badgexant) {
                    newbadgexant = badgexantlev;
                    userReference.getRef().child("badgexant").setValue(newbadgexant);
                    badgeAwardedToggle = 2;
                }
            }
        } catch (Exception e) {
        }
        try {
            if (badgeAwardedToggle == 1) {
                if (badgexmedlev > badgexmed) {
                    newbadgexmed = badgexmedlev;
                    userReference.getRef().child("badgexmed").setValue(newbadgexmed);
                    badgeAwardedToggle = 2;
                }
            }
        } catch (Exception e) {
        }
        try {
            if (badgeAwardedToggle == 1) {
                if (badgexrenlev > badgexren) {
                    newbadgexren = badgexrenlev;
                    userReference.getRef().child("badgexren").setValue(newbadgexren);
                    badgeAwardedToggle = 2;
                }
            }
        } catch (Exception e) {
        }
        try {
            if (badgeAwardedToggle == 1) {
                if (badgexenllev > badgexenl) {
                    newbadgexenl = badgexenllev;
                    userReference.getRef().child("badgexenl").setValue(newbadgexenl);
                    badgeAwardedToggle = 2;
                }
            }
        } catch (Exception e) {
        }
        try {
            if (badgeAwardedToggle == 1) {
                if (badgexmodlev > badgexmod) {
                    newbadgexmod = badgexmodlev;
                    userReference.getRef().child("badgexmod").setValue(newbadgexmod);
                    badgeAwardedToggle = 2;
                }
            }
        } catch (Exception e) {
        }
        try {
            if (badgeAwardedToggle == 1) {
                if (badgexconlev > badgexcon) {
                    newbadgexcon = badgexconlev;
                    userReference.getRef().child("badgexcon").setValue(newbadgexcon);
                    badgeAwardedToggle = 2;
                }
            }
        } catch (Exception e) {
        }


        /// trainsition to extended answer section

        // takes user to expanded answer page carrying over values needed to determing right screen to use and the actual expanded answer
        Intent intent = new Intent(Game.this, ExpandedAnswer.class);
        intent.putExtra("iiiexpanded", ExpandedAnswerPut);
        intent.putExtra("bbbcategory", ExpAnsCategoryPut);
        intent.putExtra("lllepoch", ExpAnsEpochPut);
        intent.putExtra("dddcorrectansw", ExpCorrectAnsPut);

        /// badge components

        if (newbadgesup > 0) {
            intent.putExtra("newbadgesup", newbadgesup);
        }
        if (newbadgestr > 0) {
            intent.putExtra("newbadgestr", newbadgestr);
        }
        if (newbadgetrt > 0) {
            intent.putExtra("newbadgetrt", newbadgetrt);
        }
        if (newbadgewin > 0) {
            intent.putExtra("newbadgewin", newbadgewin);
        }

        if (newbadgexant > 0) {
            intent.putExtra("newbadgexant", newbadgexant);
        }
        if (newbadgexmed > 0) {
            intent.putExtra("newbadgexmed", newbadgexmed);
        }
        if (newbadgexren > 0) {
            intent.putExtra("newbadgexren", newbadgexren);
        }
        if (newbadgexenl > 0) {
            intent.putExtra("newbadgexenl", newbadgexenl);
        }
        if (newbadgexmod > 0) {
            intent.putExtra("newbadgexmod", newbadgexmod);
        }
        if (newbadgexcon > 0) {
            intent.putExtra("newbadgexcon", newbadgexcon);
        }
        finish();
        startActivity(intent);



    }

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

                    btnWrongGlowX.setVisibility(View.GONE);
                    shadeX.setVisibility(View.GONE);
                    imgCorrectorCheckX.setVisibility(View.GONE);
                    loutCoinAwardX.setVisibility(View.GONE);
                    btnWrongX.setVisibility(View.VISIBLE);

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    // this is the main timer logic for the 10 then 5 seconds a user has to answer the question
    public void startTimer(){

        //Log.i("TIMING", "started timer - timersetting:  " + timersetting);

        // will be called at every 1000 milliseconds i.e. every second.
        countDownTimer = new CountDownTimer(timersetting, 100) { // when interval was higher was possible
            public void onTick(long millisUntilFinished) {                              //be down the path for timeout but not yeet there

                    double millisleftRough = millisUntilFinished / 1000;

                    int millisleftFinal = (int) Math.round(millisleftRough);

                   // Toast.makeText(Game.this, "M = " + millisleftFinal, Toast.LENGTH_SHORT).show();

                    String timerCountString = millisleftFinal + "";
                    txtTimerX.setText(timerCountString);  // converting timer to string and setting the counter  which starts of as GONE
                if (ticksetting == 5000 & millisUntilFinished < 8000 & tickerToggle == 1) {  // making the counter  and hourglass visible at 5 seconds

                    tickerToggle = 2;
                    txtTimerX.setVisibility(View.VISIBLE);
                    imgHPTimerX.setVisibility(View.VISIBLE);


                    if (width2 > 1500) { //graphics changes for tablets

                        txtGameAnswerDisplayX.setTextSize(30);
                        txtGameQuestionX.animate().scaleY(0.95f).setDuration(300);
                        loutGameQuestionX.animate().scaleY(.95f).setDuration(300);
                        loutGameQuestionX.animate().translationY(-80).setDuration(300);
                        loutGameAnswerDisplayX.animate().translationY(-200).setDuration(1);


                    } else if (height2 < 1300) {  // graphics changes for small lower res phones

                        txtGameAnswerDisplayX.setTextSize(16);
                        txtGameQuestionX.animate().scaleY(0.95f).setDuration(300);
                        loutGameQuestionX.animate().scaleY(.95f).setDuration(300);
                        loutGameQuestionX.animate().translationY(-80).setDuration(300);
                        loutGameAnswerDisplayX.animate().translationY(-100).setDuration(1);


                    } else {

//                        loutGameAnswerDisplayX.animate().scaleY(1.1f);
//                        txtGameAnswerDisplayX.animate().scaleY(1.2f);
//                        txtGameAnswerDisplayX.setTextSize(15);

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

                if (millisUntilFinished < 200) { // making buttons disappear a bit early to make sure no chance of simultaneous timeout and press causing all kinds of fuck ups
                    loutGameAnswerDisplayX.setVisibility(View.GONE);

                }
                if (millisUntilFinished <1000) {

                    imgHPTimer5X.setVisibility(View.GONE);
                    imgHPTimerX.setVisibility(View.VISIBLE);
                } else
                if (millisUntilFinished <2000) {

                    imgHPTimer4X.setVisibility(View.GONE);
                    imgHPTimer5X.setVisibility(View.VISIBLE);
                }else
                if (millisUntilFinished < 3500) {
                    imgHPTimer3X.setVisibility(View.GONE);
                    imgHPTimer4X.setVisibility(View.VISIBLE);
                } else
                if (millisUntilFinished < 5000) {

                    imgHPTimer2X.setVisibility(View.GONE);
                    imgHPTimer3X.setVisibility(View.VISIBLE);
                } else
                if (millisUntilFinished < 7000) {


                    imgHPTimerX.setVisibility(View.GONE);
                    imgHPTimer2X.setVisibility(View.VISIBLE);
                }

            }

            public void onFinish() { // WHEN TIMING OUT:
                // other than the toast treats this as a wrong answer, makes streak deductions and sends to expanded answer
                //Log.i("QUIZQ", "On Finish Main");

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

                        //Log.i("QUIZQ", "On Finish Corrector");
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

                        // adds one to the counter and its shared pref for interstitial ads
                        Log.i("INTERSTITIAL", "Counter on timeout = " + mAdvertCounterGame);
                        mAdvertCounterGame = mAdvertCounterGame +1;
                        SharedPreferences.Editor editor = sharedAdvertCounterGame.edit();
                        editor.putInt("CounterGame", mAdvertCounterGame); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
                        editor.apply(); // saves the value

                        if (coinsOwned < 1) {

                            // resets the counter for interstitial ads
                            mAdvertCounterGame = 0;
                            SharedPreferences.Editor editor2 = sharedAdvertCounterGame.edit();
                            editor2.putInt("CounterGame", mAdvertCounterGame); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
                            editor2.apply(); // saves the value
                            Log.i("INTERSTITIAL", "Counter on reset = " + mAdvertCounterGame);

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
                                        Log.i("TIMER", "From timeout to ad toggle:  " + adMobToggle);
                                        mRewardedAdGameScreenCoins.show();
                                    }

                                }
                            }.start();


                        } else {


                            Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                            intent.putExtra("iiiexpanded", ExpandedAnswerPut);
                            intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                            intent.putExtra("lllepoch", ExpAnsEpochPut);
                            intent.putExtra("dddcorrectansw", ExpCorrectAnsPut);
                            //("QUIZQ", "Here");
                            finish();
                            startActivity(intent);


                        }

                    }
                }.start();


            }
        }.start();


    }

    public void stopTimer() {

        countDownTimer.cancel();
        Log.i("TIMING", "Stopping Timer");

    }

    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    public void onBackPressed(){

        // just leaving this blank so back press should just be a nothing

    }


    ///////////////// AD-MOB BEGINS /////////////////////////////////////////////////////////////
    @Override
    public void onRewardedVideoAdLoaded() {

        Log.i("ADMOB", "loaded");

    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.i("ADMOB", "opened");

    }

    @Override
    public void onRewardedVideoStarted() {

        mRewardedAdGameScreenCoins.loadAd("ca-app-pub-3940256099942544/5224354917", new AdRequest.Builder().build());
        Log.i("ADMOB", "started");
    }

    @Override
    public void onRewardedVideoAdClosed() {

        Log.i("ADMOB", "closed");

        // if user closes add the display defaults back to the screen showing the message they need to watch and then it times out and puts up the ad again
        CountDownTimer rewatchTimer = new CountDownTimer(3000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                if (mRewardedAdGameScreenCoins.isLoaded()) {
                    mRewardedAdGameScreenCoins.show();
                    Log.i("ADMOB", "closed and reshowing on finish");
                }
            }
        }.start();

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

       // Log.i("TIMING", "On Reward toggle: " + adMobToggle);

        Log.i("ADMOB", "rewarded, coins before reward  " + coinsOwned);

        coinsOwned = coinsOwned + 100;
        String coinsOwnedZ = Integer.toString(coinsOwned);
        txtCoinCounterX.setText(coinsOwnedZ);
        userReference.getRef().child("coins").setValue(coinsOwned);
        int coinsOwnedSort = - coinsOwned;
        userReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);

        Log.i("ADMOB", "rewarded, coins AFTER reward  " + coinsOwned + "  and toggle = " + adMobToggle);

        if (adMobToggle == 1) { // toggles is 1 when sent here from oncreate vs. a wrong answer so restarting game activity instead of going to expanded answer
           // Log.i("TIMING", "Final toggle: " + adMobToggle);
            shadeX.setVisibility(View.GONE);
            txtAdMessageX.setVisibility(View.GONE);
            Log.i("ADMOB", "rewarded, going to oncreate");
            finish();
            startActivity(getIntent());

        } else {

            Log.i("ADMOB", "rewarded, going to expanded");

            Intent intent = new Intent(Game.this, ExpandedAnswer.class);
            intent.putExtra("dddcorrectansw", ExpCorrectAnsPut);
            intent.putExtra("iiiexpanded", ExpandedAnswerPut);
            intent.putExtra("bbbcategory", ExpAnsCategoryPut);
            intent.putExtra("lllepoch", ExpAnsEpochPut);
            finish();
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

            //Log.i("TIMING", "Go to add Toggle  " + adMobToggle);

            shadeX.setVisibility(View.VISIBLE);
            txtAdMessageX.setVisibility(View.VISIBLE);

            // resets the counter for interstitial ads
            mAdvertCounterGame = 0;
            SharedPreferences.Editor editor2 = sharedAdvertCounterGame.edit();
            editor2.putInt("CounterGame", mAdvertCounterGame); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
            editor2.apply(); // saves the value
            Log.i("INTERSTITIAL", "Counter on reset = " + mAdvertCounterGame);

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

    // come here only from wrong answers to make it easier to send to expnded cuz don't have to worry about badges
    // However the counter will keep boing on right answers too but will just jump to add on wrong even if counter goes beyond 10 on rights
    // in every scenario here the app shoots the user to Expanded; WHAT WILL HAPPEN IF USER CLICKS ON THE ADD
    private void interstitialAdvert(){

        Log.i("INTERSTITIAL", "in the intestitial");

        mInterstitialGame.show();
        SharedPreferences.Editor editor = sharedAdvertCounterGame.edit();
        editor.putInt("CounterGame", 0); // this only kicks in on next on create so need to set actual mAdvertCounter to 0 below so the add does not loop
        editor.apply(); // saves the value
        mAdvertCounterGame = 0;

        mInterstitialGame.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.i("INTERSTITIAL", "in the onloaded");

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.i("INTERSTITIAL", "in the failed to load");
                // Code to be executed when an ad request fails.
                Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                intent.putExtra("iiiexpanded", ExpandedAnswerPut);
                intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                intent.putExtra("lllepoch", ExpAnsEpochPut);
                intent.putExtra("dddcorrectansw", ExpCorrectAnsPut);
                finish();
                startActivity(intent);
            }

            @Override
            public void onAdOpened() {
                Log.i("INTERSTITIAL", "in the AdOpened");
                // Code to be executed when the ad is displayed.
                mInterstitialGame.loadAd(new AdRequest.Builder().build());

            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                mInterstitialGame.loadAd(new AdRequest.Builder().build());

                Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                intent.putExtra("iiiexpanded", ExpandedAnswerPut);
                intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                intent.putExtra("lllepoch", ExpAnsEpochPut);
                intent.putExtra("dddcorrectansw", ExpCorrectAnsPut);
                finish();
                startActivity(intent);

            }

            @Override
            public void onAdLeftApplication() {
                Log.i("INTERSTITIAL", "in the adleft app");
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                Log.i("INTERSTITIAL", "in ad closed");
                // Code to be executed when the interstitial ad is closed.
                mInterstitialGame.loadAd(new AdRequest.Builder().build());

                Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                intent.putExtra("iiiexpanded", ExpandedAnswerPut);
                intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                intent.putExtra("lllepoch", ExpAnsEpochPut);
                intent.putExtra("dddcorrectansw", ExpCorrectAnsPut);
                finish();
                startActivity(intent);

            }
        });


    }
}
