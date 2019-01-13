package com.ydev.convolution2018.Phoenix;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.ydev.convolution2018.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PhoenixFragment extends Fragment {


    public PhoenixFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_phoenix, container, false);

        ImageView phoenixImage = (ImageView) view.findViewById(R.id.phoenixImage);
        Picasso.with(getActivity()).load(R.drawable.phoenix_image).fit().into(phoenixImage);

        return view;
    }

}
