package com.ydev.convolution2018.Accommodation;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ydev.convolution2018.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccommodationFragment extends Fragment {


    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static View layoutView;
    private Spinner spinnerYear;
    private TextInputEditText edtName, edtCollege, edtEmail, edtPhone, edtAddress, edtEmergencyNumber;
    private RadioGroup radioGender;
    private Button btnReset;
    //For spinner
    private int year;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private CheckBox checkBoxD1, checkBoxD2, checkBoxD3;

    private TextView accommodationCharge;

    private String dayCode;
    private int dayCharge = 0;
    private int chargePerDay = 0;
    private DatabaseReference mRef;

    public AccommodationFragment() {
        // Required empty public constructor
    }

    private static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layoutView = inflater.inflate(R.layout.accommodation_fragment, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mRef = database.getReference();

        mRef.child("perDayCharge").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chargePerDay = dataSnapshot.getValue(Integer.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        edtName = (TextInputEditText) layoutView.findViewById(R.id.edtName);
        edtCollege = (TextInputEditText) layoutView.findViewById(R.id.edtCollege);
        edtEmail = (TextInputEditText) layoutView.findViewById(R.id.edtEmail);
        edtPhone = (TextInputEditText) layoutView.findViewById(R.id.edtPhone);
        edtAddress = (TextInputEditText) layoutView.findViewById(R.id.edtAddress);
        edtEmergencyNumber = (TextInputEditText) layoutView.findViewById(R.id.edtEmergencyNumber);

        spinnerYear = (Spinner) layoutView.findViewById(R.id.spinnerYear);

        accommodationCharge = (TextView) layoutView.findViewById(R.id.accommodationAmount);

        btnReset = (Button) layoutView.findViewById(R.id.btnResetAcc);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnResetClick();
            }
        });
        progressDialog = new ProgressDialog(getActivity());

        radioGender = (RadioGroup) layoutView.findViewById(R.id.radioGroupGender);
        RadioButton radioMale = (RadioButton) layoutView.findViewById(R.id.radioBtnMale);
        RadioButton radioFemale = (RadioButton) layoutView.findViewById(R.id.radioBtnFemale);

        radioGender.check(R.id.radioBtnMale);

        checkBoxD1 = (CheckBox) layoutView.findViewById(R.id.chkBoxDOne);
        checkBoxD2 = (CheckBox) layoutView.findViewById(R.id.chkBoxDTwo);
        checkBoxD3 = (CheckBox) layoutView.findViewById(R.id.chkBoxDThree);

        checkBoxD1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    dayCharge += chargePerDay;
                    accommodationCharge.setText(dayCharge+"");
                }else{
                    dayCharge -= chargePerDay;
                    accommodationCharge.setText(dayCharge+"");
                }

            }
        });
        checkBoxD2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    dayCharge += chargePerDay;
                    accommodationCharge.setText(dayCharge+"");
                }else{
                    dayCharge -= chargePerDay;
                    accommodationCharge.setText(dayCharge+"");

                }
            }
        });
        checkBoxD3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    dayCharge += chargePerDay;
                    accommodationCharge.setText(dayCharge+"");
                }else{
                    dayCharge -= chargePerDay;
                    accommodationCharge.setText(dayCharge+"");
                }
            }
        });

        Button btnSubmit = (Button) layoutView.findViewById(R.id.btnTeamOneSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSubmitClick();

            }
        });


        ArrayAdapter adapterYear = ArrayAdapter.createFromResource(getActivity(), R.array.year,
                android.R.layout.simple_spinner_item);
        adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterYear);

        mAuth = FirebaseAuth.getInstance();

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = Integer.parseInt(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getActivity(), "Please select Year of study.", Toast.LENGTH_SHORT).show();
            }
        });


        return layoutView;
    }

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context =context;
    }

    private void btnResetClick() {

        AccommodationFragment accommodationFragment = new AccommodationFragment();
        FragmentTransaction registrationFragmentTransaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
        registrationFragmentTransaction.replace(R.id.frame, accommodationFragment, "Registration Desk Fragment");
        registrationFragmentTransaction.commit();

    }

    private void btnSubmitClick() {

        boolean allValid = true;

        String name = edtName.getText().toString().trim();
        String college = edtCollege.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String emergencyNumber = edtEmergencyNumber.getText().toString().trim();

        String gender;
        int mf = radioGender.getCheckedRadioButtonId();

        RadioButton selectedGender = (RadioButton) layoutView.findViewById(mf);
        gender = selectedGender.getText().toString();

         dayCode = daySelectedCode();

        String allDetails = "Name: " + name + "\n" +
                "College: " + college + "\n" +
                "Year: " + year + "\n" +
                "Email: " + email + "\n" +
                "Phone: " + phone + "\n" +
                "Address: " + address + "\n" +
                "Gender: " + gender + "\n" +
                "Emergency Number: " + emergencyNumber + "\n" +
                "Day: "+ dayCode;


        if (TextUtils.isEmpty(name)) {
            edtName.setError("Fields can't be empty.");
            YoYo.with(Techniques.Shake).duration(600).playOn(edtName);
            allValid = false;

        }
        if (TextUtils.isEmpty(emergencyNumber)) {
            edtEmergencyNumber.setError("Fields can't be empty.");
            allValid = false;
            YoYo.with(Techniques.Shake).duration(600).playOn(edtEmergencyNumber);

        }
        if (TextUtils.isEmpty(college)) {
            edtCollege.setError("Fields can't be empty.");
            allValid = false;
            YoYo.with(Techniques.Shake).duration(600).playOn(edtCollege);

        }
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Fields can't be empty.");
            allValid = false;
            YoYo.with(Techniques.Shake).duration(600).playOn(edtEmail);

        }
        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Fields can't be empty.");
            allValid = false;
            YoYo.with(Techniques.Shake).duration(600).playOn(edtPhone);

        }
        if (TextUtils.isEmpty(address)) {
            edtAddress.setError("Fields can't be empty.");
            allValid = false;
            YoYo.with(Techniques.Shake).duration(600).playOn(edtAddress);

        }
        if (!validate(email)) {
            edtEmail.setError("Invalid Email.");
            allValid = false;
            YoYo.with(Techniques.Shake).duration(600).playOn(edtEmail);

        }
        if (year == 0) {
            allValid = false;
            YoYo.with(Techniques.Shake).duration(600).playOn(spinnerYear);

        }
        if (phone.length() != 10) {
            edtPhone.setError("Invalid Phone Number.");
            allValid = false;
            YoYo.with(Techniques.Shake).duration(600).playOn(edtPhone);

        }
        if (dayCode.equals("")){
            allValid = false;
            YoYo.with(Techniques.Shake).duration(600).playOn(checkBoxD1);
            YoYo.with(Techniques.Shake).duration(600).playOn(checkBoxD2);
            YoYo.with(Techniques.Shake).duration(600).playOn(checkBoxD3);

        }

        if (allValid) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Confirm Student details");
            builder.setMessage(allDetails);

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    progressDialog.setMessage("Please Wait...");
                    progressDialog.setTitle("Registering Candidate");
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                    registration();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

    private String daySelectedCode() {

        String day = "";

        if (checkBoxD1.isChecked() && checkBoxD2.isChecked() && checkBoxD3.isChecked()){
            day ="D123";

        }
        else if (checkBoxD1.isChecked() && checkBoxD2.isChecked() ){
            day ="D12";

        }else if (checkBoxD2.isChecked() && checkBoxD3.isChecked()){
            day ="D23";

        }
        else if (checkBoxD1.isChecked() && checkBoxD3.isChecked()){
            day ="D13";

        }
        else if (checkBoxD1.isChecked()){
            day ="D1";
        }
        else if (checkBoxD2.isChecked()){
            day ="D2";

        }else if (checkBoxD3.isChecked()){
            day ="D3";

        }else{

            day = "";
        }

    return day;
    }

    private void registration() {

        String name = edtName.getText().toString().trim();
        String college = edtCollege.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String emergencyNumber = edtEmergencyNumber.getText().toString().trim();


        String gender;

        int mf = radioGender.getCheckedRadioButtonId();
        RadioButton selectedGender = (RadioButton) layoutView.findViewById(mf);

        gender = selectedGender.getText().toString();

        AccommodationAsync registerAsync = new AccommodationAsync(getActivity());

        registerAsync.execute(mAuth.getCurrentUser().getEmail(),
                name,
                college,
                String.valueOf(year),
                email,
                phone,
                address,
                gender,
                emergencyNumber,
                dayCode);


    }


    private class AccommodationAsync extends AsyncTask<String, String, String> {

        Context context;
        String result;

        public AccommodationAsync(Context registrationActivity) {

            context = registrationActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String register_url = "http://phoenixatuit.com/Mobile_CAP/";
            try {
                URL url = new URL(register_url);
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
                        + URLEncoder.encode("address", "UTF-8") + "=" + URLEncoder.encode(params[6], "UTF-8") + "&"
                        + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(params[7], "UTF-8") + "&"
                        + URLEncoder.encode("emergencyNumber", "UTF-8") + "=" + URLEncoder.encode(params[8], "UTF-8") +"&"
                        + URLEncoder.encode("day","UTF-8") + "=" + URLEncoder.encode(params[9], "UTF-8");
                        ;
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

                progressDialog.dismiss();

            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();


                //Toast.makeText(context, "Something went wrong :(", Toast.LENGTH_SHORT).show();
            }
            return result;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("true")) {
                UpdateLeaderboard update = new UpdateLeaderboard(getActivity());
                update.execute(mAuth.getCurrentUser().getUid(), String.valueOf(1));
            } else {

                Toast.makeText(context, "Unable to update Leaderboard. Contact Tech support ASAP", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class UpdateLeaderboard extends AsyncTask<String, String, String> {

        Context context;

        public UpdateLeaderboard(Context registrationActivity) {

            context = registrationActivity;
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

                String post_data = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&" +
                        URLEncoder.encode("mcount", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");

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

                progressDialog.dismiss();


            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("false")) {
                Toast.makeText(context, "Unable to update Leader board.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Registered", Toast.LENGTH_LONG).show();
            }
        }
    }

}
