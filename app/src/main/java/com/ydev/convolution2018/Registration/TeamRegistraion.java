package com.ydev.convolution2018.Registration;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class TeamRegistraion extends Fragment implements View.OnClickListener {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final int ALL_EVENT_COST = 1000;
    private static final int BTN_RESET = 50;
    private static final int BTN_REGISTER = 60;
    private static int totalPrice = 0;
    private static TextView registrationAmount;
    static ArrayList<String> teamOneEventSelectedList = new ArrayList<>();
    static ArrayList<String> phpEventList = new ArrayList<>();
    static ArrayList<String> phpList = new ArrayList<>();
    private static TextView txtViewTeamPrice;
    private static int size = 0;
    private Button btnSelectEvent;
    private DatabaseReference mRef;
    private FirebaseUser mUser;
    boolean regSuccessful = false;
    private LinearLayout linearLayoutTeamSize, linearLayoutSelectedEvent;
    private ProgressDialog progDialog;
    private String zonals = "";
    private ArrayList<String> eventSelectorList = new ArrayList<>();
    private ArrayList<String> eventSelectorPriceList = new ArrayList<>();

    public TeamRegistraion() {
        // Required empty public constructor

        mRef = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    private static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static void updatePriceTeamRegistration(int value, boolean checked) {


        if (checked) {

            totalPrice += value * size;
            //totalPrice *= size;
            txtViewTeamPrice.setText(String.valueOf(totalPrice));
            registrationAmount.setText("Total Amount: Rs " + totalPrice);
        }

        if (!checked) {
            totalPrice -= value * size;
            //totalPrice *= size;
            txtViewTeamPrice.setText(String.valueOf(totalPrice));
            registrationAmount.setText("Total Amount: Rs " + totalPrice);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_registration, container, false);

        final Spinner spinnerTeamSize = (Spinner) view.findViewById(R.id.spinnerTeamSize);
        linearLayoutTeamSize = (LinearLayout) view.findViewById(R.id.linearLayoutTeamSize);
        btnSelectEvent = (Button) view.findViewById(R.id.btnTeamSelectEvent);
        linearLayoutSelectedEvent = (LinearLayout) view.findViewById(R.id.linearLayoutSelectedEvent);

        ArrayAdapter spinnerAdapterTeamSize = ArrayAdapter.createFromResource(getActivity(), R.array.team_size,
                android.R.layout.simple_spinner_item);
        spinnerAdapterTeamSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeamSize.setAdapter(spinnerAdapterTeamSize);


        spinnerTeamSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0) {
                    size = Integer.parseInt(parent.getItemAtPosition(position).toString());
                    Toast.makeText(getActivity(), "" + size, Toast.LENGTH_SHORT).show();
                    selectedTeamSize();
                    linearLayoutSelectedEvent.removeAllViewsInLayout();
                    phpList.clear();
                    registrationAmount.setText("Total Amount: Rs " + "0");
                    YoYo.with(Techniques.Shake).duration(1000).playOn(btnSelectEvent);
                } else {


                    Toast.makeText(getActivity(), "Kindly select Team size!", Toast.LENGTH_SHORT).show();
                    YoYo.with(Techniques.Shake).duration(1000).playOn(spinnerTeamSize);
                    size = 0;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSelectEvent.setOnClickListener(this);

        progDialog = new ProgressDialog(getActivity());
        progDialog.setMessage("Please Wait...");
        progDialog.setTitle("Registering Candidate");
        progDialog.setCancelable(false);
        progDialog.setIndeterminate(true);

        mRef.child("zonals").child("registration").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                zonals = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                //Toast.makeText(getActivity(), "Something went wrong. Try again later. ", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @SuppressLint("ResourceType")
    private void selectedTeamSize() {

        /*
        if (size == 0) {
            Toast.makeText(getActivity(), "Kingly select Team size!", Toast.LENGTH_SHORT).show();
            return;
        }
        */

        linearLayoutTeamSize.removeAllViewsInLayout();
        totalPrice = 0;

        TextView name = new TextView(getActivity());
        name.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        name.setTextColor(Color.CYAN);
        name.setText("Name");
        name.setTextSize(18);
        linearLayoutTeamSize.addView(name);

        for (int i = 0; i < size; i++) {

            EditText edtName = new EditText(getActivity());
            edtName.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            edtName.setInputType(InputType.TYPE_CLASS_TEXT);
            edtName.setHint("Member " + (i + 1));
            edtName.setId(i);
            edtName.setTextSize(16);
            linearLayoutTeamSize.addView(edtName);
        }

        TextView college = new TextView(getActivity());
        college.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        college.setTextColor(Color.CYAN);
        college.setText("College");
        college.setTextSize(18);

        linearLayoutTeamSize.addView(college);

        EditText edtCollege = new EditText(getActivity());
        edtCollege.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        edtCollege.setInputType(InputType.TYPE_CLASS_TEXT);
        edtCollege.setId(70);
        edtCollege.setTextSize(16);
        linearLayoutTeamSize.addView(edtCollege);

        TextView email = new TextView(getActivity());
        email.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        email.setTextColor(Color.CYAN);
        email.setText("Email");
        email.setTextSize(18);


        linearLayoutTeamSize.addView(email);

        for (int i = 0; i < size; i++) {

            EditText edtEmail = new EditText(getActivity());
            edtEmail.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            edtEmail.setInputType(InputType.TYPE_CLASS_TEXT);
            edtEmail.setHint("member" + (i + 1) + "@xyz.com");
            edtEmail.setId(10 + i);
            edtEmail.setTextSize(16);
            linearLayoutTeamSize.addView(edtEmail);
        }

        TextView phone = new TextView(getActivity());
        phone.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        phone.setTextColor(Color.CYAN);
        phone.setText("Phone");
        phone.setTextSize(18);


        linearLayoutTeamSize.addView(phone);

        for (int i = 0; i < size; i++) {

            EditText edtPhone = new EditText(getActivity());
            edtPhone.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            edtPhone.setHint("Member " + (i + 1));
            edtPhone.setId(20 + i);
            edtPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtPhone.setTextSize(16);
            linearLayoutTeamSize.addView(edtPhone);

        }

        TextView year = new TextView(getActivity());
        year.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        year.setTextColor(Color.CYAN);
        year.setText("Year");
        year.setTextSize(18);

        linearLayoutTeamSize.addView(year);

        for (int i = 0; i < size; i++) {

            EditText edtYear = new EditText(getActivity());
            edtYear.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            edtYear.setInputType(InputType.TYPE_CLASS_PHONE);
            edtYear.setHint("Member " + (i + 1) + ": (1,2,3,4)");
            edtYear.setId(30 + i);
            edtYear.setTextSize(16);
            linearLayoutTeamSize.addView(edtYear);
        }

        LinearLayout.LayoutParams regAmountParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        regAmountParams.setMargins(5, 5, 5, 10);
        registrationAmount = new TextView(getActivity());
        registrationAmount.setText("Total Amount: Rs " + totalPrice);
        registrationAmount.setLayoutParams(regAmountParams);
        registrationAmount.setTextSize(20);
        registrationAmount.setTextColor(Color.RED);
        registrationAmount.setId(40);
        registrationAmount.setVisibility(View.INVISIBLE);

        linearLayoutTeamSize.addView(registrationAmount);

        LinearLayout horizontal = new LinearLayout(getActivity());
        horizontal.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        Button btnSubmit = new Button(getActivity());
        Button btnReset = new Button(getActivity());

        btnSubmit.setText("Register");
        btnReset.setText("Reset");

        btnReset.setBackground(getActivity().getResources().getDrawable(R.drawable.button_border));
        btnSubmit.setBackground(getActivity().getResources().getDrawable(R.drawable.button_border));
        btnReset.setId(50);
        btnSubmit.setId(60);

        btnReset.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        LinearLayout.LayoutParams btnS = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnS.leftMargin = 5;
        btnS.weight = 3;

        LinearLayout.LayoutParams btnR = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        btnR.weight = 1;

        btnSubmit.setLayoutParams(btnS);
        btnReset.setLayoutParams(btnR);

        horizontal.addView(btnReset);
        horizontal.addView(btnSubmit);

        linearLayoutTeamSize.addView(horizontal);


    }

    Context context;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        this.context = context;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id) {

            case R.id.btnTeamSelectEvent:
                selectEventDialog();
                break;
            case BTN_RESET:
                //Reset
                TeamRegistraion teamRegistraion = new TeamRegistraion();
                FragmentTransaction transaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame, teamRegistraion, "Team Registraion Fragment");
                transaction.commit();

                break;
            case BTN_REGISTER:
                //Register
                boolean valid = validation();

                if (size == 0) {
                    Toast.makeText(getActivity(), "Kindly Select team size!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (phpList.size() == 0) {
                    Toast.makeText(getActivity(), "Please select Event!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (valid && phpList.size() != 0) {
                    showConfirmationDialog();
                    //phpList.clear();
                }

                break;

        }

    }

    private void showConfirmationDialog() {

        String details;

        details = String.format("Team Size: %d\n\n",size);
        for (int i = 0; i < size; i++) {

            details += "Member " + (i + 1) + "\n";
            EditText edtName = (EditText) linearLayoutTeamSize.findViewById(i);
            EditText edtEmail = (EditText) linearLayoutTeamSize.findViewById(10 + i);
            EditText edtPhone = (EditText) linearLayoutTeamSize.findViewById(20 + i);
            EditText edtYear = (EditText) linearLayoutTeamSize.findViewById(30 + i);
            EditText edtCollege = (EditText) linearLayoutTeamSize.findViewById(70);

            String name = edtName.getText().toString();
            String email = edtEmail.getText().toString();
            String phone = edtPhone.getText().toString();
            String year = edtYear.getText().toString();
            String college = edtCollege.getText().toString();

            details += String.format(
                    "   Name: %s\n" +
                            "   Email: %s\n" +
                            "   Phone: %s\n" +
                            "   Year: %s\n" +
                            "   College: %s\n\n", name, email, phone, year, college);

        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Student details");
        builder.setMessage(details);


        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int which) {


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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void registration() {

        String name, email, phone, year, college;

        if (phpList.size() == 0) {
            Toast.makeText(getActivity(), "Kindly Select Event", Toast.LENGTH_SHORT).show();
            btnSelectEvent.setTextColor(Color.RED);
            return;
        }


        for (int j = 0; j < phpList.size(); j++) {

            long randomNum = ThreadLocalRandom.current().nextLong(10000, 999900 + 1);
            String teamID = "conv2018" + randomNum;
            for (int i = 0; i < size; i++) {

                EditText edtName = (EditText) linearLayoutTeamSize.findViewById(i);
                EditText edtEmail = (EditText) linearLayoutTeamSize.findViewById(10 + i);
                EditText edtPhone = (EditText) linearLayoutTeamSize.findViewById(20 + i);
                EditText edtYear = (EditText) linearLayoutTeamSize.findViewById(30 + i);
                EditText edtCollege = (EditText) linearLayoutTeamSize.findViewById(70);

                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                phone = edtPhone.getText().toString();
                year = edtYear.getText().toString();
                college = edtCollege.getText().toString();

                RegisterAsync registerAsync = new RegisterAsync(getActivity());

                registerAsync.execute(mUser.getEmail(),
                        name,
                        college,
                        String.valueOf(year),
                        email,
                        phone,
                        phpList.get(j),
                        teamID);

                //TODO : Check the response
                if((j == (phpList.size() -1)) && (i == size-1)){
                    Toast.makeText(context, "Registration Successful ", Toast.LENGTH_SHORT).show();
                    TeamRegistraion teamRegistraion = new TeamRegistraion();
                    FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, teamRegistraion, "Team Registraion Fragment");
                    transaction.commit();
                }
            }
        }

    }

    private boolean validation() {

        boolean allValid = true;

        for (int i = 0; i < size; i++) {

            EditText edtName = (EditText) linearLayoutTeamSize.findViewById(i);
            EditText edtEmail = (EditText) linearLayoutTeamSize.findViewById(10 + i);
            EditText edtPhone = (EditText) linearLayoutTeamSize.findViewById(20 + i);
            EditText edtYear = (EditText) linearLayoutTeamSize.findViewById(30 + i);
            EditText edtCollege = (EditText) linearLayoutTeamSize.findViewById(70);

            String name = edtName.getText().toString();
            String email = edtEmail.getText().toString();
            String phone = edtPhone.getText().toString();
            String year = edtYear.getText().toString();
            String college = edtCollege.getText().toString();


            if (TextUtils.isEmpty(name)) {
                edtName.setError("Field can't be empty.");
                allValid = false;

            }
            if (TextUtils.isEmpty(college)) {
                edtCollege.setError("Field can't be empty.");
                allValid = false;

            }
            if (TextUtils.isEmpty(phone)) {
                edtPhone.setError("Field can't be empty.");
                allValid = false;
            }

            if (email.isEmpty()) {
                edtEmail.setError("Field can't be empty.");
                allValid = false;
            } else if (!validateEmail(email)) {
                edtEmail.setError("Invalid Email.");
                allValid = false;
            }

            if (year.isEmpty()) {
                edtYear.setError("Field can't be empty.");
                allValid = false;
            } else if (Integer.parseInt(year)>6 || Integer.parseInt(year)<=0) {
                edtYear.setError("Invalid Year.");
                allValid = false;
            }

            if (phone.length() != 10) {
                edtPhone.setError("Invalid Phone Number.");
                allValid = false;
            }

        }

        return allValid;
    }

    private void selectEventDialog() {

        if (size == 0) {

            Toast.makeText(getActivity(), "Kindly Select Team Size First!", Toast.LENGTH_SHORT).show();
            return;
        }
        btnSelectEvent.setTextColor(Color.WHITE);
        totalPrice = 0;
        linearLayoutSelectedEvent.removeAllViewsInLayout();
        teamOneEventSelectedList.clear();
        phpList.clear();

        registrationAmount.setText("Total Amount: Rs " + totalPrice);
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.event_selector);

        final ListView eventList = (ListView) dialog.findViewById(R.id.listViewEventSelector);
        Button btnSet = (Button) dialog.findViewById(R.id.btnEventSet);
        TextView dialogHeader = (TextView) dialog.findViewById(R.id.txtDialogHeader);
        txtViewTeamPrice = (TextView) dialog.findViewById(R.id.txtViewTotalPrice);


        if (zonals.equals("false")) {
            eventSelectorList = new ArrayList<>(
                    Arrays.asList(getActivity().getResources().getStringArray(R.array.event_lists)));
            eventSelectorPriceList = new ArrayList<>(
                    Arrays.asList(getActivity().getResources().getStringArray(R.array.event_lists_prices)));
            phpEventList = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.php_events_final)));

            dialogHeader.setText("Events");
        }
        if (zonals.equals("true")) {

            eventSelectorList = new ArrayList<>(
                    Arrays.asList(getActivity().getResources().getStringArray(R.array.zonals_event)));
            eventSelectorPriceList = new ArrayList<>(
                    Arrays.asList(getActivity().getResources().getStringArray(R.array.zonals_event_price)));
            phpEventList = new ArrayList<>(Arrays.asList(getActivity().getResources().getStringArray(R.array.php_events_zonals)));

            dialogHeader.setText("Zonals Events");
        }

        TeamRegistrationListViewClass adapter = new TeamRegistrationListViewClass(getActivity(),
                eventSelectorList, eventSelectorPriceList, size);
        eventList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for (int i = 0; i < teamOneEventSelectedList.size(); i++) {

                    TextView event = new TextView(getActivity());
                    event.setText(teamOneEventSelectedList.get(i));
                    event.setId(4000 + i);
                    event.setTextColor(Color.CYAN);
                    event.setVisibility(View.VISIBLE);
                    event.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.event_background));

                    event.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, 60));

                    event.setGravity(Gravity.CENTER);
                    event.setPadding(10, 0, 10, 0);

                    linearLayoutSelectedEvent.addView(event);
                }


                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

    }

    private class RegisterAsync extends AsyncTask<String, String, String> {

        Context context;
        String result;

        public RegisterAsync(Context registrationActivity) {

            context = registrationActivity;
        }

        @Override
        protected void onPreExecute() {

            progDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            //Todo
            String register_url = "http://phoenixatuit.com/Mobile_CAP/event_registration.php";
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
                        + URLEncoder.encode("event", "UTF-8") + "=" + URLEncoder.encode(params[6], "UTF-8") + "&"
                        + URLEncoder.encode("teamid", "UTF-8") + "=" + URLEncoder.encode(params[7], "UTF-8");

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


            if (s.equals("true")) {

                UpdateLeaderboard updateLeaderboard = new UpdateLeaderboard(context);
                updateLeaderboard.execute("1");

            } else {

                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }

            progDialog.dismiss();
        }
    }

    private class UpdateLeaderboard extends AsyncTask<String, String, String> {

        Context context;

        public UpdateLeaderboard(Context context) {

            this.context = context;
        }

        @Override
        protected void onPreExecute() {

            progDialog.show();
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

            progDialog.dismiss();
        }
    }
}
