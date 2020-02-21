package com.pelotheban.insanehistoryquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
