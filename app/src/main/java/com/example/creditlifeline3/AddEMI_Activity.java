package com.example.creditlifeline3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.util.Calendar;


public class AddEMI_Activity extends AppCompatActivity {

    private static final String TAG = "AddEMI_Activity";

    TextView startDateTextView , endDateTextView;
    EditText principalEditText , rateEditText ;
    DatePickerDialog datePickerDialog;
    Calendar calender;
    DatabaseReference databaseEMI;

    String principalString , rateString;
    Long principal , tenure , emi , interest , amount;
    Double rate ,rateMonthly , emiDouble;
    LocalDate startDate , endDate;
    String startDateString , endDateString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_emi_);

        Initialize();

        startDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(AddEMI_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDate = new LocalDate(year,(month+1),dayOfMonth);
                        startDateString = startDate.toString();
                        startDateTextView.setText(startDateString);
                    }
                },calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        endDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(AddEMI_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDate = new LocalDate(year,(month+1),dayOfMonth);
                        if (checkEndDate(endDate) == 1)
                            return;

                        endDateString = endDate.toString();
                        endDateTextView.setText(endDateString);
                    }
                },calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });


        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = validateInput();
                if(result == 1) // if any issue , exit the onClick function
                    return;

                calculateTenure();
                calculateEMI();
                calculateAmount();
                calculateInterest();
                clearAllFields();
                addLoanDetails();
            }
        });
    }

    private void Initialize(){
        Log.d(TAG, "Initialize: started");
        startDateTextView = findViewById(R.id.startDateTextView);
        startDateString = "";
        endDateTextView = findViewById(R.id.endDateTextView);
        endDateString = "";
        principalEditText = findViewById(R.id.principalEditText);
        rateEditText = findViewById(R.id.rateEditText);
        calender = Calendar.getInstance();
        databaseEMI = FirebaseDatabase.getInstance().getReference("EMI");
    }

    private int validateInput(){
        /* check that principal , rate , startDate , endDate are not blank */
        Log.d(TAG, "validateInput: started");
        principalString = principalEditText.getText().toString();
        rateString = rateEditText.getText().toString();

        if(principalString.isEmpty()){
            principalEditText.setError("Enter non-zero principal value");
            principalEditText.requestFocus();
            return 1;
        }else if(Long.parseLong(principalString)==0){ //doing Integer.par... in else because it crashes if you try to convert empty string to integer
            principalEditText.setError("Enter non-zero principal value");
            principalEditText.requestFocus();
            return 1;
        }

        if(rateString.isEmpty()){
            rateEditText.setError("Enter non-zero rate of interest");
            rateEditText.requestFocus();
            return 1;
        }else if(Double.parseDouble(rateString)==0.0){
            rateEditText.setError("Enter non-zero rate of interest");
            rateEditText.requestFocus();
            return 1;
        }

        if(startDateString.isEmpty()){
            startDateTextView.setError("Select starting date of Loan",null);
            startDateTextView.requestFocus();
            return 1;
        }else{
            startDateTextView.clearFocus();
        }

        if(endDateString.isEmpty()){
            endDateTextView.setError("Select ending date of Loan",null);
            endDateTextView.requestFocus();
            return 1;
        }else{
            endDateTextView.clearFocus();
        }

        return 0;
    }

    private void calculateTenure(){
        tenure = Long.valueOf(Months.monthsBetween(startDate,endDate).getMonths());
    }

    private void calculateEMI(){
        principal = Long.parseLong(principalEditText.getText().toString().trim());
        rate = Double.parseDouble(rateEditText.getText().toString().trim());
        rateMonthly = rate/1200;
        emiDouble = (principal * rateMonthly * Math.pow(rateMonthly+1,tenure))/(Math.pow(rateMonthly+1,tenure)-1);
        emi =  Math.round(emiDouble);
    }

    private void calculateAmount(){
        amount = tenure * emi;
    }

    private void calculateInterest(){
        interest = amount - principal;
    }

    private void clearAllFields(){
        principalEditText.setText("");
        rateEditText.setText("");
        startDateTextView.setText("");
        endDateTextView.setText("");
    }

    private int checkEndDate(LocalDate endDate){
        if(Months.monthsBetween(startDate,endDate).getMonths() <= 0){
            Toast.makeText(AddEMI_Activity.this,"Select end date of loan atleast 1 month after start date",Toast.LENGTH_LONG).show();
            return 1; //startDate and endDate are
        }
        return 0;
    }

    private void addLoanDetails(){
        Log.d(TAG, "addLoanDetails: started");
        // To hide keyboard when SAVE button is clicked
        View view = this.getCurrentFocus();
        if(view!=null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
        final String rupee = getString(R.string.rupeeSymbol);



        String id = databaseEMI.push().getKey();
        EMI emi_obj = new EMI(id,principal,rate,startDateString,endDateString);
        emi_obj.tenure = tenure;
        emi_obj.emi = emi;
        emi_obj.amount = amount;
        emi_obj.interest = interest;
        databaseEMI.child(id).setValue(emi_obj).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(AddEMI_Activity.this,"Load details saved ! ",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // Toast.makeText(AddEMI_Activity.this," Details could not be saved !",Toast.LENGTH_SHORT).show();
            }
        });

        /*Todo: Validations : 1) disable dates till 1 month after startdate in endDate datepickerdialog
         */
        //ToDo: accept Annual or Monthly rate of interest using spinner
    }
}
