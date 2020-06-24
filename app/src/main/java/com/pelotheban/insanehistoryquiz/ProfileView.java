package com.pelotheban.insanehistoryquiz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.OnCountryPickerListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class ProfileView extends AppCompatActivity implements OnCountryPickerListener {

   // Basic set up

    private LinearLayout loutProfileViewX;

    private TextView txtUIDX, txtProfileNameX;
    private int height2;
    private int width2;

   ///Firebase

   private String userUID, profileName, countryName;
   private DatabaseReference profileReference;
   private Query profileQuery;

   private FirebaseAuth mAuthPF;

   // PopUp

    private AlertDialog dialog;

    private String popupMenuToggle;
    private FloatingActionButton fabPopUpPFX, fabPopUpCollPFX, fabPopUpFAQminiPFtX, fabPopUpLogOutminiPFX, fabPopUpPremiumminiPFX;
    private TextView txtFAQButtonPFX, txtLogoutButtonPFX, txtPremiumButtonPFX;
    private View shadeX; // to shade the background when menu out

    // Tracker panel UI Elements

    private TextView pfTxtCoinCounterX, pfTxtConStreakX, pfTxtTotalAnswersX, pfTxtLongestStreakX, pfTxtTotalQuestionsX, pfCorrectPercentX;
    private String coinCounterString, conStreakString, totalAnswersString, longestStreakString, totalQuestionsString, correctPercentString;
    private int coinCounter, conStreak, totalAnswers, longestStreak, totalQuestions;
    private Double correctPercent;

    //Profile inputs and outputs

    private String attemptedBackToggle; // need a way to know if tried and failed to back out because after that even if alteredState toggles are "no" don't have a page to back out to.
                                        // also need it for
    private ImageView imgProfileX, imgProfileGlowX, imgProfilePicEditX, imgProfilePicSaveX;
    private Bitmap recievedProfileImageBitmap;
    private ProgressDialog pd;
    private String imageIdentifier, imageLink, imageLinkToDelete;
    private String imagePresentToggle, alteredPicToggle;


    private EditText edtProfileNameX;
    private ImageView imgProfileNameEditX, imgProfileNameSaveX;
    private String alteredNameToggle;

    public CountryPicker countryPicker;
    private ImageView imgFlagX, imgFlagEditX, imgFlagSaveX;
    private String imageFlagIdentifier, imageFlagLink, imageFlagLinkToDelete;
    private String imageFlagPresentToggle, alteredFlagPicToggle;
    private TextView txtCountryX;



   //Badges
        // primary badges
    private ImageView imgSupporterBadgeX, imgLongStreakBadgeX, imgTotalAnsweredBadgeX, imgWinningPercentageBadgeX; // badge display

    private String supporterBadgeLevelString, longestStreakBadgeLevelString, totalAnsweredBadgeLevelString, winningPerentageBadgeLevelString;// pull from userQuery
    private int supporterBadgeLevel, longestStreakBadgeLevel, totalAnsweredBadgeLevel, winningPerentageBadgeLevel; // convert to int for manipulation
    private String supporterBadge, longestStreakBadge, totalAnsweredBadge, winningPercentageBadge; // to find right badge details in badgeQuery

        // category master badges
    private ImageView imgAntiquityBadgeX, imgMedievalBadgeX, imgRenaissanceBadgeX, imgEnlightenmentBadgeX, imgModernBadgeX, imgContemporaryBadgeX;

    private String antiquityBadgeLevelString, medievalBadgeLevelString, renaissanceBadgeLevelString,
            enlightenmentBadgeLevelString, modernBadgeLevelString, contemporaryBadgeLevelString;
    private int antiquityBadgeLevel, medievalBadgeLevel, renaissanceBadgeLevel, enlightenmentBadgeLevel, modernBadgeLevel, contemporaryBadgeLevel;
    private String antiquityBadge, medievalBadge, renaissanceBadge, enlightenmentBadge, modernBadge, contemporaryBadge;

        // Ancilary badges

    private ImageView imgArtBonusBadgeX, imgFBShareBadgeX, imgVillainsRevealedBadgeX, imgPremiumBadgeX;
    private String artBonusBadgeLevelString, fbShareBadgeLevelString, villainsRevealedString, premiumLevelString;
    private int artBonusBadgeLevel, fbShareBadgeLevel, villainsRevealedLevel, premiumLevel;
    private String artBonusBadge, fbShareBadge, villainsRevealedBadge, premiumBadge;

        // badge views

    private String badgeView;
    private LinearLayout loutProfileBadgesX;
    private TextView txtProfileBadgeMessageX;
    private ImageView imgProfileBadgeAwardX;
    private String badgeSortkey;
    private String badgeImageLink;
    private String badgeProfileAwardMsg;

    // Facebook Share

    private FloatingActionButton fabSharePFX;

    private ImageView imgFBShareX, imgFBShareGlowX, imgNoFBShareX;
    private TextView txtDialogSpacerX;


        //screenshot
        private ImageView imgTestScreenshotX;
        private View main;
        private String filename;

    // premium

    private String showPremiumDialogToggle2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);//pop up

        // Basic Set Up
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        double height = size.y;

        height2 = (int) Math.round(height);
        width2 = (int) Math.round(width);

        loutProfileViewX = findViewById(R.id.loutProfileView);
        mAuthPF = FirebaseAuth.getInstance();

        // Tracker panel UI Elements

        pfTxtCoinCounterX = findViewById(R.id.pfTxtCoinCounter);
        pfTxtConStreakX = findViewById(R.id.pfTxtConStreak);
        pfTxtTotalAnswersX = findViewById(R.id.pfTxtTotalAnswers);
        pfTxtLongestStreakX = findViewById(R.id.pfTxtLongestStreak);
        pfTxtTotalQuestionsX = findViewById(R.id.pfTxtTotalQuestions);
        pfCorrectPercentX = findViewById(R.id.pfCorrectPercent);

        // ////////////////// Profile inputs and outputs  BEGINS ///////////////////////////////////////////////////

        alteredPicToggle = "no";
        alteredNameToggle = "no";
        alteredFlagPicToggle = "no";
        attemptedBackToggle = "no";

        imgProfilePicSaveX = findViewById(R.id.imgProfilePicSave);

        imgProfileGlowX = findViewById(R.id.imgProfileGlow);
        imgProfileX = findViewById(R.id.imgProfile);
        imgProfileX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imgProfileX.setVisibility(View.GONE);
                imgProfileGlowX.setVisibility(View.VISIBLE);

                CountDownTimer profileTimer = new CountDownTimer(300, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {


                        imgProfileGlowX.setVisibility(View.GONE);
                        imgProfileX.setVisibility(View.VISIBLE);
                        addImage();
                    }
                }.start();


            }
        });

        imgProfilePicEditX = findViewById(R.id.imgProfilePicEdit);
        imgProfilePicEditX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imgProfileX.setVisibility(View.GONE);
                imgProfileGlowX.setVisibility(View.VISIBLE);

                CountDownTimer profileTimer = new CountDownTimer(300, 100) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {


                        imgProfileGlowX.setVisibility(View.GONE);
                        imgProfileX.setVisibility(View.VISIBLE);
                        addImage();
                    }
                }.start();
            }
        });

        imgProfilePicSaveX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImageToServer ();

            }
        });

        edtProfileNameX = findViewById(R.id.edtProfileName);
        edtProfileNameX.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtProfileNameX.setRawInputType(InputType.TYPE_CLASS_TEXT);


        imgProfileNameEditX = findViewById(R.id.imgProfileNameEdit);
        imgProfileNameSaveX = findViewById(R.id.imgProfileNameSave);

        txtProfileNameX = findViewById(R.id.txtProfileName);

        if (width2 > 1500) { // changes in fot for tablet and then small format phone

            txtProfileNameX.setTextSize(30);
            imgProfileNameEditX.animate().translationY(50);
            imgProfileNameSaveX.animate().translationY(50);
        } else if (height2 < 1300) {

        }

        txtProfileNameX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtProfileNameX.setVisibility(View.GONE);
                edtProfileNameX.setVisibility(View.VISIBLE);
                alteredNameToggle = "yes";

                imgProfileNameEditX.setVisibility(View.GONE);
                imgProfileNameSaveX.setVisibility(View.VISIBLE);
            }
        });

        imgProfileNameEditX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtProfileNameX.setVisibility(View.GONE);
                edtProfileNameX.setVisibility(View.VISIBLE);
                alteredNameToggle = "yes";

                imgProfileNameEditX.setVisibility(View.GONE);
                imgProfileNameSaveX.setVisibility(View.VISIBLE);
            }
        });

        imgProfileNameSaveX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                profileReference.getRef().child("profilename").setValue(edtProfileNameX.getText().toString());
                edtProfileNameX.setVisibility(View.GONE);
                txtProfileNameX.setVisibility(View.VISIBLE);
                imgProfileNameSaveX.setVisibility(View.GONE);
                imgProfileNameEditX.setVisibility(View.VISIBLE);
                alteredNameToggle = "no";
                recreate();

            }
        });

        countryPicker = new CountryPicker.Builder().with(this).listener(this).build();
        imgFlagX = findViewById(R.id.imgFlag);
        imgFlagX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countryPicker.showDialog(getSupportFragmentManager());
                imgFlagEditX.setVisibility(View.GONE);
                imgFlagSaveX.setVisibility(View.VISIBLE);


            }
        });

        txtCountryX = findViewById(R.id.txtCountry);

        imgFlagEditX = findViewById(R.id.imgFlagEdit);
        imgFlagEditX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countryPicker.showDialog(getSupportFragmentManager());
                imgFlagEditX.setVisibility(View.GONE);
                imgFlagSaveX.setVisibility(View.VISIBLE);

            }
        });

        imgFlagSaveX = findViewById(R.id.imgFlagSave);
        imgFlagSaveX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFlagImageToServer();

            }
        });

        if (width2 > 1500) { // changes in fot for tablet and then small format phone
            imgFlagEditX.animate().translationY(50);
            imgFlagSaveX.animate().translationY(50);

        } else if (height2 < 1300) {

        }

        // ////////////////// Profile inputs and outputs  ENDS ///////////////////////////////////////////////////



        //////////// pop-up
        fabPopUpPFX = findViewById(R.id.fabPopUpPF);
        fabPopUpPFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowNewFABbasedMenu();
            }
        });

        popupMenuToggle = "Not";

        fabPopUpCollPFX = findViewById(R.id.fabPopUpCollPF);
        fabPopUpFAQminiPFtX = findViewById(R.id.fabPopUpFAQminiPF);
        fabPopUpLogOutminiPFX = findViewById(R.id.fabPopUpLogOutminiPF);
        fabPopUpPremiumminiPFX = findViewById(R.id.fabPopUpPremiumminiPF);

        txtFAQButtonPFX = findViewById(R.id.txtFAQButtonPF);
        txtLogoutButtonPFX = findViewById(R.id.txtLogoutButtonPF);
        txtPremiumButtonPFX = findViewById(R.id.txtPremiumButtonPF);

        shadeX = findViewById(R.id.shade);

        //// end of pop-up


        /// FACEBOOK SHARE

        main = findViewById(R.id.loutProfileView);
        imgTestScreenshotX = findViewById(R.id.imgTestProf); // the way the code is below we need this even if we don't ever populate it and it is GONE

        fabSharePFX = findViewById(R.id.fabSharePF);

        if (width2 > 1500){
            fabSharePFX.animate().translationY(-125);
        }

        fabSharePFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(ProfileView.this);
                View view = inflater.inflate(R.layout.zzz_fbshare_dialog, null);

                dialog = new AlertDialog.Builder(ProfileView.this)
                        .setView(view)
                        .create();

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialog.show();
                double dialogWidth = width2*.75;
                int dialogWidthFinal = (int) Math.round(dialogWidth);
                double dialogHeight = dialogWidthFinal*1.5;
                int dialogHeightFinal = (int) Math.round(dialogHeight);

                dialog.getWindow().setLayout(dialogWidthFinal, dialogHeightFinal);

                imgFBShareX = view.findViewById(R.id.imgFBShare);
                imgFBShareGlowX = view.findViewById(R.id.imgFBShareGlow);
                imgNoFBShareX = view.findViewById(R.id.imgNoFBShare);
                txtDialogSpacerX = view.findViewById(R.id.txtDialogSpacer);

                imgNoFBShareX.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                try {

                    Bitmap b = YYYjcScreenshot.takescreenshotOfRootView(imgTestScreenshotX);
                    //imgTestScreenshotX.setImageBitmap(b);

                    //write file
                    filename = "bitmap.png";
                    FileOutputStream stream = ProfileView.this.openFileOutput(filename, Context.MODE_PRIVATE);
                    b.compress(Bitmap.CompressFormat.PNG, 100, stream);

                    //clean up
                    stream.close();
                    //b.recycle();

                    imgFBShareX.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imgFBShareX.setVisibility(View.GONE);
                            imgNoFBShareX.setVisibility(View.GONE);
                            imgFBShareGlowX.setVisibility(View.VISIBLE);
                            txtDialogSpacerX.setVisibility(View.VISIBLE);

                            CountDownTimer glowtimer = new CountDownTimer(500,100) {
                                @Override
                                public void onTick(long millisUntilFinished) {

                                }

                                @Override
                                public void onFinish() {

                                    //intent
                                    Intent intent = new Intent(ProfileView.this, FacebookShare.class);
                                    intent.putExtra("image", filename);
                                    intent.putExtra("source", "profile");
                                    startActivity(intent);
                                    dialog.dismiss();

                                }
                            }.start();



                        }
                    });



                }catch (Exception e){

                }

            }
        });






        userUID = FirebaseAuth.getInstance().getUid();
        profileReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(userUID);

        //// badges ////

        imgSupporterBadgeX = findViewById(R.id.imgSupporterBadge);
        imgLongStreakBadgeX = findViewById(R.id.imgLongStreakBadge);
        imgTotalAnsweredBadgeX = findViewById(R.id.imgTotalAnsweredBadge);
        imgWinningPercentageBadgeX = findViewById(R.id.imgWinPercentageBadge);

        imgAntiquityBadgeX = findViewById(R.id.imgAntiquityBadge);
        imgMedievalBadgeX = findViewById(R.id.imgMedievalBadge);
        imgRenaissanceBadgeX = findViewById(R.id.imgRenaissanceBadge);
        imgEnlightenmentBadgeX = findViewById(R.id.imgEnlightenmentBadge);
        imgModernBadgeX = findViewById(R.id.imgModernBadge);
        imgContemporaryBadgeX = findViewById(R.id.imgContemporaryBadge);

        imgArtBonusBadgeX = findViewById(R.id.imgArtBonusBadge);
        imgFBShareBadgeX = findViewById(R.id.imgFBShareBadge);
        imgVillainsRevealedBadgeX = findViewById(R.id.imgVillainsRevealedBadge);
        imgPremiumBadgeX = findViewById(R.id.imgPremiumBadge);

        ///// POPULATING ALL THE COUNTERS BEGINS ////////////////////////////////////////////////////

        profileQuery = FirebaseDatabase.getInstance().getReference().child("my_users").orderByChild("user").equalTo(userUID);
        profileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userPs: dataSnapshot.getChildren()) {

                    try {
                    // if a Profile name already exists this will pull it down and set text view; if it doesn't it will throw it to catch
                    profileName = userPs.child("profilename").getValue().toString();
                    txtProfileNameX.setText(profileName);

                    } catch (Exception e) {

                        profileName = "Serbitar"; // for now generates static profile name will set to random; happens only if one does not already exist
                        txtProfileNameX.setText(profileName); // populates the text box
                        profileReference.getRef().child("profilename").setValue(profileName); //puts the generated profile name to FB so it will have it next time around

                    }

                    try {
                        coinCounterString = userPs.child("coins").getValue().toString();
                        coinCounter = Integer.valueOf(coinCounterString);
                    } catch (Exception e) {
                        coinCounterString = "";
                    }

                    try {
                        conStreakString = userPs.child("constreak").getValue().toString();
                        conStreak = Integer.valueOf(conStreakString);
                    } catch (Exception e) {
                        conStreakString = "";
                    }

                    try {
                        totalAnswersString = userPs.child("totalanswered").getValue().toString();
                        totalAnswers = Integer.valueOf(totalAnswersString);
                    } catch (Exception e) {
                        totalAnswersString = "";
                    }

                    try {
                        longestStreakString = userPs.child("longeststreak").getValue().toString();
                        longestStreak = Integer.valueOf(longestStreakString);
                    } catch (Exception e) {
                        longestStreakString = "";
                    }

                    try {
                        totalQuestionsString = userPs.child("totalquestions").getValue().toString();
                        totalQuestions = Integer.valueOf(totalQuestionsString);
                    } catch (Exception e) {
                        totalQuestionsString ="";
                    }

                    try {

                        correctPercent = ( (double) totalAnswers / totalQuestions)*100;
                        int correctPercent2 = (int) Math.round(correctPercent);
                        correctPercentString = correctPercent2 +"%";
                        pfCorrectPercentX.setText(correctPercentString);

                    } catch (Exception e) {
                        correctPercentString = "";
                    }

                    try {
                        imageLink = userPs.child("imagelink").getValue().toString();
                        imageIdentifier = userPs.child("imageidentifier").getValue().toString();

                        imagePresentToggle ="yes";
                        imageLinkToDelete = imageLink;

                        Picasso.get().load(imageLink).into(imgProfileX);

                    } catch (Exception e) {
                        imagePresentToggle = "no";
                    }

                    try {
                        imageFlagLink = userPs.child("imageflaglink").getValue().toString();
                        imageFlagIdentifier = userPs.child("imageflagidentifier").getValue().toString();

                        imageFlagPresentToggle ="yes";
                        imageFlagLinkToDelete = imageFlagLink;

                        Picasso.get().load(imageFlagLink).into(imgFlagX);

                    } catch (Exception e) {
                        imageFlagPresentToggle = "no";
                    }

                    try {

                        countryName = userPs.child("countryname").getValue().toString();
                        txtCountryX.setText(countryName);

                    } catch (Exception e) {

                        txtCountryX.setText("Global Citizen"); // populates the text box
                    }

                    try {

                        showPremiumDialogToggle2 = userPs.child("premiumasktoggle").getValue().toString();

                    } catch (Exception e) {

                        showPremiumDialogToggle2 = "catch";
                    }

                    /////badges////////////////////////////////////////

                    try {

                        supporterBadgeLevelString = userPs.child("badgesup").getValue().toString();
                        supporterBadgeLevel = Integer.valueOf(supporterBadgeLevelString);

                    } catch (Exception e) {
                    }

                    try {

                        longestStreakBadgeLevelString = userPs.child("badgestr").getValue().toString();
                        longestStreakBadgeLevel = Integer.valueOf(longestStreakBadgeLevelString);

                    } catch (Exception e) {
                    }

                    try {

                        totalAnsweredBadgeLevelString = userPs.child("badgetrt").getValue().toString();
                        totalAnsweredBadgeLevel = Integer.valueOf(totalAnsweredBadgeLevelString);

                    } catch (Exception e) {
                    }

                    try {

                        winningPerentageBadgeLevelString = userPs.child("badgewin").getValue().toString();
                        winningPerentageBadgeLevel = Integer.valueOf(winningPerentageBadgeLevelString);

                    } catch (Exception e) {
                    }

                    try {

                        artBonusBadgeLevelString = userPs.child("badgebon").getValue().toString();
                        artBonusBadgeLevel = Integer.valueOf(artBonusBadgeLevelString);

                    } catch (Exception e) {
                    }

                    try {

                        fbShareBadgeLevelString = userPs.child("badgesha").getValue().toString();
                        fbShareBadgeLevel = Integer.valueOf(fbShareBadgeLevelString);

                    } catch (Exception e) {
                    }

                    try {

                        villainsRevealedString = userPs.child("badgevil").getValue().toString();
                        villainsRevealedLevel = Integer.valueOf(villainsRevealedString);

                    } catch (Exception e) {
                    }

                    try {

                        premiumLevelString = userPs.child("badgepre").getValue().toString();
                        premiumLevel = Integer.valueOf(premiumLevelString);

                    } catch (Exception e) {
                    }



                    try {

                        antiquityBadgeLevelString = userPs.child("badgexant").getValue().toString();
                        antiquityBadgeLevel = Integer.valueOf(antiquityBadgeLevelString);

                    } catch (Exception e) {
                    }

                    try {

                        medievalBadgeLevelString = userPs.child("badgexmed").getValue().toString();
                        medievalBadgeLevel = Integer.valueOf(medievalBadgeLevelString);

                    } catch (Exception e) {
                    }

                    try {

                        renaissanceBadgeLevelString = userPs.child("badgexren").getValue().toString();
                        renaissanceBadgeLevel = Integer.valueOf(renaissanceBadgeLevelString);

                    } catch (Exception e) {
                    }

                    try {

                        enlightenmentBadgeLevelString = userPs.child("badgexenl").getValue().toString();
                        enlightenmentBadgeLevel = Integer.valueOf(enlightenmentBadgeLevelString);

                    } catch (Exception e) {
                    }

                    try {

                        modernBadgeLevelString = userPs.child("badgexmod").getValue().toString();
                        modernBadgeLevel = Integer.valueOf(modernBadgeLevelString);

                    } catch (Exception e) {
                    }

                    try {

                        contemporaryBadgeLevelString = userPs.child("badgexcon").getValue().toString();
                        contemporaryBadgeLevel = Integer.valueOf(contemporaryBadgeLevelString);

                    } catch (Exception e) {
                    }

                    badgeAssign();
                }

                pfTxtCoinCounterX.setText(coinCounterString);
                pfTxtConStreakX.setText(conStreakString);
                pfTxtTotalAnswersX.setText(totalAnswersString);
                pfTxtLongestStreakX.setText(longestStreakString);
                pfTxtTotalQuestionsX.setText(totalQuestionsString);
                pfCorrectPercentX.setText(correctPercentString);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //////////////// POPULATING ALL COUNTERS ENDS ///////////////////////////////////////////

        ///////////////// populating badges ////////////////////////////////////////

        //badge view buttons - comes after the query that determines badge levels
        //loutProfileBadgesX = findViewById(R.id.loutProfileBadges);


        imgSupporterBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                badgeView = "supporter";
                badgeViews(badgeView);

            }
        });

        imgLongStreakBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                badgeView = "streak";
                badgeViews(badgeView);

            }
        });

        imgTotalAnsweredBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                badgeView = "total";
                badgeViews(badgeView);

            }
        });

        imgWinningPercentageBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                badgeView = "winper";
                badgeViews(badgeView);

            }
        });

        imgAntiquityBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                badgeView = "antiquity";
                badgeViews(badgeView);

            }
        });

        imgMedievalBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                badgeView = "medieval";
                badgeViews(badgeView);

            }
        });

        imgRenaissanceBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                badgeView = "renaissance";
                badgeViews(badgeView);

            }
        });

        imgEnlightenmentBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                badgeView = "enlightenment";
                badgeViews(badgeView);

            }
        });

        imgModernBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                badgeView = "modern";
                badgeViews(badgeView);

            }
        });

        imgContemporaryBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                badgeView = "contemporary";
                badgeViews(badgeView);

            }
        });

        imgArtBonusBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                badgeView = "bonus";
                badgeViews(badgeView);

            }
        });

        imgFBShareBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                badgeView = "fbshare";
                badgeViews(badgeView);

            }
        });

        imgVillainsRevealedBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                badgeView = "villains";
                badgeViews(badgeView);

            }
        });

        imgPremiumBadgeX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                badgeView = "premium";
                badgeViews(badgeView);

            }
        });



    }

    /////////////////////////////////////////END OF ONCREAT /////////////////////////////////////////////////////////

    private void badgeViews(String badgeView) {

        badgeSortkey = "";

        if (badgeView.equals("supporter")) {

            badgeSortkey = supporterBadge;

            if (badgeSortkey.equals("sup1")) {
                badgeProfileAwardMsg = "THE SHOPKEEPER BADGE for reaching the 125 coin Level!";
            }
            if (badgeSortkey.equals("sup2")) {
                badgeProfileAwardMsg = "THE BOOK MERCHANT BADGE for reaching the 175 coin Level!";
            }
            if (badgeSortkey.equals("sup3")) {
                badgeProfileAwardMsg = "THE TAX COLLECTOR BADGE for reaching the 225 coin Level!";
            }
            if (badgeSortkey.equals("sup4")) {
                badgeProfileAwardMsg = "THE EQUITE CLASS BADGE for reaching the 500 coin Level!";
            }
            if (badgeSortkey.equals("sup5")) {
                badgeProfileAwardMsg = "THE SENATORIAL CLASS BADGE for reaching the 1000 coin Level!";
            }

        }

        if (badgeView.equals("streak")) {

            badgeSortkey = longestStreakBadge;

            if (badgeSortkey.equals("str1")) {
                badgeProfileAwardMsg = "THE STUDENT BADGE for getting 3 correct answers in a row!";
            }
            if (badgeSortkey.equals("str2")) {
                badgeProfileAwardMsg = "THE TUTOR BADGE for getting 10 correct answers in a row!";
            }
            if (badgeSortkey.equals("str3")) {
                badgeProfileAwardMsg = "THE RECTOR BADGE for getting 15 correct answers in a row!";
            }
            if (badgeSortkey.equals("str4")) {
                badgeProfileAwardMsg = "THE PHILOSOPHER BADGE for getting 20 correct answers in a row!";
            }
            if (badgeSortkey.equals("str5")) {
                badgeProfileAwardMsg = "THE MASTER BADGE for getting 25 correct answers in a row!";
            }
        }

        if (badgeView.equals("total")) {

            badgeSortkey = totalAnsweredBadge;

            if (badgeSortkey.equals("trt1")) {
                badgeProfileAwardMsg = "THE SCRIBE BADGE for getting to a total of 5 correct answers!";
            }
            if (badgeSortkey.equals("trt2")) {
                badgeProfileAwardMsg = "THE LIBRARIAN BADGE for getting to a total of 25 correct answers!";
            }
            if (badgeSortkey.equals("trt3")) {
                badgeProfileAwardMsg = "THE MASTER LIBRARIAN BADGE for getting to a total of 100 correct answers!";
            }
            if (badgeSortkey.equals("trt4")) {
                badgeProfileAwardMsg = "THE CHRONICLER BADGE for getting to a total of 250 correct answers!";
            }
            if (badgeSortkey.equals("trt5")) {
                badgeProfileAwardMsg = "THE HISTORIAN BADGE for getting to a total of 500 correct answers!";

            }
        }

        if (badgeView.equals("winper")) {

            badgeSortkey = winningPercentageBadge;

            if (badgeSortkey.equals("win1")) {
                badgeProfileAwardMsg = "THE COIN-FLIP BADGE for achieving a 50% correct answer level!";
            }
            if (badgeSortkey.equals("win2")) {
                badgeProfileAwardMsg = "THE ADEPT BADGE for achieving a 60% correct answer level!";
            }
            if (badgeSortkey.equals("win3")) {
                badgeProfileAwardMsg = "THE PRODIGY BADGE for achieving a 70% correct answer level!";
            }
            if (badgeSortkey.equals("win4")) {
                badgeProfileAwardMsg = "THE GENIUS BADGE for achieving an 80% correct answer level!";
            }
            if (badgeSortkey.equals("win5")) {
                badgeProfileAwardMsg = "THE INFALLIBE BADGE for achieving a 90% correct answer level!";
            }

        }

        if (badgeView.equals("bonus")) {

            badgeSortkey = artBonusBadge;

            if (badgeSortkey.equals("bon")) {
                badgeProfileAwardMsg = "THE ART INSPECTOR BADGE for getting 10 Badge Bonus questions right!";
            }
        }

        if (badgeView.equals("fbshare")) {

            badgeSortkey = fbShareBadge;

            if (badgeSortkey.equals("sha")) {
                badgeProfileAwardMsg = "THE TOWN CRIER BADGE for 3 Facebook Shares!";
            }
        }

        if (badgeView.equals("villains")) {

            badgeSortkey = villainsRevealedBadge;

            if (badgeSortkey.equals("vil")) {
                badgeProfileAwardMsg = "THE VILLAIN SPOTTER BADGE for 5 Revealed Villains!";
            }
        }

        if (badgeView.equals("premium")) {

            badgeSortkey = premiumBadge;

            if (badgeSortkey.equals("pre")) {
                badgeProfileAwardMsg = "THE PREMIUM HISTORIAN BADGE for supporting our QUIZ!";
            }
        }


        if (badgeView.equals("antiquity")) {

            badgeSortkey = antiquityBadge;

            if (badgeSortkey.equals("xant1")) {
                badgeProfileAwardMsg = "THE HOPLITE BADGE for getting to 5 correct answers in the ANTIQUITY category!";
            }
            if (badgeSortkey.equals("xant2")) {
                badgeProfileAwardMsg = "THE LEGIONNAIRE BADGE for getting to 25 correct answers in the ANTIQUITY category!";
            }
            if (badgeSortkey.equals("xant3")) {
                badgeProfileAwardMsg = "THE DECURION BADGE for getting to 50 correct answers in the ANTIQUITY category!";
            }
            if (badgeSortkey.equals("xant4")) {
                badgeProfileAwardMsg = "THE CENTURION BADGE for getting to 100 correct answers in the ANTIQUITY category!";
            }
            if (badgeSortkey.equals("xant5")) {
                badgeProfileAwardMsg = "THE LEGATE BADGE for getting to 200 correct answers in the ANTIQUITY category!";
            }

        }

        if (badgeView.equals("medieval")) {

            badgeSortkey = medievalBadge;

            if (badgeSortkey.equals("xmed1")) {
                badgeProfileAwardMsg = "THE SQUIRE BADGE for getting to 5 correct answers in the MEDIEVAL category!";
            }
            if (badgeSortkey.equals("xmed2")) {
                badgeProfileAwardMsg = "THE KNIGHT BADGE for getting to 25 correct answers in the MEDIEVAL category!";
            }
            if (badgeSortkey.equals("xmed3")) {
                badgeProfileAwardMsg = "THE BARON BADGE for getting to 50 correct answers in the MEDIEVAL category!";
            }
            if (badgeSortkey.equals("xmed4")) {
                badgeProfileAwardMsg = "THE MARQUIS BADGE for getting to 100 correct answers in the MEDIEVAL category!";
            }
            if (badgeSortkey.equals("xmed5")) {
                badgeProfileAwardMsg = "THE DUKE BADGE for getting to 200 correct answers in the MEDIEVAL category!";
            }

        }
        if (badgeView.equals("renaissance")) {

            badgeSortkey = renaissanceBadge;
            if (badgeSortkey.equals("xren1")) {
                badgeProfileAwardMsg = "THE TITIAN BADGE for getting to 5 correct answers in the RENAISSANCE category!";
            }
            if (badgeSortkey.equals("xren2")) {
                badgeProfileAwardMsg = "THE DONATELLO BADGE for getting to 25 correct answers in the RENAISSANCE category!";
            }
            if (badgeSortkey.equals("xren3")) {
                badgeProfileAwardMsg = "THE BERNINI BADGE for getting to 50 correct answers in the RENAISSANCE category!";
            }
            if (badgeSortkey.equals("xren4")) {
                badgeProfileAwardMsg = "THE MICHELANGELO BADGE for getting to 100 correct answers in the RENAISSANCE category!";
            }
            if (badgeSortkey.equals("xren5")) {
                badgeProfileAwardMsg = "THE DA VINCI BADGE for getting to 200 correct answers in the RENAISSANCE category!";
            }

        }
        if (badgeView.equals("enlightenment")) {

            badgeSortkey = enlightenmentBadge;

            if (badgeSortkey.equals("xenl1")) {
                badgeProfileAwardMsg = "THE DESCARTES BADGE for getting to 5 correct answers in the ENLIGHTENMENT category!";
            }
            if (badgeSortkey.equals("xenl2")) {
                badgeProfileAwardMsg = "THE JOHN LOCKE BADGE for getting to 25 correct answers in the ENLIGHTENMENT category!";
            }
            if (badgeSortkey.equals("xenl3")) {
                badgeProfileAwardMsg = "THE VOLTAIRE BADGE for getting to 50 correct answers in the ENLIGHTENMENT category!";
            }
            if (badgeSortkey.equals("xenl4")) {
                badgeProfileAwardMsg = "THE EDWARD GIBBON BADGE for getting to 100 correct answers in the ENLIGHTENMENT category!";
            }
            if (badgeSortkey.equals("xenl5")) {
                badgeProfileAwardMsg = "THE ISAAC NEWTON BADGE for getting to 200 correct answers in the ENLIGHTENMENT category!";
            }

        }
        if (badgeView.equals("modern")) {

            badgeSortkey = modernBadge;

            if (badgeSortkey.equals("xmod1")) {
                badgeProfileAwardMsg = "THE DYNAMITE BADGE for getting to 5 correct answers in the MODERN HISTORY category!";
            }
            if (badgeSortkey.equals("xmod2")) {
                badgeProfileAwardMsg = "THE GATLING-GUN BADGE for getting to 25 correct answers in the MODERN HISTORY category!";
            }
            if (badgeSortkey.equals("xmod3")) {
                badgeProfileAwardMsg = "THE AUTOMOBILE BADGE for getting to 50 correct answers in the MODERN HISTORY category!";
            }
            if (badgeSortkey.equals("xmod4")) {
                badgeProfileAwardMsg = "THE TANK BADGE for getting to 100 correct answers in the MODERN HISTORY category!";
            }
            if (badgeSortkey.equals("xmod5")) {
                badgeProfileAwardMsg = "THE AVIATOR BADGE for getting to 200 correct answers in the MODERN HISTORY category!";
            }

        }
        if (badgeView.equals("contemporary")) {

            badgeSortkey = contemporaryBadge;

            if (badgeSortkey.equals("xcon1")) {
                badgeProfileAwardMsg = "THE INDUSTRIAL BADGE for getting to 5 correct answers in the CONTEMPORARY HISTORY category!";
            }
            if (badgeSortkey.equals("xcon2")) {
                badgeProfileAwardMsg = "THE NUCLEAR BADGE for getting to 25 correct answers in the CONTEMPORARY HISTORY category!";
            }
            if (badgeSortkey.equals("xcon3")) {
                badgeProfileAwardMsg = "THE BIO-TECH BADGE for getting to 50 correct answers in the CONTEMPORARY HISTORY category!";
            }
            if (badgeSortkey.equals("xcon4")) {
                badgeProfileAwardMsg = "THE DIGITAL BADGE for getting to 100 correct answers in the CONTEMPORARY HISTORY category!";
            }
            if (badgeSortkey.equals("xcon5")) {
                badgeProfileAwardMsg = "THE ARTIFICIAL INTELLIGENCE BADGE for getting to 200 correct answers in the CONTEMPORARY HISTORY category!";
            }


        }

        Log.i("BSHOW", badgeSortkey);

        if (badgeSortkey.equals("")) {

            Log.i("BSHOW", badgeSortkey + "in the iff");

        } else {

            Log.i("BSHOW", badgeSortkey + "in the else");

            Query badgeQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(badgeSortkey);
            badgeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot badgeSnapshot) {

                    for (DataSnapshot badges : badgeSnapshot.getChildren()) {

                        Log.i("BSHOW", badgeSortkey + " in the snapshot");

                        // loutProfileBadgesX.setVisibility(View.VISIBLE);

                        badgeImageLink = badges.child("badgeimagelink").getValue().toString();

                        badgeDialogShow();


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }



    }

    public void badgeDialogShow() {

        LayoutInflater inflater = LayoutInflater.from(ProfileView.this);
        View view = inflater.inflate(R.layout.zzz_badgeinfo, null);

        dialog = new AlertDialog.Builder(ProfileView.this)
                .setView(view)
                .create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        double dialogWidth = width2*.75;
        int dialogWidthFinal = (int) Math.round(dialogWidth);
        double dialogHeight = dialogWidthFinal*1.5;
        int dialogHeightFinal = (int) Math.round(dialogHeight);

        dialog.getWindow().setLayout(dialogWidthFinal, dialogHeightFinal);

        txtProfileBadgeMessageX = view.findViewById(R.id.txtProfileBadgeMessage);
        imgProfileBadgeAwardX = view.findViewById(R.id.imgProfileBadgeAwardD);


        Log.i("BAWARD", badgeImageLink);
        Picasso.get().load(badgeImageLink).into(imgProfileBadgeAwardX);
        txtProfileBadgeMessageX.setText(badgeProfileAwardMsg);

        try {

            CountDownTimer badgeAwardTimer = new CountDownTimer(5000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    //loutProfileBadgesX.setVisibility(View.GONE);
                    dialog.dismiss();


                }
            }.start();

        } catch (Exception e) {
        }


    }



    //onClick set up in XML; gets rid of keyboard when background tapped
    public void loginLayoutTapped (View view) {

        try { // we need this because if you tap with no keyboard up the app will crash

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public  void onSelectCountry (Country country) {
        imgFlagX.setImageResource(country.getFlag());
        txtCountryX.setText(country.getName());
        alteredFlagPicToggle = "yes";

    }

    ///////////////////////////////////BEGIN POP-UP MENU //////////////////////////////////////////////////////////


    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    private void ShowNewFABbasedMenu() {

        popupMenuToggle = "pressed";

        fabPopUpPFX.setVisibility(View.GONE);
        fabPopUpCollPFX.setVisibility(View.VISIBLE);
        fabPopUpFAQminiPFtX.setVisibility(View.VISIBLE);
        fabPopUpLogOutminiPFX.setVisibility(View.VISIBLE);
        fabSharePFX.setVisibility(View.GONE);
        fabPopUpPremiumminiPFX.setVisibility(View.VISIBLE);

       // txtFAQButtonPFX.setVisibility(View.VISIBLE);
       // txtLogoutButtonPFX.setVisibility(View.VISIBLE);

        fabPopUpLogOutminiPFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenuToggle = "Not";

                fabPopUpPFX.setVisibility(View.VISIBLE);
                fabPopUpCollPFX.setVisibility(View.GONE);
                fabPopUpFAQminiPFtX.setVisibility(View.GONE);
                fabPopUpLogOutminiPFX.setVisibility(View.GONE);
                fabSharePFX.setVisibility(View.VISIBLE);
                fabPopUpPremiumminiPFX.setVisibility(View.GONE);

                txtFAQButtonPFX.setVisibility(View.GONE);
                txtLogoutButtonPFX.setVisibility(View.GONE);
                txtPremiumButtonPFX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

                alertDialogLogOut();

            }
        });

        fabPopUpFAQminiPFtX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupMenuToggle = "Not";

                fabPopUpPFX.setVisibility(View.VISIBLE);
                fabPopUpCollPFX.setVisibility(View.GONE);
                fabPopUpFAQminiPFtX.setVisibility(View.GONE);
                fabPopUpLogOutminiPFX.setVisibility(View.GONE);
                fabSharePFX.setVisibility(View.VISIBLE);
                fabPopUpPremiumminiPFX.setVisibility(View.GONE);

                txtFAQButtonPFX.setVisibility(View.GONE);
                txtLogoutButtonPFX.setVisibility(View.GONE);
                txtPremiumButtonPFX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

                fagDisplay();

            }
        });

        shadeX.setVisibility(View.VISIBLE);

        fabPopUpCollPFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenuToggle = "Not";

                fabPopUpPFX.setVisibility(View.VISIBLE);
                fabPopUpCollPFX.setVisibility(View.GONE);
                fabPopUpFAQminiPFtX.setVisibility(View.GONE);
                fabPopUpLogOutminiPFX.setVisibility(View.GONE);
                fabSharePFX.setVisibility(View.VISIBLE);
                fabPopUpPremiumminiPFX.setVisibility(View.GONE);


                txtFAQButtonPFX.setVisibility(View.GONE);
                txtLogoutButtonPFX.setVisibility(View.GONE);
                txtPremiumButtonPFX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

            }
        });

        fabPopUpPremiumminiPFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenuToggle = "Not";

                fabPopUpPFX.setVisibility(View.VISIBLE);
                fabPopUpCollPFX.setVisibility(View.GONE);
                fabPopUpFAQminiPFtX.setVisibility(View.GONE);
                fabPopUpLogOutminiPFX.setVisibility(View.GONE);
                fabPopUpPremiumminiPFX.setVisibility(View.GONE);
                fabSharePFX.setVisibility(View.VISIBLE);

                txtFAQButtonPFX.setVisibility(View.GONE);
                txtLogoutButtonPFX.setVisibility(View.GONE);
                txtPremiumButtonPFX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

                Log.i("PREMIUM", "before if show = " + showPremiumDialogToggle2);
                if(showPremiumDialogToggle2.equals("bought")) {

                    alreadyPremiumSnackbar();

                } else {

                    Intent intent = new Intent(ProfileView.this, Premium.class);
                    startActivity(intent);
                }

            }
        });

    }

    //////////////////Profile Edit and Display Methods BEGIN /////////////////////////////////////////////////

    private void addImage() {

        //Toast.makeText(ProfileView.this, "Here", Toast.LENGTH_SHORT).show();

        if(ActivityCompat.checkSelfPermission(ProfileView.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE},
                    1000);
        }else {

            getChosenImage();

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1000) {

            if(grantResults.length >0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){

                getChosenImage();

            }
        }
    }

    private void getChosenImage() {

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
                    recievedProfileImageBitmap = BitmapFactory.decodeFile(picturePath);

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
                    recievedProfileImageBitmap = Bitmap.createScaledBitmap(recievedProfileImageBitmap, 400,400,true);

                    // correcting the rotation on the resized file using the degree variable of how much to fix we got above
                    Bitmap bitmap = recievedProfileImageBitmap;
                    Matrix matrix = new Matrix();
                    matrix.postRotate(degree);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);


                    imgProfileX.setImageBitmap(bitmap);
                    imgProfilePicSaveX.setVisibility(View.VISIBLE);
                    imgProfilePicEditX.setVisibility(View.GONE);
                    alteredPicToggle = "yes";


                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

        }

    }

    private void uploadImageToServer () {

        pd = new ProgressDialog(ProfileView.this,R.style.CustomAlertDialog);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

        // Get the data from an ImageView as bytes; does not crash when user does not select and image because takes default image provided by the app
        imgProfileX.setDrawingCacheEnabled(true);
        imgProfileX.buildDrawingCache();
        Bitmap bitmapColAdd = ((BitmapDrawable) imgProfileX.getDrawable()).getBitmap(); // we already have the bitmap
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // create this bitmap using recieved to upload stock image if user did not upload theirs;
        bitmapColAdd.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] data = baos.toByteArray();

        imageIdentifier = UUID.randomUUID() + ".jpg";   //initialized here because needs to be unique for each image but is random = unique??

        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("profileimages")
                .child(imageIdentifier).putBytes(data);
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

                            imageLink = task.getResult().toString();
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

        profileReference.getRef().child("imagelink").setValue(imageLink);
        profileReference.getRef().child("imageidentifier").setValue(imageIdentifier);
        imgProfilePicSaveX.setVisibility(View.GONE);
        imgProfilePicEditX.setVisibility(View.VISIBLE);
        alteredPicToggle = "no";

        if (imagePresentToggle.equals("yes")) {

            StorageReference mPictureReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageLinkToDelete);

            mPictureReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    //Toast.makeText(ProfileView.this, "Old Image Deleted", Toast.LENGTH_SHORT).show();

                    //this deletes the image but until you close out of the app, that is not updated in the UI...
                    //... which is OK because it also re-uploads the same image; but why?

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //Toast.makeText(ProfileView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

        collectionLoadSnackbar();

    }

    private void uploadFlagImageToServer () {

        pd = new ProgressDialog(ProfileView.this,R.style.CustomAlertDialog);
        pd.setCancelable(false);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();

        //First upload name which is much simpler
        profileReference.getRef().child("countryname").setValue(txtCountryX.getText().toString());

        // Get the data from an ImageView as bytes; does not crash when user does not select and image because takes default image provided by the app
        imgFlagX.setDrawingCacheEnabled(true);
        imgFlagX.buildDrawingCache();
        Bitmap bitmapColAdd = ((BitmapDrawable) imgFlagX.getDrawable()).getBitmap(); // we already have the bitmap
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // create this bitmap using recieved to upload stock image if user did not upload theirs;
        bitmapColAdd.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        imageFlagIdentifier = UUID.randomUUID() + ".jpg";   //initialized here because needs to be unique for each image but is random = unique??

        UploadTask uploadTask = FirebaseStorage.getInstance().getReference().child("flagimages")
                .child(imageFlagIdentifier).putBytes(data);
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

                            imageFlagLink = task.getResult().toString();
                            pd.dismiss();

                            // setting up as separate method to let image upload finish before calling the put function which requires the imageLink
                            uploadflagimageinfo ();
                        }
                    }
                });
            }
        });

    }

    private void uploadflagimageinfo (){

        profileReference.getRef().child("imageflaglink").setValue(imageFlagLink);
        profileReference.getRef().child("imageflagidentifier").setValue(imageFlagIdentifier);
        imgFlagSaveX.setVisibility(View.GONE);
        imgFlagEditX.setVisibility(View.VISIBLE);
        alteredFlagPicToggle = "no";

        if (imageFlagPresentToggle.equals("yes")) {

            StorageReference mPictureReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageFlagLinkToDelete);

            mPictureReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    //Toast.makeText(ProfileView.this, "Old Image Deleted", Toast.LENGTH_SHORT).show();

                    //this deletes the image but until you close out of the app, that is not updated in the UI...
                    //... which is OK because it also re-uploads the same image; but why?

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    //Toast.makeText(ProfileView.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

        collectionLoadSnackbar();

    }



    private void collectionLoadSnackbar(){


        Snackbar snackbar;

        snackbar = Snackbar.make(loutProfileViewX, "Your information was updated successfully", Snackbar.LENGTH_SHORT);


        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();

        // THE COLOR SET BELOW WORKS but the default is white which is what we want; keeping code for reference
        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);
        textView.setTextColor(getResources().getColor(R.color.colorPrimary));

    }

    //////////////////Profile Edit and Display Methods ENDS /////////////////////////////////////////////////

    ///////////////// Badges beings ////////////////////////////////////////////////////////////////

    private void badgeAssign() {

        supporterBadge = ""; //need this as default to avoid null point exceptions in downstream ifs
        if (supporterBadgeLevel == 1){
            supporterBadge = "sup1";
        }
        if (supporterBadgeLevel == 2){
            supporterBadge = "sup2";
        }
        if (supporterBadgeLevel == 3){
            supporterBadge = "sup3";
        }
        if (supporterBadgeLevel == 4){
            supporterBadge = "sup4";
        }
        if (supporterBadgeLevel == 5){
            supporterBadge = "sup5";
        }

        longestStreakBadge = "";
        if (longestStreakBadgeLevel == 1){
            longestStreakBadge = "str1";
        }
        if (longestStreakBadgeLevel == 2){
            longestStreakBadge = "str2";
        }
        if (longestStreakBadgeLevel == 3){
            longestStreakBadge = "str3";
        }
        if (longestStreakBadgeLevel == 4){
            longestStreakBadge = "str4";
        }
        if (longestStreakBadgeLevel == 5){
            longestStreakBadge = "str5";
        }

        totalAnsweredBadge = "";
        if (totalAnsweredBadgeLevel == 1){
            totalAnsweredBadge = "trt1";
        }
        if (totalAnsweredBadgeLevel == 2){
            totalAnsweredBadge = "trt2";
        }
        if (totalAnsweredBadgeLevel == 3){
            totalAnsweredBadge = "trt3";
        }
        if (totalAnsweredBadgeLevel == 4){
            totalAnsweredBadge = "trt4";
        }
        if (totalAnsweredBadgeLevel == 5){
            totalAnsweredBadge = "trt5";
        }

        winningPercentageBadge = "";
        if (winningPerentageBadgeLevel == 1){
            winningPercentageBadge = "win1";
        }
        if (winningPerentageBadgeLevel == 2){
            winningPercentageBadge = "win2";
        }
        if (winningPerentageBadgeLevel == 3){
            winningPercentageBadge = "win3";
        }
        if (winningPerentageBadgeLevel == 4){
            winningPercentageBadge = "win4";
        }
        if (winningPerentageBadgeLevel == 5){
            winningPercentageBadge = "win5";
        }

        artBonusBadge = "";
        if (artBonusBadgeLevel == 1){
            artBonusBadge = "bon";
        }

        fbShareBadge = "";
        if (fbShareBadgeLevel == 1){
            fbShareBadge = "sha";
        }

        villainsRevealedBadge = "";
        if (villainsRevealedLevel == 1){
            villainsRevealedBadge = "vil";
        }

        premiumBadge = "";
        if (premiumLevel == 1){
            premiumBadge = "pre";
        }

        antiquityBadge ="";
        if (antiquityBadgeLevel == 1){
            antiquityBadge = "xant1";
        }
        if (antiquityBadgeLevel == 2){
            antiquityBadge = "xant2";
        }
        if (antiquityBadgeLevel == 3){
            antiquityBadge = "xant3";
        }
        if (antiquityBadgeLevel == 4){
            antiquityBadge = "xant4";
        }
        if (antiquityBadgeLevel == 5){
            antiquityBadge = "xant5";
        }

        medievalBadge = "";
        if (medievalBadgeLevel == 1){
            medievalBadge = "xmed1";
        }
        if (medievalBadgeLevel == 2){
            medievalBadge = "xmed2";
        }
        if (medievalBadgeLevel == 3){
            medievalBadge = "xmed3";
        }
        if (medievalBadgeLevel == 4){
            medievalBadge = "xmed4";
        }
        if (medievalBadgeLevel == 5){
            medievalBadge = "xmed5";
        }

        renaissanceBadge = "";
        if (renaissanceBadgeLevel == 1){
            renaissanceBadge = "xren1";
        }
        if (renaissanceBadgeLevel == 2){
            renaissanceBadge = "xren2";
        }
        if (renaissanceBadgeLevel == 3){
            renaissanceBadge = "xren3";
        }
        if (renaissanceBadgeLevel == 4){
            renaissanceBadge = "xren4";
        }
        if (renaissanceBadgeLevel == 5){
            renaissanceBadge = "xren5";
        }

        enlightenmentBadge = "";
        if (enlightenmentBadgeLevel == 1){
            enlightenmentBadge = "xenl1";
        }
        if (enlightenmentBadgeLevel == 2){
            enlightenmentBadge = "xenl2";
        }
        if (enlightenmentBadgeLevel == 3){
            enlightenmentBadge = "xenl3";
        }
        if (enlightenmentBadgeLevel == 4){
            enlightenmentBadge = "xenl4";
        }
        if (enlightenmentBadgeLevel == 5){
            enlightenmentBadge = "xenl5";
        }

        modernBadge = "";
        if (modernBadgeLevel == 1){
            modernBadge = "xmod1";
        }
        if (modernBadgeLevel == 2){
            modernBadge = "xmod2";
        }
        if (modernBadgeLevel == 3){
            modernBadge = "xmod3";
        }
        if (modernBadgeLevel == 4){
            modernBadge = "xmod4";
        }
        if (modernBadgeLevel == 5){
            modernBadge = "xmod5";
        }

        contemporaryBadge = "";
        if (contemporaryBadgeLevel == 1){
            contemporaryBadge = "xcon1";
        }
        if (contemporaryBadgeLevel == 2){
            contemporaryBadge = "xcon2";
        }
        if (contemporaryBadgeLevel == 3){
            contemporaryBadge = "xcon3";
        }
        if (contemporaryBadgeLevel == 4){
            contemporaryBadge = "xcon4";
        }
        if (contemporaryBadgeLevel == 5){
            contemporaryBadge = "xcon5";
        }

       // Toast.makeText(ProfileView.this, supporterBadge, Toast.LENGTH_LONG).show();

        Query supporterBadgeProfileQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(supporterBadge);
        supporterBadgeProfileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges: badgeProfSnapshot.getChildren()) {

                    if (supporterBadgeLevel > 0) {
                        String imageBadgeProfLink = badges.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgSupporterBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Query longestStreakBadgeProfileQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(longestStreakBadge);
        longestStreakBadgeProfileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges2: badgeProfSnapshot.getChildren()) {

                    if (longestStreakBadgeLevel > 0) {
                        String imageBadgeProfLink = badges2.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgLongStreakBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Query totalAnsweredBadgeProfileQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(totalAnsweredBadge);
        totalAnsweredBadgeProfileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges3: badgeProfSnapshot.getChildren()) {

                    if (totalAnsweredBadgeLevel > 0) {
                        String imageBadgeProfLink = badges3.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgTotalAnsweredBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Query winningPercentageBadgeProfileQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(winningPercentageBadge);
        winningPercentageBadgeProfileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges4: badgeProfSnapshot.getChildren()) {

                    if (winningPerentageBadgeLevel > 0) {
                        String imageBadgeProfLink = badges4.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgWinningPercentageBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Query artBonusBadgeQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(artBonusBadge);
        artBonusBadgeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges11: badgeProfSnapshot.getChildren()) {

                    if (artBonusBadgeLevel > 0) {
                        String imageBadgeProfLink = badges11.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgArtBonusBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Query fbShareBadgeQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(fbShareBadge);
        fbShareBadgeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges11: badgeProfSnapshot.getChildren()) {

                    if (fbShareBadgeLevel > 0) {
                        String imageBadgeProfLink = badges11.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgFBShareBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Query villainsRevealedQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(villainsRevealedBadge);
        villainsRevealedQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges12: badgeProfSnapshot.getChildren()) {

                    if (villainsRevealedLevel > 0) {
                        String imageBadgeProfLink = badges12.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgVillainsRevealedBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Query premiumQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(premiumBadge);
        premiumQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges13: badgeProfSnapshot.getChildren()) {

                    if (premiumLevel > 0) {
                        String imageBadgeProfLink = badges13.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgPremiumBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Query antiquityBadgeProfileQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(antiquityBadge);
        antiquityBadgeProfileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges5: badgeProfSnapshot.getChildren()) {

                    if (antiquityBadgeLevel > 0) {
                        String imageBadgeProfLink = badges5.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgAntiquityBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Query medievalBadgeProfileQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(medievalBadge);
        medievalBadgeProfileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges6: badgeProfSnapshot.getChildren()) {

                    if (medievalBadgeLevel > 0) {
                        String imageBadgeProfLink = badges6.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgMedievalBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Query renaissanceBadgeProfileQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(renaissanceBadge);
        renaissanceBadgeProfileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges7: badgeProfSnapshot.getChildren()) {

                    if (renaissanceBadgeLevel > 0) {
                        String imageBadgeProfLink = badges7.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgRenaissanceBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Query enlightenmentBadgeProfileQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(enlightenmentBadge);
        enlightenmentBadgeProfileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges8: badgeProfSnapshot.getChildren()) {

                    if (enlightenmentBadgeLevel > 0) {
                        String imageBadgeProfLink = badges8.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgEnlightenmentBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Query modernBadgeProfileQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(modernBadge);
        modernBadgeProfileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges9: badgeProfSnapshot.getChildren()) {

                    if (modernBadgeLevel > 0) {
                        String imageBadgeProfLink = badges9.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgModernBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Query contemporaryBadgeProfileQuery = FirebaseDatabase.getInstance().getReference().child("badges").orderByChild("badgename").equalTo(contemporaryBadge);
        contemporaryBadgeProfileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot badgeProfSnapshot) {
                for (DataSnapshot badges10: badgeProfSnapshot.getChildren()) {

                    if (contemporaryBadgeLevel > 0) {
                        String imageBadgeProfLink = badges10.child("badgeimagelink").getValue().toString();
                        Picasso.get().load(imageBadgeProfLink).into(imgContemporaryBadgeX);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    //////////////////Badges ends ///////////////////////////////////////////////////////////////////////

    private void alertDialogLogOut() {

        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzz_dialog_logout, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        dialog.show();

        ImageView imgIconX = view.findViewById(R.id.imgIcon);
        imgIconX.setImageDrawable(getResources().getDrawable(R.drawable.logout));

        TextView txtTitleX = view.findViewById(R.id.txtTitle);
        txtTitleX.setText("Logout");

        TextView txtMsgX = view.findViewById(R.id.txtMsg);
        txtMsgX.setText("Do you really want to Logout?");

        Button btnYesX = view.findViewById(R.id.btnYes);
        btnYesX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuthPF.signOut();

                logoutSnackbar();
                transitionBackToLogin ();
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
    //Method called from LogOut to get us back to Login screen
    private void transitionBackToLogin () {

        new CountDownTimer(1000, 500) {


            public void onTick(long millisUntilFinished) {
                // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
            }

            public void onFinish() {
                Intent intent = new Intent(ProfileView.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    private void logoutSnackbar(){

        Snackbar snackbar;

        snackbar = Snackbar.make(loutProfileViewX, "Good bye", Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorPrimaryDark));

        snackbar.show();


        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);

    }

    private void fagDisplay() {

        shadeX.setVisibility(View.VISIBLE);


        //Everything in this method is code for a custom dialog
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.zzx_dia_view_faq, null);

        dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.show();
        dialog.getWindow().setAttributes(lp);

        ImageView btnFAQbackX = view.findViewById(R.id.btnFAQback);
        btnFAQbackX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shadeX.setVisibility(View.INVISIBLE);
                dialog.dismiss();

            }
        });

    }


    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    public void onBackPressed() {

        shadeX.setVisibility(View.GONE);

        if(alteredPicToggle.equals("yes") | alteredNameToggle.equals("yes") | alteredFlagPicToggle.equals("yes")) {

            //Everything in this method is code for a custom dialog
            LayoutInflater inflater = LayoutInflater.from(this);
            View view = inflater.inflate(R.layout.zzz_dialog_logout, null);



            dialog = new AlertDialog.Builder(this)
                    .setView(view)
                    .create();

            dialog.show();

            TextView txtMsgX = view.findViewById(R.id.txtMsg);
            txtMsgX.setText("You have some unsaved changes. Do you really want to exit the Profile screen?");

            Button btnYesX = view.findViewById(R.id.btnYes);
            btnYesX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Intent intent = new Intent(ProfileView.this, HomePage.class);
////                    startActivity(intent);

                    finish();

                }
            });

            Button btnNoX = view.findViewById(R.id.btnNo);
            btnNoX.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        } else {

            finish();

        }

    }

    private void alreadyPremiumSnackbar(){

        Snackbar snackbar;

        snackbar = Snackbar.make(loutProfileViewX, "You are already a Premium Member", Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();


        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);

    }


}
