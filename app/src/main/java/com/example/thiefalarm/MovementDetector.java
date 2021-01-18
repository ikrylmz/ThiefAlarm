package com.example.thiefalarm;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.Nullable;

public class MovementDetector implements SensorEventListener {

    public interface IMovement {
        void onMovementTrigger();
    }



    private final SensorManager mSensorManager;

    @Nullable
    private final Sensor mAccelerometer;

    float sensivity = 10f;

    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;

    private IMovement mListener;


    private int mLastAccuracy;

    public MovementDetector(Activity activity) {

        mSensorManager = (SensorManager) activity.getSystemService(Activity.SENSOR_SERVICE);
        // Can be null if the sensor hardware is not available
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

    }

    public void startListening(IMovement listener) {
        if (mListener == listener) {
            return;
        }
        mListener = listener;
        if (mAccelerometer == null) {
            LogUtil.w("Accelerometer vector sensor not available; will not provide movement  data.");
            return;
        }

        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stopListening() {
        mSensorManager.unregisterListener(this);
        mListener = null;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (mLastAccuracy != accuracy) {
            mLastAccuracy = accuracy;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (mListener == null) {
            return;
        }
        if (mLastAccuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
            return;
        }
        if (event.sensor == mAccelerometer) {
            updateOrientation(event.values);
        }
    }



    @SuppressWarnings("SuspiciousNameCombination")
    private void updateOrientation(float[] values) {


            mGravity = values.clone();

            float x = mGravity[0];
            float y = mGravity[1];
            float z = mGravity[2];

            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float)Math.sqrt(x*x + y*y + z*z);

            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;

            if(mAccel >= sensivity){
                mListener.onMovementTrigger();
            }



    }

}
