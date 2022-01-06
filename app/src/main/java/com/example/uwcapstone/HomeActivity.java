package com.example.uwcapstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private static HomeActivity instance;
    private Spinner thresholdSpinner;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String msg = "";
    public static String frequencyInput = "6000";
    public static String sampleRateInput = "48000";
    public static String bandWidthInput = "1000";

    static Sender mClientThread;
    static Receiver mReceiverThread;

    Button mStartSendBtn;
    Button mStartReceiveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        instance = this;

        // Request permission
        checkAndRequestPermissions();

        // initial UI state
        mStartSendBtn = findViewById(R.id.seeker);
        mStartReceiveBtn = findViewById(R.id.helper);

        // When seeker role is selected
        mStartSendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //mStartSendBtn.setEnabled(false);

                // Create the object of AlertDialog Builder class
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
                builder.setCancelable(false);

                // Set the Negative button with No name OnClickListener method is use of DialogInterface interface.
                builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // If user click cancel, then dialog box is canceled.
                            dialog.cancel();
                        }
                    });

                builder.setTitle("Choose Your Environment")
                        .setItems(R.array.threshold, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                Log.d("test", String.valueOf(which));

                                // Create new fragment and transaction
                                Fragment aboutUsFragment = new AboutUsFragment();

                                getSupportFragmentManager().beginTransaction().replace(R.id.container, aboutUsFragment).commit();

                            }
                        });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();

                // Show the Alert Dialog box
                alertDialog.show();
            }
        });


    }

    private void checkAndRequestPermissions() {
        int permissionWifi = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE);
        int writepermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRecordAudio = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);


        ArrayList<String> listPermissionsNeeded = new ArrayList<>();

        if (writepermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (permissionWifi != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if (permissionRecordAudio != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }
}