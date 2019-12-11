package com.example.threads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity_TAG";

    // Launcher -> tap:
    /// System calls Zygote (Copy ART and assign us the main thread)
    private TextView textView;

    Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            String result = message.getData().getString("RESULT");
            updateUI(result);
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.result);
        String name = Thread.currentThread().getName();
        Log.d(TAG, "onCreate: " + name);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                doNetwork();
            }
        });
        thread.start();
//        doNetwork();

        // Intent service worker
        MyIntentService.startActionBaz(this, "Param", "Param");

    }

    private void doNetwork() {
        HttpURLConnection connection;
        try {
            URL url = new URL("https://google.com");
            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader read = new InputStreamReader(connection.getInputStream());
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    textView.setText("My result");
//                }
//            });
            Message msg = new Message();
            Bundle data = new Bundle();
            data.putString("RESULT", "Christian result");
            msg.setData(data);
            myHandler.sendMessage(msg);

        } catch (Exception e) {
            Log.e(TAG, "doNetwork: ", e);
        }
    }

    private void updateUI(String result) {
        textView.setText(result);
    }

    // I/O -> Reading and writing
    // Files, Databases, Network
    // Heavy computation -> math
    // Matrix transformations, bitmap processing

}
