package com.example.uwcapstone;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Request permission
        checkAndRequestPermissions();

        // initial UI state
        Button mStartSendBtn = (Button)view.findViewById(R.id.seeker);
        Button mStartReceiveBtn = (Button)view.findViewById(R.id.helper);

        // When seeker role is selected
        mStartSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final String [] items = new String[] {"Quiet", "Moderate", "Noisy"};
                final Integer[] icons = new Integer[] {R.drawable.quiet, R.drawable.moderate, R.drawable.noisy};
                final ListAdapter adapter = new ArrayAdapterWithIcon(getActivity(), items, icons);

                // Create the object of AlertDialog Builder class
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View titleView = getLayoutInflater().inflate(R.layout.environment_dialog, null);

                // Set Cancelable true for when the user clicks on the outside the Dialog Box then it will remain show
                builder.setCancelable(true);
                builder.setIcon(R.drawable.app_logo);
                builder.setCustomTitle(titleView);

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
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item ) {

                                //Toast.makeText(getActivity(), "Item Selected: " + item, Toast.LENGTH_SHORT).show();

                                String threshold = items[item];
                                Log.d("HomeFragment", threshold);

                                Bundle bundle = new Bundle();
                                bundle.putString("threshold", String.valueOf(threshold));

                                // Create new fragment and transaction

                                SeekerFragment seekerFragment = new SeekerFragment();
                                seekerFragment.setArguments(bundle);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, seekerFragment).commit();

                            }
                        });

                // Create the Alert dialog
                AlertDialog alertDialog = builder.create();

                // Show the Alert Dialog box
                alertDialog.show();
            }
        });


        mStartReceiveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // start to listen to sequence
//                Intent intent = new Intent(getContext(), MainActivity.class);
//                intent.putExtra("EXTRA_SESSION_ID", "tttt");
//                startActivity(intent);

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new HelperFragment()).commit();

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void checkAndRequestPermissions() {
        int permissionWifi = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_WIFI_STATE);
        int writepermission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionRecordAudio = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO);


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
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
        }
    }

}
