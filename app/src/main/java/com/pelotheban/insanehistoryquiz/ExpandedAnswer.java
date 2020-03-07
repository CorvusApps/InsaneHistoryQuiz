package com.pelotheban.insanehistoryquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ExpandedAnswer extends AppCompatActivity {

   private ImageButton btnPlayAgainX, btnEAProfileX, btnLeadersX;
   private AlertDialog dialog;
   private String ExpandedAnswerGet, ExpAnsCategoryGet, ExpAnsEpochGet;
   private TextView txtExpandedAnswerShowX, txtExpAnsCategoryX, txtExpAnsEpochX;
   private int expAnsBacgroundNo;

   private TextView txtEACoinCounterX, txtEAConStreakX;

   //Firebase

    private DatabaseReference gameReference;

    private String uid; // this is for the user account side
    private DatabaseReference userReference;
    private Query sortUsersQuery;
    private String coinsOwnedString, consStreakString;
    private int coinsOwned, consStreak;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expanded_answer);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        txtEACoinCounterX = findViewById(R.id.txtEACoinCounter);
        txtEAConStreakX = findViewById(R.id.txtEAConStreak);

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

        expAnsBacgroundNo = 1; //default setting for background in case we miss a category or don't have a pic for it

        ExpandedAnswerGet = getIntent().getStringExtra("iiiexpanded");
        txtExpandedAnswerShowX = findViewById(R.id.txtExpandedAnswerShow);
        txtExpandedAnswerShowX.setText(ExpandedAnswerGet);

        ExpAnsCategoryGet = getIntent().getStringExtra("bbbcategory");
        txtExpAnsCategoryX = findViewById(R.id.txtExpAnsCategory);
        txtExpAnsCategoryX.setText(ExpAnsCategoryGet);

        ExpAnsEpochGet = getIntent().getStringExtra("lllepoch");
        txtExpAnsEpochX = findViewById(R.id.txtExpAnsEpoch);
        txtExpAnsEpochX.setText(ExpAnsEpochGet);

        //////// Logic for background picture based on mix of cateogy and epoch //////////////////////////

        if (ExpAnsEpochGet.equals("Hellenistic") && ExpAnsCategoryGet.equals("Roman")) {

            expAnsBacgroundNo = 2;

        }

        // use the No to select a pic which is then set

        ////// end of logic for background pics ///////////////////////////////////////////


        btnPlayAgainX = findViewById(R.id.btnPlayAgain);
        btnPlayAgainX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ExpandedAnswer.this, Game.class);
                startActivity(intent);


            }
        });

        btnEAProfileX = findViewById(R.id.btnEAProfile);
        btnEAProfileX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ExpandedAnswer.this, ProfileView.class);
                startActivity(intent);

            }
        });

        btnLeadersX = findViewById(R.id.btnEALeaders);
        btnLeadersX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ExpandedAnswer.this, LeaderBoard.class);
                startActivity(intent);

            }
        });


    }

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

}
