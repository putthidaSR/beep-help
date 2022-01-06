package com.example.uwcapstone;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.sonicmeter.android.multisonicmeter.Params;
import com.sonicmeter.android.multisonicmeter.Utils;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelperFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelperFragment extends Fragment {

    private static FragmentActivity instance;
    private boolean isListening;
    String msg = "";

    static Receiver mReceiverThread;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HelperFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelperFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelperFragment newInstance(String param1, String param2) {
        HelperFragment fragment = new HelperFragment();
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

        View view = inflater.inflate(R.layout.fragment_helper, container, false);
        instance = getActivity();

        // initial audiotrack player
        Utils.initPlayer(Params.sampleRate, 0);

        Utils.initConvolution((Params.signalSequenceLength * Params.bitCount));

        // initial UI state
        final ImageButton imgBtn = (ImageButton)view.findViewById(R.id.play_or_stop);
        final TextView playStopLabel = (TextView)view.findViewById(R.id.play_label);

        // Start by default
        isListening = true;
        mReceiverThread = new Receiver("Helper");
        mReceiverThread.start();

        imgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Stop if currently seeking
                if (instance != null && isListening) {
                    Log.d("SeekerFragment", "attempt to stop");
                    imgBtn.setImageResource(R.drawable.play);
                    playStopLabel.setText("Start Listening For Help");
                    playStopLabel.setTextColor(Color.parseColor("#0748ab"));

                    mReceiverThread.stopThread();
                    mReceiverThread = null;
                    isListening = false;

                } else if (!isListening) {
                    imgBtn.setImageResource(R.drawable.stop);
                    playStopLabel.setText("Stop Listening");

                    isListening = true;

                    mReceiverThread = new Receiver("Helper");
                    mReceiverThread.start();
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // this event will enable the back
    // function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                if (isListening && mReceiverThread != null) {
                    mReceiverThread.stopThread();
                    mReceiverThread = null;
                    isListening = false;
                    instance = null;
                }
                Log.d("HelperFragment", "Back pressed");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void log(final String text) {
        if (instance != null) {
            instance.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView logBox = instance.findViewById(R.id.helper_textview_log);
                    logBox.setMovementMethod(ScrollingMovementMethod.getInstance());
                    //Calendar cal = Calendar.getInstance();
                    logBox.append("  " + text + "\n");//cal.get(Calendar.MINUTE)+ ":" +cal.get(Calendar.SECOND)+ ":" + cal.get(Calendar.MILLISECOND) +
                }
            });
        } else {
            Log.d("HelperFragment", "No Log");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("HelperFragment", "onStop()");

        if (isListening && mReceiverThread != null) {
            mReceiverThread.stopThread();
            mReceiverThread = null;
            isListening = false;
            instance = null;
        }
    }
}