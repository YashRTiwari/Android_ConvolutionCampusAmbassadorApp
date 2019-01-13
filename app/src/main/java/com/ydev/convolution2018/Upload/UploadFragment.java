package com.ydev.convolution2018.Upload;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.ydev.convolution2018.AdapterClasses.ListViewAdapterClass;
import com.ydev.convolution2018.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {

    private final static int INTENT_MESSAGE_CODE = 1;
    private static final int UPLOAD_ACTIVITY = 1005;
    private ProgressDialog progressDialog;
    private DatabaseReference mRef;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private View view;
    private ArrayList<String> uploadFileArray;
    private UploadFileDetailClass uploadFileDetail;
    private Date date;
    private ListView uploadListView;
    private ListViewAdapterClass adapter;

    public UploadFragment() {
        // Required empty public constructor
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference();

        date = new Date();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_upload, container, false);
        progressDialog = new ProgressDialog(getActivity());
        uploadListView = (ListView) view.findViewById(R.id.listViewUpload);

        uploadFileDetail = new UploadFileDetailClass();
        date = new Date();

        Button btnUploadContent = (Button) view.findViewById(R.id.btnUploadContent);
        btnUploadContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        upload();
                    } else {
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , 123);
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Something went wrong.", Toast.LENGTH_SHORT).show();
                }
            }
        });






        return view;
    }

    private void upload() {
        try {

            Intent documentIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
            documentIntent.setType("image/*");
            startActivityForResult(Intent.createChooser(documentIntent, "Select Picture"), INTENT_MESSAGE_CODE);

        } catch (Exception e) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);

        }

    }

    @Override
    public void onStart() {
        super.onStart();


        mRef.child("Upload").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> fileName = new ArrayList<>();
                ArrayList<String> fileDate = new ArrayList<>();
                ArrayList<String> imageuri = new ArrayList<>();

                // Toast.makeText(getActivity(), "upload child", Toast.LENGTH_SHORT).show();

                if (dataSnapshot.exists()) {

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        UploadFileDetailClass fileData = data.getValue(UploadFileDetailClass.class);
                        fileName.add(fileData.getFileName());
                        fileDate.add(fileData.getDate());
                        imageuri.add(fileData.getImageuri());

                    }
                    adapter = new ListViewAdapterClass(0, getActivity(), UPLOAD_ACTIVITY, fileName, fileDate, imageuri);
                    uploadListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        progressDialog.setTitle("Uploading");
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        if (resultCode == RESULT_OK) {

            progressDialog.show();

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Upload");
            builder.setCancelable(false);

            final Uri imageURI = data.getData();
            final String fileName = imageURI.getPath().substring(imageURI.getPath().lastIndexOf("/") + 1);


            SimpleDateFormat getDate = new SimpleDateFormat("yyyy-MM-dd HH:mm aaa");

            uploadFileDetail.setDate(getDate.format(date));
            uploadFileDetail.setFileName(fileName);

            final StorageReference imageAddressInFirebase = storageReference.child(mAuth.getCurrentUser().
                    getEmail()).child(fileName);


            builder.setMessage("Do you want to upload " + fileName + "?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    imageAddressInFirebase.putFile(imageURI).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            if (taskSnapshot.getTotalByteCount() > 1024 * 1024 * 2) {

                                Toast.makeText(getActivity(), "Size of file too large!", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                                return;

                            }

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            uploadFileDetail.setImageuri(taskSnapshot.getDownloadUrl().toString());
                            mRef.child("Upload").child(mAuth.getCurrentUser().getUid()).push().setValue(uploadFileDetail);
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), fileName + " Uploaded", Toast.LENGTH_SHORT).show();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Snackbar.make(view.findViewById(R.id.btnUploadContent), "Failed to upload " + fileName,
                                    Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();

                        }
                    });
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    progressDialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();


        } else if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getActivity(), "No File Selected!", Toast.LENGTH_SHORT).show();

        } else {
            Snackbar.make(view.findViewById(R.id.btnUploadContent), "Something went wrong. Try Again!",
                    Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
    }
}
