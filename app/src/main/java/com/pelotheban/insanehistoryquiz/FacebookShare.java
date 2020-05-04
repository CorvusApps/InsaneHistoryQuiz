package com.pelotheban.insanehistoryquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
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

    // Firebase

    private DatabaseReference gameReference;
    private String uid; // this is for the user account side
    private DatabaseReference userReference;
    private Query sortUsersQuery;

    private String coinsOwnedString;
    private int coinsOwned;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        setContentView(R.layout.activity_facebook_share);

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

        //draw in coin amount

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
                        Toast.makeText(FacebookShare.this, "share successful", Toast.LENGTH_LONG).show();

                        coinsOwned = coinsOwned + 50;
                        userReference.getRef().child("coins").setValue(coinsOwned);
                        int coinsOwnedSort = - coinsOwned;
                        userReference.getRef().child("coinsownedsort").setValue(coinsOwnedSort);

                    }

                    @Override
                    public void onCancel() {

                        Toast.makeText(FacebookShare.this, "share cancel", Toast.LENGTH_LONG).show();

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
