package com.example.creditlifeline3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class EMI_BreakupActivity extends AppCompatActivity {

    private static final String TAG = "EMI_BreakupActivity";
    PieChart EMI_piechart;
    Long emi , principal , interest;
    TextView emiTextView , principalTextView , interestTextView;
    String rupeeSymbol;
    EMI emi_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emi__breakup);

        Initialize();
        getIncomingIntent();
        setTextViews();
        generatePieChart();
    }

    private void Initialize(){
        Log.d(TAG, "Initialize: started");
        EMI_piechart = findViewById(R.id.EMI_piechart);
        emiTextView = findViewById(R.id.emiTextView);
        principalTextView = findViewById(R.id.principalTextView);
        interestTextView = findViewById(R.id.interestTextView);
        rupeeSymbol = getString(R.string.rupeeSymbol);
        emi_obj = new EMI();
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: started");
        if(getIntent().hasExtra("emi_installment") && getIntent().hasExtra("emi_principal") && getIntent().hasExtra("emi_interest")) {
            emi_obj.emi = getIntent().getLongExtra("emi_installment", 0);
            emi_obj.principal = getIntent().getLongExtra("emi_principal", 0);
            emi_obj.interest = getIntent().getLongExtra("emi_interest", 0);

        }
    }

    private void setTextViews(){
        emiTextView.setText(rupeeSymbol+" "+emi_obj.emi);
        principalTextView.setText(rupeeSymbol+" "+emi_obj.principal);
        interestTextView.setText(rupeeSymbol+" "+emi_obj.interest);
    }

    private void generatePieChart(){
        EMI_piechart.setUsePercentValues(true);
        EMI_piechart.getDescription().setEnabled(false);
        EMI_piechart.setExtraOffsets(5,10,5,5);

        EMI_piechart.setDragDecelerationFrictionCoef(0.95f);

        EMI_piechart.setDrawHoleEnabled(true);
        EMI_piechart.setHoleColor(Color.WHITE);
        EMI_piechart.setTransparentCircleRadius(60f);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        yValues.add(new PieEntry(emi_obj.principal,"Principal"));
        yValues.add(new PieEntry(emi_obj.interest,"Interest"));

        Description description = new Description();
        description.setText("EMI breakup");
        description.setTextSize(25);
        EMI_piechart.setDescription(description);

        PieDataSet dataSet = new PieDataSet(yValues,"Entities");
        dataSet.setSliceSpace(4f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        EMI_piechart.setData(data);
    }
}
