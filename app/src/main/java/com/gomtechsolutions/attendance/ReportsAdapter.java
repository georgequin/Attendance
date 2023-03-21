package com.gomtechsolutions.attendance;

import android.content.Context;
import android.hardware.lights.LightState;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder>{
    public List<String> dates,numberOfStudents;
    Context context;
    LayoutInflater layoutInflater;
    public ReportsAdapter(Context context, List<String> dates, List<String> numberOfStudents){
        this.dates = dates;
        this.numberOfStudents = numberOfStudents;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }
    @NonNull
    @NotNull
    @Override
    public ReportsAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.report_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ReportsAdapter.ViewHolder holder, int position) {
        holder.dates_tv.setText(dates.get(position));
        holder.numberOfStudents_tv.setText(numberOfStudents.get(position));
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dates_tv, numberOfStudents_tv;
        CardView reportCard;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            dates_tv = itemView.findViewById(R.id.dates_tv);
            numberOfStudents_tv = itemView.findViewById(R.id.numbers_tv);
            reportCard = itemView.findViewById(R.id.report_card);

            reportCard.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "Name and Number", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
