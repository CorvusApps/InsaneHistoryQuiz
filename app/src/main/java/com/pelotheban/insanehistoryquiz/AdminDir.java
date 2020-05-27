package com.pelotheban.insanehistoryquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminDir extends AppCompatActivity {


    Button btnManualX, btnExcelX, btnReviewX, btnAddBadgesX, btnAddPanelsX, btnAddPaintingsX, btnAddArtQuestionsManualX, btnAddArtQuestionsExcelX;
    Button btnAddVillainsX, btnAddVillainQuestionsManualX, btnAddVillainQuestionsExcelX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dir);

        btnManualX = findViewById(R.id.btnManual);
        btnManualX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminDir.this, ManualQInput.class);
                startActivity(intent);
            }
        });

        btnExcelX = findViewById(R.id.btnExcel);
        btnExcelX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminDir.this, Excel.class);
                startActivity(intent);

            }
        });

        btnReviewX = findViewById(R.id.btnReview);
        btnReviewX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminDir.this, QuestionList.class);
                startActivity(intent);

            }
        });

        btnAddBadgesX = findViewById(R.id.btnAddBadges);
        btnAddBadgesX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDir.this, AddBadges.class);
                startActivity(intent);
            }
        });

        btnAddPanelsX = findViewById(R.id.btnAddPanels);
        btnAddPanelsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (AdminDir.this, AddPanels.class);
                startActivity(intent);
            }
        });

        btnAddPaintingsX = findViewById(R.id.btnAddPaintings);
        btnAddPaintingsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminDir.this, AddPaintings.class);
                startActivity(intent);

            }
        });

        btnAddArtQuestionsManualX = findViewById(R.id.btnAddArtQuestionsManual);
        btnAddArtQuestionsManualX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminDir.this, ManualPaintQInput.class);
                startActivity(intent);

            }
        });

        btnAddArtQuestionsExcelX = findViewById(R.id.btnAddArtQuestionsExcel);
        btnAddArtQuestionsExcelX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdminDir.this, ExcelPaint.class);
                startActivity(intent);

            }
        });

        btnAddVillainsX = findViewById(R.id.btnAddVillains);
        btnAddVillainsX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminDir.this, AddVillains.class);
                startActivity(intent);

            }
        });

        btnAddVillainQuestionsManualX = findViewById(R.id.btnAddVillainQuestionsManual);
        btnAddVillainQuestionsManualX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminDir.this, ManualVillainQInput.class);
                startActivity(intent);

            }
        });

        btnAddVillainQuestionsExcelX = findViewById(R.id.btnAddVillainQuestionsExcel);
        btnAddVillainQuestionsExcelX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminDir.this, ExcelVillain.class);
                startActivity(intent);

            }
        });

    }
}
