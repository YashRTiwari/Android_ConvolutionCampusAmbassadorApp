package com.ydev.convolution2018.RegistrationDashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ydev.convolution2018.R;

import java.util.ArrayList;

/**
 * Created by YRT on 15/12/2017.
 */

class ListViewAdapter extends BaseAdapter {

    private ArrayList<String> eventName;
    private ArrayList<Integer> eventImages;

    private LayoutInflater layoutInflater;
    private Context context;
    private ImageView imageC;

    public ListViewAdapter(Context context, ArrayList<String> eventName, ArrayList<Integer> eventImages) {

        this.eventName = eventName;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.eventImages = eventImages;
        this.context = context;
    }

    @Override
    public int getCount() {
        return eventName.size();
    }

    @Override
    public Object getItem(int position) {
        return eventName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        view = layoutInflater.inflate(R.layout.total_registration_listview, null);

        TextView name = (TextView) view.findViewById(R.id.txtViewEventName);
        imageC = (ImageView) view.findViewById(R.id.imgView);

        name.setText(eventName.get(position));

        Picasso.with(context).load(eventImages.get(position)).fit().into(imageC);

        return view;
    }
}
