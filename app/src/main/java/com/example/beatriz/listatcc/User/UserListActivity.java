package com.example.beatriz.listatcc.User;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.beatriz.listatcc.Model.User;
import com.example.beatriz.listatcc.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity{
    private ListView mUserListView;
    private UserListAdapter mUserAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private ChildEventListener mChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mUserListView = (ListView)findViewById(R.id.user_list);
        mUsersDatabaseReference = mFirebaseDatabase.getReference().child("users");


        //Initialize user ListView and its adapter
        List<User> users = new ArrayList<>();
        mUserAdapter = new UserListAdapter(this,R.layout.user_cell,users);
        mUserListView.setAdapter(mUserAdapter);

        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    User userList = dataSnapshot.getValue(User.class);
                    mUserAdapter.add(userList);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                public void onCancelled(DatabaseError databaseError) {}
            };
            mUsersDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }

    private void updateFriendInShareWith(){

    }
}
