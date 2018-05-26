package com.example.hhj73.fix;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.StringTokenizer;

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
        String str = m_data.get(position).getMessage();
        StringTokenizer stringTokenizer = new StringTokenizer(str, ":");
        stringTokenizer.nextToken();
        String msg = stringTokenizer.nextToken();
        msg = msg.trim();

        holder.r_message.setText(msg);
        holder.r_time.setText(m_data.get(position).getTime());

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
