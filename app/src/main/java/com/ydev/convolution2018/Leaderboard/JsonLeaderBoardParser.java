package com.ydev.convolution2018.Leaderboard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by YRT on 29/10/2017.
 */

class JsonLeaderBoardParser {

    public static ArrayList<LeaderboardStuff> getLeaderBoardStuff(String data) throws JSONException {

        ArrayList<LeaderboardStuff> arrayList = new ArrayList<>();


        JSONObject leaderboardJsonObject = new JSONObject(data);

        JSONArray resultArray = leaderboardJsonObject.getJSONArray("server_response");

        // JSONObject jsonObjectLeaderBoard = resultArray.getJSONObject(1);

        for (int i = 0; i < resultArray.length(); i++) {

            LeaderboardStuff leaderboardStuff = new LeaderboardStuff();

            JSONObject jsonObjectLeaderBoard = resultArray.getJSONObject(i);

            leaderboardStuff.setName(getString("name", jsonObjectLeaderBoard));
            leaderboardStuff.setUid(getString("uid", jsonObjectLeaderBoard));
            leaderboardStuff.setPhotourl(getString("photourl", jsonObjectLeaderBoard));
            leaderboardStuff.setEmail(getString("email", jsonObjectLeaderBoard));
            leaderboardStuff.setRegistration(getInt("registration", jsonObjectLeaderBoard));

            arrayList.add(leaderboardStuff);

        }

        return arrayList;
    }

    private static JSONObject getJsonObject(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getJSONObject(tagName);
    }

    private static String getString(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jsonObject) throws JSONException {
        return (float) jsonObject.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt(tagName);
    }

    private static boolean getBoolean(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getBoolean(tagName);
    }


}
