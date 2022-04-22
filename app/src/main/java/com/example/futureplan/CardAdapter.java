package com.example.futureplan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<CardItem> cardItems;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public CardAdapter(List<CardItem> cardItems) {
        this.cardItems = cardItems;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CardViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.card_item,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        holder.setCardImage(cardItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        TextView fiszkisrc;
        CardView cv;
        TextView buttonFlashLearn;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            fiszkisrc = itemView.findViewById(R.id.fiszkiText);
            cv = itemView.findViewById(R.id.cardviewitem);
            buttonFlashLearn = itemView.findViewById(R.id.buttonFlashLearn);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }

        void setCardImage(CardItem cardItem) {
            fiszkisrc.setText(cardItem.getText());
            int col = cardItem.getColor();
            cv.setCardBackgroundColor(col);
            buttonFlashLearn.setBackgroundColor(col);
        }


    }
}
