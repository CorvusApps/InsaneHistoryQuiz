package com.pelotheban.insanehistoryquiz;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.util.Locale;


public class QuestionList extends AppCompatActivity {


    // UI to be able to go to modify questions
    private TextView txtQuestionUIDX;


    // Firebase related
    private FirebaseAuth qAuth;
    private DatabaseReference qRefDatabase;

    //UI Components
    private RecyclerView rcvQuestionsX;

    private CoordinatorLayout loutQuestionsX; //primarily used for snackbars

    // custom view to use as a shade behind custom dialogs
    private View shadeX;

    //creating instance variables that can be used to pass info to the coin modify screen

    private String categoryY, questionY, correctAnswerY, wrongAnswer1Y, wrongAnswer2Y, wrongAnswer3Y, wrongAnswer4Y, extentendedAnswerY, questionUidY, answerToggleY;
    private int questionNumberY;


    //For Sorting

    private LinearLayoutManager layoutManagerQuestions;
    private SharedPreferences sortSharedPrefQuestions;

    private Query sortQueryQuestions;

    // card elements for on-click functionality

    // PROBABLY NEED SOMETHING HERE



    @Override
    @SuppressLint("RestrictedApi") // suppresses the issue with not being able to use visibility with the FAB
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);


        // Firebase related
        qAuth = FirebaseAuth.getInstance();

        qRefDatabase = FirebaseDatabase.getInstance().getReference().child("questions");
        qRefDatabase.keepSynced(true);


        //Shared preferences for sorting
        sortSharedPrefQuestions = getSharedPreferences("SortSetting3", MODE_PRIVATE);
        String mSorting2 = sortSharedPrefQuestions.getString("Sort2", "aaaqno"); // where if no settings - so here in refcoins it defaults to this

        if(mSorting2.equals("aaaqno")) {

            sortQueryQuestions = qRefDatabase.orderByChild("aaaqno");
            layoutManagerQuestions = new LinearLayoutManager(this);
            layoutManagerQuestions.setReverseLayout(false);

        }

        //UI Components
        rcvQuestionsX = findViewById(R.id.rcvQuestions);
        rcvQuestionsX.setHasFixedSize(true); //Not sure this applies or why it is here
        rcvQuestionsX.setLayoutManager(layoutManagerQuestions);



        loutQuestionsX = findViewById(R.id.loutQuestionsCO); //primarily used for snackbars); //primarily used for snackbars

        // custom view to use as a shade behind custom dialogs
        shadeX = findViewById(R.id.shade);


// The Code setting out recycler view /////////////////////////////////////////////////////////////////
// The tutorial had this section of code through to setAdapter in separate on Start Method but for StaggeredGrid that seemed to cause the recycler view to be destroyed and not come back once we moved off the screen works fine here


        final FirebaseRecyclerAdapter<ZZZjcQuestions, ZZZjcQuestionsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<ZZZjcQuestions, ZZZjcQuestionsViewHolder>
                (ZZZjcQuestions.class,R.layout.yyy_questions_list, ZZZjcQuestionsViewHolder.class,sortQueryQuestions) {


            @Override
            protected void populateViewHolder(ZZZjcQuestionsViewHolder viewHolder, ZZZjcQuestions model, int position) {
                viewHolder.setAaaqno(model.getAaaqno());
                viewHolder.setBbbcategory(model.getBbbcategory());
                viewHolder.setCccquestion(model.getCccquestion());
                viewHolder.setDddcorrectansw(model.getDddcorrectansw());
                viewHolder.setEeewrongans1(model.getEeewrongans1());
                viewHolder.setFffwrongans2(model.getFffwrongans2());
                viewHolder.setGggwrongans3(model.getGggwrongans3());
                viewHolder.setHhhwrongans4(model.getHhhwrongans4());
                viewHolder.setIiiexpanded(model.getIiiexpanded());
                viewHolder.setJjjquid(model.getJjjquid());
                viewHolder.setKkkanstoggle(model.getKkkanstoggle());

            }

            // The Code setting out recycler view /////////////////////////////////////////////////////////////////

            // This method is part of the onItemClick AND onLongItem Click code NOT to populate the recycler view /////////////////////////////////////////////////////
            @Override
            public ZZZjcQuestionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                ZZZjcQuestionsViewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                viewHolder.setOnItemClickListener(new ZZZjcQuestionsViewHolder.OnItemClickListener() {


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
        rcvQuestionsX.setAdapter(firebaseRecyclerAdapter);
    }

    ///////////////////////// END -------> ON-CREATE ////////////////////////////////////////////////////////////////////

    //////////////////////// START ------> RECYCLER VIEW COMPONENTS /////////////////////////////////////////////////////
    /////////////////// includes: viewholder, sort, expoloding card view dialog, functions from dialog //////////////////


    // View holder for the recycler view
    public static class ZZZjcQuestionsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        public ZZZjcQuestionsViewHolder(View itemView) {

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




        public void setAaaqno(int aaaqno){

            TextView txtQuetionNumberX = (TextView)mView.findViewById(R.id.txtQuestionNumber);
            String id2 = String.valueOf(aaaqno);
            txtQuetionNumberX.setText(id2);

        }

        public void setBbbcategory(String bbbcategory){

            TextView txtCategoryX = (TextView)mView.findViewById(R.id.txtCategory);
            txtCategoryX.setText(bbbcategory);

        }

        public void setCccquestion(String cccquestion) {
            TextView txtQuestionsX = (TextView)mView.findViewById(R.id.txtQuestion);
            txtQuestionsX.setText(cccquestion);

        }


        public void setDddcorrectansw(String dddcorrectansw) {
            TextView txtRightAnswerX = (TextView)mView.findViewById(R.id.txtRightAnswer);
            txtRightAnswerX.setText(dddcorrectansw);
        }

        public void setEeewrongans1(String eeewrongans1) {
            TextView txtWrongAnswer1X = (TextView)mView.findViewById(R.id.txtWrongAnswer1);
            txtWrongAnswer1X.setText(eeewrongans1);
        }

        public void setFffwrongans2(String fffwrongans2) {
            TextView txtWrongAnswer2X = (TextView)mView.findViewById(R.id.txtWrongAnswer2);
            txtWrongAnswer2X.setText(fffwrongans2);
        }

        public void setGggwrongans3(String gggwrongans3) {
            TextView txtWrongAnswer3X = (TextView)mView.findViewById(R.id.txtWrongAnswer3);
            txtWrongAnswer3X.setText(gggwrongans3);
        }

        public void setHhhwrongans4(String hhhwrongans4) {
            TextView txtWrongAnswer4X = (TextView)mView.findViewById(R.id.txtWrongAnswer4);
            txtWrongAnswer4X.setText(hhhwrongans4);
        }

        public void setIiiexpanded(String iiiexpanded) {
            TextView txtExtendedAnswerX = (TextView)mView.findViewById(R.id.txtExtendedAnswer);
            txtExtendedAnswerX.setText(iiiexpanded);
        }

        public void setJjjquid(String jjjquid) {
            TextView txtQuestionUidX = (TextView)mView.findViewById(R.id.txtQuestionUID);
            txtQuestionUidX.setText("UID:" + jjjquid);
        }

        public void setKkkanstoggle(String kkkanstoggle) {
            TextView txtAnswerToggleX = (TextView)mView.findViewById(R.id.txtAnswerToggle);
            txtAnswerToggleX.setText(kkkanstoggle);
        }




        // Custom built onItemClickListener for the recycler view; seems to cover LongClick as well
        ////////////////////////////////////////////////////////////////////////////////////////
        //Listen to the video as this is a bit confusing

        private ZZZjcQuestionsViewHolder.OnItemClickListener hpListener;

        public interface OnItemClickListener {

            void onItemClick(View view, int position);
            void onItemLongClick (View view, int position);
        }

        public void setOnItemClickListener(ZZZjcQuestionsViewHolder.OnItemClickListener listener) {

            hpListener = listener;
        }

        ///////////////////////////////////////////////////////////////////////////////////////

    }

    //////////////////////// END -------->>> RECYCLER VIEW COMPONENTS /////////////////////////////////////////////////////
    /////////////////// includes: viewholder, sort, expoloding card view dialog, functions from dialog //////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> POP-UP ////////////////////////////////////////////////////////////////////

    /////////////////// Start-New Pop up Version //////////////////////////////////////////////////////////////////////



    /////////////////// END-New Pop up Version //////////////////////////////////////////////////////////////////////

    /////////////////// Start-OLD Pop up Version //////////////////////////////////////////////////////////////////////



    ///////////////////////// END -------> POP-UP MENU ///////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> SNACKBARS ////////////////////////////////////////////////////////////




    ///////////////////////// END -------> SNACKBARS //////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> FAQ AND ONE TIME /////////////////////////////////////////////////////


    //////////////////////// END ------->>> FAQ & ONE TIME ////////////////////////////////////////////////////////////////

    ///////////////////////// START ----->>> LOGOUT AND ON-BACK PRESS /////////////////////////////////////////////////////
    //Dialog that comes up when user chooses logout from the popup menu; strange legacy menu name but standard yes no dialog


}
