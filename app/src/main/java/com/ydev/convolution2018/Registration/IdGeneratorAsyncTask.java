package com.ydev.convolution2018.Registration;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by YRT on 07/01/2018.
 */

public class IdGeneratorAsyncTask extends AsyncTask<String, String, String> {

    private String urlString = "https://convolutionrgpv.in/accomodation/index.php";
    String result;
    Context context;

    public IdGeneratorAsyncTask(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setDoOutput(true);

            OutputStream outputStream = httpUrlConnection.getOutputStream();
            OutputStreamWriter osWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(osWriter);

            String post_data = URLEncoder.encode("cap", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&"
                    + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&"
                    + URLEncoder.encode("college", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&"
                    + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8") + "&"
                    + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[4], "UTF-8") + "&"
                    + URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(params[5], "UTF-8") + "&"
                    + URLEncoder.encode("convid", "UTF-8") + "=" + URLEncoder.encode(params[6], "UTF-8");

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();

            InputStream inputStream = httpUrlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            result = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {

                result += line;

            }

            bufferedReader.close();
            inputStream.close();
            httpUrlConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if (Boolean.parseBoolean(result)){
            Toast.makeText(context, "Email Sent", Toast.LENGTH_SHORT).show();

            UpdateLeaderboardAsyncTask updateLeaderboardAsyncTask = new UpdateLeaderboardAsyncTask(context);
            updateLeaderboardAsyncTask.execute("1");
        }else{
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
