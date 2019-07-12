package com.example.creditlifeline3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SIP_Fragment extends Fragment {

    private static final String TAG = "SIP_Fragment";
    DatabaseReference databaseSIP;
    RecyclerView sipRecyclerView;
    SIP_Adapter sipAdapter;
    ArrayList<SIP> sipArrayList;
    String id , rupeeSymbol;
    FloatingActionButton goToAddSIP_Button;
    Intent goToAddSIP_ActivityIntent;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
        return inflater.inflate(R.layout.fragment_sip,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: started");

        databaseSIP = FirebaseDatabase.getInstance().getReference("SIP");
        sipRecyclerView = view.findViewById(R.id.allSIP_RecyclerView);
        sipRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        sipArrayList = new ArrayList<>();
        goToAddSIP_Button = view.findViewById(R.id.goToAddSIP_Button);
        goToAddSIP_ActivityIntent = new Intent(getContext(),AddSIP_Activity.class);
        rupeeSymbol = getContext().getString(R.string.rupeeSymbol);

        goToAddSIP_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(goToAddSIP_ActivityIntent);
            }
        });

        displayAllSIP();

    }

    private void displayAllSIP(){
        Log.d(TAG, "displayAllSIP: started");
        databaseSIP.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sipArrayList.clear();
                for(DataSnapshot sipElement : dataSnapshot.getChildren()){
                    SIP sip = sipElement.getValue(SIP.class);
                    sipArrayList.add(sip);
                }
                sipAdapter = new SIP_Adapter(sipArrayList , getContext() , rupeeSymbol);
                sipRecyclerView.setAdapter(sipAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
