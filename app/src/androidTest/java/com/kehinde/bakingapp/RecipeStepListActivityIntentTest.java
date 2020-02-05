package com.kehinde.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.kehinde.bakingapp.activities.RecipeListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by kehinde on 6/7/17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeStepListActivityIntentTest {

    private static final String STEP_EXTRA = "STEP_EXTRA";
    private IdlingResource mIdlingResource;


    @Rule
    public IntentsTestRule<RecipeListActivity> intentsTestRule = new IntentsTestRule<>(
            RecipeListActivity.class);


    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = intentsTestRule.getActivity().getIdlingResource();
        // Register Idling Resources
        Espresso.registerIdlingResources(mIdlingResource);

//        // Stub all external intents
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }


    /**
     * Clicks on a RecyclerViewItem and checks if it has intent with extra with the key STEP_EXTRA
     */
    @Test
    public void clickRecyclerViewItemHasIntentWithAKey() {

        // Click on the Recipe List RecyclerView item at position 1
        onView(withId(R.id.recipe_list_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));


        //Check to make sure the device is not a large screen device so as to check the intent
        if (onView(withId(R.id.detail_container)) == null) {
            // Click on the Step List RecyclerView item at position 0
            onView(withId(R.id.step_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

            //Checks if the key is present
            intended(hasExtraWithKey(STEP_EXTRA));
        }

    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
