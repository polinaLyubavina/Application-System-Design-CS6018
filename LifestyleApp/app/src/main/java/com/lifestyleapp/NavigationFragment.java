package com.lifestyleapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
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
import java.util.ArrayList;

import android.gesture.GestureOverlayView;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.gesture.Gesture;

public class NavigationFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener, SensorEventListener, OnGesturePerformedListener {

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
    private User user;

    private SensorManager sensorManager;

    private GestureOverlayView objGestureOverlayView;

    private boolean running = false;
    private boolean startGesturePerformed = false;
    private boolean isStepSensorAvailable = false;
    private float totalSteps = 0f;
    private float previousTotalSteps = 0f;

    private GestureLibrary objGestureLib;


    private UserRepository userRepository;

    MediaPlayer startSound;
    MediaPlayer stopSound;

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
        if (running && startGesturePerformed) {
            totalSteps = sensorEvent.values[0];
            int currentSteps = (int) (totalSteps - previousTotalSteps);
            stepCounterTextView.setText(String.valueOf(currentSteps));


        }
    }


    private void saveData() {
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putFloat("key1", previousTotalSteps);
//        editor.apply();
        if(user != null) {
            userRepository.db.userDao().update((int) previousTotalSteps, user.getFullName());
        }


    }

    private void loadData() {
//        SharedPreferences sharedPreferences = getContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//        Float savedNumber = sharedPreferences.getFloat("key1", 0f);
//        Log.d("NavigationFragment", String.valueOf(savedNumber));
        if(user != null && user.getSteps() != null) {
            previousTotalSteps = user.getSteps();
        }
        else {
            previousTotalSteps = 0f;
        }
        stepCounterTextView.setText(String.valueOf(previousTotalSteps));

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
            isStepSensorAvailable = false;
        }
        else {
            isStepSensorAvailable = true;
            Toast.makeText(getContext(), "Make a circle clockwise to start step counter, counter clockwise to stop.", Toast.LENGTH_LONG).show();

            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
            loadData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        saveData();
    }

    @Override
    public boolean onLongClick(View view) {
        Log.d("EventListener", "Long press detected");
        switch (view.getId()) {
            case R.id.steps_text_view: {
                Log.d("EventListener", "Long press detected");
                totalSteps = 0f;
                previousTotalSteps = 0f;
                saveData();
                stepCounterTextView.setText(String.valueOf(user.getSteps()));
                Toast.makeText(getContext(), "Steps have been reset successfully.", Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    @Override
    public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
        ArrayList<Prediction> objPrediction = objGestureLib.recognize(gesture);

        if(!isStepSensorAvailable) {
            Toast.makeText(getContext(), "Gestures will only work if the sensor is available on the device.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(objPrediction.size() > 0 && objPrediction.get(0).score > 1) {
            String gestureName = objPrediction.get(0).name;
            if(gestureName == "StartStep") {
                startGesturePerformed = true;
                Toast.makeText(getContext(), "Step counter started.", Toast.LENGTH_SHORT).show();
                startSound.start();

            }
            if(gestureName == "StopStep") {
                startGesturePerformed = false;
                Toast.makeText(getContext(), "Step counter stopped.", Toast.LENGTH_SHORT).show();
                stopSound.start();

            }

        }



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

        // GET USER FROM VIEW MODEL (IF THERE IS ONE), THEN SET THE TEXT FIELDS ON THE UI
        navigationViewModel = ViewModelProviders.of(this).get(NavigationViewModel.class);
        if (user != null) {
            loadData();
        }

        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 20);
        }

        startSound = MediaPlayer.create(getContext(), R.raw.drums);
        stopSound = MediaPlayer.create(getContext(), R.raw.ding);


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
        objGestureLib = GestureLibraries.fromRawResource(getContext(), R.raw.gestures);
        if(!objGestureLib.load()) {
            Toast.makeText(getContext(), "Gestures failed to load, gestures will not work.", Toast.LENGTH_SHORT).show();
        }
        objGestureOverlayView = (GestureOverlayView) navFragmentView.findViewById(R.id.WidgetGesture);
        objGestureOverlayView.addOnGesturePerformedListener(this);

        profileButton.setOnClickListener(this);
        weightManButton.setOnClickListener(this);
        hikesButton.setOnClickListener(this);
        weatherButton.setOnClickListener(this);
        stepCounterTextView.setOnClickListener(this);
        stepCounterTextView.setOnLongClickListener(this);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        user = userViewModel.getProfileViewModelData().getValue();

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