package com.lifestyleapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NavigationFragment extends Fragment implements View.OnClickListener {

    View navFragmentView;

    OnNavSelectedListener listener;
    Button profileButton;
    Button weightManButton;
    Button hikesButton;
    Button weatherButton;
    ImageView profilePhotoView;
    public final int PROFILE_BUTTON_INDEX = 1;
    public final int WEIGHT_BUTTON_INDEX = 2;
    public final int WEATHER_BUTTON_INDEX = 3;

    private NavigationViewModel navigationViewModel;

    private UserViewModel userViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnNavSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnNavSelectedListener");
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

        // GET USER FROM VIEWMODEL (IF THERE IS ONE), THEN SET THE TEXT FIELDS ON THE UI
        navigationViewModel = ViewModelProviders.of(this).get(NavigationViewModel.class);



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

        profileButton.setOnClickListener(this);
        weightManButton.setOnClickListener(this);
        hikesButton.setOnClickListener(this);
        weatherButton.setOnClickListener(this);

        userViewModel = new ViewModelProvider(this.getActivity()).get(UserViewModel.class);

        userViewModel.getProfileViewModelData().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {

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
            }
        });

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
                String city = userViewModel.getCity();//navigationViewModel.getCity();
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

        }
    }
}