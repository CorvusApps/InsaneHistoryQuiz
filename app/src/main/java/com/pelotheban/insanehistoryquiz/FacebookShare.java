package com.pelotheban.insanehistoryquiz;

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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.FileInputStream;

public class FacebookShare extends AppCompatActivity {

    ImageView imgScreenshotX;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Bitmap bmp;

//    Target target = new Target() {
//        @Override
//        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//
//            Log.i("FBShare", "onBitmapLoaded");
//
//            //Share photo
//            SharePhoto sharePhoto = new SharePhoto.Builder()
//                    .setBitmap(bitmap)
//                    .build();
//
//            //one solution that didn't work was removing this if but can bring it back in conjuction with others
//            if(ShareDialog.canShow(SharePhotoContent.class)){
//
//                Log.i("FBShare", "canShow");
//
//                SharePhotoContent content = new SharePhotoContent.Builder()
//                        .addPhoto(sharePhoto)
//                        .build();
//                shareDialog.show(content);
//           }
//
//        }
//
//        @Override
//        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//
//        }
//
//        @Override
//        public void onPrepareLoad(Drawable placeHolderDrawable) {
//
//        }
//    };



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
            imgScreenshotX.setImageBitmap(bmp);

        } catch (Exception e) {

        }


        //Init FB
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);



        imgScreenshotX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

            Toast.makeText(FacebookShare.this, "pressed",Toast.LENGTH_SHORT).show();

                //Create callback

                shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(FacebookShare.this, "share successful", Toast.LENGTH_LONG).show();
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

                //set pic for now from link
                //Picasso.get().load("https://en.wikipedia.org/wiki/Battle_of_Grunwald_(Matejko)#/media/File:Jan_Matejko,_Bitwa_pod_Grunwaldem.jpg").into(target);

            }
        });


    }
}
