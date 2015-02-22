package com.rakeshdas.myosphero;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
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

    private Button mConnect, mConnectSphero, mCalibrateSphero;
    private TextView mText, mCaptionTxt, mSpeedText;
    private String TAG = "MyoSphero";
    private SpheroConnectionView mSpheroConnectionView;
    private Sphero mRobot;
//    private CalibrationView mCalibrationView;
    float speed = (float) 0.4;
    private SeekBar mSpeedSeek;
    private boolean spheroConnected;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mConnect = (Button)findViewById(R.id.connectBtn);
        mText = (TextView)findViewById(R.id.mainTextView);
        mCaptionTxt = (TextView)findViewById(R.id.captionTextView);
        mCalibrateSphero = (Button) findViewById(R.id.calibrateSpheroBtn);
        mSpeedSeek = (SeekBar)findViewById(R.id.speedSeekBar);
        mSpeedSeek.setMax(9);
        mSpeedText = (TextView)findViewById(R.id.speedTextView);
        //Set the speed via the seekbar
        mSpeedSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress){
                    case 0:
                        mSpeedText.setText(R.string.speed10);
                        speed = 0.1f;
                        break;
                    case 1:
                        mSpeedText.setText(R.string.speed20);
                        speed = 0.2f;
                        break;
                    case 2:
                        mSpeedText.setText(R.string.speed30);
                        speed = 0.3f;
                        break;
                    case 3:
                        mSpeedText.setText(R.string.speed40);
                        speed = 0.4f;
                        break;
                    case 4:
                        mSpeedText.setText(R.string.speed50);
                        speed = 0.5f;
                        break;
                    case 5:
                        mSpeedText.setText(R.string.speed60);
                        speed = 0.6f;
                        break;
                    case 6:
                        mSpeedText.setText(R.string.speed70);
                        speed = 0.7f;
                        break;
                    case 7:
                        mSpeedText.setText(R.string.speed80);
                        speed = 0.8f;
                        break;
                    case 8:
                        mSpeedText.setText(R.string.speed90);
                        speed = 0.9f;
                        break;
                    case 9:
                        mSpeedText.setText(R.string.speed100);
                        speed = 1.0f;
                        break;
                    default:
                        mSpeedText.setText(R.string.speed40);
                        speed = 0.4f;
                        break;

                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mConnectSphero = (Button)findViewById(R.id.connectSpheroBtn);
        mConnectSphero.setEnabled(false);
        spheroConnected = false;
       //Init the hub for the Myo
        initHub();
        //Connect the sphero
        mConnectSphero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mSpheroConnectionView.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                mSpheroConnectionView.setLayoutParams(params);
            }
        });
        mConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanMyos();
            }
        });
        //Keeps screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);/*
        mCalibrationView = (CalibrationView)findViewById(CalibrationView);
        mCalibrationView.setColor(Color.WHITE);
        mCalibrationView.setCircleColor(Color.WHITE);
        mCalibrationView.enable();*/
        //Launch activity to calibrate the Sphero
        mCalibrateSphero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calibrationIntent = new Intent(MainActivity.this, CalibrationActivity.class);
                calibrationIntent.putExtra("Robot", (Parcelable) mRobot);
                startActivity(calibrationIntent);
            }
        });
        //Connect Sphero by inflating the connection view
        mSpheroConnectionView = (SpheroConnectionView)findViewById(R.id.sphero_connection_view);mSpheroConnectionView.addConnectionListener(new ConnectionListener() {
            @Override
            public void onConnected(Robot robot) {
                mRobot = (Sphero) robot;
                //mCalibrationView.setRobot(mRobot);
                mConnectSphero.setText(R.string.disconnectSphero);
                mConnectSphero.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mRobot.disconnect();
                    }
                });
                spheroConnected = true;
                Toast.makeText(getApplicationContext(), R.string.pickSphero, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onConnectionFailed(Robot robot) {
                Toast.makeText(getApplicationContext(), "Couldn't connect to the Sphero!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onDisconnected(Robot robot) {
                Toast.makeText(getApplicationContext(), "Sphero disconnected!", Toast.LENGTH_LONG).show();
                mConnectSphero.setText(R.string.connectSphero);
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
    //inflates view to connect Myo
    private void scanMyos(){
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }
    //DeviceListener fot Myo
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
            mCaptionTxt.setClickable(true);
            mCaptionTxt.setText(Html.fromHtml("<a href = 'https://support.getmyo.com/hc/en-us/articles/200755509-How-to-perform-the-sync-gesture'> How do I perform the sync gesture? </a>"));
            mCaptionTxt.setMovementMethod(LinkMovementMethod.getInstance());
            mConnect.setText(R.string.disconnectMyo);
            myo.unlock(Myo.UnlockType.HOLD);
            mConnectSphero.setEnabled(true);
        }

        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            super.onDisconnect(myo, timestamp);
            Toast.makeText(getApplicationContext(), "Myo Disconnected!", Toast.LENGTH_LONG).show();
            mText.setText(R.string.main);
            mConnect.setText(R.string.connect);
        }

        @Override
        public void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection) {
            super.onArmSync(myo, timestamp, arm, xDirection);
            mText.setText(myo.getArm() == Arm.LEFT ? R.string.left : R.string.right);
            mCaptionTxt.setText(R.string.lock);
        }

        @Override
        public void onArmUnsync(Myo myo, long timestamp) {
            super.onArmUnsync(myo, timestamp);
            mText.setText(R.string.unsync);
            mCaptionTxt.setText(R.string.armUnsyncHelp);
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
                    if(spheroConnected){
                    mText.setText(R.string.pFist);
                    mRobot.drive(0f, speed);
                    myo.unlock(Myo.UnlockType.HOLD);
                    break;}
                    else{
                        Toast.makeText(getApplicationContext(), "Sphero isn't connected yet!", Toast.LENGTH_LONG).show();
                    }
                }
                case FINGERS_SPREAD:{
                    if(spheroConnected){
                    mText.setText(R.string.pFingersSpread);
                    mRobot.stop();
                    myo.unlock(Myo.UnlockType.HOLD);
                    break;}
                    else{
                        Toast.makeText(getApplicationContext(), "Sphero isn't connected yet!", Toast.LENGTH_LONG).show();
                    }
                }
                case WAVE_IN:{
                    if(spheroConnected){
                    mText.setText(R.string.pWaveIn);
                    mRobot.drive(270f,speed);
                    myo.unlock(Myo.UnlockType.HOLD);
                    break;}
                    else{
                        Toast.makeText(getApplicationContext(), "Sphero isn't connected yet!", Toast.LENGTH_LONG).show();
                    }
                }
                case WAVE_OUT:{
                    if(spheroConnected){
                    mText.setText(R.string.pWaveOut);
                    mRobot.drive(90f, speed);
                    myo.unlock(Myo.UnlockType.HOLD);
                    break;}
                    else{
                        Toast.makeText(getApplicationContext(), "Sphero isn't connected yet!", Toast.LENGTH_LONG).show();
                    }
                }

            }
            if (pose != Pose.UNKNOWN && pose != Pose.REST){
                myo.unlock(Myo.UnlockType.HOLD);
                myo.notifyUserAction();

            }
            else{
                myo.unlock(Myo.UnlockType.HOLD);
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
    }
    @Override
    //Disconnects Sphero on exit
    protected void onPause(){
        super.onPause();
        BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
        if(mRobot != null){
            mRobot.disconnect();
        }
    }
}
