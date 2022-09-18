package com.lifestyleapp;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class UserViewModel extends AndroidViewModel {

    private MutableLiveData<User> userMutableLiveData;
    private UserRepository profilePageRepository;

    public UserViewModel(Application application) {

        super(application);

        profilePageRepository = UserRepository.getInstance();

        userMutableLiveData = profilePageRepository.getUserData();

    }

    // FORWARD ALL OF THE DATA TO THE REPOSITORY
    public void setProfileViewModelData(String fullName, int age, String city, String country, double height, double weight, int gender, @Nullable String profilePhotoFileName, @Nullable int profilePhotoSize, @Nullable Integer steps, double bmi, double bmr, boolean sedentary){
        profilePageRepository.setUserData(fullName, age, city, country, height, weight, gender, profilePhotoFileName, profilePhotoSize, steps, bmi, bmr, sedentary);
    }

    // RETRIEVE DATA FROM THE REPOSITORY
    public LiveData<User> getProfileViewModelData() {
        return profilePageRepository.getUserData();
    }

}
