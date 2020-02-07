package com.pelotheban.insanehistoryquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

public class ManualQInput extends AppCompatActivity {

    EditText edtQuestionNumberX, edtCategoryX, edtQuestionX, edtCorrectAnswerX, edtWrongAnswer1X, edtWrongAnswer2X, edtWrongAnswer3X
            ,edtWrongAnswer4X, edtExpandedAnswerX;

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
        edtExpandedAnswerX.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtExpandedAnswerX.setRawInputType(InputType.TYPE_CLASS_TEXT);

    }
}
