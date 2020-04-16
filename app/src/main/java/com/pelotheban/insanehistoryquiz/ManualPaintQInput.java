package com.pelotheban.insanehistoryquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ManualPaintQInput extends AppCompatActivity {

    // UI Elements

    EditText edtBadgeIDX, edtCorrectPaintAnswerX, edtWrongPaintAnswer1X, edtWrongPaintAnswer2X, edtWrongPaintAnswer3X, edtExpandedPaintAnswerX;


    LinearLayout loutManualPaintQInputX;

    Button btnPaintUploadX;

    // Firebase upload elements

    String BadgeIDY, CorrectPaintAnswerY, WrongPaintAnswer1Y, WrongPaintAnswer2Y, WrongPaintAnswer3Y, ExpandedPaintAnswerY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_paint_qinput);

        edtBadgeIDX = findViewById(R.id.edtBadgeID);
        edtBadgeIDX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtBadgeIDX.setRawInputType(InputType.TYPE_CLASS_TEXT);


        edtCorrectPaintAnswerX = findViewById(R.id.edtCorrectPaintAnswer);
        edtCorrectPaintAnswerX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtCorrectPaintAnswerX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtWrongPaintAnswer1X = findViewById(R.id.edtWrongPaintAnswer1);
        edtWrongPaintAnswer1X.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtWrongPaintAnswer1X.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtWrongPaintAnswer2X = findViewById(R.id.edtWrongPaintAnswer2);
        edtWrongPaintAnswer2X.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtWrongPaintAnswer2X.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtWrongPaintAnswer3X = findViewById(R.id.edtWrongPaintAnswer3);
        edtWrongPaintAnswer3X.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtWrongPaintAnswer3X.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtExpandedPaintAnswerX = findViewById(R.id.edtExpandedPaintAnswer);
        edtExpandedPaintAnswerX.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtExpandedPaintAnswerX.setRawInputType(InputType.TYPE_CLASS_TEXT);


        //setting up keyboard hiding when tapping outside of edttext

        loutManualPaintQInputX = findViewById(R.id.loutManualPaintQInput);
        loutManualPaintQInputX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try { // we need this because if you tap with no keyboard up the app will crash

                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }
        });

        btnPaintUploadX = findViewById(R.id.btnPaintUpload);
        btnPaintUploadX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("paintquestions");
                DatabaseReference dbPushReference = dbReference.push();
                String questionUidX = dbPushReference.getKey(); // this then is the key for the question


                BadgeIDY = edtBadgeIDX.getText().toString();
                CorrectPaintAnswerY = edtCorrectPaintAnswerX.getText().toString();
                WrongPaintAnswer1Y = edtWrongPaintAnswer1X.getText().toString();
                WrongPaintAnswer2Y = edtWrongPaintAnswer2X.getText().toString();
                WrongPaintAnswer3Y = edtWrongPaintAnswer3X.getText().toString();

                ExpandedPaintAnswerY = edtExpandedPaintAnswerX.getText().toString();


                HashMap<String, Object> dataMap = new HashMap<>();

                dataMap.put("aaabadgeid", BadgeIDY);

                dataMap.put("bbbcorrectpaintansw", CorrectPaintAnswerY);
                dataMap.put("cccwrongpaintans1", WrongPaintAnswer1Y);
                dataMap.put("dddwrongpaintans2", WrongPaintAnswer2Y);
                dataMap.put("eeewrongpaintans3", WrongPaintAnswer3Y);

                dataMap.put("fffpaintexpanded", ExpandedPaintAnswerY);
                dataMap.put("gggquid", questionUidX);


                dbPushReference.setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        Toast.makeText(ManualPaintQInput.this,"question added", Toast.LENGTH_SHORT).show();

                        edtBadgeIDX.setText("");
                        edtCorrectPaintAnswerX.setText("");
                        edtWrongPaintAnswer1X.setText("");
                        edtWrongPaintAnswer2X.setText("");
                        edtWrongPaintAnswer3X.setText("");

                        edtExpandedPaintAnswerX.setText("");



                    }
                });

            }
        });


    }
}
