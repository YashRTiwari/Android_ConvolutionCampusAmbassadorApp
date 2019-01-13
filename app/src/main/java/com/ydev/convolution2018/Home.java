package com.ydev.convolution2018;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.ydev.convolution2018.About_UIT.AboutUit;
import com.ydev.convolution2018.Accommodation.AccommodationFragment;
import com.ydev.convolution2018.AdapterClasses.ListViewAdapterClass;
import com.ydev.convolution2018.HomeFeed.HomeFeedFragment;
import com.ydev.convolution2018.Leaderboard.LeaderBoardFragment;
import com.ydev.convolution2018.Notification.NotificationFragment;
import com.ydev.convolution2018.Notification_Handler_Services.MyFirebaseIDService;
import com.ydev.convolution2018.Phoenix.PhoenixFragment;
import com.ydev.convolution2018.Phoenix.TeamFragment;
import com.ydev.convolution2018.Registration.IdRegenerator;
import com.ydev.convolution2018.Registration.TeamRegistraion;
import com.ydev.convolution2018.RegistrationDashboard.DashboardRegistrationFragment;
import com.ydev.convolution2018.Upload.UploadFragment;

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

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener{

    private final static int INTENT_MESSAGE_CODE = 1;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    public static String LEADERBOARD_RECORD = "LEADERBOARD RECORD";
    public static String UID_TO_NAME = "UID TO NAME";
    public static MenuItem menuItem;
    private NavigationView navigationView;
    private TextView capname;
    private ImageView capImage;
    String LOGGED_IN_CAP = "Logged in CAP";
    private boolean[] authorized = {false};
    int count = 1;
    private MenuItem homeItem;
    private ImageButton btnCall;
    private ImageButton btnUpload;
    private ImageButton btnRegister;
    private boolean isVisible = false;
    private ImageButton fab;
    private ProgressDialog progressDialog;
    private Uri downloadUri;
    private FirebaseAuth mAuth;
    private ListView eventListView;
    private DatabaseReference mRef;
    private GoogleApiClient googleApiClient;
    private int  UPDATE_CONSTANT = 1009;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        int permissionResolved = ContextCompat.checkSelfPermission(Home.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        progressDialog = new ProgressDialog(Home.this);

        if (permissionResolved != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Write Permission Denied", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(Home.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        }

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase databse = FirebaseDatabase.getInstance();
        mRef = databse.getReference();

        sharedPreferences = getPreferences(0);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean("FIRST_LOGIN", true)) {

            new CapForMobileDataAsync(this).execute(mAuth.getCurrentUser().getUid(), mAuth.getCurrentUser().getDisplayName(),
                    mAuth.getCurrentUser().getEmail(), mAuth.getCurrentUser().getPhotoUrl().toString());
            editor.putBoolean("FIRST_LOGIN", false);
            editor.commit();
        }

        View header = navigationView.getHeaderView(0);
        capname = (TextView) header.findViewById(R.id.capName);
        capname.setText(mAuth.getCurrentUser().getDisplayName().toUpperCase());

        capImage = (ImageView) header.findViewById(R.id.capImage);
        Picasso.with(this).load(mAuth.getCurrentUser().getPhotoUrl()).fit().into(capImage, new Callback() {
            @Override
            public void onSuccess() {

                Bitmap image = ((BitmapDrawable) capImage.getDrawable()).getBitmap();
                RoundedBitmapDrawable roundImage = RoundedBitmapDrawableFactory.create(getResources(), image);
                roundImage.setCircular(true);
                roundImage.setCornerRadius(48);
                capImage.setImageDrawable(roundImage);

            }

            @Override
            public void onError() {

                capImage.setImageResource(R.drawable.defaultimageforcap);
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        //Token ID for specific Notification
        MyFirebaseIDService token = new MyFirebaseIDService();

        if (!MyFirebaseIDService.token.isEmpty()) {
            token.sendRegistrationToServer(MyFirebaseIDService.token);
        } else {
            Toast.makeText(this, "Token Failed", Toast.LENGTH_SHORT).show();
        }

        homeMenuItemSelected();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUpdate();

        mRef.child("authorizeduser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    for (DataSnapshot validUser : dataSnapshot.getChildren()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user.getUid().equals(validUser.getValue().toString())) {
                            authorized[0] = true;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(Home.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUpdate(){

        mRef.child("Update").child("version").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    String  version = dataSnapshot.getValue().toString();

                    if (!version.equals(BuildConfig.VERSION_NAME) ){

                        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setTitle("Update");
                        builder.setMessage("Kindly update the app.");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent googlePlay = new Intent(Intent.ACTION_VIEW);
                                googlePlay.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.ydev.convolution2018"));
                                startActivityForResult(googlePlay, UPDATE_CONSTANT);
                            }
                        });
                        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                finish();
                            }
                        });

                        builder.setIcon(R.drawable.convolutionlogo).create().show();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(Home.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED){
            checkUpdate();
        }
    }

    private boolean checkIfUserIsAuthorized() {
        return authorized[0];
    }

    private void homeMenuItemSelected() {

        setTitle("Home");
        HomeFeedFragment homeFeedFragment = new HomeFeedFragment();
        FragmentTransaction homeFeedFragmentTransaction = getSupportFragmentManager().beginTransaction();
        homeFeedFragmentTransaction.replace(R.id.frame, homeFeedFragment, "Home");
        homeFeedFragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        if (getSupportFragmentManager().findFragmentByTag("Home") == null) {
            homeMenuItemSelected();

            Menu temp = navigationView.getMenu();
            homeItem = temp.findItem(R.id.home);

            homeItem.setChecked(true);

        } else {
            finish();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);

        menuItem = menu.findItem(R.id.refreshLeaderBoard);

        //Menuitem white color
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spannableString = new SpannableString(item.getTitle().toString());
            spannableString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spannableString.length(), 0);
            item.setTitle(spannableString);
        }

        return true;
    }

    private void signOut() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);
        alertDialogBuilder.setTitle("Sign Out");
        alertDialogBuilder.setMessage("Are you sure?");
        alertDialogBuilder.setIcon(R.drawable.convolutionlogo);

        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Auth.GoogleSignInApi.signOut(googleApiClient);
                Intent moveToMain = new Intent(Home.this, MainActivity.class);
                finish();      //To make sure app doesnt go back to Home Activity
                mAuth.signOut();
                startActivity(moveToMain);

            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {

            case R.id.signOutButton:
                signOut();
                break;

            case R.id.notification:
                setTitle("Notifications");
                NotificationFragment notificationFragment = new NotificationFragment();
                FragmentTransaction notificationFragmentTransaction = getSupportFragmentManager().beginTransaction();
                notificationFragmentTransaction.replace(R.id.frame, notificationFragment, "Registration Desk Fragment");
                notificationFragmentTransaction.commit();

                break;

            case R.id.refreshLeaderBoard:

                new LeaderBoardFragment.JSONItunesStuffTask(Home.this).execute();

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean authorizationGuard() {

        if (!checkIfUserIsAuthorized()) {

            homeMenuItemSelected();

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Authorization Failed");
            builder.setMessage("You are not authorized to use this app! Contact Tech Support");

            builder.setIcon(R.drawable.convolutionlogo).create().show();

            return false;
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.home:
                homeMenuItemSelected();
                break;

            case R.id.dashboard:
                dashboard();
                break;

            case R.id.accommodation:
                if (authorizationGuard() ) {

                    mRef.child("maineventaccess").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {

                                for (DataSnapshot validUser : dataSnapshot.getChildren()) {

                                    String key = validUser.getKey();
                                    if (key.equals("acc")){

                                        if (validUser.getValue().equals("true")) {
                                            setTitle("Accommodation ");
                                            AccommodationFragment accommodationFragment = new AccommodationFragment();
                                            FragmentTransaction registrationFragmentTransaction = getSupportFragmentManager().beginTransaction();
                                            registrationFragmentTransaction.replace(R.id.frame, accommodationFragment, "Registration Desk Fragment");
                                            registrationFragmentTransaction.commit();
                                        }else{
                                            Toast.makeText(Home.this, "Window will open soon.", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Toast.makeText(Home.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    });



                }

                break;

            case R.id.register:
                if (authorizationGuard()) {
                    setTitle("Zonal Registration ");
                    TeamRegistraion teamRegistraion = new TeamRegistraion();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame, teamRegistraion, "Team Registraion Fragment");
                    transaction.commit();


                }

                break;

            case R.id.leaderboard:
                setTitle("Leader Board ");
                menuItem.setVisible(true);
                LeaderBoardFragment leaderboardFragment = new LeaderBoardFragment();
                FragmentTransaction leaderboardFragmentTransaction = getSupportFragmentManager().beginTransaction();
                leaderboardFragmentTransaction.replace(R.id.frame, leaderboardFragment, "Leaderboard Fragment");
                leaderboardFragmentTransaction.commit();
                break;

            case R.id.idGenerator:
                if (authorizationGuard() ) {

                    mRef.child("maineventaccess").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {

                                for (DataSnapshot validUser : dataSnapshot.getChildren()) {

                                    String key = validUser.getKey();
                                    if (key.equals("idg")) {
                                        if (validUser.getValue().equals("true")) {
                                            setTitle("Bhopal Registration");
                                            IdRegenerator fragment = new IdRegenerator();
                                            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                                            transaction1.replace(R.id.frame, fragment, "ID Fragment");
                                            transaction1.commit();
                                        }else{
                                            Toast.makeText(Home.this, "Window will open soon.", Toast.LENGTH_LONG).show();

                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //Toast.makeText(Home.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                        }
                    });



                }

                break;

            case R.id.upload:
                if (authorizationGuard()) {
                    setTitle("Upload");
                    UploadFragment fragment = new UploadFragment();
                    FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
                    transaction1.replace(R.id.frame, fragment, "Upload Fragmnet");
                    transaction1.commit();
                }

                break;
            case R.id.download:
                download();
                break;

            case R.id.call:
                makeCall();
                break;

            case R.id.techsupport:
                techsupport();
                break;

            case R.id.uit:
                aboutUit();
                break;

            case R.id.phoenix:
                phoenix();
                break;

            case R.id.team:
                team();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void team() {

        setTitle("Team");
        TeamFragment fragment = new TeamFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment, "Team");
        transaction.commit();
    }

    private void phoenix() {

        setTitle("Phoenix");
        PhoenixFragment fragment = new PhoenixFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment, "Phoenix");
        transaction.commit();

    }

    private void aboutUit() {

        setTitle("UIT");
        AboutUit fragment = new AboutUit();
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.frame, fragment, "About Uit Fragment");
        transaction1.commit();

    }

    private void techsupport() {

        Dialog callDialog = new Dialog(Home.this);
        callDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        callDialog.setContentView(R.layout.call_layout);

        ListView numberList = (ListView) callDialog.findViewById(R.id.listViewCallNumber);

        final ArrayList<String> name = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.tech_support_name)));
        ArrayList<String> role = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.tech_suppot_role)));

        ListViewAdapterClass adapter = new ListViewAdapterClass(getResources().getInteger(R.integer.tech_supprt), Home.this, name, role);

        numberList.setAdapter(adapter);
        callDialog.show();

        numberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle("Confirm");
                builder.setMessage("Do you wish to contact " + name.get(position) + "?");

                builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);

                        String number = getResources().getStringArray(R.array.tech_support_number)[position];

                        if (ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            callIntent.setData(Uri.parse("tel:" + number));
                            startActivity(callIntent);
                        } else {
                            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.CALL_PHONE}, 2);
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

    }

    private void dashboard() {

        setTitle("Dashboard");
        DashboardRegistrationFragment dashboard = new DashboardRegistrationFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, dashboard, "Dashboard Fragment");
        transaction.commit();

    }


    private void download() {

        setTitle("Download ");
        DownloadFragment downloadFragment = new DownloadFragment();
        FragmentTransaction downloadFragmentTransaction = getSupportFragmentManager().beginTransaction();
        downloadFragmentTransaction.replace(R.id.frame, downloadFragment, "Download Fragment");
        downloadFragmentTransaction.commit();

    }

    private void makeCall() {

        Dialog callDialog = new Dialog(Home.this);
        callDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        callDialog.setContentView(R.layout.call_layout);

        ListView numberList = (ListView) callDialog.findViewById(R.id.listViewCallNumber);

        final ArrayList<String> name = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.phoenix_member_name)));
        ArrayList<String> role = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.phoenix_member_role)));

        ListViewAdapterClass adapter = new ListViewAdapterClass(Home.this, name, role, getResources().getInteger(R.integer.call_activity));

        numberList.setAdapter(adapter);
        callDialog.show();

        numberList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setTitle("Confirm");
                builder.setMessage("Do you wish to contact " + name.get(position) + "?");

                builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);

                        String number = getResources().getStringArray(R.array.phoenix_member_contact)[position];

                        if (ActivityCompat.checkSelfPermission(Home.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                            callIntent.setData(Uri.parse("tel:" + number));
                            startActivity(callIntent);
                        } else {
                            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.CALL_PHONE}, 2);
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
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_SHORT).show();
    }

    private class CapForMobileDataAsync extends AsyncTask<String, String, String> {

        Context context;
        String result;

        public CapForMobileDataAsync(Context registrationActivity) {

            context = registrationActivity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            String register_url = "http://phoenixatuit.com/Mobile_CAP/capdetails.php";
            try {
                URL url = new URL(register_url);
                HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
                httpUrlConnection.setRequestMethod("POST");
                httpUrlConnection.setDoOutput(true);

                OutputStream outputStream = httpUrlConnection.getOutputStream();
                OutputStreamWriter osWriter = new OutputStreamWriter(outputStream, "UTF-8");
                BufferedWriter bufferedWriter = new BufferedWriter(osWriter);

                String post_data = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8") + "&"
                        + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8") + "&"
                        + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(params[2], "UTF-8") + "&"
                        + URLEncoder.encode("photourl", "UTF-8") + "=" + URLEncoder.encode(params[3], "UTF-8");
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

        }
    }

}
