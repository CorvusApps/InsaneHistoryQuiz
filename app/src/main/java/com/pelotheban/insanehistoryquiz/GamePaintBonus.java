package com.pelotheban.insanehistoryquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class GamePaintBonus extends AppCompatActivity {

    private String badgeIDRec;

    // UI Elements

    private ImageView imgShowPaintingX, imgShowPaintingVerticalX;
    private LinearLayout loutShowPaintingX, loutShowPaintingVerticalX;
    private Button btnAnswer1X, btnAnswer2X, btnAnswer3X, btnAnswer4X;
    private Button btnAnswer1XRed, btnAnswer2XRed, btnAnswer3XRed, btnAnswer4XRed;
    private Button btnAnswer1XGreen, btnAnswer2XGreen, btnAnswer3XGreen, btnAnswer4XGreen;

    private ImageView imgGoldCoinsX;
    private TextView txtCoinPaintCounterX;

    private LinearLayout loutPaintGameButtonsX;
    private ImageView btnPaintProfileX, btnPaintPlayX, btnPaintLeaderX;
    private TextView txtSetQuestionX;

    private LinearLayout loutPaintExpandedX;
    private TextView txtPaintExpandedAnswerShowX;

    private int randAnswer;

    private ImageView imgCoin1X, imgCoin2X, imgCoin3X, imgCoin4X, imgCoin5X, imgCoin6X;

        // Timer related

        private CountDownTimer paintTimer;
        private TextView txtPaintTimerX;
        private ImageView imgPaintTimerX, imgPaintTimer2X, imgPaintTimer3X, imgPaintTimer4X, imgPaintTimer5X;

    //Firebase

    private DatabaseReference gamePaintReference;

    private String uid; // this is for the user account side
    private DatabaseReference userPaintReference;
    private Query sortUsersPaintQuery, paintQuestionQuery, paintingsQuery;
    private String coinsOwnedPaintString;
    private int coinsOwnedPaint;
    private FirebaseAuth mAuthEA;

    private String correctPaintAnsRec, wrongPaintAns1Rec, wrongPaintAns2Rec, wrongPaintAns3Rec, paintExpandedRec;
    private String imagePaintLinkRec, orientationRec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_paint_bonus);

        imgGoldCoinsX = findViewById(R.id.imgGoldCoins);
        txtCoinPaintCounterX = findViewById(R.id.txtCoinPaintCounter);
        txtSetQuestionX = findViewById(R.id.txtSetQuestion);

        loutPaintExpandedX = findViewById(R.id.loutPaintExpanded);
        txtPaintExpandedAnswerShowX = findViewById(R.id.txtPaintExpandedAnswerShow);

        badgeIDRec = getIntent().getStringExtra("badgeid");
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

        imgShowPaintingX = findViewById(R.id.imgShowPainting);
        imgShowPaintingVerticalX = findViewById(R.id.imgShowPaintingVertical);

        loutShowPaintingX = findViewById(R.id.loutShowPainting);
        loutShowPaintingVerticalX = findViewById(R.id.loutShowPaintingVertical);

        loutPaintGameButtonsX = findViewById(R.id.loutGamePaintButtons);

        txtPaintTimerX = findViewById(R.id.txtPaintTimer);
        imgPaintTimerX = findViewById(R.id.imgPaintTimer);
        imgPaintTimer2X = findViewById(R.id.imgPaintTimer2);
        imgPaintTimer3X = findViewById(R.id.imgPaintTimer3);
        imgPaintTimer4X = findViewById(R.id.imgPaintTimer4);
        imgPaintTimer5X = findViewById(R.id.imgPaintTimer5);

        imgCoin1X = findViewById(R.id.imgCoin1);
        imgCoin2X = findViewById(R.id.imgCoin2);
        imgCoin3X = findViewById(R.id.imgCoin3);
        imgCoin4X = findViewById(R.id.imgCoin4);
        imgCoin5X = findViewById(R.id.imgCoin5);
        imgCoin6X = findViewById(R.id.imgCoin6);

        Log.i("PAINTLAMM", badgeIDRec);

        paintingsQuery = FirebaseDatabase.getInstance().getReference().child("paintings").orderByChild("paintingname").equalTo(badgeIDRec);
        Log.i("PAINTLAMM", paintingsQuery.toString());
        paintingsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot paintingSnapshot) {

                for (DataSnapshot paintingImages : paintingSnapshot.getChildren()) {

                    imagePaintLinkRec = paintingImages.child("paintingimagelink").getValue().toString();
                    orientationRec = paintingImages.child("orientation").getValue().toString();

                    Log.i("PAINTLAMM", imagePaintLinkRec);
                    Log.i("PAINTLAMM", orientationRec);

                    paintingImageSet();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        paintQuestionQuery = FirebaseDatabase.getInstance().getReference().child("paintquestions").orderByChild("aaabadgeid").equalTo(badgeIDRec);
        paintQuestionQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot questionSnapshot) {

                for (DataSnapshot paintQuestions : questionSnapshot.getChildren()) {

                   correctPaintAnsRec = paintQuestions.child("bbbcorrectpaintansw").getValue().toString();
                   wrongPaintAns1Rec = paintQuestions.child("cccwrongpaintans1").getValue().toString();
                   wrongPaintAns2Rec = paintQuestions.child("dddwrongpaintans2").getValue().toString();
                   wrongPaintAns3Rec = paintQuestions.child("eeewrongpaintans3").getValue().toString();
                   paintExpandedRec = paintQuestions.child("fffpaintexpanded").getValue().toString();

                   buttonsSet();
                   countdownPaintTimer();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        uid = FirebaseAuth.getInstance().getUid();

        userPaintReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid);

        sortUsersPaintQuery = FirebaseDatabase.getInstance().getReference().child("my_users").orderByChild("user").equalTo(uid);
        sortUsersPaintQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot userSnapshot) {

                for (DataSnapshot paintUsers : userSnapshot.getChildren()) {

                    coinsOwnedPaintString = paintUsers.child("coins").getValue().toString();
                    coinsOwnedPaint = Integer.parseInt(coinsOwnedPaintString);

                    userStatsSet();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnAnswer1X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paintTimer.cancel();

                if (randAnswer == 1) {

                    btnAnswer1X.setVisibility(View.GONE);
                    btnAnswer1XGreen.setVisibility(View.VISIBLE);



                } else {

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

                CountDownTimer correctorPaintTimer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        grantPaintCoins();
                        paintExpandedAnswer();
                    }
                }.start();



            }
        });

        btnAnswer2X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paintTimer.cancel();

                if (randAnswer == 2) {

                    btnAnswer2X.setVisibility(View.GONE);
                    btnAnswer2XGreen.setVisibility(View.VISIBLE);


                } else {

                    btnAnswer2X.setVisibility(View.GONE);
                    btnAnswer2XRed.setVisibility(View.VISIBLE);

                }

                CountDownTimer correctorPaintTimer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        grantPaintCoins();
                        paintExpandedAnswer();
                    }
                }.start();

            }
        });
        btnAnswer3X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paintTimer.cancel();

                if (randAnswer == 3) {

                    btnAnswer3X.setVisibility(View.GONE);
                    btnAnswer3XGreen.setVisibility(View.VISIBLE);


                } else {

                    btnAnswer3X.setVisibility(View.GONE);
                    btnAnswer3XRed.setVisibility(View.VISIBLE);

                }

                CountDownTimer correctorPaintTimer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        grantPaintCoins();
                        paintExpandedAnswer();
                    }
                }.start();

            }
        });
        btnAnswer4X.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                paintTimer.cancel();

                if (randAnswer == 4) {

                    btnAnswer4X.setVisibility(View.GONE);
                    btnAnswer4XGreen.setVisibility(View.VISIBLE);


                } else {

                    btnAnswer4X.setVisibility(View.GONE);
                    btnAnswer4XRed.setVisibility(View.VISIBLE);

                }

                CountDownTimer correctorPaintTimer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        grantPaintCoins();
                        paintExpandedAnswer();
                    }
                }.start();

            }
        });



        btnPaintProfileX = findViewById(R.id.btnPaintProfile);
        btnPaintProfileX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GamePaintBonus.this, ProfileView.class);
                startActivity(intent);

            }
        });

        btnPaintPlayX = findViewById(R.id.btnPaintPlay);
        btnPaintPlayX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GamePaintBonus.this, Game.class);
                startActivity(intent);

            }
        });

        btnPaintLeaderX = findViewById(R.id.btnPaintLeaders);
        btnPaintLeaderX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GamePaintBonus.this, LeaderBoard.class);
                startActivity(intent);

            }
        });





    }
    private void paintingImageSet(){

        Log.i("PAINTLAMM", "in set + " + orientationRec);

        if (orientationRec.equals("horizontal")) {

            Log.i("PAINTLAMM", "in horizontal");
            loutShowPaintingX.setVisibility(View.VISIBLE);
            loutShowPaintingVerticalX.setVisibility(View.GONE);
            Picasso.get().load(imagePaintLinkRec).into(imgShowPaintingX);

        } else if(orientationRec.equals("vertical")) {

            Log.i("PAINTLAMM", "in vertical");
            loutShowPaintingX.setVisibility(View.GONE);
            loutShowPaintingVerticalX.setVisibility(View.VISIBLE);
            Picasso.get().load(imagePaintLinkRec).into(imgShowPaintingVerticalX);

        }
    }

    private void buttonsSet(){

       randAnswer = new Random().nextInt(4) + 1;

        if (randAnswer == 1) {

            btnAnswer1X.setText(correctPaintAnsRec);
            btnAnswer2X.setText(wrongPaintAns1Rec);
            btnAnswer3X.setText(wrongPaintAns2Rec);
            btnAnswer4X.setText(wrongPaintAns3Rec);

            btnAnswer1XRed.setText(correctPaintAnsRec);
            btnAnswer2XRed.setText(wrongPaintAns1Rec);
            btnAnswer3XRed.setText(wrongPaintAns2Rec);
            btnAnswer4XRed.setText(wrongPaintAns3Rec);

            btnAnswer1XGreen.setText(correctPaintAnsRec);


        } else if (randAnswer == 2) {

            btnAnswer2X.setText(correctPaintAnsRec);
            btnAnswer4X.setText(wrongPaintAns1Rec);
            btnAnswer3X.setText(wrongPaintAns2Rec);
            btnAnswer1X.setText(wrongPaintAns3Rec);

            btnAnswer2XRed.setText(correctPaintAnsRec);
            btnAnswer4XRed.setText(wrongPaintAns1Rec);
            btnAnswer3XRed.setText(wrongPaintAns2Rec);
            btnAnswer1XRed.setText(wrongPaintAns3Rec);

            btnAnswer2XGreen.setText(correctPaintAnsRec);



        } else if (randAnswer == 3) {

            btnAnswer3X.setText(correctPaintAnsRec);
            btnAnswer1X.setText(wrongPaintAns1Rec);
            btnAnswer2X.setText(wrongPaintAns2Rec);
            btnAnswer4X.setText(wrongPaintAns3Rec);

            btnAnswer3XRed.setText(correctPaintAnsRec);
            btnAnswer1XRed.setText(wrongPaintAns1Rec);
            btnAnswer2XRed.setText(wrongPaintAns2Rec);
            btnAnswer4XRed.setText(wrongPaintAns3Rec);

            btnAnswer3XGreen.setText(correctPaintAnsRec);



        } else if (randAnswer == 4) {

            btnAnswer4X.setText(correctPaintAnsRec);
            btnAnswer3X.setText(wrongPaintAns1Rec);
            btnAnswer2X.setText(wrongPaintAns2Rec);
            btnAnswer1X.setText(wrongPaintAns3Rec);

            btnAnswer4XRed.setText(correctPaintAnsRec);
            btnAnswer3XRed.setText(wrongPaintAns1Rec);
            btnAnswer2XRed.setText(wrongPaintAns2Rec);
            btnAnswer1XRed.setText(wrongPaintAns3Rec);

            btnAnswer4XGreen.setText(correctPaintAnsRec);


        }
    }

    private void userStatsSet() {

        txtCoinPaintCounterX.setText(coinsOwnedPaintString);

    }

    private void countdownPaintTimer(){

        paintTimer = new CountDownTimer(10000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

                double millisleftRough = millisUntilFinished / 1000;
                int millisleftFinal = (int) Math.round(millisleftRough);
                String timerCountString = millisleftFinal + "";
                txtPaintTimerX.setText(timerCountString);  // converting timer to string and setting the counter



                if (millisUntilFinished <500) {

                    imgPaintTimer5X.setVisibility(View.GONE);
                    imgPaintTimerX.setVisibility(View.VISIBLE);
                } else
                if (millisUntilFinished <3000) {

                    imgPaintTimer4X.setVisibility(View.GONE);
                    imgPaintTimer5X.setVisibility(View.VISIBLE);
                }else
                if (millisUntilFinished < 6000) {
                    imgPaintTimer3X.setVisibility(View.GONE);
                    imgPaintTimer4X.setVisibility(View.VISIBLE);
                } else
                if (millisUntilFinished < 8500) {

                    imgPaintTimer2X.setVisibility(View.GONE);
                    imgPaintTimer3X.setVisibility(View.VISIBLE);
                } else
                if (millisUntilFinished < 9500) {


                    imgPaintTimerX.setVisibility(View.GONE);
                    imgPaintTimer2X.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFinish() {

                btnAnswer1X.setVisibility(View.GONE);
                btnAnswer2X.setVisibility(View.GONE);
                btnAnswer3X.setVisibility(View.GONE);
                btnAnswer4X.setVisibility(View.GONE);

                btnAnswer1XRed.setVisibility(View.VISIBLE);
                btnAnswer2XRed.setVisibility(View.VISIBLE);
                btnAnswer3XRed.setVisibility(View.VISIBLE);
                btnAnswer4XRed.setVisibility(View.VISIBLE);

                CountDownTimer correctorPaintTimer = new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        paintExpandedAnswer();
                    }
                }.start();

            }
        }.start();



    }

    private void paintExpandedAnswer(){

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

        loutPaintGameButtonsX.setVisibility(View.VISIBLE);

        imgPaintTimerX.setVisibility(View.GONE);
        imgPaintTimer2X.setVisibility(View.GONE);
        imgPaintTimer3X.setVisibility(View.GONE);
        imgPaintTimer4X.setVisibility(View.GONE);
        imgPaintTimer5X.setVisibility(View.GONE);

        txtPaintTimerX.setVisibility(View.GONE);
        txtSetQuestionX.setVisibility(View.GONE);

        loutPaintExpandedX.setVisibility(View.VISIBLE);
        txtPaintExpandedAnswerShowX.setText(correctPaintAnsRec + " \n" +"------- \n" + paintExpandedRec);

    }

    private void grantPaintCoins() {

        coinsOwnedPaint = coinsOwnedPaint + 10;

        userPaintReference.getRef().child("coins").setValue(coinsOwnedPaint);
        int coinsOwnedSort = - coinsOwnedPaint;
        userPaintReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);

        grantPaintCoinsAnimation();


    }

    private void grantPaintCoinsAnimation(){

        imgGoldCoinsX.animate().scaleY(1.5f).scaleX(1.5f).setDuration(400);
        txtCoinPaintCounterX.setVisibility(View.GONE);

        imgCoin1X.animate().rotation(1440).setDuration(500);
        imgCoin2X.animate().rotation(1440).setDuration(750);
        imgCoin3X.animate().rotation(1440).setDuration(1000);
        imgCoin4X.animate().rotation(1440).setDuration(1250);
        imgCoin5X.animate().rotation(1440).setDuration(1500);
        imgCoin6X.animate().rotation(1440).setDuration(1750);

        CountDownTimer goldAwardTimer = new CountDownTimer(2000, 50) {
            @Override
            public void onTick(long millisUntilFinished) {

                if (millisUntilFinished <500) {

                    imgCoin6X.setVisibility(View.GONE);

                } else
                if (millisUntilFinished <750) {

                    imgCoin5X.setVisibility(View.GONE);
                }else
                if (millisUntilFinished < 1000) {
                    imgCoin4X.setVisibility(View.GONE);
                } else
                if (millisUntilFinished < 1250) {

                    imgCoin3X.setVisibility(View.GONE);
                } else
                if (millisUntilFinished < 1500) {
                    imgCoin2X.setVisibility(View.GONE);

                }

                if (millisUntilFinished < 1750) {
                    imgCoin1X.setVisibility(View.GONE);

                }



            }

            @Override
            public void onFinish() {

                String coinsOwedZ = Integer.toString(coinsOwnedPaint);
                txtCoinPaintCounterX.setText(coinsOwedZ);
                txtCoinPaintCounterX.setVisibility(View.VISIBLE);

                imgGoldCoinsX.animate().scaleY(1).scaleX(1).setDuration(400);

            }
        }.start();



    }

    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    public void onBackPressed(){

        // just leaving this blank so back press should just be a nothing

    }
}
