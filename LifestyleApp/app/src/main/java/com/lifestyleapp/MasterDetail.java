package com.lifestyleapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MasterDetail extends AppCompatActivity implements NavigationFragment.OnNavSelectedListener, ProfilePageFragment.OnLifePressListener, WeightFragment.OnLifePressFromWeightListener {

    private NavigationFragment mMasterListNavFrag;
    private ProfilePageFragment profilePageFragment;
    private WeightFragment weightFragment;
    private WeatherFragment weatherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);

        mMasterListNavFrag = new NavigationFragment();
        profilePageFragment = new ProfilePageFragment();
        weightFragment = new WeightFragment();
        weatherFragment = new WeatherFragment();


        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        if (isTablet()) {
            fTrans.replace(R.id.master_detail_nav_pane_tablet, mMasterListNavFrag, "frag_masterlist");
            fTrans.replace(R.id.master_detail_right_pane_tablet, profilePageFragment, "frag_profile");
        } else {
            fTrans.replace(R.id.master_detail_pane_phone, mMasterListNavFrag, "frag_masterlist");
        }
        fTrans.commit();

    }

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
    }