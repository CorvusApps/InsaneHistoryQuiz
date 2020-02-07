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

public class MainActivity extends AppCompatActivity {

    //Google sign in variables

    GoogleSignInClient mGoogleSignInClient;
    SignInButton btnGoogleLoginX;
    FirebaseAuth mAuth;
    String email;

            // Google login graphical overlay variables

            ImageView imgGoogleLogoOverlayX;
            TextView txtGoogleLoginOverlayTextX;

            // For login success snackbar
            ConstraintLayout loutMainActivityX;

    TextView txtFBtestX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Firebase basics

        mAuth = FirebaseAuth.getInstance();


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

                Toast.makeText(MainActivity.this, "in google", Toast.LENGTH_SHORT).show();

                googleSignIn();

            }
        });

        // Google Login graphical overlays

        imgGoogleLogoOverlayX = findViewById(R.id.imgGoogleLogoOverlay);
        imgGoogleLogoOverlayX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnGoogleLoginX.performClick();
                Toast.makeText(MainActivity.this, "in overlay", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();


                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("questions");
                DatabaseReference dbPushReference = dbReference.push();

                HashMap<String, Object> dataMap = new HashMap<>();

                dataMap.put("ask", "what the fuck");
                dataMap.put("answer", "fuck you");

                dbPushReference.setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        Toast.makeText(MainActivity.this,"question added", Toast.LENGTH_SHORT).show();
                    }
                });




//                Query query = dbReference.orderByChild("questions");
//
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
//
//                            ds.getRef().child("ask").setValue("what the fuck?");
//                            ds.getRef().child("answer").setValue("fuck you");
//
//                            Toast.makeText(MainActivity.this,"git2", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });

                ///////////// END of testing stuff in oncreate //////////////////////////////////////////

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        try {

            // Check if user is signed in (non-null) and update UI accordingly.
            FirebaseUser currentUser = mAuth.getCurrentUser();

            if (currentUser != null) {

                // Toast.makeText(Login.this, currentUser.toString(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(MainActivity.this, HomePage.class);
                startActivity(intent);
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


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


                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
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

                        loginSnackbar();

                        transitionToHome();

                    }

                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                //Snackbar.make(loutLoginX, e.getMessage(), Snackbar.LENGTH_INDEFINITE).show();
            }
        });

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

    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    public void onBackPressed() {



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


}
