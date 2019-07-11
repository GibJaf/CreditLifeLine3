package com.example.creditlifeline3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EMI_Fragment extends Fragment {

    private static final String TAG = "EMI_Fragment";
    RecyclerView recyclerView;
    DatabaseReference databaseLoans;
    EMI_Adapter adapter;
    private ArrayList<EMI> EMI_List = new ArrayList<>();
    FloatingActionButton addEMI_Button;
    Intent goToAddNewEMI;
    String rupeeSymbol ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
        return  inflater.inflate(R.layout.fragment_emi,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated: started");
        super.onViewCreated(view, savedInstanceState);
        //here initialize all component views of recycler view
        recyclerView = view.findViewById(R.id.allLoansRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseLoans = FirebaseDatabase.getInstance().getReference("EMI");
        addEMI_Button = view.findViewById(R.id.addEMI_Button);
        rupeeSymbol = getContext().getString(R.string.rupeeSymbol);
        initRecyclerView();

        addEMI_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAddNewEMI = new Intent(getContext(),AddEMI_Activity.class);
                startActivity(goToAddNewEMI);
            }
        });

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: started");
        databaseLoans.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EMI_List.clear();
                for(DataSnapshot loanSnapShot : dataSnapshot.getChildren()){
                    EMI emi = loanSnapShot.getValue(EMI.class);
                    EMI_List.add(emi);
                }
                adapter = new EMI_Adapter(EMI_List, getContext() , rupeeSymbol );
                //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
