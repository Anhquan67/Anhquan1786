package com.example.hikeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hikeapp.R;
import com.example.hikeapp.model.HikeModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.ViewHolder> {
    private Context context;
    private ArrayList<HikeModel> arrayList;
    private OnClickListener onClickListener;

    public HikeAdapter(Context context, ArrayList<HikeModel> arrayList, OnClickListener onClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.hike_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HikeModel hikeModel = arrayList.get(position);
        holder.name.setText(hikeModel.getName());
        holder.date.setText(hikeModel.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(hikeModel.getUuid());
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onEditClick(hikeModel.getUuid());
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onDeleteClick(hikeModel.getUuid());
            }
        });
    }

    public void onDataChange(ArrayList<HikeModel> arrayList) {
        this.arrayList.clear();
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        MaterialButton btnEdit;
        MaterialButton btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            date = itemView.findViewById(R.id.date);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface OnClickListener {
        void onItemClick(String id);

        void onEditClick(String id);

        void onDeleteClick(String id);
    }
}