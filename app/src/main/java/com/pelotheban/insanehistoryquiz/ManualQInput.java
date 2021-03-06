package com.pelotheban.insanehistoryquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class ManualQInput extends AppCompatActivity {

    // UI Elements

    EditText edtQuestionNumberX, edtCategoryX, edtEpochX, edtEraX, edtQuestionX, edtCorrectAnswerX, edtWrongAnswer1X, edtWrongAnswer2X, edtWrongAnswer3X
            ,edtWrongAnswer4X, edtExpandedAnswerX, edtAnsToggleX;

    LinearLayout loutManualQInputX;

    Button btnUploadX;

    // Firebase upload elements

    int QuestionNumberY;

    String CategoryY, EpochY, EraY, QuestionY, CorrectAnswerY, WrongAnswer1Y, WrongAnswer2Y, WrongAnswer3Y, WrongAnswer4Y, ExpandedAnswerY, AnsToggleY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_qinput);

        edtQuestionNumberX = findViewById(R.id.edtQuestionNumber);
        edtQuestionNumberX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtQuestionNumberX.setRawInputType(InputType.TYPE_CLASS_NUMBER);// this plus textMultiline in XML allows for wraping text but provides a next button on keyboard to tab over

        edtCategoryX = findViewById(R.id.edtCategory);
        edtCategoryX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtCategoryX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtEpochX = findViewById(R.id.edtEpoch);
        edtCategoryX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtCategoryX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtEraX = findViewById(R.id.edtEra);
        edtCategoryX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtCategoryX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtQuestionX = findViewById(R.id.edtQuestion);
        edtQuestionX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtQuestionX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtCorrectAnswerX = findViewById(R.id.edtCorrectAnswer);
        edtCorrectAnswerX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtCorrectAnswerX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtWrongAnswer1X = findViewById(R.id.edtWrongAnswer1);
        edtWrongAnswer1X.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtWrongAnswer1X.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtWrongAnswer2X = findViewById(R.id.edtWrongAnswer2);
        edtWrongAnswer2X.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtWrongAnswer2X.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtWrongAnswer3X = findViewById(R.id.edtWrongAnswer3);
        edtWrongAnswer3X.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtWrongAnswer3X.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtWrongAnswer4X = findViewById(R.id.edtWrongAnswer4);
        edtWrongAnswer4X.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtWrongAnswer4X.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtExpandedAnswerX = findViewById(R.id.edtExpandedAnswer);
        edtExpandedAnswerX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtExpandedAnswerX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtAnsToggleX = findViewById(R.id.edtAnswerToggle);
        edtAnsToggleX.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtAnsToggleX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        //setting up keyboard hiding when tapping outside of edttext

        loutManualQInputX = findViewById(R.id.loutManualQInput);
        loutManualQInputX.setOnClickListener(new View.OnClickListener() {
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

        btnUploadX = findViewById(R.id.btnUpload);
        btnUploadX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("questions");
                DatabaseReference dbPushReference = dbReference.push();
                String questionUidX = dbPushReference.getKey(); // this then is the key for the question

                String QuestionNumberZ = edtQuestionNumberX.getText().toString();
                QuestionNumberY = Integer.parseInt(QuestionNumberZ);

                CategoryY = edtCategoryX.getText().toString();
                EpochY = edtEpochX.getText().toString();
                EraY = edtEraX.getText().toString();
                QuestionY = edtQuestionX.getText().toString();
                CorrectAnswerY = edtCorrectAnswerX.getText().toString();
                WrongAnswer1Y = edtWrongAnswer1X.getText().toString();
                WrongAnswer2Y = edtWrongAnswer2X.getText().toString();
                WrongAnswer3Y = edtWrongAnswer3X.getText().toString();
                WrongAnswer4Y = edtWrongAnswer4X.getText().toString();
                ExpandedAnswerY = edtExpandedAnswerX.getText().toString();
                AnsToggleY = edtAnsToggleX.getText().toString();

                HashMap<String, Object> dataMap = new HashMap<>();

                dataMap.put("aaaqno", QuestionNumberY);
                dataMap.put("bbbcategory", CategoryY);

                dataMap.put("cccquestion", QuestionY);
                dataMap.put("dddcorrectansw", CorrectAnswerY);
                dataMap.put("eeewrongans1", WrongAnswer1Y);
                dataMap.put("fffwrongans2", WrongAnswer2Y);
                dataMap.put("gggwrongans3", WrongAnswer3Y);
                dataMap.put("hhhwrongans4", WrongAnswer4Y);
                dataMap.put("iiiexpanded", ExpandedAnswerY);
                dataMap.put("jjjquid", questionUidX);
                dataMap.put("kkkanstoggle", AnsToggleY);

                dataMap.put("lllepoch", EpochY);
                dataMap.put("mmmera", EraY);


                dbPushReference.setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        Toast.makeText(ManualQInput.this,"question added", Toast.LENGTH_SHORT).show();

                        edtQuestionNumberX.setText("");
                        edtCategoryX.setText("");
                        edtEpochX.setText("");
                        edtEraX.setText("");
                        edtQuestionX.setText("");
                        edtCorrectAnswerX.setText("");
                        edtWrongAnswer1X.setText("");
                        edtWrongAnswer2X.setText("");
                        edtWrongAnswer3X.setText("");
                        edtWrongAnswer4X.setText("");
                        edtExpandedAnswerX.setText("");
                        edtAnsToggleX.setText("");


                    }
                });

            }
        });


    }
}
