package com.ydev.convolution2018.RegistrationDashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ydev.convolution2018.R;

import java.util.ArrayList;

/**
 * Created by YRT on 16/12/2017.
 */

class ListViewAdapterStudentList extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<RegisteredCandidateClass> listOfStudents;

    public ListViewAdapterStudentList(Context context, ArrayList<RegisteredCandidateClass> listOfStudents) {

        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.listOfStudents = listOfStudents;
    }

    @Override
    public int getCount() {
        return listOfStudents.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfStudents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = layoutInflater.inflate(R.layout.registration_dashboard, null);
        TextView serial = (TextView) view.findViewById(R.id.textViewRegisteredStudentNumber);
        serial.setText((listOfStudents.size() - position) + "");

        TextView name = (TextView) view.findViewById(R.id.textViewRegisteredStudentName);
        TextView email = (TextView) view.findViewById(R.id.textViewRegisteredStudentEmail);

        name.setText(listOfStudents.get(position).getName());
        email.setText(listOfStudents.get(position).getEmail());

        return view;
    }
}
