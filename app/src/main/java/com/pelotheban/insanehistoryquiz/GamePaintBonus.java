package com.pelotheban.insanehistoryquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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

    private ScrollView scvAGPBX;
    private ViewTreeObserver vto;
    private int scrollToggle;

    private ImageView imgShowPaintingX, imgShowPaintingVerticalX;

        // for zoom
        Matrix matrix = new Matrix();
        Float scale = 1f;
        ScaleGestureDetector SGD;

    private LinearLayout loutShowPaintingX, loutShowPaintingVerticalX;
    private Button btnAnswer1X, btnAnswer2X, btnAnswer3X, btnAnswer4X;
    private Button btnAnswer1XRed, btnAnswer2XRed, btnAnswer3XRed, btnAnswer4XRed;
    private Button btnAnswer1XGreen, btnAnswer2XGreen, btnAnswer3XGreen, btnAnswer4XGreen;

    private ImageView imgGoldCoinsX;
    private TextView txtCoinPaintCounterX;

    private LinearLayout loutPaintGameButtonsX;
    private ImageView btnPaintProfileX, btnPaintPlayX, btnPaintLeaderX;
    private ImageView btnPaintProfileGlowX, btnPaintPlayGlowX, btnPaintLeaderGlowX;
    private TextView txtSetQuestionX;

    private LinearLayout loutPaintExpandedX;
    private TextView txtPaintExpandedAnswerShowX;

    private int height2;
    private int width2;

    private int randAnswer;

    private LinearLayout loutGoldCoinsX;
    private ImageView imgCoin1X, imgCoin2X, imgCoin3X, imgCoin4X, imgCoin5X, imgCoin6X, imgCoin7X, imgCoin8X, imgCoin9X, imgCoin10X;
    private TextView txtCoinPaintAdderX;

    String winner;
        // Timer related

        private CountDownTimer paintTimer;
        private TextView txtPaintTimerX;
        private ImageView imgPaintTimerX, imgPaintTimer2X, imgPaintTimer3X, imgPaintTimer4X, imgPaintTimer5X;

    //Firebase

    private DatabaseReference gamePaintReference;

    private String uid; // this is for the user account side
    private DatabaseReference userPaintReference;
    private Query sortUsersPaintQuery, paintQuestionQuery, paintingsQuery;
    private String coinsOwnedPaintString, artBonusWonString;
    private int coinsOwnedPaint, artBonusWon;
    private FirebaseAuth mAuthEA;

    private String correctPaintAnsRec, wrongPaintAns1Rec, wrongPaintAns2Rec, wrongPaintAns3Rec, paintExpandedRec;
    private String imagePaintLinkRec, orientationRec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_paint_bonus);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        double height = size.y;

        height2 = (int) Math.round(height);
        width2 = (int) Math.round(width);

        scvAGPBX = findViewById(R.id.scvAGPB);
        scrollToggle = 1;

        imgGoldCoinsX = findViewById(R.id.imgGoldCoins);
        txtCoinPaintCounterX = findViewById(R.id.txtCoinPaintCounter);
        txtSetQuestionX = findViewById(R.id.txtSetQuestion);
        winner = "no";

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

        if (width2 > 1500) { // changes in fot for tablet and then small format phone

            btnAnswer1X.setTextSize(25);
            btnAnswer2X.setTextSize(25);
            btnAnswer3X.setTextSize(25);
            btnAnswer4X.setTextSize(25);

            btnAnswer1XGreen.setTextSize(25);
            btnAnswer2XGreen.setTextSize(25);
            btnAnswer3XGreen.setTextSize(25);
            btnAnswer4XGreen.setTextSize(25);

            btnAnswer1XRed.setTextSize(25);
            btnAnswer2XRed.setTextSize(25);
            btnAnswer3XRed.setTextSize(25);
            btnAnswer4XRed.setTextSize(25);
        } else if (height2 < 1300) {

            btnAnswer1X.setTextSize(16);
            btnAnswer2X.setTextSize(16);
            btnAnswer3X.setTextSize(16);
            btnAnswer4X.setTextSize(16);

            btnAnswer1XGreen.setTextSize(16);
            btnAnswer2XGreen.setTextSize(16);
            btnAnswer3XGreen.setTextSize(16);
            btnAnswer4XGreen.setTextSize(16);

            btnAnswer1XRed.setTextSize(16);
            btnAnswer2XRed.setTextSize(16);
            btnAnswer3XRed.setTextSize(16);
            btnAnswer4XRed.setTextSize(16);
        }

        imgShowPaintingX = findViewById(R.id.imgShowPainting);
        imgShowPaintingVerticalX = findViewById(R.id.imgShowPaintingVertical);

            //for zoom
            SGD = new ScaleGestureDetector(this, new ScaleListener());


        loutShowPaintingX = findViewById(R.id.loutShowPainting);
        loutShowPaintingVerticalX = findViewById(R.id.loutShowPaintingVertical);

        loutPaintGameButtonsX = findViewById(R.id.loutGamePaintButtons);

        txtPaintTimerX = findViewById(R.id.txtPaintTimer);
        imgPaintTimerX = findViewById(R.id.imgPaintTimer);
        imgPaintTimer2X = findViewById(R.id.imgPaintTimer2);
        imgPaintTimer3X = findViewById(R.id.imgPaintTimer3);
        imgPaintTimer4X = findViewById(R.id.imgPaintTimer4);
        imgPaintTimer5X = findViewById(R.id.imgPaintTimer5);

        loutGoldCoinsX = findViewById(R.id.loutGoldCoins);
        txtCoinPaintAdderX = findViewById(R.id.txtCoinPaintAdder);
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
                    artBonusWon = 0; // this should be overwritten if get into the try
                    Log.i ("ARTBONUS", "initial assingment to ow: " + artBonusWon);

                    try {

                        artBonusWonString = paintUsers.child("artbonuswon").getValue().toString();
                        artBonusWon = Integer.parseInt(artBonusWonString);
                        Log.i ("ARTBONUS", "pulled from FB: " + artBonusWon);

                    } catch (Exception e) {

                    }

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

                    winner = "yes";
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

                CountDownTimer correctorPaintTimer = new CountDownTimer(1000, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        if (winner.equals("yes")) {
                            grantPaintCoins();
                        }

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
                    winner = "yes";

                    btnAnswer2X.setVisibility(View.GONE);
                    btnAnswer2XGreen.setVisibility(View.VISIBLE);


                } else {

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
                            grantPaintCoins();
                        }
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
                    winner = "yes";

                    btnAnswer3X.setVisibility(View.GONE);
                    btnAnswer3XGreen.setVisibility(View.VISIBLE);


                } else {

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
                            grantPaintCoins();
                        }
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
                    winner = "yes";

                    btnAnswer4X.setVisibility(View.GONE);
                    btnAnswer4XGreen.setVisibility(View.VISIBLE);


                } else {

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
                            grantPaintCoins();
                        }
                        paintExpandedAnswer();
                    }
                }.start();

            }
        });


        btnPaintProfileGlowX = findViewById(R.id.btnPaintProfileGlow);
        btnPaintProfileX = findViewById(R.id.btnPaintProfile);
        btnPaintProfileX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnPaintProfileX.setVisibility(View.GONE);
                btnPaintProfileGlowX.setVisibility(View.VISIBLE);

                CountDownTimer glowTimer = new CountDownTimer(500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        Intent intent = new Intent(GamePaintBonus.this, ProfileView.class);
                        startActivity(intent);
                        btnPaintProfileX.setVisibility(View.VISIBLE);
                        btnPaintProfileGlowX.setVisibility(View.GONE);

                    }
                }.start();

            }
        });

        btnPaintPlayGlowX = findViewById(R.id.btnPaintPlayGlow);
        btnPaintPlayX = findViewById(R.id.btnPaintPlay);
        btnPaintPlayX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnPaintPlayX.setVisibility(View.GONE);
                btnPaintPlayGlowX.setVisibility(View.VISIBLE);

                CountDownTimer glowTimer = new CountDownTimer(500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        Intent intent = new Intent(GamePaintBonus.this, Game.class);
                        startActivity(intent);


                    }
                }.start();



            }
        });

        btnPaintLeaderGlowX = findViewById(R.id.btnPaintLeadersGlow);
        btnPaintLeaderX = findViewById(R.id.btnPaintLeaders);
        btnPaintLeaderX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnPaintLeaderX.setVisibility(View.GONE);
                btnPaintLeaderGlowX.setVisibility(View.VISIBLE);

                CountDownTimer glowTimer = new CountDownTimer(500, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {

                        Intent intent = new Intent(GamePaintBonus.this, LeaderBoard.class);
                        startActivity(intent);
                        btnPaintLeaderX.setVisibility(View.VISIBLE);
                        btnPaintLeaderGlowX.setVisibility(View.GONE);

                    }
                }.start();

            }
        });
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            scale = scale * detector.getScaleFactor();
            scale = Math.max(0.1f, Math.min(scale, 5f));
            matrix.setScale(scale, scale);
            imgShowPaintingX.setImageMatrix(matrix);
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        SGD.onTouchEvent(event);
        return true;
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
            Log.i("SCROLLTAG", "in vertical; height = " + height2);

            if (width2 > 1500) { // changes in fot for tablet and then small format phone

                vto = scvAGPBX.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Log.i("SCROLLTAG", "scrolltoggle onGlobalLayout = " + scrollToggle);

                        scvAGPBX.post(new Runnable() {
                            @Override
                            public void run() {
                                if (scrollToggle == 1) {
                                    scvAGPBX.smoothScrollBy(0, 1000);
                                    scrollToggle = 2;
                                    Log.i("SCROLLTAG", "in IF post toggle = " + scrollToggle);
                                }


                            }
                        });

                    }
                });


            } else if (height2 < 1300) {

                // scvAGPBX.smoothScrollTo(0, imgShowPaintingVerticalX.getTop()); // did not work
               // scvAGPBX.smoothScrollBy(0, 200);

                vto = scvAGPBX.getViewTreeObserver();
                vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                     @Override
                     public void onGlobalLayout() {

                         Log.i("SCROLLTAG", "scrolltoggle onGlobalLayout = " + scrollToggle);

                         scvAGPBX.post(new Runnable() {
                             @Override
                             public void run() {
                                 if (scrollToggle == 1) {
                                     scvAGPBX.smoothScrollBy(0, 200);
                                     scrollToggle = 2;
                                     Log.i("SCROLLTAG", "in IF post toggle = " + scrollToggle);
                                 }


                             }
                         });

                     }
                });

                        Log.i("SCROLLTAG", "in height IF = " + height2);

            }

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

        paintTimer = new CountDownTimer(20000, 100) {
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
                if (millisUntilFinished <4000) {

                    imgPaintTimer4X.setVisibility(View.GONE);
                    imgPaintTimer5X.setVisibility(View.VISIBLE);
                }else
                if (millisUntilFinished < 10000) {
                    imgPaintTimer3X.setVisibility(View.GONE);
                    imgPaintTimer4X.setVisibility(View.VISIBLE);
                } else
                if (millisUntilFinished < 18500) {

                    imgPaintTimer2X.setVisibility(View.GONE);
                    imgPaintTimer3X.setVisibility(View.VISIBLE);
                } else
                if (millisUntilFinished < 19500) {


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

        if (winner.equals("no")) {

            loutPaintGameButtonsX.setVisibility(View.VISIBLE);

            CountDownTimer scrolltimer = new CountDownTimer(150, 50 ) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    scvAGPBX.smoothScrollBy(0,4000);
                }
            }.start();


        }

        imgPaintTimerX.setVisibility(View.GONE);
        imgPaintTimer2X.setVisibility(View.GONE);
        imgPaintTimer3X.setVisibility(View.GONE);
        imgPaintTimer4X.setVisibility(View.GONE);
        imgPaintTimer5X.setVisibility(View.GONE);

        txtPaintTimerX.setVisibility(View.GONE);
        txtSetQuestionX.setVisibility(View.GONE);

        loutPaintExpandedX.setVisibility(View.VISIBLE);

        if (width2 > 1500) { // changes in fot for tablet and then small format phone
            Log.i("EXPTAG", "width = " + width2);

            txtPaintExpandedAnswerShowX.setTextSize(30);
            txtPaintExpandedAnswerShowX.setText(correctPaintAnsRec + " \n" + "------- \n" + paintExpandedRec);


        } else if (height2 < 1300) {

            Log.i("EXPTAG", "height = " + height2);
            txtPaintExpandedAnswerShowX.setTextSize(17);
            txtPaintExpandedAnswerShowX.setText(correctPaintAnsRec + " \n" + "------- \n" + paintExpandedRec);


        } else {

            txtPaintExpandedAnswerShowX.setText(correctPaintAnsRec + " \n" + "------- \n" + paintExpandedRec);

        }

    }

    private void grantPaintCoins(){

        Log.i ("ARTBONUS", "" + artBonusWon);
        coinsOwnedPaint = coinsOwnedPaint + 10;
        artBonusWon = artBonusWon +1;


        userPaintReference.getRef().child("coins").setValue(coinsOwnedPaint);
        int coinsOwnedSort = - coinsOwnedPaint;
        userPaintReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);
        userPaintReference.getRef().child("artbonuswon").setValue(artBonusWon);
        Log.i ("ARTBONUS", "" + artBonusWon);


        scvAGPBX.fullScroll(View.FOCUS_UP);
        grantPaintCoinsAnimation();


    }

    private void grantPaintCoinsAnimation(){

        imgGoldCoinsX.animate().scaleY(1.5f).scaleX(1.5f).setDuration(400);
        txtCoinPaintCounterX.setVisibility(View.GONE);

        //loutGoldCoinsX.animate().scaleY(1.15f);
        txtCoinPaintAdderX.setVisibility(View.VISIBLE);

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

                        txtCoinPaintAdderX.setText("+10");
                        tickcounter = 10;
                    }


                } else
                if (millisUntilFinished <1100) {

                    imgCoin9X.setVisibility(View.GONE);
                    if (tickcounter == 8) {

                        txtCoinPaintAdderX.setText("+9");
                        tickcounter = 9;
                    }

                }else
                if (millisUntilFinished < 1350) {
                    imgCoin8X.setVisibility(View.GONE);
                    if (tickcounter == 7) {

                        txtCoinPaintAdderX.setText("+8");
                        tickcounter = 8;
                    }

                } else
                if (millisUntilFinished < 1600) {

                    imgCoin7X.setVisibility(View.GONE);
                    if (tickcounter == 6) {

                        txtCoinPaintAdderX.setText("+7");
                        tickcounter = 7;
                    }

                } else
                if (millisUntilFinished < 1850) {
                    imgCoin6X.setVisibility(View.GONE);
                    if (tickcounter == 5) {

                        txtCoinPaintAdderX.setText("+6");
                        tickcounter = 6;
                    }


                }

                if (millisUntilFinished < 2100) {
                    imgCoin5X.setVisibility(View.GONE);
                    if (tickcounter == 4) {

                        txtCoinPaintAdderX.setText("+5");
                        tickcounter = 5;
                    }


                }

                if (millisUntilFinished < 2350) {
                    imgCoin4X.setVisibility(View.GONE);
                    if (tickcounter == 3) {

                        txtCoinPaintAdderX.setText("+4");
                        tickcounter = 4;
                    }

                } else
                if (millisUntilFinished < 2600) {

                    imgCoin3X.setVisibility(View.GONE);
                    if (tickcounter == 2) {

                        txtCoinPaintAdderX.setText("+3");
                        tickcounter = 3;
                    }

                } else
                if (millisUntilFinished < 2850) {
                    imgCoin2X.setVisibility(View.GONE);
                    if (tickcounter == 1) {

                        txtCoinPaintAdderX.setText("+2");
                        tickcounter = 2;
                    }


                }

                if (millisUntilFinished < 3100) {
                    imgCoin1X.setVisibility(View.GONE);
                    if (tickcounter == 0) {

                        txtCoinPaintAdderX.setText("+1");
                        tickcounter = 1;
                    }

                }

            }

            @Override
            public void onFinish() {

                loutGoldCoinsX.animate().scaleY(1);
                txtCoinPaintAdderX.setVisibility(View.GONE);
                String coinsOwedZ = Integer.toString(coinsOwnedPaint);
                txtCoinPaintCounterX.setText(coinsOwedZ);
                txtCoinPaintCounterX.setVisibility(View.VISIBLE);

                imgGoldCoinsX.animate().scaleY(1).scaleX(1).setDuration(400);
                loutPaintGameButtonsX.setVisibility(View.VISIBLE);
                //scvAGPBX.fullScroll(View.FOCUS_DOWN);
                Log.i("SCROLLTAG", "Final toggle = " + scrollToggle);
                scvAGPBX.smoothScrollBy(0,2000);

                CountDownTimer scrolltimer = new CountDownTimer(450, 50 ) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        scvAGPBX.smoothScrollBy(0,2000);
                    }
                }.start();


            }
        }.start();



    }

    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    public void onBackPressed(){

        // just leaving this blank so back press should just be a nothing

    }
}
