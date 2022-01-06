package com.example.uwcapstone;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeekerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeekerFragment extends Fragment {

    private static MainActivity instance;
    private Spinner thresholdSpinner;
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String msg = "";
    public static String frequencyInput = "6000";
    public static String sampleRateInput = "48000";
    public static String bandWidthInput = "1000";

    static Sender mClientThread;
    static Receiver mReceiverThread;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SeekerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeekerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SeekerFragment newInstance(String param1, String param2) {
        SeekerFragment fragment = new SeekerFragment();
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
        View view = inflater.inflate(R.layout.fragment_seeker, container, false);

        // Get bundle value from HomeFragment
        Bundle bundle = this.getArguments();
        String data = bundle.getString("threshold");
        Log.d("data", data);

        // initial UI state
        final Button mStartSeekBtn = (Button)view.findViewById(R.id.seeker);
        final Button mStopSeekBtn = (Button)view.findViewById(R.id.stopSeek);

        mStartSeekBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // start to listen to sequence
                mStartSeekBtn.setEnabled(false);
                mStopSeekBtn.setEnabled(true);
                Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
            }
        });

        mStopSeekBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mStopSeekBtn.setEnabled(false);
                mStartSeekBtn.setEnabled(true);

                // start to listen to sequence
                Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
            }
        });


        // Inflate the layout for this fragment
        return view;


    }
}