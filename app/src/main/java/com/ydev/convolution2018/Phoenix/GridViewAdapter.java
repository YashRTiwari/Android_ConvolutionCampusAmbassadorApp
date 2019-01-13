package com.ydev.convolution2018.Phoenix;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ydev.convolution2018.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by YRT on 20/12/2017.
 */

class GridViewAdapter extends BaseAdapter {

    private ArrayList<String> names;
    private ArrayList<String> fullNames;

    private Context context;
    private LayoutInflater layoutInflater;

    public GridViewAdapter(Context context) {

        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        names = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.names)));
        fullNames = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.fullnames)));
        ArrayList<String> email = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.emails)));
        ArrayList<String> year = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.year_team)));


    }

    public void addImages() {


    }

    @Override
    public int getCount() {
        return fullNames.size();
    }

    @Override
    public Object getItem(int position) {
        return fullNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        view = layoutInflater.inflate(R.layout.team_grid_view, null);
        TextView name = (TextView) view.findViewById(R.id.txtTeamName);
        name.setTextSize(13);
        name.setText(names.get(position));

        ImageView img = (ImageView) view.findViewById(R.id.imageView);

        TypedArray imgs = context.getResources().obtainTypedArray(R.array.team_images);
        imgs.getResourceId(position, -1);

        Picasso.with(context).load(imgs.getResourceId(position, -1)).centerInside().fit().into(img);
        //Add Aysnc task.

        return view;
    }
}
