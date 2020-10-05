package com.dev.moskal.postbrowser.app

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dev.moskal.postbrowser.BaseHiltTest
import com.dev.moskal.postbrowser.R
import com.dev.moskal.postbrowser.data.db.PostBrowserDao
import com.dev.moskal.postbrowser.data.di.ApiModule
import com.dev.moskal.postbrowser.data.di.DbModule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * (nearly) end to end tests.
 * We should go one step further and instead of injecting mock API we should mock response
 * with eg. Mockwebserver.
 *
 * Not so much test scenario in this app anyway.
 */
@RunWith(AndroidJUnit4::class)
@UninstallModules(ApiModule::class, DbModule::class)
@HiltAndroidTest
class MainActivityTest : BaseHiltTest() {

    @Inject
    lateinit var dao: PostBrowserDao

    @Test
    fun openPostDetails() {
        // open post
        onView(withText("title2!")).perform(click())

        // check if post detail are visible
        onView(withId(R.id.title)).check(matches(withText("title2!")))
        onView(withId(R.id.body)).check(matches(withText("body2")))

        // click on album
        onView(withText("album_title2")).perform(click())

        //check if photos were expanded
        onView(withId(R.id.photo)).check(matches(isDisplayed()))

        // click on album
        onView(withText("album_title2")).perform(click())

        //check if photos were hidden
        onView(withId(R.id.photo)).check(doesNotExist())
    }
}