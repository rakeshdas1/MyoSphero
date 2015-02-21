package com.rakeshdas.myosphero;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import orbotix.sphero.Sphero;
import orbotix.view.calibration.CalibrationView;


public class CalibrationActivity extends ActionBarActivity {
private CalibrationView mCalibrationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        mCalibrationView = (CalibrationView)findViewById(R.id.CalibrationView);
        Intent calibrationIntent = getIntent();
        Bundle bundle = getIntent().getExtras();
        Sphero mRobot = (Sphero) bundle.getSerializable("Robot");
        mCalibrationView = (CalibrationView)findViewById(R.id.CalibrationView);
        mCalibrationView.setColor(Color.WHITE);
        mCalibrationView.setCircleColor(Color.WHITE);
        mCalibrationView.enable();
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
