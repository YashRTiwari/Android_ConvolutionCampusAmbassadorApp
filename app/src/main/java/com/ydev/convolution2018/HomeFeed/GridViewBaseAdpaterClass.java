package com.ydev.convolution2018.HomeFeed;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.Tasks;
import com.squareup.picasso.Picasso;
import com.ydev.convolution2018.DownloadFragment;
import com.ydev.convolution2018.Home;
import com.ydev.convolution2018.R;

import java.util.ArrayList;

/**
 * Created by YRT on 02/12/2017.
 */

class GridViewBaseAdpaterClass extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context context;

    private ArrayList<String> eventName;
    private ArrayList<Integer> eventImages;

    public GridViewBaseAdpaterClass(Context context, ArrayList<String> eventName, ArrayList<Integer> eventImages) {

        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.eventImages = eventImages;
        this.eventName = eventName;
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
    public View getView(int position, View view, ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.event_layout_file, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.gridEventImage);

        Picasso.with(context).load(eventImages.get(position)).fit().centerInside().into(imageView);


        Animation fadeIn = AnimationUtils.loadAnimation(context, R.anim.fadein);
        view.startAnimation(fadeIn);


        Button btnEvent = (Button) view.findViewById(R.id.btnEvent);
        btnEvent.setText(eventName.get(position));

        final View viewFinal = view;
        viewFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //YoYo.with(Techniques.Shake).duration(1000).playOn(viewFinal);
                //Toast.makeText(context, "All the details will be available soon in Download section !", Toast.LENGTH_LONG).show();
                ((AppCompatActivity)context).setTitle("Download");
                DownloadFragment fragment = new DownloadFragment();
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        return view;
    }
}
