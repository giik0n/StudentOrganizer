package com.babylone.alex.studentorganizer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.babylone.alex.studentorganizer.Classes.Lesson;
import com.babylone.alex.studentorganizer.Classes.Message;
import com.babylone.alex.studentorganizer.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends BaseAdapter {
    private Activity activity;
    ArrayList<Message> arrayList;
    private static final int MSG_LEFT = 0;
    private static final int MSG_RIGHT = 1;
    FirebaseUser mAuth;
    LayoutInflater inflater;
    String image;

    public MessageAdapter(Activity activity, ArrayList<Message> arrayList, String image) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.image = image;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        if(view == null){
            switch (getItemViewType(i)){
                case MSG_RIGHT:
                    view = inflater.inflate(R.layout.chat_item_right,null);
                    break;
                case MSG_LEFT:
                    view = inflater.inflate(R.layout.chat_item_left,null);
                    break;
            }
        }

        switch (getItemViewType(i)){
            case MSG_RIGHT:
                TextView message = view.findViewById(R.id.show_message_right);
                message.setText(arrayList.get(i).getMessage());
                break;
            case MSG_LEFT:
                TextView messageLeft = view.findViewById(R.id.show_message);
                messageLeft.setText(arrayList.get(i).getMessage());
                CircleImageView imageView = view.findViewById(R.id.profile_chat_image);
                try {
                    Picasso.get().load(image).into(imageView);
                }catch (IllegalArgumentException e){
                    e.printStackTrace();
                }
                break;
        }
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        if (arrayList.get(position).getSender().equals(mAuth.getUid())){
            return MSG_RIGHT;
        }else {
            return MSG_LEFT;
        }
    }
}
