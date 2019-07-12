package com.example.creditlifeline3;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class EMI_Adapter extends RecyclerView.Adapter<EMI_Adapter.ViewHolder> {

    private static final String TAG = "EMI_Adapter";
    private ArrayList<EMI> EMI_List = new ArrayList<>();
    private Context context;
    String rupeeSymbol;
    Intent goToEMI_Breakup;

    public EMI_Adapter(ArrayList<EMI> EMI_List, Context context , String rupeeSymbol) {
        this.EMI_List = EMI_List;
        if(context!=null) {
            this.context = context;
        }
        this.rupeeSymbol = rupeeSymbol;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loan_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.principalTextView.setText(rupeeSymbol+" "+EMI_List.get(position).principal);
        holder.emiTextView.setText(rupeeSymbol+" "+EMI_List.get(position).emi);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEMI_Breakup = new Intent(context , EMI_BreakupActivity.class);
                goToEMI_Breakup.putExtra("emi_installment",EMI_List.get(position).getEmi());
                goToEMI_Breakup.putExtra("emi_principal",EMI_List.get(position).getPrincipal());
                goToEMI_Breakup.putExtra("emi_interest",EMI_List.get(position).getInterest());
                context.startActivity(goToEMI_Breakup);
            }
        });
    }

    @Override
    public int getItemCount() {
        return EMI_List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView principalTextView , emiTextView;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            principalTextView = itemView.findViewById(R.id.principalTextView);
            emiTextView = itemView.findViewById(R.id.emiTextView);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
