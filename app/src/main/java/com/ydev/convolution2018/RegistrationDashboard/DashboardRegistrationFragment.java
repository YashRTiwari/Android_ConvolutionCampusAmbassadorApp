package com.ydev.convolution2018.RegistrationDashboard;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ydev.convolution2018.R;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardRegistrationFragment extends Fragment implements AdapterView.OnItemClickListener {

    private FirebaseUser mUser;
    private Context context;
    int i = 0;
    private ArrayList<Integer> eventImages;

    public DashboardRegistrationFragment() {
        // Required empty public constructor

        mUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard_registration, container, false);

        ListView listViewEventWise = (ListView) view.findViewById(R.id.listViewTotalRegistration);

        ArrayList<String> events = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.all_events)));
        eventImages = new ArrayList<>();

        addEventImages();
        ListViewAdapter adapter = new ListViewAdapter(getActivity(), events, eventImages);
        listViewEventWise.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listViewEventWise.setOnItemClickListener(this);

        return view;
    }

    private void addEventImages() {

        //Zonals
        eventImages.add(R.drawable.gripperrobot);
        eventImages.add(R.drawable.quizologist);
        eventImages.add(R.drawable.codeio);
        eventImages.add(R.drawable.eureka);
        //EVents
        eventImages.add(R.drawable.dalalstreet);
        eventImages.add(R.drawable.horcrux);
        eventImages.add(R.drawable.quizologist);
        eventImages.add(R.drawable.novus);
        eventImages.add(R.drawable.hackathon);
        eventImages.add(R.drawable.gripperrobot);
        eventImages.add(R.drawable.mazerunner);
        //Workshops
        eventImages.add(R.drawable.autocad);
        eventImages.add(R.drawable.machinelearning);
        eventImages.add(R.drawable.dalalstreet);
        //Accommodation
        eventImages.add(R.drawable.accommodation_icon);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent intent1 = new Intent(getActivity(), StudentList.class);

        switch (position) {

            case DashboardConstant.zonals_code_io:
                intent1.putExtra("event", DashboardConstant.zonals_code_io);
                startActivity(intent1);
                break;

            case DashboardConstant.zonals_eureka:
                intent1.putExtra("event", DashboardConstant.zonals_eureka);
                startActivity(intent1);
                break;

            case DashboardConstant.zonals_quizologist:
                intent1.putExtra("event", DashboardConstant.zonals_quizologist);
                startActivity(intent1);
                break;

                /*
            case DashboardConstant.zonals_robo_gripper:
                intent1.putExtra("event", DashboardConstant.zonals_robo_gripper);
                startActivity(intent1);
                break;
*/
            case DashboardConstant.dalaal_street:
                intent1.putExtra("event", DashboardConstant.dalaal_street);
                startActivity(intent1);
                break;

            case DashboardConstant.quizologist:
                intent1.putExtra("event", DashboardConstant.quizologist);
                startActivity(intent1);
                break;

            case DashboardConstant.gripper:
                intent1.putExtra("event", DashboardConstant.gripper);
                startActivity(intent1);
                break;

            case DashboardConstant.novus:
                intent1.putExtra("event", DashboardConstant.novus);
                startActivity(intent1);
                break;

            case DashboardConstant.maze_runner:
                intent1.putExtra("event", DashboardConstant.maze_runner);
                startActivity(intent1);
                break;

            case DashboardConstant.horcrux:
                intent1.putExtra("event", DashboardConstant.horcrux);
                startActivity(intent1);
                break;

            case DashboardConstant.hackathon:
                intent1.putExtra("event", DashboardConstant.hackathon);
                startActivity(intent1);
                break;

            case DashboardConstant.iot:
                intent1.putExtra("event", DashboardConstant.iot);
                startActivity(intent1);
                break;

            case DashboardConstant.machine_learning:
                intent1.putExtra("event", DashboardConstant.machine_learning);
                startActivity(intent1);
                break;

            case DashboardConstant.stock_trading_workshop:
                intent1.putExtra("event", DashboardConstant.stock_trading_workshop);
                startActivity(intent1);
                break;

            case DashboardConstant.accommodation:
                intent1.putExtra("event", DashboardConstant.accommodation);
                startActivity(intent1);
                break;

        }

    }


}
