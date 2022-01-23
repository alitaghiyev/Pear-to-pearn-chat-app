package com.alitaghiyev.alitaghiyevfinal.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.alitaghiyev.alitaghiyevfinal.models.Message;
import com.alitaghiyev.alitaghiyevfinal.activities.ChatActivity;
import com.alitaghiyev.alitaghiyevfinal.R;
import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.CardViewDesignThingsHolder>{
    private Context context;
    private ArrayList<Message> messageArrayList = null;

    public MessageAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.context = context;
        this.messageArrayList = messageArrayList;
    }

    @NonNull
    @Override
    public CardViewDesignThingsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.messagelayout, parent, false);
        return new CardViewDesignThingsHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull CardViewDesignThingsHolder holder, int position) {
        Message message = messageArrayList.get(position);

        if (message.getKimden().equals(ChatActivity.user.getUid())) {
            holder.mlClLeftMessage.setBackground(context.getDrawable(R.drawable.leftmessagelayout));
            holder.mlClRightMessage.setVisibility(View.INVISIBLE);
            holder.mlClLeftMessage.setVisibility(View.VISIBLE);
            holder.tvMlClLeftMessage.setText(message.getMesaj());
            holder.tvMlClLeftDate.setText(message.getTarihsaat());
        }else if (message.getKime().equals(ChatActivity.user.getUid())){
            holder.mlClRightMessage.setBackground(context.getDrawable(R.drawable.rightmessagelayout));
            holder.mlClLeftMessage.setVisibility(View.INVISIBLE);
            holder.mlClRightMessage.setVisibility(View.VISIBLE);
            holder.tvMlClRightMessage.setText(message.getMesaj());
            holder.tvMlClRightDate.setText(message.getTarihsaat());
        }

    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }


    public static class CardViewDesignThingsHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout mlClLeftMessage, mlClRightMessage;
        private TextView tvMlClLeftMessage, tvMlClRightMessage;
        private TextView tvMlClLeftDate, tvMlClRightDate;

        public CardViewDesignThingsHolder(@NonNull View itemView) {
            super(itemView);
            mlClLeftMessage = itemView.findViewById(R.id.mlClLeftMessage);
            mlClRightMessage = itemView.findViewById(R.id.mlClRightMessage);
            tvMlClLeftMessage = itemView.findViewById(R.id.tvMlClLeftMessage);
            tvMlClRightMessage = itemView.findViewById(R.id.tvMlClRightMessage);
            tvMlClLeftDate = itemView.findViewById(R.id.tvMlClLeftDate);
            tvMlClRightDate = itemView.findViewById(R.id.tvMlClRightDate);

        }
    }
}
