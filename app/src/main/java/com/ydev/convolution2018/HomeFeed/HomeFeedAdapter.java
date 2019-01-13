package com.ydev.convolution2018.HomeFeed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ydev.convolution2018.R;

import java.util.ArrayList;

/**
 * Created by YRT on 18/12/2017.
 */
class HomeFeedAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private ArrayList<String> title;
    private ArrayList<String> content;
    private ArrayList<Integer> image;

    public HomeFeedAdapter(Context context, ArrayList<Integer> image, ArrayList<String> title, ArrayList<String> content) {

        this.title = title;
        this.image = image;
        this.content = content;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public Object getItem(int position) {
        return title.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = layoutInflater.inflate(R.layout.home_feed_list_view_layout, null);

        TextView eventTitle = (TextView) view.findViewById(R.id.eventTitle);
        TextView eventContent = (TextView) view.findViewById(R.id.eventContent);
        final ImageView eventImage = (ImageView) view.findViewById(R.id.eventImage);


        eventTitle.setText(title.get(position));
        eventContent.setText(content.get(position));

        Picasso.with(context).load(image.get(position)).fit().centerInside().into(eventImage);
        return view;
    }
}
