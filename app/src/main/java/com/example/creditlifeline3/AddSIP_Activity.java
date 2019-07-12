package com.example.creditlifeline3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;

public class AddSIP_Activity extends AppCompatActivity {

    private static final String TAG = "AddSIP_Activity";
    EditText principalEditText , rateEditText , tenureEditText;
    Long principal , tenure;
    Double rate;
    SIP sip;
    DatabaseReference databaseSIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sip_);

        Log.d(TAG, "onCreate: started");
        Initialize();

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ValidateInput() == 1)
                    return;
                CalculateFV();
                ClearAllFields();
                AddSIP_Details();
            }
        });
    }

    private void Initialize(){
        Log.d(TAG, "Initialize: started");
        principalEditText = findViewById(R.id.principalEditText);
        rateEditText = findViewById(R.id.rateEditText);
        tenureEditText = findViewById(R.id.tenureEditText);
        sip = new SIP();
        databaseSIP = FirebaseDatabase.getInstance().getReference("SIP");
    }

    private int ValidateInput(){
        Log.d(TAG, "ValidateInput: started");


        String principalString = principalEditText.getText().toString().trim();
        if(principalString.isEmpty()){
            principalEditText.setError("Enter some principal value");
            principalEditText.requestFocus();
            return 1;
        }else if(Long.parseLong(principalString) <= 0){
            principalEditText.setError("Principal value must be greater than 0");
            principalEditText.requestFocus();
            return 1;
        }
        sip.principal = Long.parseLong(principalString);

        String rateString = rateEditText.getText().toString().trim();
        if(rateString.isEmpty()){
            rateEditText.setError("Enter some rate value");
            rateEditText.requestFocus();
            return 1;
        }else if(Double.parseDouble(rateString) == 0.0){
            rateEditText.setError("Rate value must be greater than 0.0");
            rateEditText.requestFocus();
            return 1;
        }
        sip.rate = Double.parseDouble(rateString);

        String tenureString = tenureEditText.getText().toString().trim();
        if(tenureString.isEmpty()){
            tenureEditText.setError("Enter some time duration");
            tenureEditText.requestFocus();
            return 1;
        }else if(Long.valueOf(tenureString) == 0){
            tenureEditText.setError("Time duration must be greater than 0");
            tenureEditText.requestFocus();
            return 1;
        }
        sip.tenure = Integer.parseInt(tenureString);

        return 0;
    }

    private void CalculateFV(){
        Log.d(TAG, "CalculateFV: started");
        Double RbyN = sip.rate/1200;
        Log.d(TAG, "CalculateFV: RbyN => "+RbyN);
        Double temp = Math.pow(1 + RbyN , sip.tenure) - 1;
        Log.d(TAG, "CalculateFV: temp => "+temp);
        sip.maturity_value = Math.round(sip.principal * temp / RbyN);
    }

    private void ClearAllFields(){
        Log.d(TAG, "ClearAllFields: started");
        principalEditText.setText("");
        rateEditText.setText("");
        tenureEditText.setText("");
    }

    private void AddSIP_Details(){
        Log.d(TAG, "AddSIP_Details: started");

        //hide keyboard
        View view = this.getCurrentFocus();
        if(view!=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }

        String id = databaseSIP.push().getKey();
        sip.id = id;
        databaseSIP.child(id).setValue(sip).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddSIP_Activity.this,"SIP details have been saved",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddSIP_Activity.this,"SIP details couldn't be saved :(",Toast.LENGTH_SHORT).show();
            }
        });
    }

    //ToDo: 
}
