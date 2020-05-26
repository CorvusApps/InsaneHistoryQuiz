package com.pelotheban.insanehistoryquiz;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class GameVillain extends AppCompatActivity implements View.OnClickListener {


    // shared preferences to / from Expanded
    private SharedPreferences sharedVillainGameplayCounter;
    private int villainGameplayCounter;

    //tiles
    private ImageView imgTile1X, imgTile2X, imgTile3X, imgTile4X, imgTile5X, imgTile6X, imgTile7X, imgTile8X, imgTile9X, imgTile10X,
                         imgTile11X, imgTile12X, imgTile13X, imgTile14X, imgTile15X, imgTile16X, imgTile17X, imgTile18X, imgTile19X, imgTile20X,
                         imgTile21X, imgTile22X, imgTile23X, imgTile24X, imgTile25X;

    private int tile1pressed, tile2pressed, tile3pressed, tile4pressed, tile5pressed, tile6pressed, tile7pressed, tile8pressed, tile9pressed,
                tile10pressed, tile11pressed, tile12pressed, tile13pressed, tile14pressed, tile15pressed, tile16pressed, tile17pressed, tile18pressed,
                 tile19pressed, tile20pressed, tile21pressed, tile22pressed, tile23pressed, tile24pressed, tile25pressed, tile26pressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_villain);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // villain gameplay counter
        sharedVillainGameplayCounter = getSharedPreferences("villainCounter", MODE_PRIVATE);

            // set the counter back to zero right away
            villainGameplayCounter = 0;
            SharedPreferences.Editor editor = sharedVillainGameplayCounter.edit();
            editor.putInt("CounterVillainGameplay", villainGameplayCounter);
            editor.apply(); // saves the value

        // tiles

        imgTile1X = findViewById(R.id.imgTile1);
        imgTile1X.setOnClickListener(this);
        imgTile2X = findViewById(R.id.imgTile2);
        imgTile2X.setOnClickListener(this);
        imgTile3X = findViewById(R.id.imgTile3);
        imgTile3X.setOnClickListener(this);
        imgTile4X = findViewById(R.id.imgTile4);
        imgTile4X.setOnClickListener(this);
        imgTile5X = findViewById(R.id.imgTile5);
        imgTile5X.setOnClickListener(this);
        imgTile6X = findViewById(R.id.imgTile6);
        imgTile6X.setOnClickListener(this);
        imgTile7X = findViewById(R.id.imgTile7);
        imgTile7X.setOnClickListener(this);
        imgTile8X = findViewById(R.id.imgTile8);
        imgTile8X.setOnClickListener(this);
        imgTile9X = findViewById(R.id.imgTile9);
        imgTile9X.setOnClickListener(this);
        imgTile10X = findViewById(R.id.imgTile10);
        imgTile10X.setOnClickListener(this);
        imgTile11X = findViewById(R.id.imgTile11);
        imgTile11X.setOnClickListener(this);
        imgTile12X = findViewById(R.id.imgTile12);
        imgTile12X.setOnClickListener(this);
        imgTile13X = findViewById(R.id.imgTile13);
        imgTile13X.setOnClickListener(this);
        imgTile14X = findViewById(R.id.imgTile14);
        imgTile14X.setOnClickListener(this);
        imgTile15X = findViewById(R.id.imgTile15);
        imgTile15X.setOnClickListener(this);
        imgTile16X = findViewById(R.id.imgTile16);
        imgTile16X.setOnClickListener(this);
        imgTile17X = findViewById(R.id.imgTile17);
        imgTile17X.setOnClickListener(this);
        imgTile18X = findViewById(R.id.imgTile18);
        imgTile18X.setOnClickListener(this);
        imgTile19X = findViewById(R.id.imgTile19);
        imgTile19X.setOnClickListener(this);
        imgTile20X = findViewById(R.id.imgTile20);
        imgTile20X.setOnClickListener(this);
        imgTile21X = findViewById(R.id.imgTile21);
        imgTile21X.setOnClickListener(this);
        imgTile22X = findViewById(R.id.imgTile22);
        imgTile22X.setOnClickListener(this);
        imgTile23X = findViewById(R.id.imgTile23);
        imgTile23X.setOnClickListener(this);
        imgTile24X = findViewById(R.id.imgTile24);
        imgTile24X.setOnClickListener(this);
        imgTile25X = findViewById(R.id.imgTile25);
        imgTile25X.setOnClickListener(this);

        tile1pressed = 1;
        tile2pressed = 1;
        tile3pressed = 1;
        tile4pressed = 1;
        tile5pressed = 1;
        tile6pressed = 1;
        tile7pressed = 1;
        tile8pressed = 1;
        tile9pressed = 1;
        tile10pressed = 1;
        tile11pressed = 1;
        tile12pressed = 1;
        tile13pressed = 1;
        tile14pressed = 1;
        tile15pressed = 1;
        tile16pressed = 1;
        tile17pressed = 1;
        tile18pressed = 1;
        tile19pressed = 1;
        tile20pressed = 1;
        tile21pressed = 1;
        tile22pressed = 1;
        tile23pressed = 1;
        tile24pressed = 1;
        tile25pressed = 1;

    }

    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    public void onBackPressed(){

        // will ultimately get this to a dialog with exit the question but lose 10 point

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.imgTile1:

                if (tile1pressed == 1) {
                    imgTile1X.animate().alpha(0f).setDuration(1000);
                    imgTile1X.animate().rotationY(180).setDuration(800);
                    tile1pressed = 2;
                }

                break;

            case R.id.imgTile2:

                if (tile2pressed == 1) {
                    imgTile2X.animate().alpha(0f).setDuration(1000);
                    imgTile2X.animate().rotationY(180).setDuration(800);
                    tile2pressed = 2;
                }

                break;

            case R.id.imgTile3:

                if (tile3pressed == 1) {
                    imgTile3X.animate().alpha(0f).setDuration(1000);
                    imgTile3X.animate().rotationY(180).setDuration(800);
                    tile3pressed = 2;
                }

                break;

            case R.id.imgTile4:

                if (tile4pressed == 1) {
                    imgTile4X.animate().alpha(0f).setDuration(1000);
                    imgTile4X.animate().rotationY(180).setDuration(800);
                    tile4pressed = 2;
                }

                break;

            case R.id.imgTile5:

                if (tile5pressed == 1) {
                    imgTile5X.animate().alpha(0f).setDuration(1000);
                    imgTile5X.animate().rotationY(180).setDuration(800);
                    tile5pressed = 2;
                }

                break;

            case R.id.imgTile6:

                if (tile6pressed == 1) {
                    imgTile6X.animate().alpha(0f).setDuration(1000);
                    imgTile6X.animate().rotationY(180).setDuration(800);
                    tile6pressed = 2;
                }

                break;

            case R.id.imgTile7:

                if (tile7pressed == 1) {
                    imgTile7X.animate().alpha(0f).setDuration(1000);
                    imgTile7X.animate().rotationY(180).setDuration(800);
                    tile7pressed = 2;
                }

                break;

            case R.id.imgTile8:

                if (tile8pressed == 1) {
                    imgTile8X.animate().alpha(0f).setDuration(1000);
                    imgTile8X.animate().rotationY(180).setDuration(800);
                    tile8pressed = 2;
                }

                break;

            case R.id.imgTile9:

                if (tile9pressed == 1) {
                    imgTile9X.animate().alpha(0f).setDuration(1000);
                    imgTile9X.animate().rotationY(180).setDuration(800);
                    tile9pressed = 2;
                }

                break;

            case R.id.imgTile10:

                if (tile10pressed == 1) {
                    imgTile10X.animate().alpha(0f).setDuration(1000);
                    imgTile10X.animate().rotationY(180).setDuration(800);
                    tile10pressed = 2;
                }

                break;

            case R.id.imgTile11:

                if (tile11pressed == 1) {
                    imgTile11X.animate().alpha(0f).setDuration(1000);
                    imgTile11X.animate().rotationY(180).setDuration(800);
                    tile11pressed = 2;
                }

                break;

            case R.id.imgTile12:

                if (tile12pressed == 1) {
                    imgTile12X.animate().alpha(0f).setDuration(1000);
                    imgTile12X.animate().rotationY(180).setDuration(800);
                    tile12pressed = 2;
                }

                break;

            case R.id.imgTile13:

                if (tile13pressed == 1) {
                    imgTile13X.animate().alpha(0f).setDuration(1000);
                    imgTile13X.animate().rotationY(180).setDuration(800);
                    tile13pressed = 2;
                }

                break;

            case R.id.imgTile14:

                if (tile14pressed == 1) {
                    imgTile14X.animate().alpha(0f).setDuration(1000);
                    imgTile14X.animate().rotationY(180).setDuration(800);
                    tile14pressed = 2;
                }

                break;

            case R.id.imgTile15:

                if (tile15pressed == 1) {
                    imgTile15X.animate().alpha(0f).setDuration(1000);
                    imgTile15X.animate().rotationY(180).setDuration(800);
                    tile15pressed = 2;
                }

                break;

            case R.id.imgTile16:

                if (tile16pressed == 1) {
                    imgTile16X.animate().alpha(0f).setDuration(1000);
                    imgTile16X.animate().rotationY(180).setDuration(800);
                    tile16pressed = 2;
                }

                break;

            case R.id.imgTile17:

                if (tile17pressed == 1) {
                    imgTile17X.animate().alpha(0f).setDuration(1000);
                    imgTile17X.animate().rotationY(180).setDuration(800);
                    tile17pressed = 2;
                }

                break;

            case R.id.imgTile18:

                if (tile18pressed == 1) {
                    imgTile18X.animate().alpha(0f).setDuration(1000);
                    imgTile18X.animate().rotationY(180).setDuration(800);
                    tile18pressed = 2;
                }

                break;

            case R.id.imgTile19:

                if (tile19pressed == 1) {
                    imgTile19X.animate().alpha(0f).setDuration(1000);
                    imgTile19X.animate().rotationY(180).setDuration(800);
                    tile19pressed = 2;
                }

                break;

            case R.id.imgTile20:

                if (tile20pressed == 1) {
                    imgTile20X.animate().alpha(0f).setDuration(1000);
                    imgTile20X.animate().rotationY(180).setDuration(800);
                    tile20pressed = 2;
                }

                break;

            case R.id.imgTile21:

                if (tile21pressed == 1) {
                    imgTile21X.animate().alpha(0f).setDuration(1000);
                    imgTile21X.animate().rotationY(180).setDuration(800);
                    tile21pressed = 2;
                }

                break;

            case R.id.imgTile22:

                if (tile22pressed == 1) {
                    imgTile22X.animate().alpha(0f).setDuration(1000);
                    imgTile22X.animate().rotationY(180).setDuration(800);
                    tile22pressed = 2;
                }

                break;

            case R.id.imgTile23:

                if (tile23pressed == 1) {
                    imgTile23X.animate().alpha(0f).setDuration(1000);
                    imgTile23X.animate().rotationY(180).setDuration(800);
                    tile23pressed = 2;
                }

                break;

            case R.id.imgTile24:

                if (tile24pressed == 1) {
                    imgTile24X.animate().alpha(0f).setDuration(1000);
                    imgTile24X.animate().rotationY(180).setDuration(800);
                    tile24pressed = 2;
                }

                break;

            case R.id.imgTile25:

                if (tile25pressed == 1) {
                    imgTile25X.animate().alpha(0f).setDuration(1000);
                    imgTile25X.animate().rotationY(180).setDuration(800);
                    tile25pressed = 2;
                }

                break;

        }

    }
}
