package com.openclassrooms.realestatemanager;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.ui.activity.MainActivity;
import com.openclassrooms.realestatemanager.utils.Utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class InternetConnectionTest {

    @Rule
    public final ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);


    @Test
    public void checkInternetConnectivityWhenIsFalseTest () throws InterruptedException {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi disable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data disable");

        TimeUnit.SECONDS.sleep(1);

        activityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                boolean state = Utils.isInternetAvailable(activity);
                assertFalse(state);
            }
        });

    }

    @Test
    public void checkInternetConnectivityWhenIsTrueTest () throws InterruptedException {
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc wifi enable");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand("svc data enable");

        TimeUnit.SECONDS.sleep(3);

        activityScenarioRule.getScenario().onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                boolean state = Utils.isInternetAvailable(activity);
                assertTrue(state);
            }
        });

    }
}