package com.pelotheban.insanehistoryquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextView txtFBtestX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

            }
        });







    }
}
