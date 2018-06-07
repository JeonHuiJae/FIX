package com.example.hhj73.fix;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ChatListAdapter_Senior extends BaseAdapter {

    ArrayList<User> users;
    ArrayList<Uri> roomPic;
    Context context;

    public ChatListAdapter_Senior(Context context, ArrayList<User> users, ArrayList<Uri> roomPic) {
        this.context = context;
        this.users = users;
        this.roomPic = roomPic;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_list_senior_row, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        ImageView userImage = (ImageView) convertView.findViewById(R.id.listRowImage);
        TextView userName = (TextView) convertView.findViewById(R.id.listRowTitle);

        userImage.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT>=21)
            userImage.setClipToOutline(true);

        userName.setText(users.get(position).getName()+" 학생");

        Glide.with(context)
                .load(roomPic.get(position))
                .centerCrop()
                .into(userImage);
//        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */


        return convertView;
    }
}
