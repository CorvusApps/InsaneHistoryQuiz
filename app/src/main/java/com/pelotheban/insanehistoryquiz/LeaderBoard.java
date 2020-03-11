package com.pelotheban.insanehistoryquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LeaderBoard extends AppCompatActivity {


    private AlertDialog dialog;
    private LinearLayout loutLeaderBoardX;

    //leader board selector buttons and recycler views
    private Button btnMostPlayedX, btnMostRightX, btnLongestStreakX, btnMostPlayed2X, btnMostRight2X, btnLongestStreak2X;
    private RecyclerView rcvLongestStreakX;

    private LinearLayoutManager layoutManagerLeaders;

    private String boardToggle;

    //popUp Menu

    private String popupMenuToggle;
    private FloatingActionButton fabPopUpLBX, fabPopUpCollLBX, fabPopUpFAQminiLBtX, fabPopUpLogOutminiLBX;
    private TextView txtFAQButtonLBX, txtLogoutButtonLBX;
    private View shadeX; // to shade the background when menu out


    //Firebase

    private FirebaseAuth mAuthLB;
    DatabaseReference  lbReference;
    Query sortLBQuery;

    private SharedPreferences sortSharedPrefLeaders, boardToggleSharedPrefLeaders;

    //For score counters

    Query sortUserLBQuery;

    String coinsOwnedString, totalAnsweredString, longestStreakString;

    TextView lbTxtCoinCounterX, lbTxtMostRightX, lbTxtLongestStreak;

    String uid;

    // To identify user in the recyclerview

    String profileName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

        loutLeaderBoardX = findViewById(R.id.loutLeaderBoard);

        // firebase and sorting
        lbReference = FirebaseDatabase.getInstance().getReference().child("my_users");
        lbReference.keepSynced(true);

        mAuthLB = FirebaseAuth.getInstance();

        sortSharedPrefLeaders = getSharedPreferences("SortSetting2", MODE_PRIVATE);
        String mSorting2 = sortSharedPrefLeaders.getString("Sort2", "longeststreaksort"); // where if no settings

        //sortLBQuery = lbReference.orderByChild("totalquestions");
        sortLBQuery = lbReference.orderByChild(mSorting2);

        //counters

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

                    try {
                        profileName = userDs.child("profilename").getValue().toString();

                    } catch (Exception e) {

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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

        txtFAQButtonLBX = findViewById(R.id.txtFAQButtonLB);
        txtLogoutButtonLBX = findViewById(R.id.txtLogoutButtonLB);

        shadeX = findViewById(R.id.shade);

        /// end of pop up

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        rcvLongestStreakX = findViewById(R.id.rcvLongestStreak);

        boardToggleSharedPrefLeaders = getSharedPreferences("BoardToggleSetting2", MODE_PRIVATE);
        boardToggle = boardToggleSharedPrefLeaders.getString("BoardToggle2", "1");

        btnMostPlayedX = findViewById(R.id.btnMostPlayed);
        btnMostRightX = findViewById(R.id.btnMostRight);
        btnLongestStreakX = findViewById(R.id.btnLongestStreak);

        btnMostPlayed2X = findViewById(R.id.btnMostPlayed2);
        btnMostRight2X = findViewById(R.id.btnMostRight2);
        btnLongestStreak2X = findViewById(R.id.btnLongestStreak2);


        if (boardToggle.equals("1")) {
            btnLongestStreakX.setVisibility(View.GONE);
            btnLongestStreak2X.setVisibility(View.VISIBLE);
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



        final FirebaseRecyclerAdapter<ZZZjcLBlongeststreak, LeaderBoard.ZZZjcLBlongstreakViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<ZZZjcLBlongeststreak, LeaderBoard.ZZZjcLBlongstreakViewHolder>
                (ZZZjcLBlongeststreak.class,R.layout.yyy_leaders, LeaderBoard.ZZZjcLBlongstreakViewHolder.class,sortLBQuery) {


            @Override
            protected void populateViewHolder(LeaderBoard.ZZZjcLBlongstreakViewHolder viewHolder, ZZZjcLBlongeststreak model, int position) {

                viewHolder.setRank(position);
                viewHolder.setProfilename(model.getProfilename(), profileName);

                if (boardToggle.equals("1")) {
                    viewHolder.setLongeststreak(model.getLongeststreak());

                }

                if (boardToggle.equals("2")) {
                    viewHolder.setTotalanswered(model.getTotalanswered());

                }

                if (boardToggle.equals("3")) {
                    viewHolder.setCoins(model.getCoins());

                }



            }

            // The Code setting out recycler view /////////////////////////////////////////////////////////////////

            // This method is part of the onItemClick AND onLongItem Click code NOT to populate the recycler view /////////////////////////////////////////////////////
            @Override
            public LeaderBoard.ZZZjcLBlongstreakViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                LeaderBoard.ZZZjcLBlongstreakViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnItemClickListener(new ZZZjcLBlongstreakViewHolder.OnItemClickListener() {


                    @Override
                    public void onItemLongClick(View view, int position) {


                    }

                    // inflates card from recycler view to see fields not visible in base view
                    @Override
                    public void onItemClick(View view, int position) {




                    }

                });

                return viewHolder;
            }

        };

        ///////////////////////////////////////////////////////

        // The onclick methods were in the broader recycler view methods - this calls for the adapter on everything
        rcvLongestStreakX.setAdapter(firebaseRecyclerAdapter);


    }

    //////////////////////// START ------> LONG STREAK RECYCLER VIEW COMPONENTS /////////////////////////////////////////////////////
    /////////////////// includes: viewholder, sort, expoloding card view dialog, functions from dialog //////////////////


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

    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    private void ShowNewFABbasedMenu() {

        popupMenuToggle = "pressed";

        fabPopUpLBX.setVisibility(View.GONE);
        fabPopUpCollLBX.setVisibility(View.VISIBLE);
        fabPopUpFAQminiLBtX.setVisibility(View.VISIBLE);
        fabPopUpLogOutminiLBX.setVisibility(View.VISIBLE);

        txtFAQButtonLBX.setVisibility(View.VISIBLE);
        txtLogoutButtonLBX.setVisibility(View.VISIBLE);

        fabPopUpLogOutminiLBX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupMenuToggle = "Not";

                fabPopUpLBX.setVisibility(View.VISIBLE);
                fabPopUpCollLBX.setVisibility(View.GONE);
                fabPopUpFAQminiLBtX.setVisibility(View.GONE);
                fabPopUpLogOutminiLBX.setVisibility(View.GONE);

                txtFAQButtonLBX.setVisibility(View.GONE);
                txtLogoutButtonLBX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

                alertDialogLogOut();

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

                txtFAQButtonLBX.setVisibility(View.GONE);
                txtLogoutButtonLBX.setVisibility(View.GONE);

                shadeX.setVisibility(View.GONE);

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


}
