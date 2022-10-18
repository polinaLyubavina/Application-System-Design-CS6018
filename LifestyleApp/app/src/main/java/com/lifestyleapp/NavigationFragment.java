package com.lifestyleapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NavigationFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener, SensorEventListener {

    View navFragmentView;

    OnNavSelectedListener listener;
    Button profileButton;
    Button weightManButton;
    Button hikesButton;
    Button weatherButton;
    ImageView profilePhotoView;
    TextView stepCounterTextView;
    public final int PROFILE_BUTTON_INDEX = 1;
    public final int WEIGHT_BUTTON_INDEX = 2;
    public final int WEATHER_BUTTON_INDEX = 3;

    private NavigationViewModel navigationViewModel;

    private UserViewModel userViewModel;

    private SensorManager sensorManager;

    private boolean running = false;
    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnNavSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnNavSelectedListener");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("StepCounter", "Sensor Change Detected!");
        if (running) {
            Log.d("StepCounter", "User is running!");
            totalSteps = sensorEvent.values[0];
            int currentSteps = (int) (totalSteps - previousTotalSteps);
            stepCounterTextView.setText(String.valueOf(currentSteps));


        }
    }

//    private void resetSteps() {
//
//        stepCounterTextView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                previousTotalSteps = totalSteps;
//                stepCounterTextView.setText(0);
//                saveData();
//                return true;
//            }
//        });
//    }

    private void saveData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("key1", previousTotalSteps);
        editor.apply();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        Float savedNumber = sharedPreferences.getFloat("key1", 0f);
        Log.d("NavigationFragment", String.valueOf(savedNumber));
        previousTotalSteps = savedNumber;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        running = true;
        Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(stepSensor == null) {
            Toast.makeText(getContext(), "No sensor detected on this device", Toast.LENGTH_SHORT).show();
        }
        else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            Toast.makeText(getContext(), "Sensor detected on this device", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public boolean onLongClick(View view) {
        Log.d("EventListener", "Long press detected");
        switch (view.getId()) {
            case R.id.steps_text_view: {
                Log.d("EventListener", "Long press detected");
                previousTotalSteps = totalSteps;
                stepCounterTextView.setText(String.valueOf(0));
                saveData();
                Toast.makeText(getContext(), "Steps have been reset successfully.", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    public interface OnNavSelectedListener {
        public void onNavSelected(int navIndex);
    }

    public NavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // GET USER FROM VIEWMODEL (IF THERE IS ONE), THEN SET THE TEXT FIELDS ON THE UI
        navigationViewModel = ViewModelProviders.of(this).get(NavigationViewModel.class);
        loadData();
//        resetSteps();
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        navFragmentView = inflater.inflate(R.layout.fragment_nav_pane, container, false);
        profileButton = navFragmentView.findViewById(R.id.my_prof_btn_frag);
        weightManButton = navFragmentView.findViewById(R.id.weight_man_btn_frag);
        hikesButton = navFragmentView.findViewById(R.id.hike_btn_frag);
        weatherButton = navFragmentView.findViewById(R.id.weather_btn_frag);
        profilePhotoView = navFragmentView.findViewById(R.id.photo_nav_pane_frag);
        stepCounterTextView = (TextView) navFragmentView.findViewById(R.id.steps_text_view);

        profileButton.setOnClickListener(this);
        weightManButton.setOnClickListener(this);
        hikesButton.setOnClickListener(this);
        weatherButton.setOnClickListener(this);
        stepCounterTextView.setOnClickListener(this);
        stepCounterTextView.setOnLongClickListener(this);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        User user = userViewModel.getProfileViewModelData().getValue();

        if (user != null) {

            if (user.getProfilePhotoPath() != null) {
                String profPhotoFileName = user.getProfilePhotoPath();

                FileInputStream fis = null;
                try {
                    fis = getContext().openFileInput(profPhotoFileName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                byte[] readBytes = new byte[user.getProfilePhotoSize()];
                try {
                    fis.read(readBytes);
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bitmap fromFileBmp = BitmapFactory.decodeByteArray(readBytes, 0, readBytes.length);
                profilePhotoView.setImageBitmap(fromFileBmp);
            }
        }
        return navFragmentView;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        profilePhotoView = navFragmentView.findViewById(R.id.photo_nav_pane_frag);

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.my_prof_btn_frag: {

                listener.onNavSelected(PROFILE_BUTTON_INDEX);
            }
            break;
            case R.id.weight_man_btn_frag: {
                listener.onNavSelected(WEIGHT_BUTTON_INDEX);
            }
            break;
            case R.id.hike_btn_frag: {
                // Search for nearby hikes on Google Maps
                String city = navigationViewModel.getCity();
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=hiking" + " " + Uri.encode(city));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);

            }
            break;
            case R.id.weather_btn_frag: {
                // Search for local weather using the browser
                listener.onNavSelected(WEATHER_BUTTON_INDEX);
            }
            break;
            case R.id.steps_text_view: {
                Toast.makeText(getContext(), "Long tap to reset steps", Toast.LENGTH_SHORT).show();
            }

        }
    }



}