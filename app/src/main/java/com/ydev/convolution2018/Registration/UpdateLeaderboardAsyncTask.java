package com.ydev.convolution2018.Registration;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

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


public class UpdateLeaderboardAsyncTask extends AsyncTask<String, String, String > {

    private Context context;

    public UpdateLeaderboardAsyncTask(Context context){

        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        String register_url = "http://phoenixatuit.com/Mobile_CAP/increment_one_with_id.php";
        String result = null;
        try {
            URL url = new URL(register_url);
            HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setDoOutput(true);

            OutputStream outputStream = httpUrlConnection.getOutputStream();
            OutputStreamWriter osWriter = new OutputStreamWriter(outputStream, "UTF-8");
            BufferedWriter bufferedWriter = new BufferedWriter(osWriter);

            String post_data = URLEncoder.encode("uid", "UTF-8") + "=" +
                    URLEncoder.encode(FirebaseAuth.getInstance().getCurrentUser().getUid(), "UTF-8") + "&" +
                    URLEncoder.encode("mcount", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
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

        if(!s.equals("Incremented")){
            Toast.makeText(context, "Unable to update Leader Board", Toast.LENGTH_LONG).show();
        }
    }
}
