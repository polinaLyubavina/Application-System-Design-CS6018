package com.lifestyleapp;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class WeightManViewModel extends AndroidViewModel {

    private MutableLiveData<User> userMutableLiveData;
    private UserRepository userRepository;

    public WeightManViewModel (Application application) {

        super(application);

        userRepository = UserRepository.getInstance();

        userMutableLiveData = userRepository.getUserData();

    }

    // FORWARD ALL OF THE DATA TO THE REPOSITORY
    public void setProfileViewModelData(String fullName, int age, String city, String country, double height, double weight, int gender, String photoLocation, int photoSize, int steps, double bmi, double bmr, boolean sedentary){
        userRepository.setUserData(fullName, age, city, country, height, weight, gender, photoLocation, photoSize, steps, bmi, bmr, sedentary);
    }

    // RETRIEVE DATA FROM THE REPOSITORY
    public LiveData<User> getProfileViewModelData() {
        return userRepository.getUserData();
    }

}