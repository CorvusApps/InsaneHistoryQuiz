package com.pelotheban.insanehistoryquiz;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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

import java.util.Random;

public class Game extends AppCompatActivity {

    // Game play UI elements and variables

    private int randQuestion;
    private String displayAnswer, displayAnswer2, displayAnswer3, displayAnswer4, displayAnswer5; // will be randomly assigned to the answer options in each question
    private int answerCounter; // scrolls through how many answers options to a question the player has seen
    private int randAnswer;
    private ImageButton btnCorrectX, btnWrongX;

    private String ExpandedAnswerPut, ExpAnsCategoryPut, ExpAnsEpochPut; // making this class variable so can go to expanded answer screen
    private String era; // pulls in era so we know which counter to grow when user answers question correctly

    private LinearLayout loutGameQuestionX;
    private ConstraintLayout loutGameAnswerDisplayX;

    private TextView txtGameQuestionX, txtGameAnswerDisplayX, txtGameExpandedAnswerX;

    private CountDownTimer countDownTimer;

    //.............//// various score counters

    private TextView txtCoinCounterX, txtConStreakX;
    private String coinGrantToggle;

    private String coinsOwnedString;
    private double coinsOwned;

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
    private ImageView imgHPTimerX, imgHPTimer2X, imgHPTimer3X, imgHPTimer4X;

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

    int height2;
    int width2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Game play
        answerCounter = 1;
        randQuestion = new Random().nextInt(5) + 1; // random question number to be displayed

        //...... displayed outputs
        loutGameQuestionX = findViewById(R.id.loutGameQuestion);
        loutGameAnswerDisplayX = findViewById(R.id.loutGameAnswerDisplay);

        txtGameQuestionX = findViewById(R.id.txtGameQuestion);
        txtGameAnswerDisplayX = findViewById(R.id.txtGameAnswerDisplay);
        txtCoinCounterX = findViewById(R.id.txtCoinCounter);
        txtConStreakX = findViewById(R.id.txtConStreak);

        // timesettings

        txtTimerX = findViewById(R.id.txtTimer);
        imgHPTimerX = findViewById(R.id.imgHPTimer);
        imgHPTimer2X = findViewById(R.id.imgHPTimer2);
        imgHPTimer3X = findViewById(R.id.imgHPTimer3);
        imgHPTimer4X = findViewById(R.id.imgHPTimer4);

        timersetting = 10000;
        ticksetting = 5000;
        tickerToggle = 1;

        //Firebase user

        uid = FirebaseAuth.getInstance().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid);
        userReference.getRef().child("user").setValue(uid);

        /// sizing the display to have both the question and then the answer mostly in the center
                /// not used yet but leaving for adjustments to different displays
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        double height = size.y;

        height2 = (int) Math.round(height);
        width2 = (int) Math.round(width);






        ////// add coins to account IF FIRST TIME /////////////////////////////////////////////////////////////
        // also using this section to pull in all the user variables like counters of questions answered etc.

        sortUsersQuery = FirebaseDatabase.getInstance().getReference().child("my_users").orderByChild("user").equalTo(uid);
        sortUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot userDs: dataSnapshot.getChildren()) {
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

                        if (longestStreak < consStreak){

                            longestStreak = consStreak;

                        }

                        //Toast.makeText(Game.this, longestStreak + " is longesst", Toast.LENGTH_LONG).show();
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
                        coinsOwned = Double.valueOf(coinsOwnedString);

                        coinGrantToggle = userDs.child("coinsgranttoggle").getValue().toString();



                    } catch (Exception e) {

                         //Toast.makeText(Game.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }



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


                        }

                    } catch (Exception e) { // this is the catch of the if above and repeating the initial coin grant query as per notes above

                  //  Toast.makeText(Game.this, "Granting", Toast.LENGTH_SHORT).show();

                    userReference.getRef().child("coins").setValue(80);
                    userReference.getRef().child("coinsgranttoggle").setValue("yes");




                    //Toast.makeText(Game.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //// END OF INITIAL COIN GRANT SECTION //////////////////////////////////////////////////////////////////////////////////

        // Firebase game SECTION BEGINS /////////////////////////////////////////////////////////////////////////////////////////

        gameReference = FirebaseDatabase.getInstance().getReference().child("questions");
        sortGameQueryQuestions = gameReference.orderByChild("aaaqno").equalTo(randQuestion);

        // First firebase pull is on create showing the question and first answer (will add a time delay on the answer)

        sortGameQueryQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot gameQs: dataSnapshot.getChildren()) {

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

                    if(randAnswer == 1){

                        displayAnswer = GameCorrectAnswerZ;

                    }else if(randAnswer == 2){

                        displayAnswer = GameWrongAnswer1Z;


                    }else if(randAnswer == 3){

                        displayAnswer = GameWrongAnswer4Z;


                    }else if(randAnswer == 4){

                        displayAnswer = GameWrongAnswer3Z;


                    }else if(randAnswer == 5){

                        displayAnswer = GameWrongAnswer1Z;
                    }


                    if (answerCounter == 1) {
                        txtGameAnswerDisplayX.setText(displayAnswer);

//                    } else if (answerCounter == 2) {
//                        txtGameAnswerDisplayX.setText(displayAnswer2);

                        // probably don't need the above because there is no value in 2 but keeping for now just in case
                   }
                     else {

                        Toast.makeText(Game.this, "Out of numbers", Toast.LENGTH_SHORT).show();

                    }
                }

                startTimer();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnCorrectX = findViewById(R.id.btnCorrect);
        btnCorrectX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (randAnswer == answerCounter) {

                    stopTimer();

                    // Toast.makeText(Game.this, "You got it", Toast.LENGTH_LONG).show();

                   // Toast.makeText(Game.this, "Antiquity  " + eraAnsweredAntiquity + "   Enlight  " + eraAnsweredEnlightenment, Toast.LENGTH_LONG).show();

                    coinsOwned = coinsOwned + 5;
                    String coinsOwedZ = Double.toString(coinsOwned);
                    txtCoinCounterX.setText(coinsOwedZ);
                    userReference.getRef().child("coins").setValue(coinsOwned);

                    consStreak = consStreak + 1;
                    String conStreakZ = Integer.toString(consStreak);
                    txtConStreakX.setText(conStreakZ);
                    userReference.getRef().child("constreak").setValue(consStreak);

                    totalAnswered = totalAnswered + 1;
                    userReference.getRef().child("totalanswered").setValue(totalAnswered);

                    if (longestStreak < consStreak){

                        longestStreak = consStreak;

                    }
                    userReference.getRef().child("longeststreak").setValue(longestStreak);

                    totalQuestions = totalQuestions + 1;
                    userReference.getRef().child("totalquestions").setValue(totalQuestions);

                    if (era.equals("Antiquity")){
                        eraAnsweredAntiquity = eraAnsweredAntiquity + 1;
                        userReference.getRef().child("eraansantiquity").setValue(eraAnsweredAntiquity);
                    }

                    if (era.equals("Middle Ages")){
                        eraAnsweredMiddleAges = eraAnsweredMiddleAges + 1;
                        userReference.getRef().child("eraansmiddle").setValue(eraAnsweredMiddleAges);
                    }

                    if (era.equals("Renaissance")){
                        eraAnsweredRenaissance = eraAnsweredRenaissance + 1;
                        userReference.getRef().child("eraansrenaissance").setValue(eraAnsweredRenaissance);
                    }

                    if (era.equals("Enlightenment")){
                        eraAnsweredEnlightenment = eraAnsweredEnlightenment + 1;
                        userReference.getRef().child("eraanselight").setValue(eraAnsweredEnlightenment);
                    }

                    if (era.equals("Early Modern")){
                        eraAnsweredEarlyModern = eraAnsweredEarlyModern + 1;
                        userReference.getRef().child("eraansearlymod").setValue(eraAnsweredEarlyModern);
                    }

                    if (era.equals("Modern")){
                        eraAnsweredModern = eraAnsweredModern + 1;
                        userReference.getRef().child("eraansmodern").setValue(eraAnsweredModern);
                    }


                    Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                    intent.putExtra("iiiexpanded" , ExpandedAnswerPut);
                    intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                    intent.putExtra("lllepoch", ExpAnsEpochPut);
                    startActivity(intent);


                } else {

                    stopTimer();

                    Toast.makeText(Game.this, "WRONG", Toast.LENGTH_LONG).show();

                    coinsOwned = coinsOwned - 10;
                    String coinsOwedZ = Double.toString(coinsOwned);
                    txtCoinCounterX.setText(coinsOwedZ);
                    userReference.getRef().child("coins").setValue(coinsOwned);

                    consStreak = 0;
                    String conStreakZ = Integer.toString(consStreak);
                    txtConStreakX.setText(conStreakZ);
                    userReference.getRef().child("constreak").setValue(consStreak);

                    totalQuestions = totalQuestions + 1;
                    userReference.getRef().child("totalquestions").setValue(totalQuestions);

                    Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                    intent.putExtra("iiiexpanded" , ExpandedAnswerPut);
                    intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                    intent.putExtra("lllepoch", ExpAnsEpochPut);
                    startActivity(intent);
                }
            }
        });


        btnWrongX = findViewById(R.id.btnWrong);
        btnWrongX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (randAnswer == answerCounter) {

                    stopTimer();

                    Toast.makeText(Game.this, "WRONG", Toast.LENGTH_LONG).show();

                    coinsOwned = coinsOwned - 10;
                    String coinsOwedZ = Double.toString(coinsOwned);
                    txtCoinCounterX.setText(coinsOwedZ);
                    userReference.getRef().child("coins").setValue(coinsOwned);

                    consStreak = 0;
                    String conStreakZ = Integer.toString(consStreak);
                    txtConStreakX.setText(conStreakZ);
                    userReference.getRef().child("constreak").setValue(consStreak);

                    totalQuestions = totalQuestions + 1;
                    userReference.getRef().child("totalquestions").setValue(totalQuestions);

                    Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                    intent.putExtra("iiiexpanded" , ExpandedAnswerPut);
                    intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                    intent.putExtra("lllepoch", ExpAnsEpochPut);
                    startActivity(intent);

                } else {

                    Toast.makeText(Game.this, "You got it", Toast.LENGTH_LONG).show();
                    stopTimer(); // need to start and reset timer as not entering the oncreate  and need to transition from one answer to next
                    timersetting = 6000;
                    ticksetting = 1000;
                    startTimer();
                    nextQuestions ();
                }

            }
        });

    }

    private void nextQuestions (){

        answerCounter = answerCounter + 1;

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

    public void startTimer(){

        // will be called at every 1500 milliseconds i.e. every 1.5 second.
        countDownTimer = new CountDownTimer(timersetting, 1000) {
            public void onTick(long millisUntilFinished) {

                    String timerCountString = millisUntilFinished / 1000 + "";
                    txtTimerX.setText(timerCountString);
                if (ticksetting == 5000 & millisUntilFinished < 6000 & tickerToggle == 1) {

                    tickerToggle = 2;
                    txtTimerX.setVisibility(View.VISIBLE);
                    imgHPTimerX.setVisibility(View.VISIBLE);



                    // we need to only do all the main UI elements movements on ONE tick at 5K while the timer needs to fade in at that time
                    // AND it needs to count down on every tick...
                    // so need to get countDowninterval to 1000 but use a toggle to only do the UI fadeins once.

//                    YoYo.with(Techniques.SlideOutUp)
//                            .delay(0)
//                            .duration(200)
//                            .repeat(0)
//                            .playOn(txtGameQuestionX);
                    //txtGameQuestionX.setVisibility(View.GONE);

                    if (width2 > 1500) {

                       // Toast.makeText(Game.this, "WIDE WIDE WIDE", Toast.LENGTH_LONG).show();

                        loutGameQuestionX.animate().translationY(-700).setDuration(1);


                    } else if (height2 < 1300) {


                        loutGameQuestionX.animate().translationY(-200).setDuration(1);

                       // Toast.makeText(Game.this, "SHOOOOOOOOOOOOOOORT  " + height2, Toast.LENGTH_LONG).show();


                    } else {

                        ObjectAnimator animatorX = ObjectAnimator.ofFloat(loutGameQuestionX, "y", 400f);
                        animatorX.setDuration(1000);
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.playTogether(animatorX);
                        animatorSet.start();

                    }

                   loutGameAnswerDisplayX.setVisibility(View.VISIBLE);


                    YoYo.with(Techniques.FadeIn)
                            .delay(0)
                            .duration(2000)
                            .repeat(0)
                            .playOn(loutGameAnswerDisplayX);

                    if (width2 > 1500) {

                       loutGameAnswerDisplayX.animate().translationY(-400).setDuration(1);



                       txtGameAnswerDisplayX.setTextSize(28);


                    } else if (height2 < 1300){

                      //  loutGameAnswerDisplayX.animate().translationY(-200).setDuration(1);

                    } else {



                    }

                } else

                if (millisUntilFinished < 5000) {

                   // Toast.makeText(Game.this, "Tick 4",Toast.LENGTH_SHORT).show();

                    imgHPTimerX.setVisibility(View.GONE);
                    imgHPTimer2X.setVisibility(View.VISIBLE);
                }
                if (millisUntilFinished < 4000) {

                    imgHPTimer2X.setVisibility(View.GONE);
                    imgHPTimer3X.setVisibility(View.VISIBLE);
                }

                if (millisUntilFinished < 3000) {
                    imgHPTimer3X.setVisibility(View.GONE);
                    imgHPTimer4X.setVisibility(View.VISIBLE);
                }

                if (millisUntilFinished <2000) {

                    imgHPTimer4X.setVisibility(View.GONE);
                    imgHPTimerX.setVisibility(View.VISIBLE);
                }

            }

            public void onFinish() {
                // other than the toast treats this as a wrong answer, makes streak deductions and sends to expanded answer
                Toast.makeText(Game.this, "TIME OUT", Toast.LENGTH_LONG).show();

                coinsOwned = coinsOwned - 10;
                String coinsOwedZ = Double.toString(coinsOwned);
                txtCoinCounterX.setText(coinsOwedZ);
                userReference.getRef().child("coins").setValue(coinsOwned);

                consStreak = 0;
                String conStreakZ = Integer.toString(consStreak);
                txtConStreakX.setText(conStreakZ);
                userReference.getRef().child("constreak").setValue(consStreak);


                Intent intent = new Intent(Game.this, ExpandedAnswer.class);
                intent.putExtra("iiiexpanded", ExpandedAnswerPut);
                intent.putExtra("bbbcategory", ExpAnsCategoryPut);
                intent.putExtra("lllepoch", ExpAnsEpochPut);
                startActivity(intent);
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

}
