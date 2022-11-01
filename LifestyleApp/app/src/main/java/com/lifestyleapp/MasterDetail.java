package com.lifestyleapp;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class MasterDetail extends AppCompatActivity implements NavigationFragment.OnNavSelectedListener, ProfilePageFragment.OnLifePressListener, WeightFragment.OnLifePressFromWeightListener {

    private NavigationFragment mMasterListNavFrag;
    private ProfilePageFragment profilePageFragment;
    private WeightFragment weightFragment;
    private WeatherFragment weatherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            // Add these lines to add the AWSCognitoAuthPlugin and AWSS3StoragePlugin plugins
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.addPlugin(new AWSS3StoragePlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

        this.uploadFile();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);

        mMasterListNavFrag = new NavigationFragment();
        profilePageFragment = new ProfilePageFragment();
        weightFragment = new WeightFragment();
        weatherFragment = new WeatherFragment();

//        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//
//        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//
//        }

        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        if (isTablet()) {
            fTrans.replace(R.id.master_detail_nav_pane_tablet, mMasterListNavFrag, "frag_masterlist");
            fTrans.replace(R.id.master_detail_right_pane_tablet, profilePageFragment, "frag_profile");
        } else {
            fTrans.replace(R.id.master_detail_pane_phone, mMasterListNavFrag, "frag_masterlist");
        }
        fTrans.commit();
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }

    @Override
    public void onNavSelected(int navIndex) {
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        if (isTablet()) {
            if (navIndex == mMasterListNavFrag.PROFILE_BUTTON_INDEX) {
                fTrans.replace(R.id.master_detail_right_pane_tablet, profilePageFragment, "frag_myprof_tab").commit();
            } else if (navIndex == mMasterListNavFrag.WEIGHT_BUTTON_INDEX) {
                fTrans.replace(R.id.master_detail_right_pane_tablet, weightFragment, "frag_weightman_tab").commit();
            }
            else if(navIndex == mMasterListNavFrag.WEATHER_BUTTON_INDEX){
                fTrans.replace(R.id.master_detail_right_pane_tablet, weatherFragment, "frag_weather_tab").commit();
            }

        } else {
            if (navIndex == mMasterListNavFrag.PROFILE_BUTTON_INDEX) {
                fTrans.replace(R.id.master_detail_pane_phone, profilePageFragment, "frag_myprof_phone").addToBackStack("profile").commit();
            } else if (navIndex == mMasterListNavFrag.WEIGHT_BUTTON_INDEX) {
                fTrans.replace(R.id.master_detail_pane_phone, weightFragment, "frag_weightman_phone").addToBackStack("weight").commit();
            } else if (navIndex == mMasterListNavFrag.WEATHER_BUTTON_INDEX) {
                fTrans.replace(R.id.master_detail_pane_phone, weatherFragment, "frag_weather_phone").addToBackStack("weather").commit();
            }

        }
    }

    @Override
    public void onLifeBtnPress() {
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        if (isTablet()) {
                fTrans.remove(profilePageFragment);
        } else {
            fTrans.replace(R.id.master_detail_pane_phone, mMasterListNavFrag);
        }
        fTrans.commit();
    }


    @Override
    public void onLifeBtnPressFromWeight() {
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        if (isTablet()) {
                fTrans.remove(weightFragment);
            }
         else {
            fTrans.replace(R.id.master_detail_pane_phone, mMasterListNavFrag);
        }
        fTrans.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
//        }
    }

    private void uploadFile() {
        File exampleFile = new File(getApplicationContext().getFilesDir(), "ExampleKey");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
            writer.append("Example file contents");
            writer.close();
        } catch (Exception exception) {
            Log.e("MyAmplifyApp", "Upload failed", exception);
        }

        Amplify.Storage.uploadFile(
                "ExampleKey",
                exampleFile,
                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }

    }