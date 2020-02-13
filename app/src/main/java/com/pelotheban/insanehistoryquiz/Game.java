package com.pelotheban.insanehistoryquiz;

import android.os.Bundle;

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

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Game extends AppCompatActivity {

    // Game play variables, toggles, etc.

    private int randQuestion;
    private String displayAnswer, displayAnswer2, displayAnswer3, displayAnswer4, displayAnswer5; // will be randomly assigned to the answer options in each question
    private int answerCounter; // scrolls through how many answers options to a question the player has seen
    private int randAnswer;
    private Button btnCorrectX, btnWrongX;
    private TextView txtCoinCounterX;

    private TextView txtGameQuestionX, txtGameAnswerDisplayX, txtGameExpandedAnswerX;

    // Firebase

    private DatabaseReference gameReference;
    private Query sortGameQueryQuestions;

    private String uid; // this is for the user account side
    private DatabaseReference userReference;
    private Query sortUsersQuery;

    private String coinsOwnedString;
    private double coinsOwned;

    String GameCorrectAnswerY;
    String test;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Game play
        answerCounter = 1;
        randQuestion = new Random().nextInt(5) + 1; // random question number to be displayed

        txtGameQuestionX = findViewById(R.id.txtGameQuestion);
        txtGameAnswerDisplayX = findViewById(R.id.txtGameAnswerDisplay);
        txtCoinCounterX = findViewById(R.id.txtCoinCounter);

        //Firebase user

        uid = FirebaseAuth.getInstance().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid);

//        Toast.makeText(Game.this, uid, Toast.LENGTH_LONG).show();
         userReference.getRef().child("coins").setValue(80);
         userReference.getRef().child("user").setValue(uid);


        // add coins to account for now without checking if any there now

        sortUsersQuery = FirebaseDatabase.getInstance().getReference().child("my_users").orderByChild("user").equalTo(uid);
        sortUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot userDs: dataSnapshot.getChildren()) {

                    coinsOwnedString = userDs.child("coins").getValue().toString();

                    coinsOwned = Double.valueOf(coinsOwnedString);

                }

                Toast.makeText(Game.this, coinsOwnedString + " Coins", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        // Firebase game

        gameReference = FirebaseDatabase.getInstance().getReference().child("questions");
        sortGameQueryQuestions = gameReference.orderByChild("aaaqno").equalTo(randQuestion);

        // First firebase pull is on create showing the question and first answer (will add a time delay on the answer)

        sortGameQueryQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                for (DataSnapshot gameQs: dataSnapshot.getChildren()) {


                    String GameQuestionZ = gameQs.child("cccquestion").getValue().toString();
                    txtGameQuestionX.setText(GameQuestionZ);

                    String GameCorrectAnswerZ = gameQs.child("dddcorrectansw").getValue().toString();
                    String GameWrongAnswer1Z = gameQs.child("eeewrongans1").getValue().toString();
                    String GameWrongAnswer2Z = gameQs.child("fffwrongans2").getValue().toString();
                    String GameWrongAnswer3Z = gameQs.child("gggwrongans3").getValue().toString();
                    String GameWrongAnswer4Z = gameQs.child("hhhwrongans4").getValue().toString();

                    randAnswer = new Random().nextInt(5) + 1;
                    //Toast.makeText(Game.this, randAnswer + "", Toast.LENGTH_LONG).show();

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

                    } else if (answerCounter == 2) {
                        txtGameAnswerDisplayX.setText(displayAnswer2);
                    } else {

                        Toast.makeText(Game.this, "Out of numbers", Toast.LENGTH_SHORT).show();

                    }

                }

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

                    Toast.makeText(Game.this, "You got it", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(Game.this, "WRONG", Toast.LENGTH_LONG).show();
                }
            }
        });


        btnWrongX = findViewById(R.id.btnWrong);
        btnWrongX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (randAnswer == answerCounter) {

                    Toast.makeText(Game.this, "WRONG", Toast.LENGTH_LONG).show();
                } else {

                    Toast.makeText(Game.this, "You got it", Toast.LENGTH_LONG).show();
                }

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                answerCounter = answerCounter + 1;

                sortGameQueryQuestions.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        for (DataSnapshot gameQs: dataSnapshot.getChildren()) {


//                            String GameQuestionZ = gameQs.child("cccquestion").getValue().toString();
//                            txtGameQuestionX.setText(GameQuestionZ);

                            String GameCorrectAnswerZ = gameQs.child("dddcorrectansw").getValue().toString();
                            String GameWrongAnswer1Z = gameQs.child("eeewrongans1").getValue().toString();
                            String GameWrongAnswer2Z = gameQs.child("fffwrongans2").getValue().toString();
                            String GameWrongAnswer3Z = gameQs.child("gggwrongans3").getValue().toString();
                            String GameWrongAnswer4Z = gameQs.child("hhhwrongans4").getValue().toString();


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
        });
    }

}
