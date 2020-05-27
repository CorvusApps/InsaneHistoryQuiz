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

public class ManualVillainQInput extends AppCompatActivity {

    // UI Elements

    EditText edtVillainIDX, edtCorrectVillainAnswerX, edtWrongVillainAnswer1X, edtWrongVillainAnswer2X, edtWrongVillainAnswer3X, edtExpandedVillainAnswerX;


    LinearLayout loutManualVillainQInputX;

    Button btnVillainUploadX;

    // Firebase upload elements

    String VillainIDY, CorrectVillainAnswerY, WrongVillainAnswer1Y, WrongVillainAnswer2Y, WrongVillainAnswer3Y, ExpandedVillainAnswerY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_villain_qinput);

        edtVillainIDX = findViewById(R.id.edtVillainID);
        edtVillainIDX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtVillainIDX.setRawInputType(InputType.TYPE_CLASS_TEXT);


        edtCorrectVillainAnswerX = findViewById(R.id.edtCorrectVillainAnswer);
        edtCorrectVillainAnswerX.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtCorrectVillainAnswerX.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtWrongVillainAnswer1X = findViewById(R.id.edtWrongVillainAnswer1);
        edtWrongVillainAnswer1X.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtWrongVillainAnswer1X.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtWrongVillainAnswer2X = findViewById(R.id.edtWrongVillainAnswer2);
        edtWrongVillainAnswer2X.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtWrongVillainAnswer2X.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtWrongVillainAnswer3X = findViewById(R.id.edtWrongVillainAnswer3);
        edtWrongVillainAnswer3X.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        edtWrongVillainAnswer3X.setRawInputType(InputType.TYPE_CLASS_TEXT);

        edtExpandedVillainAnswerX = findViewById(R.id.edtExpandedVillainAnswer);
        edtExpandedVillainAnswerX.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtExpandedVillainAnswerX.setRawInputType(InputType.TYPE_CLASS_TEXT);


        //setting up keyboard hiding when tapping outside of edttext

        loutManualVillainQInputX = findViewById(R.id.loutManualVillainQInput);
        loutManualVillainQInputX.setOnClickListener(new View.OnClickListener() {
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

        btnVillainUploadX = findViewById(R.id.btnVillainUpload);
        btnVillainUploadX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference().child("villainquestions");
                DatabaseReference dbPushReference = dbReference.push();
                String questionUidX = dbPushReference.getKey(); // this then is the key for the question


                VillainIDY = edtVillainIDX.getText().toString();
                CorrectVillainAnswerY = edtCorrectVillainAnswerX.getText().toString();
                WrongVillainAnswer1Y = edtWrongVillainAnswer1X.getText().toString();
                WrongVillainAnswer2Y = edtWrongVillainAnswer2X.getText().toString();
                WrongVillainAnswer3Y = edtWrongVillainAnswer3X.getText().toString();

                ExpandedVillainAnswerY = edtExpandedVillainAnswerX.getText().toString();


                HashMap<String, Object> dataMap = new HashMap<>();

                dataMap.put("aaavillainid", VillainIDY);

                dataMap.put("bbbcorrectvillainansw", CorrectVillainAnswerY);
                dataMap.put("cccwrongvillainans1", WrongVillainAnswer1Y);
                dataMap.put("dddwrongvillainans2", WrongVillainAnswer2Y);
                dataMap.put("eeewrongvillainans3", WrongVillainAnswer3Y);

                dataMap.put("fffvillainexpanded", ExpandedVillainAnswerY);
                dataMap.put("gggquid", questionUidX);


                dbPushReference.setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {


                        Toast.makeText(ManualVillainQInput.this,"question added", Toast.LENGTH_SHORT).show();

                        edtVillainIDX.setText("");
                        edtCorrectVillainAnswerX.setText("");
                        edtWrongVillainAnswer1X.setText("");
                        edtWrongVillainAnswer2X.setText("");
                        edtWrongVillainAnswer3X.setText("");

                        edtExpandedVillainAnswerX.setText("");



                    }
                });

            }
        });


    }
}