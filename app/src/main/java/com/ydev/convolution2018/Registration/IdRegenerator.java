package com.ydev.convolution2018.Registration;


import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.ydev.convolution2018.R;

import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class IdRegenerator extends Fragment implements View.OnClickListener {


    private EditText edtName, edtCollege, edtEmail, edtPhone;
    private Button btnSubmit, btnReset;
    private Spinner spinnerYear;

    private String name, email, phone, college, year;
    public IdRegenerator() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_id_regenerator, container, false);

        edtName = (EditText) view.findViewById(R.id.edtName);
        edtCollege = (EditText) view.findViewById(R.id.edtCollege);
        edtEmail = (EditText) view.findViewById(R.id.edtEmail);
        edtPhone = (EditText) view.findViewById(R.id.edtPhone);

        btnReset = (Button) view.findViewById(R.id.btnResetAcc);
        btnSubmit = (Button) view.findViewById(R.id.btnTeamOneSubmit);

        btnSubmit.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        spinnerYear = (Spinner) view.findViewById(R.id.spinnerYear);

        ArrayAdapter spinnerAdapterTeamSize = ArrayAdapter.createFromResource(getActivity(), R.array.year,
                android.R.layout.simple_spinner_item);
        spinnerAdapterTeamSize.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(spinnerAdapterTeamSize);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    year = String.valueOf(parent.getSelectedItem());
                }else{
                    year = String.valueOf(0);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){

            case R.id.btnTeamOneSubmit:
                if(validation()){
                    showConfirmationDialog();
                }
                break;
            case R.id.btnResetAcc:
                btnResetClicked();
                break;
        }
    }

    private void btnResetClicked() {

        edtEmail.setText("");
        edtPhone.setText("");
        edtName.setText("");
        edtCollege.setText("");

        spinnerYear.setSelection(0);
    }

    private boolean validation() {

        boolean allValid = true;

            name = edtName.getText().toString();
            email = edtEmail.getText().toString();
            phone = edtPhone.getText().toString();
            college = edtCollege.getText().toString();

            if (year == String.valueOf(0)){
                allValid = false;
                YoYo.with(Techniques.Shake).duration(1000).playOn(spinnerYear);

            }
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
            } else if (!validate(email)) {
                edtEmail.setError("Invalid Email.");
                allValid = false;
            }

            if (phone.length() != 10) {
                edtPhone.setError("Invalid Phone Number.");
                allValid = false;
            }

        return allValid;
    }

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }


    private void showConfirmationDialog() {

        String details;

            details = String.format(
                    "   Name: %s\n" +
                            "   Email: %s\n" +
                            "   Phone: %s\n" +
                            "   Year: %s\n" +
                            "   College: %s\n\n", name, email, phone, year, college);



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Student details");
        builder.setMessage(details);


        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long randomNum = ThreadLocalRandom.current().nextLong(10_000, 1_000_000);

                String id = "CONV"+randomNum;
                String cap = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                IdGeneratorAsyncTask task = new IdGeneratorAsyncTask(getActivity());
                task.execute( cap, name, college, year, email, phone, id);

                btnResetClicked();
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
