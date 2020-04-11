package com.pelotheban.insanehistoryquiz;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Google sign in variables

    GoogleSignInClient mGoogleSignInClient;
    SignInButton btnGoogleLoginX;
    FirebaseAuth mAuth;

    FirebaseAuth.AuthStateListener mAuthListener;

    String email;

            // Google login graphical overlay variables

            ImageView imgGoogleLogoOverlayX;
            TextView txtGoogleLoginOverlayTextX;

            // For login success snackbar
            ConstraintLayout loutMainActivityX;


    // Place holder test stuff - not used for anything right now
    TextView txtFBtestX;

    /// For default value to be written to Firebase

    private String userUID;
    private DatabaseReference profileReference;

    StringBuilder randomStringBuilder; // needed to generate random user name
    TextView txtPlacehoderX;
    String randomProfileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase basics

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i("LOGIN", "in authstate changed");

                if (firebaseAuth.getCurrentUser() != null) {
                    Log.i("LOGIN", "in authstate changed" + firebaseAuth.getCurrentUser().getUid());
                    Intent intent = new Intent(MainActivity.this, HomePage.class);
                    startActivity(intent);
                    finish();
                } else {
                    // User is signed out
                    Log.i("LOGIN", "onAuthStateChanged:signed_out");

                }

            }
        };



        /// For default value to be written to Firebase
        txtPlacehoderX = findViewById(R.id.txtPlaceholder);

        // Configure google sign-in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) //getString(R.string.default_web_client_id
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        //Google sign in variables

        btnGoogleLoginX = (SignInButton) findViewById(R.id.btnGoogleLogin);
        btnGoogleLoginX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                googleSignIn();

            }
        });

        // Google Login graphical overlays

        imgGoogleLogoOverlayX = findViewById(R.id.imgGoogleLogoOverlay);
        imgGoogleLogoOverlayX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnGoogleLoginX.performClick(); // this doesn't really work so doing it directly below by just going to googlesignin

                googleSignIn();

            }
        });

        txtGoogleLoginOverlayTextX = findViewById(R.id.txtGoogleLoginOverlayText);
        txtGoogleLoginOverlayTextX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnGoogleLoginX.performClick(); // the perfomrm clicks don't work - may have to be in the same view as the button but no need to expertiment as can call google directly
                googleSignIn();

            }
        });

        // For login success snackbar
        loutMainActivityX = findViewById(R.id.loutMainActivity);


        //////// TESTING STUFF ////////////////////////////////////////////////////////////////////////////////


        txtFBtestX = findViewById(R.id.txtFBtest);

        txtFBtestX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();


            }
        });

    }
// The onStart test to see if already logged in and if so goes straight to home page
    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

///////////////////// BEGIN GOOGLE SIGN IN METHODS ///////////////////////////////////
    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == 101) {


                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount acct = task.getResult(ApiException.class);

                    firebaseAuthWithGoogle(acct);
                } catch (ApiException e) {


                 //   Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d("GOOGLE", "firebaseAuthWithGoogle:" + acct.getId());


        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Sign in success, update I with the signed-in user's information

                        FirebaseUser user = mAuth.getCurrentUser();

                        email = authResult.getUser().getEmail();

                        ///////////////// START Get a bunch of default values into firebase

                          userUID = FirebaseAuth.getInstance().getUid();
//                        profileReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(userUID);
//

                        addFirebaseProperties (); // just cleaner to do it in a seperate method
                        ////////////////  End get a bunch of default values into firebase

                        loginSnackbar();

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

             //   Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();


            }
        });

    }

    private void  addFirebaseProperties () {


        profileReference = FirebaseDatabase.getInstance().getReference().child("my_users").child(userUID);
        profileReference.getRef().child("user").setValue(userUID);

        Query loginProfileQuery = FirebaseDatabase.getInstance().getReference().child("my_users").orderByChild("user").equalTo(userUID);
        loginProfileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userLs: dataSnapshot.getChildren()){

                    try { // if this is successful nothing happens (profile already in) if it's null, crashes, gets caught and then a random is generated below

                         String profilenametemp = userLs.child("profilename").getValue().toString();
                         txtFBtestX.setText(profilenametemp);

                    } catch (Exception e) {

                        Random generator = new Random();
                        randomStringBuilder = new StringBuilder();

                        char tempChar;
                        for (int i = 0; i < 15; i++){
                            tempChar = (char) (generator.nextInt(96) + 32);
                            randomStringBuilder.append(tempChar).toString();
                            txtPlacehoderX.setText(randomStringBuilder);

                        }
                        randomProfileName = txtPlacehoderX.getText().toString();
                        profileReference.getRef().child("profilename").setValue(randomProfileName); //puts the generated profilename to FB
                        // in theory could have added the initial coin donation and sort settings here but originally done
                        // in homepage so kept it there to avoid complications of moving it
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        transitionToHome();


    }

    private void transitionToHome () {

        new CountDownTimer(2000, 500) {

            public void onTick(long millisUntilFinished) {
                // imgCoverR.animate().rotation(360).setDuration(500); // why only turned once?
            }

            public void onFinish() {
                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    private void loginSnackbar(){

        Snackbar snackbar;

        snackbar = Snackbar.make(loutMainActivityX, "Welcome: " + email, Snackbar.LENGTH_INDEFINITE);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorPrimaryDark));

        snackbar.show();


        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);

    }

    ////// END OF GOOGLE LOGIN METHODS //////////////////////////////////////////////////////////////

    @Override
    @SuppressLint("RestrictedApi") // apparently onBackPressed is depricated but this suppresses the error
    public void onBackPressed() {

    // basically makes the back arrow non-functional

    }




}
