package com.ydev.convolution2018.RegistrationDashboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by YRT on 12/12/2017.
 */

class JSONRegisteredCandidateParser {

    public static ArrayList<RegisteredCandidateClass> getData(String data) throws JSONException {

        ArrayList<RegisteredCandidateClass> listOfRegisteredCandidate = new ArrayList<>();

        JSONObject regCandidateJSON = new JSONObject(data);

        //TODO
        JSONArray resultArray = regCandidateJSON.getJSONArray("server_response");

        for (int i = 0; i < resultArray.length(); i++) {

            RegisteredCandidateClass candidate = new RegisteredCandidateClass();

            JSONObject obj = resultArray.getJSONObject(i);
            candidate.setName(getString("name", obj));
            candidate.setCollege(getString("college", obj));
            candidate.setPhone(getString("phone", obj));
            candidate.setEmail(getString("email", obj));
            candidate.setYear(getString("year", obj));

            listOfRegisteredCandidate.add(candidate);
        }

        return listOfRegisteredCandidate;
    }

    private static String getString(String tag, JSONObject object) throws JSONException {
        return object.getString(tag);
    }
}
