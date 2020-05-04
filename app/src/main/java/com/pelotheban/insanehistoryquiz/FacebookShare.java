package com.pelotheban.insanehistoryquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.FileInputStream;

public class FacebookShare extends AppCompatActivity {

    ImageView imgScreenshotX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_share);

        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");

        try {

            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();

            imgScreenshotX = findViewById(R.id.imgScreenshot);
            imgScreenshotX.setImageBitmap(bmp);

        } catch (Exception e) {

        }






    }
}
