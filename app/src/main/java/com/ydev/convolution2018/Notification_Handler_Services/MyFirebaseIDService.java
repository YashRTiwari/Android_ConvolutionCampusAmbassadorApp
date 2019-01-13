package com.ydev.convolution2018.Notification_Handler_Services;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by YRT on 19/10/2017.
 */

//Created to get token
public class MyFirebaseIDService extends FirebaseInstanceIdService {

    public static String token;
    private DatabaseReference mRef;
    private FirebaseAuth mAuth;

    public MyFirebaseIDService() {

        onTokenRefresh();
    }

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        String TAG = "INSTANCEID";

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);

        token = refreshedToken;
    }

    public void sendRegistrationToServer(String refreshedToken) {

        mRef = FirebaseDatabase.getInstance().getReference().child("FCM Token");
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            mRef.child(user.getUid()).setValue(refreshedToken);

        }


    }


}
