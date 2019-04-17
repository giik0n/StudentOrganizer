package com.babylone.alex.studentorganizer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.babylone.alex.studentorganizer.Classes.ChatUser;
import com.babylone.alex.studentorganizer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUsersAdapter extends BaseAdapter {
    Activity activity;
    ArrayList<ChatUser> arrayList;
    LayoutInflater inflater;

    public ChatUsersAdapter(Activity activity, ArrayList<ChatUser> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.chat_user_item,null);
        CircleImageView imageView = view.findViewById(R.id.chat_user_image);
        TextView username = view.findViewById(R.id.chat_user_name);
        TextView last_message = view.findViewById(R.id.chat_last_message);
        String id = arrayList.get(i).getId();
        username.setText(arrayList.get(i).getName());
        if (!TextUtils.isEmpty(arrayList.get(i).getImage())) Picasso.get().load(arrayList.get(i).getImage()).resize(100,100).into(imageView);
        return view;
    }
}
