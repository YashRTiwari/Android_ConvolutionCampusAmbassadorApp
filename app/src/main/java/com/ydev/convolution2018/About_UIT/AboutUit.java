package com.ydev.convolution2018.About_UIT;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.ydev.convolution2018.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUit extends Fragment implements View.OnClickListener {

    private ImageView imgUIT;
    private ImageView imgMAP;
    private ImageView imgHistory;

    public AboutUit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_uit, container, false);

        imgUIT = (ImageView) view.findViewById(R.id.imgUIT);
        imgMAP = (ImageView) view.findViewById(R.id.imgMAP);
        imgHistory = (ImageView) view.findViewById(R.id.imgHistory);

        imgMAP.setOnClickListener(this);

        setImage();
        return view;
    }

    private void setImage() {

        Picasso.with(getActivity()).load(R.drawable.uitrgpv).fit().into(imgUIT);
        Picasso.with(getActivity()).load(R.drawable.map).fit().into(imgMAP);
        Picasso.with(getActivity()).load(R.drawable.history).fit().into(imgHistory);

    }


    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.imgMAP) {

            String uri = getActivity().getResources().getString(R.string.map_url);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }

    }
}
