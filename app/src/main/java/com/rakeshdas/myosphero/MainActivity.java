package com.rakeshdas.myosphero;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.XDirection;
import com.thalmic.myo.scanner.ScanActivity;

import orbotix.robot.base.Robot;
import orbotix.sphero.ConnectionListener;
import orbotix.sphero.Sphero;
import orbotix.view.connection.SpheroConnectionView;


public class MainActivity extends Activity {

    private Button mConnect;
    private TextView mText;
    private String TAG = "MyoSphero";
    private TextView mCaptionTxt;
    private SpheroConnectionView mSpheroConnectionView;
    private Sphero mRobot;
    private String howSyncHelp = "<a href = 'https://support.getmyo.com/hc/en-us/articles/200755509-How-to-perform-the-sync-gesture'> How do I perform the sync gesture? </a>";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConnect = (Button)findViewById(R.id.connectBtn);
        mText = (TextView)findViewById(R.id.mainTextView);
        mCaptionTxt = (TextView)findViewById(R.id.captionTextView);
        mCaptionTxt.setClickable(true);
        mCaptionTxt.setMovementMethod(LinkMovementMethod.getInstance());
        mCaptionTxt.setText(Html.fromHtml(howSyncHelp));
        initHub();
        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanMyos();
            }
        });
        mSpheroConnectionView = (SpheroConnectionView)findViewById(R.id.sphero_connection_view);mSpheroConnectionView.addConnectionListener(new ConnectionListener() {
            @Override
            public void onConnected(Robot robot) {
                mRobot = (Sphero) robot;
            }

            @Override
            public void onConnectionFailed(Robot robot) {
                Toast.makeText(getApplicationContext(), "Couldn't connect to the Sphero!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDisconnected(Robot robot) {
                Toast.makeText(getApplicationContext(), "Sphero disconnected!", Toast.LENGTH_LONG).show();
                mSpheroConnectionView.startDiscovery();
            }
        });
    }
    private void initHub(){
        Hub hub = Hub.getInstance();
        if (hub.init(this)){
            Log.e(TAG, "Initialized the Hub");
        }
        if (!hub.init(this)){
            Log.e(TAG, "Could not initialize the Hub");
        }
        hub.addListener(mListener);
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
            Toast.makeText(getApplicationContext(), "Myo Connected!", Toast.LENGTH_LONG).show();
            mText.setText(R.string.sync);
            mConnect.setText(R.string.disconnectMyo);
            mCaptionTxt.setVisibility(View.VISIBLE);
        }

        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            super.onDisconnect(myo, timestamp);
            Toast.makeText(getApplicationContext(), "Myo Disconnected!", Toast.LENGTH_LONG).show();
            mText.setText(R.string.main);
            mConnect.setText(R.string.connect);
            mCaptionTxt.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
            super.onArmSync(myo, timestamp, arm, xDirection);
            mText.setText(myo.getArm() == Arm.LEFT ? R.string.left : R.string.right);
            mCaptionTxt.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
            super.onArmUnsync(myo, timestamp);
            mText.setText(R.string.unsync);
        }

        @Override
        public void onUnlock(Myo myo, long timestamp) {
            super.onUnlock(myo, timestamp);
            mCaptionTxt.setText(R.string.unlock);
        }

        @Override
        public void onLock(Myo myo, long timestamp) {
            super.onLock(myo, timestamp);
            mCaptionTxt.setText(R.string.lock);
        }

        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            super.onPose(myo, timestamp, pose);
            switch (pose){
                case FIST:{
                    mText.setText(R.string.pFist);
                    mRobot.drive(0f, 0.2f);
                    myo.unlock(Myo.UnlockType.HOLD);
                    break;
                }
                case FINGERS_SPREAD:{
                    mText.setText(R.string.pFingersSpread);
                    mRobot.stop();
                    myo.unlock(Myo.UnlockType.HOLD);
                    break;
                }
                case WAVE_IN:{
                    mText.setText(R.string.pWaveIn);
                    mRobot.drive(270f, 0.2f);
                    myo.unlock(Myo.UnlockType.HOLD);
                    break;
                }
                case WAVE_OUT:{
                    mText.setText(R.string.pWaveOut);
                    mRobot.drive(90f, 0.2f);
                    myo.unlock(Myo.UnlockType.HOLD);
                    break;
                }

            }
            if (pose != Pose.UNKNOWN && pose != Pose.REST){
                myo.unlock(Myo.UnlockType.HOLD);
                myo.notifyUserAction();

            }
            else{
                myo.unlock(Myo.UnlockType.TIMED);
            }
        }

        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
            super.onOrientationData(myo, timestamp, rotation);
        }
    };
    @Override
    protected void onResume(){
        super.onResume();
        mSpheroConnectionView.startDiscovery();
    }
    @Override
    protected void onPause(){
        super.onPause();
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        if(mRobot != null){
            mRobot.disconnect();
        }
    }
}
