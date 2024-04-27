package com.example.itransportthings.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.itransportthings.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HomeFragment extends Fragment {
    String server_url = "http://130.229.154.164:8000";


    private TextView textView;
    private TextView textView2;
    private TextView textView3;
    private Handler handler;
    private int clickCount = 0;
    private boolean longPress = false;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button myCallButton = view.findViewById(R.id.button);
        Button myShutdownButton = view.findViewById(R.id.button2);
        Button mySendButton = view.findViewById(R.id.button3);
        textView = view.findViewById(R.id.textView2);
        textView2 = view.findViewById(R.id.textView3);
        textView3 = view.findViewById(R.id.textView4);
        handler = new Handler();

        myCallButton.setOnTouchListener((v, event) -> { //call button
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    handler.postDelayed(longPressRunnable, 5000); // 5 seconds
                    return true;
                case MotionEvent.ACTION_UP:
                    if (!longPress) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("You clicked the button, hold to call robot");
                        textView.postDelayed(() -> textView.setVisibility(View.INVISIBLE), 3000);
                    }
                    handler.removeCallbacks(longPressRunnable);
                    longPress = false;
                    return true;
            }
            return false;
        });

        myShutdownButton.setOnClickListener(new View.OnClickListener() { //shutdown button
            @Override
            public void onClick(View v) {
                clickCount++;
                click(clickCount);
                if(clickCount >= 5){
                    //shutdown();
                    clickCount = 0;
                }
            }
        });

        mySendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you really want to send the robot?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        textView3.setVisibility(View.VISIBLE);
                        textView3.setText("Robot has been sent away");
                        textView3.postDelayed(() -> textView3.setVisibility(View.INVISIBLE), 3000);
                        sendRobot();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dont do anything here i guess
                    }
                });
                AlertDialog mDialog = builder.create();
                mDialog.show();
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private final Runnable longPressRunnable = () -> {
        requireActivity().runOnUiThread(() -> {
            callRobot();
            textView.setVisibility(View.VISIBLE);
            textView.setText("Long press detected, calling robot...");
            textView.postDelayed(() -> textView.setVisibility(View.INVISIBLE), 3000);
        });
        longPress = true;
    };

    @SuppressLint("SetTextI18n")
    private void click(int clicks){
        if (clicks < 5) {
            textView2.setVisibility(View.VISIBLE);
            textView2.setText("Press this " + (5-clicks) + " more to shutdown");
        }
        if (clicks == 5){
            shutdown();
            textView2.setVisibility(View.VISIBLE);
            textView2.setText("Robot has been shutdown");
            textView2.postDelayed(() -> textView2.setVisibility(View.INVISIBLE), 3000);
        }
    }

    private class ParseTask extends AsyncTask<String, Void, Void> {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        String data;

        @Override
        protected Void doInBackground(String... params) {
            try {
                data = params[0];
                Log.d("tag", "doInBackground: " + data);
                String site_url_json = server_url + data;
                Log.d("tag", "doInBackground: " + site_url_json);
                URL url = new URL(site_url_json);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void callRobot(){
        new ParseTask().execute("/run/?action=start");
    }
    private void shutdown(){
        new ParseTask().execute("/run/?action=stop");
    }

    private void sendRobot(){
        new ParseTask().execute("/run/?action=send");
    }
}