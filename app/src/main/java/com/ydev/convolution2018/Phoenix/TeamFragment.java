package com.ydev.convolution2018.Phoenix;


import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ydev.convolution2018.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ArrayList<String> fullNames;
    private ArrayList<String> years;
    private ArrayList<String> emails;
    private ArrayList<String> phones;
    private ArrayList<String> names;


    private TypedArray imgs;

    public TeamFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team, container, false);
        GridView gridViewTeam = (GridView) view.findViewById(R.id.gridViewTEam);
        Toast.makeText(getActivity(), "Tap to view details!", Toast.LENGTH_SHORT).show();


        fullNames = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.fullnames)));
        emails = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.emails)));
        years = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.year_team)));
        phones = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.team_member_number)));
        names = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.names)));


        imgs = getActivity().getResources().obtainTypedArray(R.array.team_images);

        GridViewAdapter adapter = new GridViewAdapter(getActivity());
        gridViewTeam.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        gridViewTeam.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.team_member_info);

        TextView tname = (TextView) dialog.findViewById(R.id.txtTeamMemberName);
        //TextView tphone = (TextView) dialog.findViewById(R.id.txtTeamMemberPhone);
        TextView temail = (TextView) dialog.findViewById(R.id.txtTeamMemberEmail);
        TextView tyear = (TextView) dialog.findViewById(R.id.txtTeamMemberYear);
        ImageView img = (ImageView) dialog.findViewById(R.id.imgTeamMemberImage);


        tname.setText(fullNames.get(position));
        temail.setText("Email: " + emails.get(position));
        //tphone.setText("Phone: "+phones.get(position));
        tyear.setText("Year: " + years.get(position));

        Picasso.with(getContext()).
                load(imgs.getResourceId(position, -1)).
                centerInside().
                fit().
                into(img);

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        temail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                String em = emails.get(position);
                intent.setData(Uri.parse("mailto:" + em)); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, em);

                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });


        /*
        tphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Confirm");
                builder.setMessage("Do you wish to contact "+names.get(position)+"?");

                builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);

                        String number = phones.get(position);

                        if(ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED ){
                            callIntent.setData(Uri.parse("tel:"+number));
                            startActivity(callIntent);
                        }else{
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CALL_PHONE},2);
                        }
                    }
                }).setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setIcon(R.drawable.convolutionlogo).create().show();
            }
        });
        */


    }
}
