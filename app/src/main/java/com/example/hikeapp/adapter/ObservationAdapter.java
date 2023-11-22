package com.example.hikeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hikeapp.R;
import com.example.hikeapp.model.ObservationItem;

import java.util.ArrayList;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ExpenseItemViewHolder> {
    private Context context;
    private ArrayList<ObservationItem> arrayList;

    public ObservationAdapter(Context context, ArrayList<ObservationItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ExpenseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View heroView = inflater.inflate(R.layout.observation_item_layout, parent, false);
        return new ExpenseItemViewHolder(heroView);
    }

    @Override
    public void onBindViewHolder(ExpenseItemViewHolder holder, int position) {
        holder.observation.setText(arrayList.get(position).getObservation());
        holder.date.setText(arrayList.get(position).getDate());
        holder.time.setText(arrayList.get(position).getTime());
        holder.comment.setText(arrayList.get(position).getComment());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ExpenseItemViewHolder extends RecyclerView.ViewHolder {
        TextView observation;
        TextView date;
        TextView time;
        TextView comment;

        public ExpenseItemViewHolder(View itemView) {
            super(itemView);
            observation = itemView.findViewById(R.id.observation);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            comment = itemView.findViewById(R.id.comment);
        }
    }
}


