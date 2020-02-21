package com.pelotheban.insanehistoryquiz;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LeaderBoard extends AppCompatActivity {

    //leader board selector buttons and recycler views
    Button btnMostPlayedX, btnMostRightX, btnLongestStreakX;

    RecyclerView rcvLongestStreakX;

    private LinearLayoutManager layoutManagerLeaders;


    //Firebase

    DatabaseReference  lbReference;
    Query sortLBQuery;



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


        lbReference = FirebaseDatabase.getInstance().getReference().child("my_users");
        lbReference.keepSynced(true);

        sortLBQuery = lbReference.orderByChild("totalquestions");
        layoutManagerLeaders = new LinearLayoutManager(this);
        layoutManagerLeaders.setReverseLayout(false);

        rcvLongestStreakX.setHasFixedSize(true); //Not sure this applies or why it is here
        rcvLongestStreakX.setLayoutManager(layoutManagerLeaders);

        //leader board selector buttons
        btnMostPlayedX = findViewById(R.id.btnMostPlayed);
        btnMostPlayedX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnMostRightX = findViewById(R.id.btnMostRight);
        btnMostRightX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        btnLongestStreakX = findViewById(R.id.btnMostPlayed);
        btnLongestStreakX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rcvLongestStreakX.setVisibility(View.VISIBLE);

                sortLBQuery = lbReference.orderByChild("longeststreak");


            }
        });



        final FirebaseRecyclerAdapter<ZZZjcLBlongeststreak, LeaderBoard.ZZZjcLBlongstreakViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<ZZZjcLBlongeststreak, LeaderBoard.ZZZjcLBlongstreakViewHolder>
                (ZZZjcLBlongeststreak.class,R.layout.yyy_leaders, LeaderBoard.ZZZjcLBlongstreakViewHolder.class,sortLBQuery) {


            @Override
            protected void populateViewHolder(LeaderBoard.ZZZjcLBlongstreakViewHolder viewHolder, ZZZjcLBlongeststreak model, int position) {

                viewHolder.setLongeststreak(model.getLongeststreak());
                viewHolder.setProfilename(model.getProfilename());

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
