package com.example.creditlifeline3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SIP_Adapter extends RecyclerView.Adapter<SIP_Adapter.ViewHolder>{

    private static final String TAG = "SIP_Adapter";
    ArrayList<SIP> sipArrayList;
    Context context;
    String rupeeSymbol;

    public SIP_Adapter(ArrayList<SIP> sipArrayList, Context context, String rupeeSymbol) {
        this.sipArrayList = sipArrayList;
        this.context = context;
        this.rupeeSymbol = rupeeSymbol;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: started");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sip_layout,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: started");
        holder.principalTextView.setText(rupeeSymbol+" "+sipArrayList.get(position).principal);
        holder.FV_TextView.setText(rupeeSymbol+" "+sipArrayList.get(position).maturity_value);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: started");
        return sipArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView principalTextView , FV_TextView;
        LinearLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            principalTextView = itemView.findViewById(R.id.principalTextView);
            FV_TextView = itemView.findViewById(R.id.futureValueTextView);
            parentLayout = itemView.findViewById(R.id.SIP_ParentLayout);
        }
    }
}
