package com.example.hhj73.fix;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    List<ChatData> m_data;

    public ChatAdapter(List<ChatData> data) {
        m_data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mychat, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.r_message.setText(m_data.get(position).getMessage());
        holder.r_time.setText(m_data.get(position).getUserName());
    }

    @Override
    public int getItemCount() {
        return m_data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView r_message;
        TextView r_time;


        public ViewHolder(View itemView) {
            super(itemView);
            r_message = itemView.findViewById(R.id.c_message);
            r_time = itemView.findViewById(R.id.c_time);
        }
    }
}
