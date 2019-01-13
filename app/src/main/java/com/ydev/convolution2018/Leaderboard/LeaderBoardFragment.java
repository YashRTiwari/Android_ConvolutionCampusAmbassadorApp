package com.ydev.convolution2018.Leaderboard;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ydev.convolution2018.AdapterClasses.ListViewAdapterClass;
import com.ydev.convolution2018.R;
import com.ydev.convolution2018.Home;

import java.util.ArrayList;

public class LeaderBoardFragment extends Fragment {

    private static ListView listViewLeaderBoardRanking;
    private static ProgressBar progressBarRefresh;
    private static String imgURL;
    private static ArrayList<Bitmap> bitmap;
    private static ArrayList<String> bitmapURL;
    long refreshEvery;

    public LeaderBoardFragment() {
        // Required empty public constructor
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);

        listViewLeaderBoardRanking = (ListView) view.findViewById(R.id.listViewLeaderBoardRanking);
        progressBarRefresh = (ProgressBar) view.findViewById(R.id.progressBarRefresh);

        new JSONItunesStuffTask(getActivity()).execute();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Home.menuItem.setVisible(false);

    }

    public static class JSONItunesStuffTask extends AsyncTask<String, Void, ArrayList<LeaderboardStuff>> {

        Context context;

        public JSONItunesStuffTask(Context context) {
            this.context = context;

            ArrayList<NameAndRegistration> arrayList = new ArrayList<>();
            bitmap = new ArrayList<>();
            bitmapURL = new ArrayList<>();
        }

        public void refreshListData() {

            new JSONItunesStuffTask(context).execute();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressBarRefresh.setVisibility(View.VISIBLE);
            progressBarRefresh.setIndeterminate(true);
        }

        @Override
        protected ArrayList<LeaderboardStuff> doInBackground(String... params) {

            ArrayList<LeaderboardStuff> arrayLeaderboardStuff = new ArrayList<>();

            LeaderHttpClient leaderHttpClient = new LeaderHttpClient();

            String data = leaderHttpClient.getLeaderboardData();

            try {
                arrayLeaderboardStuff = JsonLeaderBoardParser.getLeaderBoardStuff(data);

                for (LeaderboardStuff stuff : arrayLeaderboardStuff) {

                    imgURL = stuff.getPhotourl();
                    bitmapURL.add(imgURL);

                }

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            return arrayLeaderboardStuff;
        }

        @Override
        protected void onPostExecute(ArrayList<LeaderboardStuff> arrayLeaderboardStuff) {
            super.onPostExecute(arrayLeaderboardStuff);

            ListViewAdapterClass adapter = new ListViewAdapterClass(context, arrayLeaderboardStuff, 1002, bitmap, bitmapURL);
            listViewLeaderBoardRanking.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            progressBarRefresh.setIndeterminate(false);
            progressBarRefresh.setVisibility(View.GONE);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            for (int i = 0; i < arrayLeaderboardStuff.size(); i++) {
                LeaderboardStuff stuff = arrayLeaderboardStuff.get(i);
                if (stuff.getEmail().equals(user.getEmail())) {
                    listViewLeaderBoardRanking.setSelection(i);
                    break;
                }
            }

        }

    }
}
