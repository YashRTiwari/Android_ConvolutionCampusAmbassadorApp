package com.ydev.convolution2018;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CODE_GOOGLE_SIGNIN = 9001;
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 9002;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static FirebaseAuth mAuth;
    private static FirebaseUser user;
    private static GoogleApiClient googleApiClient;
    private final String TAG = "Debug";
    private FirebaseDatabase database;
    private DatabaseReference mRef;
    int count = 0;
    private boolean IS_PERSONAL_INFO_ADDED = false;
    private ProgressDialog progressDialog;
    private ImageView imgLogo;
    private ImageView imgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Button btnGoogleLogin = (Button) findViewById(R.id.btnGoogleLogin);

        ImageView imgHeader = (ImageView) findViewById(R.id.imgHeader);
        ImageView imgFooter = (ImageView) findViewById(R.id.imgFooter);
        imgLogo = (ImageView) findViewById(R.id.imgConvLogo);
        imgName = (ImageView) findViewById(R.id.imgName);


        mRef = FirebaseDatabase.getInstance().getReference();
        Picasso.with(this).load(R.drawable.convolutionlogo).fit().centerInside().into(imgLogo, new Callback() {
            @Override
            public void onSuccess() {

                YoYo.with(Techniques.RubberBand).duration(1000).playOn(imgLogo);

            }

            @Override
            public void onError() {

            }
        });
        Picasso.with(this).load(R.drawable.footerimage).fit().centerCrop().into(imgFooter);
        Picasso.with(this).load(R.drawable.headerimage).fit().centerCrop().into(imgHeader);
        Picasso.with(this).load(R.drawable.convolutiontext).fit().centerCrop().into(imgName, new Callback() {
            @Override
            public void onSuccess() {
                YoYo.with(Techniques.DropOut).duration(1000).playOn(imgName);

            }

            @Override
            public void onError() {

            }
        });


        btnGoogleLogin.setOnClickListener(MainActivity.this);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (user != null) {
                    // User is signed in
                    moveToHomepage();
                }
            }
        };

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


    }

    private boolean checkInternetConnection() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            Toast.makeText(this, "Make sure you are connected to internet!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnGoogleLogin:
                if (checkInternetConnection()) {
                    googleSignIn();
                }
                break;

        }

    }

    private void googleSignIn() {

        Intent googleSigninIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(googleSigninIntent, REQUEST_CODE_GOOGLE_SIGNIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_GOOGLE_SIGNIN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {

                GoogleSignInAccount account = result.getSignInAccount();
                authenticateWithFirebase(account);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();


            }


        }

    }

    private void authenticateWithFirebase(GoogleSignInAccount account) {

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    progressDialog.dismiss();
                    mRef.child("User Uid").child(mAuth.getCurrentUser().getUid()).child(mAuth.getCurrentUser().getDisplayName()).setValue(mAuth.getCurrentUser().getUid());
                    moveToHomepage();

                } else {
                    makeToast("Login Failed");
                }
            }
        });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        try {
            connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void moveToHomepage() {

        Intent intentSignin = new Intent(MainActivity.this, Home.class);
        startActivity(intentSignin);
        finish();
    }

    private void makeToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}

