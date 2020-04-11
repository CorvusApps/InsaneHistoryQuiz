package com.pelotheban.insanehistoryquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AddBadges extends AppCompatActivity {

    private ImageView imgAddBadgeX;
    private Button btnImportBadgePicX, btnSaveBadgePicX;
    private Bitmap recievedBadgeImageBitmap;
    private ProgressDialog pd;
    private String badgeImageIdentifier;
    private String badgeImageLink;
    private EditText edtBadgeNameX;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_badges);



        imgAddBadgeX = findViewById(R.id.imgAddBadge);
        imgAddBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addBadgeImage();
            }
        });

        btnSaveBadgePicX = findViewById(R.id.btnSaveBadgePic);
        btnSaveBadgePicX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadBadgeImageToServer ();
            }
        });

        edtBadgeNameX = findViewById(R.id.edtBadgeName);
        edtBadgeNameX.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtBadgeNameX.setRawInputType(InputType.TYPE_CLASS_TEXT);


    }

    private void addBadgeImage() {

        //Toast.makeText(ProfileView.this, "Here", Toast.LENGTH_SHORT).show();

        if(ActivityCompat.checkSelfPermission(AddBadges.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE},
                    1000);
        }else {

            getChosenBadgeImage();

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1000) {

            if(grantResults.length >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){

                getChosenBadgeImage();

            }
        }
    }

    private void getChosenBadgeImage() {

        // gets image from internal storage - GALLERY
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2000) {

            if(resultCode == Activity.RESULT_OK) {

                //Do something with captured image

                try {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex); // path to the Bitmap created from pulled image
                    cursor.close();
                    recievedBadgeImageBitmap = BitmapFactory.decodeFile(picturePath);

                    // getting to the rotation wierdness from large files using the picturePath to id the file
                    int degree = 0;
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(picturePath);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    if (exif != null) {
                        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
                        if (orientation != -1) {
                            // We only recognise a subset of orientation tag values.
                            switch (orientation) {
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    degree = 90;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    degree = 180;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    degree = 270;
                                    break;
                            }

                        }
                    }

                    // resizing the image to a standard size that is easy on the storage
                    recievedBadgeImageBitmap = Bitmap.createScaledBitmap(recievedBadgeImageBitmap, 75,100,true);

                    // correcting the rotation on the resized file using the degree variable of how much to fix we got above
                    Bitmap bitmap = recievedBadgeImageBitmap;
                    Matrix matrix = new Matrix();
                    matrix.postRotate(degree);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);


                    imgAddBadgeX.setImageBitmap(bitmap);


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

        }

    }


    ////////////////////////////////SAVING IMAGE //////////////////////////////////////////////

    private void uploadBadgeImageToServer () {

        pd = new ProgressDialog(AddBadges.this,R.style.CustomAlertDialog);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

        // Get the data from an ImageView as bytes; does not crash when user does not select and image because takes default image provided by the app
        imgAddBadgeX.setDrawingCacheEnabled(true);
        imgAddBadgeX.buildDrawingCache();
        Bitmap bitmapColAdd = ((BitmapDrawable) imgAddBadgeX.getDrawable()).getBitmap(); // we already have the bitmap
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // create this bitmap using recieved to upload stock image if user did not upload theirs;
        bitmapColAdd.compress(Bitmap.CompressFormat.PNG, 25, baos);
        byte[] data = baos.toByteArray();

        badgeImageIdentifier = UUID.randomUUID() + ".png";   //initialized here because needs to be unique for each image but is random = unique??

        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("badgeimages")
                .child(badgeImageIdentifier).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads

                //makes the exception message an instance variable string that can be used in a custom dialog below

//                exceptions = exception.toString();
//                alertDialogException();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                // get the download link of the image uploaded to server
                taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    // apparently the onCompleteListener is to allow this to happen in the backround vs. UI thread
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()) {

                            badgeImageLink = task.getResult().toString();
                            pd.dismiss();

                            // setting up as separate method to let image upload finish before calling the put function which requires the imageLink
                            uploadimageinfo ();
                        }
                    }
                });
            }
        });

    }


    private void uploadimageinfo (){

        DatabaseReference badgeReference = FirebaseDatabase.getInstance().getReference().child("badges");

        DatabaseReference dbPushReference = badgeReference.push();
       // String badgeUidX = dbPushReference.getKey(); // this then is the key for the question

        //Toast.makeText(AddBadges.this,badgeImageIdentifier, Toast.LENGTH_LONG).show();

        String badgeName = edtBadgeNameX.getText().toString();

        HashMap<String, Object> dataMap = new HashMap<>();

        dataMap.put("badgeimagelink", badgeImageLink);
        dataMap.put("badgeimageidentifier", badgeImageIdentifier);
        dataMap.put("badgename", badgeName);

        dbPushReference.setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(AddBadges.this,"badge added", Toast.LENGTH_LONG).show();

            }
        });





    }

}
