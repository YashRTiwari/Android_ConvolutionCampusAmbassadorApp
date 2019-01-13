package com.ydev.convolution2018.AdapterClasses;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ydev.convolution2018.DownloadFileDetails;
import com.ydev.convolution2018.Leaderboard.LeaderboardStuff;
import com.ydev.convolution2018.Leaderboard.LoadingCapImagesAsync;
import com.ydev.convolution2018.Notification.NotificationObject;
import com.ydev.convolution2018.R;

import java.io.File;
import java.util.ArrayList;


public class ListViewAdapterClass extends BaseAdapter {

    private final int CALL_ACTIVITY = 1000;
    private final int HOME_FEED_ACTIVITY = 1001;
    private final int LEADERBOAD_ACTIVITY = 1002;
    private final int DOWNLOAD_ACTIVITY = 1003;
    private final int NOTIFICATION_ACTIVITY = 1004;
    private final int UPLOAD_ACTIVITY = 1005;
    private final int TECH_SUPPORT = 1006;
    private ArrayList<Bitmap> bitmaps;
    private int[] techImages = {R.drawable.yash_tiwari, R.drawable.ayush_gupta};
    //Leaderboard Fragment
    private ArrayList<String> bitmapURL;
    private ArrayList<DownloadFileDetails> files;
    //NOTIFICATION fragment
    private ArrayList<NotificationObject> notificationObjects;
    private int[] callImages = {
            R.drawable.ashesh_pathak,
            R.drawable.avi_agrawal,
            R.drawable.divyanshu_rathore};
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<LeaderboardStuff> leaderboardStuffs;
    //Activity Call
    private ArrayList<String> name, role;
    private int activity;
    //Activity Home Feed
    private ArrayList<Integer> eventImageArrayList;
    private ArrayList<String> eventTitleArrayList;
    private ArrayList<String> eventContentArrayList;
    //Upload Fragment
    private ArrayList<String> fileName;
    private ArrayList<String> fileDate;

    public ListViewAdapterClass(Context context, ArrayList<String> name, ArrayList<String> role, int activity) {

        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.name = name;
        this.role = role;
        this.activity = activity;

    }

    public ListViewAdapterClass(int activity, Context context, ArrayList<String> name, ArrayList<String> role) {

        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.name = name;
        this.role = role;
        this.activity = activity;

    }

    public ListViewAdapterClass(Context context, ArrayList<LeaderboardStuff> maps, int activity, ArrayList<Bitmap> bitmaps, ArrayList<String> bitmapURL) {

        this.context = context;
        this.activity = activity;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.leaderboardStuffs = maps;
        this.bitmaps = bitmaps;
        this.bitmapURL = bitmapURL;

    }


    //Download Fragment
    public ListViewAdapterClass(Context context, int activity, ArrayList<DownloadFileDetails> files) {

        this.context = context;
        this.activity = activity;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.files = files;

    }
    public ListViewAdapterClass(ArrayList<NotificationObject> notificationObjects, Context context, int activity) {

        this.context = context;
        this.activity = activity;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.notificationObjects = notificationObjects;

    }

    ArrayList<String> imageuri;

    public ListViewAdapterClass(int temp, Context context, int activity, ArrayList<String> fileName, ArrayList<String> fileDate, ArrayList<String> imageuri) {

        this.activity = activity;
        this.fileDate = fileDate;
        this.fileName = fileName;
        this.context = context;
        this.imageuri = imageuri;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        switch (activity) {

            case CALL_ACTIVITY:
                return callImages.length;
            case TECH_SUPPORT:
                return name.size();

            case HOME_FEED_ACTIVITY:
                return eventImageArrayList.size();

            case LEADERBOAD_ACTIVITY:
                return leaderboardStuffs.size();

            case DOWNLOAD_ACTIVITY:
                return files.size();

            case NOTIFICATION_ACTIVITY:
                return notificationObjects.size();

            case UPLOAD_ACTIVITY:
                return fileName.size();


        }

        return 0;
    }

    @Override
    public Object getItem(int position) {


        switch (activity) {

            case CALL_ACTIVITY:
                return position;
            case HOME_FEED_ACTIVITY:
                return position;
            case LEADERBOAD_ACTIVITY:
                return position;
            case DOWNLOAD_ACTIVITY:
                return position;
            case NOTIFICATION_ACTIVITY:
                return position;
            case UPLOAD_ACTIVITY:
                return position;
            case TECH_SUPPORT:
                return position;


        }

        return 0;
    }

    @Override
    public long getItemId(int position) {

        switch (activity) {
            case CALL_ACTIVITY:
                return position;
            case HOME_FEED_ACTIVITY:
                return position;
            case LEADERBOAD_ACTIVITY:
                return position;
            case DOWNLOAD_ACTIVITY:
                return position;
            case NOTIFICATION_ACTIVITY:
                return position;
            case UPLOAD_ACTIVITY:
                return position;
            case TECH_SUPPORT:
                return position;

        }

        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        // view = layoutInflater.inflate(R.layout.blank, null);
        if (activity == CALL_ACTIVITY) {
            view = layoutInflater.inflate(R.layout.call_list_view_layout, null);

            TextView txtPhoenixMember = (TextView) view.findViewById(R.id.txtPhoenixMember);
            TextView txtPhoenixRole = (TextView) view.findViewById(R.id.txtPhoenixMemberRole);
            final ImageView imgPheonixMemberImage = (ImageView) view.findViewById(R.id.imgPhoenixMemberImage);

            txtPhoenixMember.setText(name.get(position));
            txtPhoenixRole.setText(role.get(position));
            Picasso.with(context).load(callImages[position]).fit().into(imgPheonixMemberImage);
            return view;
        }
        if (activity == TECH_SUPPORT) {

            view = layoutInflater.inflate(R.layout.call_list_view_layout, null);

            TextView txtPhoenixMember = (TextView) view.findViewById(R.id.txtPhoenixMember);
            TextView txtPhoenixRole = (TextView) view.findViewById(R.id.txtPhoenixMemberRole);
            ImageView imgPheonixMemberImage = (ImageView) view.findViewById(R.id.imgPhoenixMemberImage);

            txtPhoenixMember.setText(name.get(position));
            txtPhoenixRole.setText(role.get(position));
            Picasso.with(context).load(techImages[position]).fit().into(imgPheonixMemberImage);

            return view;


        } else if (activity == LEADERBOAD_ACTIVITY) {

            view = layoutInflater.inflate(R.layout.leader_board_list_view, null);

            TextView txtRank = (TextView) view.findViewById(R.id.txtRank);
            TextView txtCapName = (TextView) view.findViewById(R.id.txtCAPName);
            TextView txtCapReg = (TextView) view.findViewById(R.id.txtCAPRegistration);
            final ImageView imgCap = (ImageView) view.findViewById(R.id.txtCapImage);

            LoadingCapImagesAsync task = new LoadingCapImagesAsync(context, imgCap);
            task.execute(bitmapURL.get(position));

            txtRank.setText(position + 1 + "");
            txtCapName.setText((leaderboardStuffs.get(position).getName()).toUpperCase());
            txtCapReg.setText(String.valueOf(leaderboardStuffs.get(position).getRegistration()));

            return view;
        } else if (activity == DOWNLOAD_ACTIVITY) {

            view = layoutInflater.inflate(R.layout.download_list_view, null);

            final TextView downloadName = (TextView) view.findViewById(R.id.downloadName);
            final TextView mimeType = (TextView) view.findViewById(R.id.mimeType);

            ImageButton imageDownloadButton = (ImageButton) view.findViewById(R.id.imageDownloadButton);

            downloadName.setText(files.get(position).getName());
            mimeType.setText(files.get(position).getExtension());
            //Check whether file exists.
            String filePath = Environment.DIRECTORY_DOWNLOADS + "/Convolution/" + files.get(position).getName() + "." + files.get(position).getExtension();
            final File f = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS) + "/Convolution/",
                    files.get(position).getName() + "." + files.get(position).getExtension());

            if (f.exists() && !f.isDirectory()) {
                imageDownloadButton.setImageResource(R.drawable.openfileimg);
            }


            imageDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (f.exists() && !f.isDirectory()) {

                        Intent openFile = new Intent(Intent.ACTION_VIEW);
                        openFile.setDataAndType(Uri.fromFile(f), files.get(position).getMime());
                        openFile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(Intent.createChooser(openFile, "Select app to open " + files.get(position).getMime()));

                        return;
                    }

                    if (files.get(position).getUrl().equals("none")) {
                        Toast.makeText(context, "This document is not yet available for download", Toast.LENGTH_LONG).show();
                        return;
                    }

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(files.get(position).getUrl()));
                    request.setTitle(downloadName.getText().toString());
                    request.setDescription("File is been downloaded");
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS + "/Convolution",
                            files.get(position).getName() + "." + files.get(position).getExtension());

                    DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    long id = manager.enqueue(request);


                }
            });

            return view;
        } else if (activity == NOTIFICATION_ACTIVITY) {

            view = layoutInflater.inflate(R.layout.notification_list_view, null);

            TextView textView = (TextView) view.findViewById(R.id.txtViewBody);
            textView.setText(notificationObjects.get(position).getNotification());

            ImageView notificationImage = (ImageView) view.findViewById(R.id.notificationImage);

            String type = notificationObjects.get(position).getType();

            switch (type){

                case "task":
                    Picasso.with(context).load(R.drawable.task_n).fit().into(notificationImage);
                    break;
                case "alert":
                    Picasso.with(context).load(R.drawable.alert_n).fit().into(notificationImage);

                    break;
                case "upload":
                    Picasso.with(context).load(R.drawable.upload_n).fit().into(notificationImage);

                    break;
                case "download":
                    Picasso.with(context).load(R.drawable.download_n).fit().into(notificationImage);

                    break;
                case "update":
                    Picasso.with(context).load(R.drawable.update_n ).fit().into(notificationImage);

                    break;
            }


            TextView textView2 = (TextView) view.findViewById(R.id.date);
            textView2.setText(notificationObjects.get(position).getDate());

            return view;
        } else if (activity == UPLOAD_ACTIVITY) {

            view = layoutInflater.inflate(R.layout.notification_list_view, null);
            TextView name = (TextView) view.findViewById(R.id.txtViewBody);
            TextView date = (TextView) view.findViewById(R.id.date);
            ImageView image = (ImageView) view.findViewById(R.id.notificationImage);
            Picasso.with(context).load(imageuri.get(position)).fit().into(image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {

                    Toast.makeText(context, "Error"+position, Toast.LENGTH_SHORT).show();
                }
            });
            name.setText(fileName.get(position));
            date.setText(fileDate.get(position));

            return view;
        } else {
            return null;
        }

    }

}
