package com.pelotheban.insanehistoryquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class GamePaintBonus extends AppCompatActivity {

    String badgeIDRec;

    // UI Elements

    ImageView imgShowPaintingX, imgShowPaintingVerticalX;
    Button btnAnswer1X, btnAnswer2X, btnAnswer3X, btnAnswer4X;

    //Firebase

    private DatabaseReference gamePaintReference;

    private String uid; // this is for the user account side
    private DatabaseReference userPaintReference;
    private Query sortUsersPaintQuery, paintQuestionQuery;
    private String coinsOwnedPaintString;
    private int coinsPaintOwned;
    private FirebaseAuth mAuthEA;

    private String correctPaintAnsRec, wrongPaintAns1Rec, wrongPaintAns2Rec, wrongPaintAns3Rec, paintExpandedRec;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_paint_bonus);

        badgeIDRec = getIntent().getStringExtra("badgeid");
        btnAnswer1X = findViewById(R.id.btnAnswer1);
        btnAnswer2X = findViewById(R.id.btnAnswer2);
        btnAnswer3X = findViewById(R.id.btnAnswer3);
        btnAnswer4X = findViewById(R.id.btnAnswer4);

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

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void buttonsSet(){

       int randAnswer = new Random().nextInt(4) + 1;

        if (randAnswer == 1) {

            btnAnswer1X.setText(correctPaintAnsRec);
            btnAnswer2X.setText(wrongPaintAns1Rec);
            btnAnswer3X.setText(wrongPaintAns2Rec);
            btnAnswer4X.setText(wrongPaintAns3Rec);


        } else if (randAnswer == 2) {

            btnAnswer2X.setText(correctPaintAnsRec);
            btnAnswer4X.setText(wrongPaintAns1Rec);
            btnAnswer3X.setText(wrongPaintAns2Rec);
            btnAnswer1X.setText(wrongPaintAns3Rec);


        } else if (randAnswer == 3) {

            btnAnswer3X.setText(correctPaintAnsRec);
            btnAnswer1X.setText(wrongPaintAns1Rec);
            btnAnswer2X.setText(wrongPaintAns2Rec);
            btnAnswer4X.setText(wrongPaintAns3Rec);


        } else if (randAnswer == 4) {

            btnAnswer4X.setText(correctPaintAnsRec);
            btnAnswer3X.setText(wrongPaintAns1Rec);
            btnAnswer2X.setText(wrongPaintAns2Rec);
            btnAnswer1X.setText(wrongPaintAns3Rec);

        }


    }
}
