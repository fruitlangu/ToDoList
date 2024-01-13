package com.example.todolistapplication

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.todolistapplication.MainActivity
import com.example.todolistapplication.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityInstrumentedTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun whenAddButtonClicked_shouldOpenNewToDoActivity() {
        // Click on the add button
        onView(withId(R.id.btn_add)).perform(click())

        // Check if the NewToDoActivity is opened
        onView(withId(R.id.et_add_title)).check(matches(isDisplayed()))
    }

    @Test
    fun whenNewItemAdded_shouldAppearInList() {
        // Add a new to-do item (assuming 'addToDoItem' is a method to add item)
        addToDoItem("New ToDo Item")

        // Check if the new item is displayed in the list
        onView(withText("New ToDo Item")).check(matches(isDisplayed()))
    }

    // Helper method to add a to-do item
    private fun addToDoItem(itemTitle: String) {
        onView(withId(R.id.btn_add)).perform(click())
        onView(withId(R.id.et_add_title)).perform(typeText(itemTitle), closeSoftKeyboard())

    }
}