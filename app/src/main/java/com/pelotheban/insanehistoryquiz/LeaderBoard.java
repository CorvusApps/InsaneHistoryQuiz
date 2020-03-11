package com.pelotheban.insanehistoryquiz;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LeaderBoard extends AppCompatActivity {

    //leader board selector buttons and recycler views
    private Button btnMostPlayedX, btnMostRightX, btnLongestStreakX, btnMostPlayed2X, btnMostRight2X, btnLongestStreak2X;
    private RecyclerView rcvLongestStreakX;

    private LinearLayoutManager layoutManagerLeaders;

    private String boardToggle;


    //Firebase

    DatabaseReference  lbReference;
    Query sortLBQuery;

    private SharedPreferences sortSharedPrefLeaders, boardToggleSharedPrefLeaders;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);

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

        lbReference = FirebaseDatabase.getInstance().getReference().child("my_users");
        lbReference.keepSynced(true);

        sortSharedPrefLeaders = getSharedPreferences("SortSetting2", MODE_PRIVATE);
        String mSorting2 = sortSharedPrefLeaders.getString("Sort2", "longeststreaksort"); // where if no settings

        //sortLBQuery = lbReference.orderByChild("totalquestions");
        sortLBQuery = lbReference.orderByChild(mSorting2);

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

                viewHolder.setProfilename(model.getProfilename());

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






        public void setLongeststreak(int longstreak){

            TextView txtScoreX = (TextView)mView.findViewById(R.id.txtScore);
            String score2 = String.valueOf(longstreak);

            txtScoreX.setText(score2);

        }

        public void setProfilename(String profilename){

            TextView txtPlayerX = (TextView)mView.findViewById(R.id.txtPlayer);

            txtPlayerX.setText(profilename);

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

}
