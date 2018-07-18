package dynamicdrillers.happysingh.internshipdemo.activities;

import android.support.test.espresso.Espresso;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.intent.Intents;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Toast;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.intent.Intents.intended;


import org.hamcrest.core.IsNot;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import dynamicdrillers.happysingh.internshipdemo.R;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);



    @Test
    public void all_ui_test() {

        // --------------------**********************************------------------------------***************************************************//
        //LOGIN ACTIVITY


        // Clicking Login Without Entering Password
        onView(withId(R.id.edt_username)).perform(clearText(), typeText("DemoUser"), closeSoftKeyboard());
        onView(withId(R.id.btn_login_main)).perform(click()).inRoot(withDecorView(IsNot.not(is(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        // Clicking Login Without Entering Username
        onView(withId(R.id.edt_username)).perform(clearText());
        onView(withId(R.id.edt_password)).perform(clearText(), typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.btn_login_main)).perform(click()).inRoot(withDecorView(IsNot.not(is(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        //Clicking Login With Entering Wrong Username
        onView(withId(R.id.edt_username)).perform(clearText(), typeText("WrongUser"), closeSoftKeyboard());
        onView(withId(R.id.edt_password)).perform(clearText(), typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.btn_login_main)).perform(click()).inRoot(withDecorView(IsNot.not(is(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));


        //Clicking Login With Entering Wrong Password
        onView(withId(R.id.edt_username)).perform(clearText(), typeText("DemoUser"), closeSoftKeyboard());
        onView(withId(R.id.edt_password)).perform(clearText(), typeText("wrongpassword"), closeSoftKeyboard());
        onView(withId(R.id.btn_login_main)).perform(click()).inRoot(withDecorView(IsNot.not(is(loginActivityActivityTestRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        //Clicking Login With Correct Username And Password
        onView(withId(R.id.edt_username)).perform(clearText(), typeText("DemoUser"), closeSoftKeyboard());
        onView(withId(R.id.edt_password)).perform(clearText(), typeText("123456"), closeSoftKeyboard());
        onView(withId(R.id.btn_login_main)).perform(click());


        // --------------------**********************************------------------------------***************************************************//
        //MAIN_ACTIVITY
//        Intents.init();
//        intended(hasComponent(MainActivity.class.getName()));


        //Clickout Logout Button for Logout
        onView(withId(R.id.btn_logout)).perform(click());


    }

}