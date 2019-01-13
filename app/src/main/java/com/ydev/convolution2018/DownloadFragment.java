package com.ydev.convolution2018;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ydev.convolution2018.AdapterClasses.ListViewAdapterClass;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DownloadFragment extends Fragment {


    private static ListView listView;
    private final int DOWNLOAD_ACTIVITY = 1003;
    private DatabaseReference mRef;
    private ArrayList<DownloadFileDetails> files;

    public DownloadFragment() {
        // Required empty public constructor

        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_download, container, false);
        listView = (ListView) view.findViewById(R.id.listViewDownload);


        files = new ArrayList<>();
        mRef.child("download").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                files.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {

                    DownloadFileDetails downloadFile = data.getValue(DownloadFileDetails.class);
                    files.add(0,downloadFile);

                }

                ListViewAdapterClass adapterClass = new ListViewAdapterClass(getActivity(), DOWNLOAD_ACTIVITY, files);
                listView.setAdapter(adapterClass);
                adapterClass.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        return view;
    }

}
