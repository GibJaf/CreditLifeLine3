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

public class SIP_BreakupActivity extends AppCompatActivity {

    private static final String TAG = "EMI_BreakupActivity";
    PieChart SIP_piechart;
    TextView sipTextView , interestTextView , futureValueTextView;
    String rupeeSymbol;
    SIP sip_obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sip__breakup);

        Initialize();
        getIncomingIntent();
        setTextViews();
        generatePieChart();
    }

    private void Initialize(){
        Log.d(TAG, "Initialize: started");
        SIP_piechart = findViewById(R.id.SIP_piechart);
        sipTextView = findViewById(R.id.sipTextView);
        interestTextView = findViewById(R.id.interestTextView);
        futureValueTextView = findViewById(R.id.futureValueTextView);
        rupeeSymbol = getString(R.string.rupeeSymbol);
        sip_obj = new SIP();
    }

    private void getIncomingIntent(){
        Log.d(TAG, "getIncomingIntent: started");
        if(getIntent().hasExtra("sip_installment") && getIntent().hasExtra("sip_futureValue")){
            sip_obj.principal = getIntent().getLongExtra("sip_installment", 0);
            sip_obj.maturity_value = getIntent().getLongExtra("sip_futureValue", 0);
            sip_obj.tenure = getIntent().getIntExtra("sip_tenure",0);
        }
    }

    private void setTextViews(){
        Log.d(TAG, "setTextViews: started");
        sipTextView.setText(rupeeSymbol+" "+sip_obj.TotalPrincipal());

        sip_obj.calculateInterest();
        interestTextView.setText(rupeeSymbol+" "+sip_obj.interest);
        //Long interest = sip_obj.maturity_value - sip_obj.principal*sip_obj.tenure;

        futureValueTextView.setText(rupeeSymbol+" "+sip_obj.maturity_value);
    }

    private void generatePieChart(){
        Log.d(TAG, "generatePieChart: started");

        SIP_piechart.setUsePercentValues(true);
        SIP_piechart.getDescription().setEnabled(false);
        SIP_piechart.setExtraOffsets(5,10,5,5);

        SIP_piechart.setDragDecelerationFrictionCoef(0.95f);

        SIP_piechart.setDrawHoleEnabled(true);
        SIP_piechart.setHoleColor(Color.WHITE);
        SIP_piechart.setTransparentCircleRadius(60f);

        ArrayList<PieEntry> yValues = new ArrayList<>();

        yValues.add(new PieEntry(sip_obj.TotalPrincipal(),"Principal"));
        yValues.add(new PieEntry(sip_obj.interest,"Interest"));

        Description description = new Description();
        description.setText("SIP breakup");
        description.setTextSize(25);
        SIP_piechart.setDescription(description);

        PieDataSet dataSet = new PieDataSet(yValues,"Entities");
        dataSet.setSliceSpace(4f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        SIP_piechart.setData(data);
    }

}
