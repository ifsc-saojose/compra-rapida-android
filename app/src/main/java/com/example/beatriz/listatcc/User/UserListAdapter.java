package com.example.beatriz.listatcc.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.beatriz.listatcc.Model.User;
import com.example.beatriz.listatcc.R;

import java.util.List;

public class UserListAdapter extends ArrayAdapter<User> {


    public UserListAdapter(Context context, int resource, List<User> users) {
        super(context, resource, users);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.user_cell, parent, false);
        }

        LinearLayout userCell = (LinearLayout) convertView.findViewById(R.id.user_cell);
        ImageView userImage = (ImageView) convertView.findViewById(R.id.user_photo);
        TextView userName = (TextView) convertView.findViewById(R.id.user_name);
        TextView userEmail = (TextView) convertView.findViewById(R.id.user_email);

        final User user = getItem(position);

        boolean isPhoto = user.getPhoto() != null;

        if (isPhoto) {
            Glide.with(userImage.getContext())
                    .load(user.getPhoto())
                    .into(userImage);
        } else {
            userImage.setVisibility(View.GONE);
        }

        userName.setText(user.getUserName());
        userEmail.setText(user.getEmail());

        userCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent userToShareList = new Intent();
                userToShareList.putExtra("EMAIL", user.getEmail());
                userToShareList.putExtra("NAME", user.getUserName());
                ((Activity)v.getContext()).setResult(Activity.RESULT_OK,userToShareList);
                ((Activity)v.getContext()).finish();

            }
        });

        return convertView;
    }
}
