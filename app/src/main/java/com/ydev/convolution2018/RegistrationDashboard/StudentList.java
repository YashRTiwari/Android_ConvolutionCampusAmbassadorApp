package com.ydev.convolution2018.RegistrationDashboard;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.ydev.convolution2018.R;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class StudentList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static ListView listView;
    private static ArrayList<RegisteredCandidateClass> list;
    String event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Registered Student");


        listView = (ListView) findViewById(R.id.studentListView);
        getEvent();
        listView.setOnItemClickListener(this);
    }

    private void getEvent() {

        int pos = getIntent().getExtras().getInt("event");
        JsonResponseCandidateRegister response = new JsonResponseCandidateRegister(this);
        response.execute(getResources().getStringArray(R.array.php_events)[pos]);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        Dialog infoDialog = new Dialog(this);
        infoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoDialog.setContentView(R.layout.student_info_dialog);

        final TextView name = (TextView) infoDialog.findViewById(R.id.name);
        TextView college = (TextView) infoDialog.findViewById(R.id.college);
        TextView email = (TextView) infoDialog.findViewById(R.id.email);
        TextView phone = (TextView) infoDialog.findViewById(R.id.phone);
        TextView year = (TextView) infoDialog.findViewById(R.id.year);

        name.setText(list.get(position).getName());
        college.setText("College: " + list.get(position).getCollege());
        email.setText("Email: " + list.get(position).getEmail());
        phone.setText("Phone: " + list.get(position).getPhone());
        year.setText("Year: " + list.get(position).getYear());

        ImageButton call = (ImageButton) infoDialog.findViewById(R.id.call);
        ImageButton mail = (ImageButton) infoDialog.findViewById(R.id.mail);

        final String phm = list.get(position).getPhone();
        final String em = list.get(position).getEmail();

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(StudentList.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Do you wish to contact " + name.getText().toString() + "?");

                builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        String number = phm;

                        if (ActivityCompat.checkSelfPermission(StudentList.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            callIntent.setData(Uri.parse("tel:" + number));
                            startActivity(callIntent);
                        } else {
                            ActivityCompat.requestPermissions(StudentList.this, new String[]{Manifest.permission.CALL_PHONE}, 2);
                        }
                    }
                }).setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.setIcon(R.drawable.convolutionlogo).create().show();

            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + em)); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, em);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        infoDialog.show();

        Window window = infoDialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private class JsonResponseCandidateRegister extends AsyncTask<String, String, String> {

        Context context;
        StringBuffer result;
        ArrayList<String> resultArray;
        ProgressDialog progressDialog;

        public JsonResponseCandidateRegister(Context registrationActivity) {

            context = registrationActivity;
            progressDialog = new ProgressDialog(context);
            resultArray = new ArrayList<>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog.setMessage("Fetching data...");
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {


            String register_url = "http://phoenixatuit.com/Mobile_CAP/json_dashboard_data.php";
            try {
                URL url = new URL(register_url);
                HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setDoOutput(true);

                OutputStream outputStream = httpUrlConnection.getOutputStream();
                OutputStreamWriter osWriter = new OutputStreamWriter(outputStream, "UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(osWriter);

                String post_data = URLEncoder.encode("cap", "UTF-8") + "=" +
                        URLEncoder.encode(FirebaseAuth.getInstance().getCurrentUser().getEmail(), "UTF-8") + "&"
                        + URLEncoder.encode("event", "UTF-8") + "=" +
                        URLEncoder.encode(params[0], "UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();

                InputStream inputStream = httpUrlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                result = new StringBuffer();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {

                    result.append(line).append("\n");

                }

                bufferedReader.close();
                inputStream.close();
                httpUrlConnection.disconnect();

                progressDialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }


            return result.toString();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            progressDialog.dismiss();

            try {
                list = JSONRegisteredCandidateParser.getData(s);

                if (list.isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(StudentList.this);
                    builder.setMessage("No Registration found");

                    builder.setIcon(R.drawable.convolutionlogo).create().show();
                    return;

                }

                ListViewAdapterStudentList adapter = new ListViewAdapterStudentList(context, list);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
