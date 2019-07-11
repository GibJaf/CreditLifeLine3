package com.example.creditlifeline3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<EMI> EMI_List = new ArrayList<>();
    EMI_Adapter adapter;
    EMI emi;
    String rupeeSymbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Initialize();
        InitRecyclerView();

        /*String id = databaseReference.push().getKey();
        emi = new EMI(id,1000,10.0,"01-01-2000","01-01-2001");
        databaseReference.child(id).setValue(emi).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(TestActivity.this,"Load details saved ! ",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TestActivity.this," Details could not be saved !",Toast.LENGTH_SHORT).show();
            }
        }); */
    }

    private void Initialize(){
        Log.d(TAG, "Initialize: started");
        databaseReference = FirebaseDatabase.getInstance().getReference("EMI");
        recyclerView = findViewById(R.id.allLoansRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(TestActivity.this));
        rupeeSymbol = getString(R.string.rupeeSymbol);
    }

    private void InitRecyclerView(){
        Log.d(TAG, "InitRecyclerView: started");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EMI_List.clear();
                for(DataSnapshot emiSnapShot : dataSnapshot.getChildren()){
                    EMI emi = emiSnapShot.getValue(EMI.class);
                    EMI_List.add(emi);
                }
                Log.d(TAG, "onDataChange: EMI_List.size() = "+EMI_List.size());
                adapter = new EMI_Adapter(EMI_List,TestActivity.this , rupeeSymbol);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
