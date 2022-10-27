package com.example.lifestyleapp

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule;
import androidx.test.rule.ActivityTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed

import com.example.lifestyleapp.R

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed


@RunWith(AndroidJUnit4::class)

/**
 * Tests the UI in right order
 */
class CorrectOrderTest {
    @Rule @JvmField
    public var activityActivityTestRule: ActivityTestRule<com.lifestyleapp.MasterDetail> =
        ActivityTestRule<com.lifestyleapp.MasterDetail>(com.lifestyleapp.MasterDetail::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.lifestyleapp", appContext.packageName)
    }

//    @Test
//    fun clickButtonHome() {
//        Espresso.onView(withId(R.id.navigation_home)).perform(Espresso.click())
//            .check(Espresso.matches(isDisplayed()))
//    }

    @Test
    fun checkOpenProfile() {
        onView(withId(R.id.my_prof_btn_frag))
            .perform(click())
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkName() {
        onView(withId(R.id.profileNameFrag))
            .perform(typeText("NameTest"))
            .check(matches(withText("NameTest")))
    }

    @Test
    fun checkAge() {
        onView(withId(R.id.profileAgeFrag))
            .perform(typeText("20"))
            .check(matches(withText("20")))
    }

    @Test
    fun checkCity() {
        onView(withId(R.id.profileCityFrag))
            .perform(typeText("Salt Lake City"))
            .check(matches(withText("Salt Lake City")))
    }

    @Test
    fun checkCountry() {
        onView(withId(R.id.profileCountryFrag))
            .perform(typeText("USA"))
            .check(matches(withText("USA")))
    }

    @Test
    fun checkGenderBtn() {
        onView(withId(R.id.profileMaleFrag))
            .perform(click())
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkSaveBtn() {
        onView(withId(R.id.saveProfileFrag))
            .perform(click())
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkProfileBackBtn() {
        onView(withId(R.id.lifeBtnMyProfFrag))
            .perform(click())
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkWeightMgt() {
        //Enter Weight Management
        onView(withId(R.id.weight_man_btn_frag))
            .perform(click())
            .check(matches(isDisplayed()))

        //Sedentary Btn
        onView(withId(R.id.calculatorSedentaryFrag))
            .perform(click())
            .check(matches(onDisplay()))

        //Back Btn
        onView(withId(R.id.lifestyle_btn_weightman_frag))
            .perform(click())
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkNearbyHikes() {
        //Enter Hikes
        onView(withId(R.id.hike_btn_frag))
            .perform(click())
            .check(matches(isDisplayed()))

        //Go back
        Espresso.pressBack()
    }

    @Test
    fun checkLocalWeather() {
        //Enter Weather
        onView(withId(R.id.weather_btn_frag))
            .perform(click())
            .check(matches(isDisplayed()))

        //Press "Go" Btn
        onView(withId(R.id.resetLocation))
            .perform(click())
            .check(matches(isDisplayed()))

        //Go back
        Espresso.pressBack()
    }
}


/**
 * Tests the UI in incorrect order of use
 */
@class IncorrectOrderTest {
    @Rule @JvmField
    public var activityActivityTestRule: ActivityTestRule<com.lifestyleapp.MasterDetail> =
        ActivityTestRule<com.lifestyleapp.MasterDetail>(com.lifestyleapp.MasterDetail::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.lifestyleapp", appContext.packageName)
    }

    @Test
    fun checkLocalWeather() {
        //Enter Weather
        onView(withId(R.id.weather_btn_frag))
            .perform(click())
            .check(matches(isDisplayed()))

        //Press "Go" Btn
        onView(withId(R.id.resetLocation))
            .perform(click())
            .check(matches("Please enter a location in '[City], [Country Abbreviation] format."))

        //Go back
        Espresso.pressBack()
    }

    @Test
    fun checkProfileNoInputs() {
        //Enter Profile
        onView(withId(R.id.my_prof_btn_frag))
            .perform(click())
            .check(matches(isDisplayed()))

        //Press "Save"
        onView(withId(R.id.saveProfileFrag))
            .perform(click())
            .check(matches("Please fill out all fields!"))

        //Go back
        onView(withId(R.id.lifeBtnMyProfFrag))
            .perform(click())
            .check(matches(isDisplayed()))
    }

    @Test
    fun checkProfilePartialInputs() {
        //Enter Profile
        onView(withId(R.id.my_prof_btn_frag))
            .perform(click())
            .check(matches(isDisplayed()))

        //Enter Name
        onView(withId(R.id.profileNameFrag))
            .perform(typeText("NameTest"))
            .check(matches(withText("NameTest")))

        //Enter Age
        onView(withId(R.id.profileAgeFrag))
            .perform(typeText("20"))
            .check(matches(withText("20")))

        //Enter City
        onView(withId(R.id.profileCityFrag))
            .perform(typeText("Salt Lake City"))
            .check(matches(withText("Salt Lake City")))

        //Enter Country
        onView(withId(R.id.profileCountryFrag))
            .perform(typeText("USA"))
            .check(matches(withText("USA")))

        //Press "Save"
        onView(withId(R.id.saveProfileFrag))
            .perform(click())
            .check(matches("Please select a gender!"))

        //Go back
        onView(withId(R.id.lifeBtnMyProfFrag))
            .perform(click())
            .check(matches(isDisplayed()))
    }
}
