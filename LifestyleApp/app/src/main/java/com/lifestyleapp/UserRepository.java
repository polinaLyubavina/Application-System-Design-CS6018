package com.lifestyleapp;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

public class UserRepository {

    // Singleton pattern setup
    // create an object of the class
    private static UserRepository instance = new UserRepository();
    // make constructor private so the class can't be instantiated
    private UserRepository() {};
    // get the only repository in existence
    public static UserRepository getInstance() { return instance; }

    private final MutableLiveData<User> currentUserLiveData = new MutableLiveData<>();

    public MutableLiveData<User> getUserData() { return currentUserLiveData; }

    public void setUserData (String fullName, int age, String city, String country, double height, double weight, int gender, @Nullable String profilePhotoFileName, @Nullable int profilePhotoSize, @Nullable Integer steps, double bmi, double bmr, boolean sedentary) {
        User userData = new User(fullName, age, city, country, height, weight, gender, profilePhotoFileName, profilePhotoSize, steps);
        currentUserLiveData.setValue(userData);
    }

}
