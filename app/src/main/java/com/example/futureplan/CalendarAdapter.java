package com.example.futureplan;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<String> daysOfMonth;
    private final onItemListener onItemListener;
    private int selectedItem = RecyclerView.NO_POSITION;

    public CalendarAdapter(ArrayList<String> daysOfMonth, CalendarAdapter.onItemListener onItemListener) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell,parent,false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.16666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.dayOfMonth.setText(daysOfMonth.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!holder.dayOfMonth.getText().toString().isEmpty()){
                    if (selectedItem == position) {
                        onItemListener.onItemClick(position, holder.dayOfMonth.getText().toString(),true);
                    }else {
                        notifyItemChanged(selectedItem);
                        selectedItem = position;
                        notifyItemChanged(selectedItem);
                        onItemListener.onItemClick(position, holder.dayOfMonth.getText().toString(),false);
                    }
                }
            }
        });
        holder.itemView.setSelected(position == selectedItem);
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface onItemListener{
    void onItemClick(int position, String dayText, boolean addEvent);
    }


}
