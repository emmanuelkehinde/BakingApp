package com.kehinde.bakingapp;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.kehinde.bakingapp.activities.RecipeListActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by kehinde on 6/7/17.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeStepListActivityTest {

    private static final String INTRODUCTION = "Recipe Introduction";
    private IdlingResource mIdlingResource;


    @Rule
    public ActivityTestRule<RecipeListActivity> activityTestRule= new ActivityTestRule<>(RecipeListActivity.class);

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResource() {
        mIdlingResource = activityTestRule.getActivity().getIdlingResource();
        // Register Idling Resources
        Espresso.registerIdlingResources(mIdlingResource);
    }

    /**
     * Clicks on a RecyclerViewItem and checks it opens the {@link com.kehinde.bakingapp.activities.RecipeStepDetailActivity}
     */
    @Test
    public void clickRecyclerViewItemDisplaysTheRecipeDescription(){

        // Click on the Recipe List RecyclerView item at position 1
        onView(withId(R.id.recipe_list_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        // Click on the Step List RecyclerView item at position 0
        onView(withId(R.id.step_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Check that the Simple Introduction text is displayed
        /*This proves that the right argument is passed between the fragments for large screen devices
        And the right intent extra is passed for smaller screen devices
        */
        onView(withId(R.id.txt_instruction)).check(matches(withText(INTRODUCTION)));


    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
