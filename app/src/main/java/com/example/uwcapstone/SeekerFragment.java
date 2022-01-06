package com.example.uwcapstone;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sonicmeter.android.multisonicmeter.Params;
import com.sonicmeter.android.multisonicmeter.Utils;

import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SeekerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SeekerFragment extends Fragment {

    private static FragmentActivity instance;
    private boolean isSeeking;
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
        instance = getActivity();
        // Get bundle value from HomeFragment
        Bundle bundle = this.getArguments();
        String data = bundle.getString("threshold");
        Log.d("data", data);
        DataFile.updateThreshold(msg);

        // initial audiotrack player
        Utils.initPlayer(Params.sampleRate, 0);

        Utils.initConvolution((Params.signalSequenceLength * Params.bitCount));

        // initial UI state
        final Button mStartSeekBtn = (Button)view.findViewById(R.id.seeker);
        final Button mStopSeekBtn = (Button)view.findViewById(R.id.stopSeek);

        final ImageButton imgBtn = (ImageButton)view.findViewById(R.id.play_or_stop);
        final TextView playStopLabel = (TextView)view.findViewById(R.id.play_label);

        // Start by default
        isSeeking = true;

        // start to listen to sequence
        mClientThread = new Sender("Seeker");
        mClientThread.start();
        Log.d("SeekerFragment", "Startttttt");

        imgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Stop if currently seeking
                if (instance != null && isSeeking) {
                    Log.d("SeekerFragment", "attempt to stop");
                    imgBtn.setImageResource(R.drawable.play);
                    playStopLabel.setText("Start Seeking For Help");
                    playStopLabel.setTextColor(Color.parseColor("#0748ab"));

                    mClientThread.stopThread();
                    mClientThread = null;
                    isSeeking = false;

                    //Toast.makeText(getActivity(), "start", Toast.LENGTH_SHORT).show();


                } else if (!isSeeking) {
                    imgBtn.setImageResource(R.drawable.stop);
                    playStopLabel.setText("Stop Seeking");

                    isSeeking = true;

                    mClientThread = new Sender("Seeker");
                    mClientThread.start();

                    // start to listen to sequence
                    //Toast.makeText(getActivity(), "Start", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public static void log(final String text) {
        if (instance != null) {
            instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView logBox = instance.findViewById(R.id.seeker_textview_log);
                    logBox.setMovementMethod(ScrollingMovementMethod.getInstance());
                    //Calendar cal = Calendar.getInstance();
                    logBox.append("  " + text + "\n");//cal.get(Calendar.MINUTE)+ ":" +cal.get(Calendar.SECOND)+ ":" + cal.get(Calendar.MILLISECOND) +
                }
            });
        } else {
            Log.d("SeekerFragment", "No Log");
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (isSeeking && mClientThread != null) {
            mClientThread.stopThread();
            mClientThread = null;
            isSeeking = false;
        }
    }

}