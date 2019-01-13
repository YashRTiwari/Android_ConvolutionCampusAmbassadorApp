package com.ydev.convolution2018.Notification;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ydev.convolution2018.AdapterClasses.ListViewAdapterClass;
import com.ydev.convolution2018.R;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    private final int NOTIFICATION_ACTIVITY = 1004;
    private DatabaseReference mRef;
    private ListView listViewNotification;
    private ArrayList<NotificationObject> notificationObjects;
    private TextView txtViewNoDel;

    public NotificationFragment() {
        // Required empty public constructor

        mRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
        notificationObjects = new ArrayList<>();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        listViewNotification = (ListView) view.findViewById(R.id.listViewNotification);
        txtViewNoDel = (TextView) view.findViewById(R.id.txtViewNoDel);

        Query myTopPostsQuery = mRef.orderByValue();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                notificationObjects.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    NotificationObject obj = data.getValue(NotificationObject.class);
                    notificationObjects.add(obj);
                }

                Collections.reverse(notificationObjects);
                if (!notificationObjects.isEmpty()) {
                    txtViewNoDel.setVisibility(View.INVISIBLE);
                    listViewNotification.setVisibility(View.VISIBLE);

                    ListViewAdapterClass adapter = new ListViewAdapterClass(notificationObjects, getActivity(), NOTIFICATION_ACTIVITY);
                    listViewNotification.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
                    listViewNotification.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getActivity(), "Something went wrong!", Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }

}
