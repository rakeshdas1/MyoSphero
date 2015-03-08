package com.rakeshdas.myosphero;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import orbotix.view.calibration.CalibrationButtonView;
import orbotix.view.calibration.CalibrationView;


public class CalibrationActivity extends Activity {
    private CalibrationView mCalibrationView;
    private CalibrationButtonView mCalibrationButtonViewAbove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        //mCalibrationView = (CalibrationView)findViewById(R.id.calibration_above);
        mCalibrationButtonViewAbove = (CalibrationButtonView)findViewById(R.id.calibration_button_above);
        Intent calibrationIntent = getIntent();
        Bundle bundle = getIntent().getExtras();
        //Sphero mRobot = (Sphero) bundle.getParcelable("Robot");
       /* mCalibrationView = (CalibrationView)findViewById(R.id.CalibrationView);
        mCalibrationView.setColor(Color.WHITE);
        mCalibrationView.setCircleColor(Color.WHITE);
        //mCalibrationView.setRobot(mRobot);
        mCalibrationView.enable();*/
        // Initialize calibrate button view where the calibration circle shows above button
        // This is the default behavior
        mCalibrationButtonViewAbove = (CalibrationButtonView)findViewById(R.id.calibration_above);
        mCalibrationButtonViewAbove.setCalibrationButton((View)findViewById(R.id.calibration_button_above));
        // You can also change the size of the calibration views
        mCalibrationButtonViewAbove.setRadius(300);
        mCalibrationButtonViewAbove.setCalibrationCircleLocation(CalibrationButtonView.CalibrationCircleLocation.ABOVE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calibration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
