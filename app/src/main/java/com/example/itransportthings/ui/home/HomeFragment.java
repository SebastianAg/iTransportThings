package com.example.itransportthings.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.itransportthings.R;



public class HomeFragment extends Fragment {


    private TextView textView;
    private Handler handler;
    private boolean longPress = false;

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button myButton = view.findViewById(R.id.button);
        textView = view.findViewById(R.id.textView2);
        handler = new Handler();

        myButton.setOnTouchListener((v, event) -> {
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

        return view;
    }

    @SuppressLint("SetTextI18n")
    private final Runnable longPressRunnable = () -> {
        requireActivity().runOnUiThread(() -> {
            textView.setVisibility(View.VISIBLE);
            textView.setText("Long press detected, calling robot...");
            textView.postDelayed(() -> textView.setVisibility(View.INVISIBLE), 3000);
        });
        longPress = true;
    };
}