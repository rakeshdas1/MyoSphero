package com.rakeshdas.myosphero;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;


public class MainActivity extends ActionBarActivity {

    private Button mConnect;
    private TextView mText;
    private String TAG = "MyoSphero";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConnect = (Button)findViewById(R.id.connectBtn);
        mText = (TextView)findViewById(R.id.mainTextView);
        initHub();

        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanMyos();
            }
        });
    }
    private void initHub(){
        Hub hub = Hub.getInstance();
        if (hub.init(this)){
            Log.e(TAG, "Initialed the Hub");
        }
        if (!hub.init(this)){
            Log.e(TAG, "Could not initialize the Hub");
            return;
        }
    }
    private void scanMyos(){
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }
    private DeviceListener mListener = new AbstractDeviceListener() {
        @Override
        public void onAttach(Myo myo, long timestamp) {
            super.onAttach(myo, timestamp);
        }

        @Override
        public void onDetach(Myo myo, long timestamp) {
            super.onDetach(myo, timestamp);
        }

        @Override
        public void onConnect(Myo myo, long timestamp) {
            super.onConnect(myo, timestamp);
            mText.setText("Myo Connected!");
        }

        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            super.onDisconnect(myo, timestamp);
            mText.setText("Myo Disconnected!");
        }

        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
            super.onArmSync(myo, timestamp, arm, xDirection);
        }

        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
            super.onArmUnsync(myo, timestamp);
        }

        @Override
        public void onUnlock(Myo myo, long timestamp) {
            super.onUnlock(myo, timestamp);
        }

        @Override
        public void onLock(Myo myo, long timestamp) {
            super.onLock(myo, timestamp);
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            super.onPose(myo, timestamp, pose);
        }

        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
            super.onOrientationData(myo, timestamp, rotation);
        }
    };





}
