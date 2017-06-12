
package com.example.beatriz.listatcc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.beatriz.listatcc.Database.DatabaseHandler;
import com.example.beatriz.listatcc.Model.Category;
import com.example.beatriz.listatcc.Model.Product;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class MainActivity extends AppCompatActivity {
    DatabaseHandler database;
    Firebase mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        database = new DatabaseHandler(getBaseContext());

        Firebase.setAndroidContext(this);
        mRootRef = new Firebase("https://web-service-e6265.firebaseio.com/");

        firebaseCategoriesListener();
        firebaseProductListener();
        configMainLayout();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private void firebaseCategoriesListener() {
        Firebase messageRef = mRootRef.child("categories");
        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Category category = dataSnapshot.getValue(Category.class);
                database.populateCategoryTable(category);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Category category = dataSnapshot.getValue(Category.class);
                database.updateCategory(category);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void firebaseProductListener() {
        Firebase messageRef = mRootRef.child("products");
        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);
                database.populateProductTable(product);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Product product = dataSnapshot.getValue(Product.class);
                database.updateProduct(product);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void configMainLayout() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, new MainMenuFragment())
                .commit();
    }

    public void setActionBarTitle(String title, boolean hasBackButton) {
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(title);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(hasBackButton);
    }

    public void switchContent(int id, Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(id, fragment)
                .addToBackStack(null)
                .commit();
    }
}
