package com.ydev.convolution2018.HomeFeed;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ydev.convolution2018.R;

public class EventFragment extends Fragment {

    private GridView gridViewEvent;
    private GridViewBaseAdpaterClass adapterClass;

    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_event, container, false);

        gridViewEvent = (GridView) view.findViewById(R.id.gridEventColumn);
        adapterClass = new GridViewBaseAdpaterClass(getActivity(), HomeFeedFragment.eventName, HomeFeedFragment.eventImages);
        gridViewEvent.setAdapter(adapterClass);
        adapterClass.notifyDataSetChanged();

        return view;
    }


}
