package com.ydev.convolution2018.Registration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.ydev.convolution2018.R;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by YRT on 10/12/2017.
 */

class TeamRegistrationListViewClass extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private int size;
    private ArrayList<String> eventSelectorList;
    private ArrayList<String> eventSelectorPriceList;
    private boolean[] isSelected;

    private ArrayList<String> phpEvents;

    public TeamRegistrationListViewClass(Context context, ArrayList<String> eventSelectorList, ArrayList<String> eventSelectorPriceList, int size) {

        this.context = context;
        this.eventSelectorList = eventSelectorList;
        this.eventSelectorPriceList = eventSelectorPriceList;
        this.size = size;
        isSelected = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false};
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        phpEvents = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.php_events)));

    }

    @Override
    public int getCount() {
        return eventSelectorList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventSelectorList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        view = layoutInflater.inflate(R.layout.event_dialog_list_view, null);

        ToggleButton btnEvent = (ToggleButton) view.findViewById(R.id.btnEvent);
        btnEvent.setTextOff(eventSelectorList.get(position));
        btnEvent.setTextOn(eventSelectorList.get(position));
        btnEvent.setText(eventSelectorList.get(position));
        btnEvent.setTag(eventSelectorList.get(position));
        btnEvent = isSelectedFunction(btnEvent, isSelected[position]);

        final ToggleButton finalBtnEvent = btnEvent;
        btnEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    TeamRegistraion.updatePriceTeamRegistration(Integer.parseInt(eventSelectorPriceList.get(position)), true);
                    isSelected[position] = true;

                    TeamRegistraion.teamOneEventSelectedList.add(finalBtnEvent.getTag().toString());
                    TeamRegistraion.phpList.add(TeamRegistraion.phpEventList.get(position));


                } else if (!isChecked) {
                    TeamRegistraion.updatePriceTeamRegistration(Integer.parseInt(eventSelectorPriceList.get(position)), false);
                    isSelected[position] = false;
                    TeamRegistraion.teamOneEventSelectedList.remove(finalBtnEvent.getTag().toString());

                }
            }
        });

        return view;
    }

    private ToggleButton isSelectedFunction(ToggleButton toggleButton, boolean value) {

        toggleButton.setChecked(value);

        return toggleButton;
    }
}
