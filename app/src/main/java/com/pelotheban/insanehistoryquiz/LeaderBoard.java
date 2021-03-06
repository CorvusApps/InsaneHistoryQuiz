package com.pelotheban.insanehistoryquiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mukesh.countrypicker.OnCountryPickerListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;

public class LeaderBoard extends AppCompatActivity {

    // basic UI set up
    private AlertDialog dialog;
    private LinearLayout loutLeaderBoardX;
    private TextView txtScoreX;
    private int width2;


    //leader board selector buttons and recycler views
    private Button btnMostPlayedX, btnMostRightX, btnLongestStreakX, btnMostPlayed2X, btnMostRight2X, btnLongestStreak2X;
    private Button btnInterstitialX, btnRewardedX, btnIntestitial2X, btnRewarded2X, btnFBSharesX, btnFBShares2X;
    private RecyclerView rcvLongestStreakX;


    private LinearLayoutManager layoutManagerLeaders;

    private String boardToggle;


    //popUp Menu

    private String popupMenuToggle;
    private FloatingActionButton fabPopUpLBX, fabPopUpCollLBX, fabPopUpFAQminiLBtX, fabPopUpLogOutminiLBX, fabPopUpPremiumminiLBX;
    private TextView txtFAQButtonLBX, txtLogoutButtonLBX, txtPremiumButtonLBX;
    private View shadeX; // to shade the background when menu out

    //Firebase

    private FirebaseAuth mAuthLB;
    private DatabaseReference  lbReference;
    private Query sortLBQuery;

    private SharedPreferences sortSharedPrefLeaders, boardToggleSharedPrefLeaders;

    //For score counters

    private Query sortUserLBQuery;

    private String coinsOwnedString, totalAnsweredString, longestStreakString;
    private String interstitialString, rewardString;

    private TextView lbTxtCoinCounterX, lbTxtMostRightX, lbTxtLongestStreak;

    private String uid;

    // To identify user in the recyclerview

    private String profileName;

    // Facebook Share

    private FloatingActionButton fabShareLBX;

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
        setContentView(R.layout.activity_leader_board);

        loutLeaderBoardX = findViewById(R.id.loutLeaderBoard);
        txtScoreX = findViewById(R.id.txtScore);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
       // double height = size.y;

      //  height2 = (int) Math.round(height);
        width2 = (int) Math.round(width);

        // firebase and sorting
        lbReference = FirebaseDatabase.getInstance().getReference().child("my_users");
        lbReference.keepSynced(true);

        mAuthLB = FirebaseAuth.getInstance();

        sortSharedPrefLeaders = getSharedPreferences("SortSetting2", MODE_PRIVATE);
        String mSorting2 = sortSharedPrefLeaders.getString("Sort2", "longeststreaksort"); // where if no settings

        //sortLBQuery = lbReference.orderByChild("totalquestions");
        sortLBQuery = lbReference.orderByChild(mSorting2).startAt(-100000).limitToFirst(15);

        //counters for the current user that go on top of buttons - NOT the ones in recyclerview

        lbTxtCoinCounterX = findViewById(R.id.lbTxtCoinCounter);
        lbTxtMostRightX = findViewById(R.id.lbTxtMostRight);
        lbTxtLongestStreak = findViewById(R.id.lbTxtLongestStreak);

        uid = mAuthLB.getUid();
        sortUserLBQuery = FirebaseDatabase.getInstance().getReference().child("my_users").orderByChild("user").equalTo(uid);
        sortUserLBQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userDs: dataSnapshot.getChildren()){

                    try {
                        coinsOwnedString = userDs.child("coins").getValue().toString();

                    } catch (Exception e) {

                    }

                    try {
                        totalAnsweredString = userDs.child("totalanswered").getValue().toString();

                    } catch (Exception e) {

                    }

                    try {
                        longestStreakString = userDs.child("longeststreak").getValue().toString();

                    } catch (Exception e) {

                    }

                    lbTxtCoinCounterX.setText(coinsOwnedString);
                    lbTxtMostRightX.setText(totalAnsweredString);
                    lbTxtLongestStreak.setText(longestStreakString);
                    Log.i("PREMIUM", " other diagnsotic lbtxtlongstreak= " + longestStreakString);
                    Log.i("PREMIUM", " other diagnsotic coins= " + coinsOwnedString);

                    try {
                        profileName = userDs.child("profilename").getValue().toString();

                    } catch (Exception e) {

                    }

                    try {
                        showPremiumDialogToggle2 = userDs.child("premiumasktoggle").getValue().toString();
                        Log.i("PREMIUM", " in try showp= " + showPremiumDialogToggle2);

                    } catch (Exception e) {

                        showPremiumDialogToggle2 = "catch";
                        Log.i("PREMIUM", " in catch showp= " + showPremiumDialogToggle2);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ///// END OF COUNTERS SECTION /////////////////////////////////////////////////////

        //pop up
        fabPopUpLBX = findViewById(R.id.fabPopUpLB);
        fabPopUpLBX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowNewFABbasedMenu();
            }
        });

        popupMenuToggle = "Not";

        fabPopUpCollLBX = findViewById(R.id.fabPopUpCollLB);
        fabPopUpFAQminiLBtX = findViewById(R.id.fabPopUpFAQminiLB);
        fabPopUpLogOutminiLBX = findViewById(R.id.fabPopUpLogOutminiLB);
        fabPopUpPremiumminiLBX = findViewById(R.id.fabPopUpPremiumminiLB);

        txtFAQButtonLBX = findViewById(R.id.txtFAQButtonLB);
        txtLogoutButtonLBX = findViewById(R.id.txtLogoutButtonLB);
        txtPremiumButtonLBX = findViewById(R.id.txtPremiumButtonLB);

        shadeX = findViewById(R.id.shade);

        /// end of pop up

        /// Facebook share

        imgTestScreenshotX = findViewById(R.id.imgTestScreeshotLB);
        fabShareLBX = findViewById(R.id.fabShareLB);

        if (width2 > 1500) {

            fabShareLBX.animate().translationX(125);
        }

        fabShareLBX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(LeaderBoard.this);
                View view = inflater.inflate(R.layout.zzz_fbshare_dialog, null);

                dialog = new AlertDialog.Builder(LeaderBoard.this)
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
                    FileOutputStream stream = LeaderBoard.this.openFileOutput(filename, Context.MODE_PRIVATE);
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
                                    Intent intent = new Intent(LeaderBoard.this, FacebookShare.class);
                                    intent.putExtra("image", filename);
                                    intent.putExtra("source", "leaderboard");
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

        /////////////////////////// RECYCLER VIEW SECTION BEGINS - includes buttons to select recycler views //////////////

        rcvLongestStreakX = findViewById(R.id.rcvLongestStreak);

        boardToggleSharedPrefLeaders = getSharedPreferences("BoardToggleSetting2", MODE_PRIVATE);
        boardToggle = boardToggleSharedPrefLeaders.getString("BoardToggle2", "1");

        btnMostPlayedX = findViewById(R.id.btnMostPlayed);
        btnMostRightX = findViewById(R.id.btnMostRight);
        btnLongestStreakX = findViewById(R.id.btnLongestStreak);

        btnMostPlayed2X = findViewById(R.id.btnMostPlayed2);
        btnMostRight2X = findViewById(R.id.btnMostRight2);
        btnLongestStreak2X = findViewById(R.id.btnLongestStreak2);

        btnInterstitialX = findViewById(R.id.btnInterstitial);
        btnRewardedX = findViewById(R.id.btnReward);

        btnIntestitial2X = findViewById(R.id.btnInterstitial2);
        btnRewarded2X = findViewById(R.id.btnReward2);

        btnFBSharesX = findViewById(R.id.btnFBShares);
        btnFBShares2X = findViewById(R.id.btnFBShares2);

        /// these button visibility combos play off the shared pref so work when user comes back to this screen at a later time
        if (boardToggle.equals("1")) {
            btnLongestStreakX.setVisibility(View.GONE);
            btnLongestStreak2X.setVisibility(View.VISIBLE);
            txtScoreX.setText("Streak");
            btnMostRightX.setVisibility(View.VISIBLE);
            btnMostRight2X.setVisibility(View.GONE);
            btnMostPlayedX.setVisibility(View.VISIBLE);
            btnMostPlayed2X.setVisibility(View.GONE);

        }

        if (boardToggle.equals("2")) {
            btnLongestStreakX.setVisibility(View.VISIBLE);
            btnLongestStreak2X.setVisibility(View.GONE);
            btnMostRightX.setVisibility(View.GONE);
            btnMostRight2X.setVisibility(View.VISIBLE);
            txtScoreX.setText("Answers");
            btnMostPlayedX.setVisibility(View.VISIBLE);
            btnMostPlayed2X.setVisibility(View.GONE);
        }
        if (boardToggle.equals("3")) {
            btnLongestStreakX.setVisibility(View.VISIBLE);
            btnLongestStreak2X.setVisibility(View.GONE);
            btnMostRightX.setVisibility(View.VISIBLE);
            btnMostRight2X.setVisibility(View.GONE);
            btnMostPlayedX.setVisibility(View.GONE);
            btnMostPlayed2X.setVisibility(View.VISIBLE);
            txtScoreX.setText("Gold");
        }

        if (boardToggle.equals("4")) {

            btnInterstitialX.setVisibility(View.GONE);
            btnIntestitial2X.setVisibility(View.VISIBLE);

        }

        if (boardToggle.equals("5")) {

            btnRewardedX.setVisibility(View.GONE);
            btnRewarded2X.setVisibility(View.VISIBLE);

        }

        if (boardToggle.equals("6")) {

            btnFBSharesX.setVisibility(View.GONE);
            btnFBShares2X.setVisibility(View.VISIBLE);

        }



        layoutManagerLeaders = new LinearLayoutManager(this);
        layoutManagerLeaders.setReverseLayout(false);

        rcvLongestStreakX.setHasFixedSize(true); //Not sure this applies or why it is here
        rcvLongestStreakX.setLayoutManager(layoutManagerLeaders);

        //leader board selector buttons
        // repurposed this one to most gold just didn't change all the names

        btnMostPlayedX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sortSharedPrefLeaders.edit();
                editor.putString("Sort2", "coinsownedsort");
                editor.apply(); // saves the value

                SharedPreferences.Editor editor2 = boardToggleSharedPrefLeaders.edit();
                editor2.putString("BoardToggle2", "3");
                editor2.apply(); // saves the value

                recreate(); // restart activity to take effect

            }
        });

        btnMostRightX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences.Editor editor = sortSharedPrefLeaders.edit();
                editor.putString("Sort2", "totalansweredsort");
                editor.apply(); // saves the value

                SharedPreferences.Editor editor2 = boardToggleSharedPrefLeaders.edit();
                editor2.putString("BoardToggle2", "2");
                editor2.apply(); // saves the value

                recreate(); // restart activity to take effect

            }
        });

        btnLongestStreakX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences.Editor editor = sortSharedPrefLeaders.edit();
                editor.putString("Sort2", "longeststreaksort");
                editor.apply(); // saves the value

                SharedPreferences.Editor editor2 = boardToggleSharedPrefLeaders.edit();
                editor2.putString("BoardToggle2", "1");
                editor2.apply(); // saves the value

                recreate(); // restart activity to take effect

            }
        });

        btnInterstitialX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sortSharedPrefLeaders.edit();
                editor.putString("Sort2", "zzzinterstitialsort");
                editor.apply(); // saves the value

                SharedPreferences.Editor editor2 = boardToggleSharedPrefLeaders.edit();
                editor2.putString("BoardToggle2", "4");
                editor2.apply(); // saves the value

                recreate(); // restart activity to take effect


            }
        });

        btnRewardedX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sortSharedPrefLeaders.edit();
                editor.putString("Sort2", "zzzrewardsort");
                editor.apply(); // saves the value

                SharedPreferences.Editor editor2 = boardToggleSharedPrefLeaders.edit();
                editor2.putString("BoardToggle2", "5");
                editor2.apply(); // saves the value

                recreate(); // restart activity to take effect


            }
        });

        btnFBSharesX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sortSharedPrefLeaders.edit();
                editor.putString("Sort2", "fbsharessort");
                editor.apply(); // saves the value

                SharedPreferences.Editor editor2 = boardToggleSharedPrefLeaders.edit();
                editor2.putString("BoardToggle2", "6");
                editor2.apply(); // saves the value

                recreate(); // restart activity to take effect


            }
        });

        /// note - eventhough rv called longstreak actually covers all the views - legacy name from earlier approach
        //////////////////////// START ------> ACTUAL RECYCLER VIEW COMPONENTS /////////////////////////////////////////////////////
        /////////////////// includes: viewholder, sort, expoloding card view dialog, functions from dialog //////////////////
        final FirebaseRecyclerAdapter<ZZZjcLBlongeststreak, LeaderBoard.ZZZjcLBlongstreakViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<ZZZjcLBlongeststreak, LeaderBoard.ZZZjcLBlongstreakViewHolder>
                (ZZZjcLBlongeststreak.class,R.layout.yyy_leaders, LeaderBoard.ZZZjcLBlongstreakViewHolder.class,sortLBQuery) {


            @Override
            protected void populateViewHolder(LeaderBoard.ZZZjcLBlongstreakViewHolder viewHolder, ZZZjcLBlongeststreak model, int position) {

                viewHolder.setRank(position);


                if (boardToggle.equals("1")) {
                    viewHolder.setLongeststreak(model.getLongeststreak());
                    viewHolder.setProfilename(model.getProfilename(), profileName);

                }

                if (boardToggle.equals("2")) {
                    viewHolder.setTotalanswered(model.getTotalanswered());
                    viewHolder.setProfilename(model.getProfilename(), profileName);

                }

                if (boardToggle.equals("3")) {
                    viewHolder.setCoins(model.getCoins());
                    viewHolder.setProfilename(model.getProfilename(), profileName);

                }

                if (boardToggle.equals("4")) {

                    viewHolder.setZzzinterstitial(model.getZzzinterstitial());
                    viewHolder.setUser(model.getUser());
                }

                if (boardToggle.equals("5")) {

                    viewHolder.setZzzreward(model.getZzzreward());
                    viewHolder.setUser(model.getUser());
                }

                if (boardToggle.equals("6")) {

                    viewHolder.setFbshares(model.getFbshares());
                    viewHolder.setUser(model.getUser());
                }

                viewHolder.setImageStock();
                viewHolder.setImage(getApplicationContext(),model.getImagelink());

                viewHolder.setImageflagStock();
                viewHolder.setImageflag(getApplicationContext(),model.getImageflaglink());



            }

            // The Code setting out recycler view /////////////////////////////////////////////////////////////////

            // This method is part of the onItemClick AND onLongItem Click code NOT to populate the recycler view /////////////////////////////////////////////////////
            @Override
            public LeaderBoard.ZZZjcLBlongstreakViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                final LeaderBoard.ZZZjcLBlongstreakViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnItemClickListener(new ZZZjcLBlongstreakViewHolder.OnItemClickListener() {


                    @Override
                    public void onItemLongClick(View view, int position) {


                    }

                    // inflates card from recycler view to see fields not visible in base view
                    @Override
                    public void onItemClick(View view, int position) {

                        //All this functionality was to allow FB to be reset to zero if you click on any person on leaderboard to 0
                        // no longer necessary as eliminated leaderboards capturing partial logins
//                        TextView txtPlayerX = (TextView)view.findViewById(R.id.txtPlayer);
//                        String fbuser = txtPlayerX.getText().toString();
//
//                        Log.i("LISTENING", "here: " + lbReference.getRef());
//
//                        final DatabaseReference fbref = lbReference.child(fbuser);
//
//                        Log.i("LISTENING", "fbref " + fbref.getRef());
//
//                        Query fbQuery = fbref;
//
//                        fbQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                                for (DataSnapshot userFB: dataSnapshot.getChildren()) {
//
//                                    fbref.getRef().child("fbsharessort").setValue(0);
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });


                       // DatabaseReference fbsetref = firebaseRecyclerAdapter.getRef(position);


                    }

                });

                return viewHolder;
            }

        };

        ///////////////////////////////////////////////////////

        // The onclick methods were in the broader recycler view methods - this calls for the adapter on everything
        rcvLongestStreakX.setAdapter(firebaseRecyclerAdapter);

    }


    // View holder for the recycler view
    public static class ZZZjcLBlongstreakViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public ZZZjcLBlongstreakViewHolder(View itemView) {

            super(itemView);
            mView = itemView;

            // Custom built onItemClickListener for the recycler view
            ////////////////////////////////////////////////////////////////////////////////////////
            //Listen to the video as this is a bit confusing - also added the OnTimeclick listener above to the parameters NOT

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    hpListener.onItemClick(view, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    hpListener.onItemLongClick(view, getAdapterPosition());

                    return true;
                }
            });

            ////////////////////////////////////////////////////////////////////////////////////////

        }

        //setting all the info from collection add to cardview;some will be hidden and passed on to expanded coin view or other activities


        public void setRank (int position) {

            TextView txtRankX = (TextView)mView.findViewById(R.id.txtRank);
            txtRankX.setText(String.valueOf(position + 1));
        }


        public void setLongeststreak(int longstreak){

            TextView txtScoreX = (TextView)mView.findViewById(R.id.txtScore);
            String score2 = String.valueOf(longstreak);

            txtScoreX.setText(score2);

        }

        public void setProfilename(String profilename, String profileName){

            TextView txtPlayerX = (TextView)mView.findViewById(R.id.txtPlayer);

            txtPlayerX.setText(profilename);

            if (profileName.equals(profilename)){

                LinearLayout loutRankItemX = (LinearLayout) mView.findViewById(R.id.loutRankItem);
                loutRankItemX.setBackgroundResource(R.color.colorAccent);

            }

        }

        public void setTotalanswered(int totalanswered){

            TextView txtScoreX = (TextView)mView.findViewById(R.id.txtScore);
            String score2 = String.valueOf(totalanswered);

            txtScoreX.setText(score2);

        }

        public void setCoins(int coins){

            TextView txtScoreX = (TextView)mView.findViewById(R.id.txtScore);
            String score2 = String.valueOf(coins);

            txtScoreX.setText(score2);

        }

        public void setImageStock(){

            ImageView imgRVProfileX = (ImageView) mView.findViewById(R.id.imgRVProfile);
            imgRVProfileX.setBackgroundResource(R.drawable.profile);

        }

        public void setImage(Context ctx, final String imageLink){

            if (imageLink != "" && imageLink != null) {
                ImageView imgRVProfileX = (ImageView) mView.findViewById(R.id.imgRVProfile);
                Picasso.get().load(imageLink).into(imgRVProfileX); //tutorial had with which got renamed to get but with took ctx as parameter...
            } else {

                ImageView imgRVProfileX = (ImageView) mView.findViewById(R.id.imgRVProfile);
                imgRVProfileX.setBackgroundResource(R.drawable.profile);

            }

        }
        public void setImageflagStock(){

            ImageView imgRVFlagX = (ImageView) mView.findViewById(R.id.imgRVFlag);
            imgRVFlagX.setBackgroundResource(R.drawable.unflag);

        }

        public void setImageflag(Context ctx, final String imageflagLink){


          ImageView imgRVFlagX = (ImageView) mView.findViewById(R.id.imgRVFlag);
          //  imgRVFlagX.setBackgroundResource(R.drawable.unflag);

            try {
                if (imageflagLink != "") {
                   // ImageView imgRVFlagX = (ImageView) mView.findViewById(R.id.imgRVFlag);
                    Picasso.get().load(imageflagLink).into(imgRVFlagX); //tutorial had with which got renamed to get but with took ctx as parameter...
                } else {

                   // ImageView imgRVFlagX = (ImageView) mView.findViewById(R.id.imgRVFlag);
                    imgRVFlagX.setBackgroundResource(R.drawable.unflag);
                }
            } catch (Exception e) {

                //ImageView imgRVFlagX = (ImageView) mView.findViewById(R.id.imgRVFlag);
                imgRVFlagX.setBackgroundResource(R.drawable.unflag);

            }

        }

        public void setZzzinterstitial(int zzzinterstitial){

            TextView txtScoreX = (TextView)mView.findViewById(R.id.txtScore);
            String score2 = String.valueOf(zzzinterstitial);

            txtScoreX.setText(score2);

        }

        public void setZzzreward(int zzzreward){

            TextView txtScoreX = (TextView)mView.findViewById(R.id.txtScore);
            String score2 = String.valueOf(zzzreward);

            txtScoreX.setText(score2);

        }

        public void setUser (String user){

            TextView txtPlayerX = (TextView)mView.findViewById(R.id.txtPlayer);

            txtPlayerX.setText(user);
            txtPlayerX.setTextSize(25);
        }

        public void setFbshares (int fbshares){

            TextView txtScoreX = (TextView)mView.findViewById(R.id.txtScore);
            String score2 = String.valueOf(fbshares);

            txtScoreX.setText(score2);

        }

        // Custom built onItemClickListener for the recycler view; seems to cover LongClick as well
        ////////////////////////////////////////////////////////////////////////////////////////
        //Listen to the video as this is a bit confusing

        private LeaderBoard.ZZZjcLBlongstreakViewHolder.OnItemClickListener hpListener;

        public interface OnItemClickListener {

            void onItemClick(View view, int position);
            void onItemLongClick (View view, int position);
        }

        public void setOnItemClickListener(LeaderBoard.ZZZjcLBlongstreakViewHolder.OnItemClickListener listener) {

            hpListener = listener;
        }

        ///////////////////////////////////////////////////////////////////////////////////////

    }

    //////////////////////// END -------->>> RECYCLER VIEW COMPONENTS /////////////////////////////////////////////////////

   //// POP UP and downstream methods like log out //////////////////////////////////////////////////////////

    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    private void ShowNewFABbasedMenu() {

        popupMenuToggle = "pressed";

        fabPopUpLBX.setVisibility(View.GONE);
        fabPopUpCollLBX.setVisibility(View.VISIBLE);
        fabPopUpFAQminiLBtX.setVisibility(View.VISIBLE);
        fabPopUpLogOutminiLBX.setVisibility(View.VISIBLE);
        fabPopUpPremiumminiLBX.setVisibility(View.VISIBLE);

        txtFAQButtonLBX.setVisibility(View.GONE);
        txtLogoutButtonLBX.setVisibility(View.GONE);

        fabPopUpLogOutminiLBX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenuToggle = "Not";

                fabPopUpLBX.setVisibility(View.VISIBLE);
                fabPopUpCollLBX.setVisibility(View.GONE);
                fabPopUpFAQminiLBtX.setVisibility(View.GONE);
                fabPopUpLogOutminiLBX.setVisibility(View.GONE);
                fabPopUpPremiumminiLBX.setVisibility(View.GONE);

                txtFAQButtonLBX.setVisibility(View.GONE);
                txtLogoutButtonLBX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

                alertDialogLogOut();

            }
        });

        fabPopUpFAQminiLBtX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupMenuToggle = "Not";

                fabPopUpLBX.setVisibility(View.VISIBLE);
                fabPopUpCollLBX.setVisibility(View.GONE);
                fabPopUpFAQminiLBtX.setVisibility(View.GONE);
                fabPopUpLogOutminiLBX.setVisibility(View.GONE);
                fabPopUpPremiumminiLBX.setVisibility(View.GONE);

                txtFAQButtonLBX.setVisibility(View.GONE);
                txtLogoutButtonLBX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

                fagDisplay();

            }
        });

        shadeX.setVisibility(View.VISIBLE);

        fabPopUpCollLBX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenuToggle = "Not";

                fabPopUpLBX.setVisibility(View.VISIBLE);
                fabPopUpCollLBX.setVisibility(View.GONE);
                fabPopUpFAQminiLBtX.setVisibility(View.GONE);
                fabPopUpLogOutminiLBX.setVisibility(View.GONE);
                fabPopUpPremiumminiLBX.setVisibility(View.GONE);

                txtFAQButtonLBX.setVisibility(View.GONE);
                txtLogoutButtonLBX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

            }
        });

        fabPopUpPremiumminiLBX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenuToggle = "Not";

                fabPopUpLBX.setVisibility(View.VISIBLE);
                fabPopUpCollLBX.setVisibility(View.GONE);
                fabPopUpFAQminiLBtX.setVisibility(View.GONE);
                fabPopUpLogOutminiLBX.setVisibility(View.GONE);
                fabPopUpPremiumminiLBX.setVisibility(View.GONE);
                fabShareLBX.setVisibility(View.VISIBLE);

                txtFAQButtonLBX.setVisibility(View.GONE);
                txtLogoutButtonLBX.setVisibility(View.GONE);
                txtPremiumButtonLBX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

                Log.i("PREMIUM", " beforer if showp= " + showPremiumDialogToggle2);

                if(showPremiumDialogToggle2.equals("bought")) {

                    alreadyPremiumSnackbar();

                } else {

                    Intent intent = new Intent(LeaderBoard.this, Premium.class);
                    startActivity(intent);
                }

            }
        });

    }

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

                mAuthLB.signOut();

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
                Intent intent = new Intent(LeaderBoard.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }.start();

    }

    private void logoutSnackbar(){

        Snackbar snackbar;

        snackbar = Snackbar.make(loutLeaderBoardX, "Good bye", Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorPrimaryDark));

        snackbar.show();


        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
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



    /////////////////////////////// END OF POP and downstream methods like logout /////////////////////////
    private void alreadyPremiumSnackbar(){

        Snackbar snackbar;

        snackbar = Snackbar.make(loutLeaderBoardX, "You are already a Premium Member", Snackbar.LENGTH_SHORT);

        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getColor(R.color.colorAccent));

        snackbar.show();


        int snackbarTextId = com.google.android.material.R.id.snackbar_text;
        TextView textView = (TextView)snackbarView.findViewById(snackbarTextId);
        textView.setTextSize(18);

    }


}
