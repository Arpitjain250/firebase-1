package com.icu;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.icu.database.Message;

import java.util.ArrayList;
import java.util.List;


public class FirebaseDataAdapter<T> {

    private Query firebaseReference;
    private Class<T> targetClass;

    private DataListener dataListener;


    public FirebaseDataAdapter(Query mRef, Class<T> targetClass, final DataListener dataListener) {
        this.firebaseReference = mRef;
        this.targetClass = targetClass;
        this.dataListener = dataListener;
        this.firebaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                T model = dataSnapshot.getValue(FirebaseDataAdapter.this.targetClass);

                dataListener.onMessageReceived((Message) model);


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });
    }


}
