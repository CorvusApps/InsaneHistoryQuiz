package com.pelotheban.insanehistoryquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileInputStream;

public class FacebookShare extends AppCompatActivity {

    ImageView imgScreenshotX;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Bitmap bmp;

    private AlertDialog dialog;
    private int width2;
    private String source;

    // Firebase

    private DatabaseReference gameReference;
    private String uid; // this is for the user account side
    private DatabaseReference userReference;
    private Query sortUsersQuery;

    private String coinsOwnedString;
    private int coinsOwned;

    private String fbSharesString;
    private int fbSharers;

    // for transfer back to home page
    private String badgeSortKeyFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_facebook_share);

        source = getIntent().getStringExtra("source");
        //if coming from the badte screen need to know which badgeid to pass to home page for the bonus question
        badgeSortKeyFB = getIntent().getStringExtra("badgeid");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        width2 = (int) Math.round(width);

        // Bring in the pic to share
        bmp = null;
        String filename = getIntent().getStringExtra("image");

        try {

            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();

            imgScreenshotX = findViewById(R.id.imgScreenshot);
           // imgScreenshotX.setImageBitmap(bmp);

        } catch (Exception e) {

        }

        //draw in coin amount and fbshares

        uid = FirebaseAuth.getInstance().getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(uid);
        sortUsersQuery = FirebaseDatabase.getInstance().getReference().child("my_users").orderByChild("user").equalTo(uid);
        sortUsersQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userDs : dataSnapshot.getChildren()){

                    try {
                        coinsOwnedString = userDs.child("coins").getValue().toString();
                        coinsOwned = Integer.valueOf(coinsOwnedString);

                    } catch (Exception e) {

                    }

                    try {
                        fbSharesString = userDs.child("fbshares").getValue().toString();
                        fbSharers = Integer.valueOf(fbSharesString);

                    } catch (Exception e) {

                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        //Init FB
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);



//        imgScreenshotX.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

                //Share photo
                SharePhoto sharePhoto = new SharePhoto.Builder()
                        .setBitmap(bmp)
                        .setCaption("Insane History Quiz - Question and Answer - Click to expand")
                        .build();

                if(ShareDialog.canShow(SharePhotoContent.class)){

                    Log.i("FBShare", "canShow");

                    SharePhotoContent content = new SharePhotoContent.Builder()
                            .addPhoto(sharePhoto)
                            .build();
                    shareDialog.show(content);
                   // shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
                }



                //Create callback
                // need override function below on-create to make this work
                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Log.i("FBShare", "onSuccess");

                        coinsOwned = coinsOwned + 50;
                        userReference.getRef().child("coins").setValue(coinsOwned);
                        int coinsOwnedSort = - coinsOwned;
                        userReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);
                        fbSharers = fbSharers + 1;
                        userReference.getRef().child("fbshares").setValue(fbSharers);
                        int fbSharesSort = - fbSharers;
                        userReference.getRef().child("fbshaaressort").setValue(fbSharesSort);


                        // reset insterstitial counter to 0 sharedpref to be picked up by game.java
                        Log.i("INTERSTITIAL", "FB setting to 0");
                        SharedPreferences sharedAdvertCounterGame;
                        sharedAdvertCounterGame = getSharedPreferences("adSettingGame", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedAdvertCounterGame.edit();
                        editor.putInt("CounterGame", 0);
                        editor.apply(); // saves the value

                        LayoutInflater inflater = LayoutInflater.from(FacebookShare.this);
                        View view = inflater.inflate(R.layout.zzz_fbreward_dialog, null);

                        dialog = new AlertDialog.Builder(FacebookShare.this)
                                .setView(view)
                                .create();

                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialog.show();
                        double dialogWidth = width2*.75;
                        int dialogWidthFinal = (int) Math.round(dialogWidth);
                        double dialogHeight = dialogWidthFinal*1.5;
                        int dialogHeightFinal = (int) Math.round(dialogHeight);

                        dialog.getWindow().setLayout(dialogWidthFinal, dialogHeightFinal);

                        CountDownTimer dialogTimer = new CountDownTimer(2000, 10000) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {

                                dialog.dismiss();
                                if (source.equals("profile")){

                                    Intent intent = new Intent(FacebookShare.this, ProfileView.class);
                                    startActivity(intent);
                                    finish();


                                } else if (source.equals("expanded")) {

                                    Intent intent = new Intent(FacebookShare.this, HomePage.class);
                                    startActivity(intent);
                                    finish();

                                } else if (source.equals("leaderboard")){

                                    Intent intent = new Intent(FacebookShare.this, LeaderBoard.class);
                                    startActivity(intent);
                                    finish();

                                } else if(source.equals("badge")){

                                    Intent intent = new Intent(FacebookShare.this, HomePage.class);
                                    intent.putExtra("badgeid", badgeSortKeyFB);
                                    startActivity(intent);
                                    finish();

                                }

                            }
                        }.start();

                    }

                    @Override
                    public void onCancel() {

                        //Toast.makeText(FacebookShare.this, "share cancel", Toast.LENGTH_LONG).show();
                        if (source.equals("profile")){

                            Intent intent = new Intent(FacebookShare.this, ProfileView.class);
                            startActivity(intent);
                            finish();


                        } else if (source.equals("expanded")) {

                            Intent intent = new Intent(FacebookShare.this, HomePage.class);
                            startActivity(intent);
                            finish();

                        } else if (source.equals("leaderboard")){

                            Intent intent = new Intent(FacebookShare.this, LeaderBoard.class);
                            startActivity(intent);
                            finish();

                        } else if(source.equals("badge")){

                            Intent intent = new Intent(FacebookShare.this, HomePage.class);
                            intent.putExtra("badgeid", badgeSortKeyFB);
                            startActivity(intent);
                            finish();

                        }

                    }

                    @Override
                    public void onError(FacebookException error) {

                        Toast.makeText(FacebookShare.this, error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });


//            }
//        });


    }

    @Override // need this for the facebook callback to work
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
