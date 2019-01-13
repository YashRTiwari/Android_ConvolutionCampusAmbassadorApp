package com.ydev.convolution2018.Leaderboard;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by YRT on 29/10/2017.
 */

class LeaderHttpClient {


    public String getLeaderboardData() {

        HttpURLConnection httpURLConnection = null;

        InputStream inputStream = null;
        try {
            String BASE_URL = "http://yrtwebhosting-com.stackstaging.com/json_cap_leaderboard.php";
            httpURLConnection = (HttpURLConnection) (new URL(BASE_URL)).openConnection();

            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();


            StringBuffer stringBuffer = new StringBuffer();
            inputStream = httpURLConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;

            while ((line = bufferedReader.readLine()) != null) {

                stringBuffer.append(line).append("\n");

            }

            inputStream.close();
            httpURLConnection.disconnect();

            return stringBuffer.toString();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                inputStream.close();
                httpURLConnection.disconnect();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        return null;
    }

}
