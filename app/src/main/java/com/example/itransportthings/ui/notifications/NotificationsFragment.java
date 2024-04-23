package com.example.itransportthings.ui.notifications;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.itransportthings.R;
import com.example.itransportthings.databinding.FragmentNotificationsBinding;

public class NotificationsFragment extends Fragment {
    //private String X = "www.google.com";
    private String X = "172.20.10.4";
    EditText ipAddress;

    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        WebView webview = view.findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(X); //Change this to pico ip
        Button mySwapButton = view.findViewById(R.id.button4);
        //EditText myEditIP = view.findViewById(R.id.editTextText);

        mySwapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("New IP-address?");
                ipAddress = new EditText(getActivity());
                builder.setView(ipAddress);

                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        X = ipAddress.getText().toString();
                        webview.loadUrl(X); //Change this to pico ip
                        //Toast.makeText(getActivity().getApplicationContext(), X, Toast.LENGTH_LONG).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dont do anything
                    }
                });

                AlertDialog ad = builder.create();

                AlertDialog mDialog = builder.create();
                mDialog.show();
            }
        });
        return view;
    }


}