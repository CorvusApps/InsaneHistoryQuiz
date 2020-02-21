package com.pelotheban.insanehistoryquiz;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class ProfileView extends AppCompatActivity {

    private TextView txtUIDX, txtProfileNameX;

    ///Fifebase

   String userUID, profileName;
   DatabaseReference profileReference;
   Query profileQuery;

   //Badges

    ImageView imgLongStreakBadgeX, imgTotalAnsweredBadgeX, imgTotalQuestionsBadgeX;

    String longestStreakString;
    int longestStreak;
    int longestStreakBadgeLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        txtUIDX = findViewById(R.id.txtUID);
        txtProfileNameX = findViewById(R.id.txtProfileName);

        userUID = FirebaseAuth.getInstance().getUid();
        profileReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(userUID);
        //txtUIDX.setText(userUID);


            Random generator = new Random();
            StringBuilder randomStringBuilder = new StringBuilder();

            char tempChar;
            for (int i = 0; i < 15; i++){
                tempChar = (char) (generator.nextInt(96) + 32);
                randomStringBuilder.append(tempChar).toString();

                txtUIDX.setText(randomStringBuilder);
            }
            //return randomStringBuilder.toString();


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

                        longestStreakString = userPs.child("longeststreak").getValue().toString();
                        longestStreak = Integer.valueOf(longestStreakString);

                        if (longestStreak >4) {
                            imgLongStreakBadgeX.setImageResource(R.drawable.badgeone);
                        }

                        if (longestStreak >9) {
                            imgLongStreakBadgeX.setImageResource(R.drawable.badgetwo);
                        }

                        if (longestStreak >19) {
                            imgLongStreakBadgeX.setImageResource(R.drawable.badgethree);
                        }

                    } catch (Exception e) {


                    }




                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        imgLongStreakBadgeX = findViewById(R.id.imgLongStreakBadge);
        imgTotalAnsweredBadgeX = findViewById(R.id.imgTotalAnsweredBadge);
        imgTotalQuestionsBadgeX = findViewById(R.id.imgTotalQuestionsBadge);





    }







}
