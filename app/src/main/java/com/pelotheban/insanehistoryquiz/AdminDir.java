package com.pelotheban.insanehistoryquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminDir extends AppCompatActivity {


    Button btnManualX;

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

    }
}
