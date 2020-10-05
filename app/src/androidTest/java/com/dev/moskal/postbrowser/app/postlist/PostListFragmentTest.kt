package com.dev.moskal.postbrowser.app.postlist

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
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
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.endsWith
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@UninstallModules(ApiModule::class, DbModule::class)
@HiltAndroidTest
/**
 * Unfortunately fragment-testing does not support hilt yet
 * so 'true' fragment test are not available yet,we are launching Main Activity anyway.
 */
class PostListFragmentTest : BaseHiltTest() {

    @Inject
    lateinit var dao: PostBrowserDao

    @Test
    fun testRemovePost() {
        // check post is visible
        onView(withText("title0!")).check(matches(isDisplayed()))

        // click on X button
        onView((allOf(withId(R.id.closeIcon), hasSibling(withText("title0!"))))).perform(click())

        // check post is gone
        onView(withText("title0!")).check(doesNotExist())
    }


    @Test
    fun testFilterPosts() {
        // check both posts are visible
        onView(withText("title1!")).check(matches(isDisplayed()))
        onView(withText("title2!")).check(matches(isDisplayed()))

        // type common part of titles
        onView(withId(R.id.search_post_et)).perform(ViewActions.replaceText("title"))

        // check both posts are visible
        onView(withText("title1!")).check(matches(isDisplayed()))
        onView(withText("title2!")).check(matches(isDisplayed()))

        // type part matching just one title
        onView(withId(R.id.search_post_et)).perform(ViewActions.replaceText("title2!"))
        Thread.sleep(200) // room to improvement - remove sleep

        // check only one post is visible
        onView(withText("title1!")).check(doesNotExist())
        onView(
            allOf(
                withText(endsWith("title2!")),
                withId(R.id.nameTV)
            )
        ).check(matches(isDisplayed()))

        // type part not matching any title
        onView(withId(R.id.search_post_et)).perform(ViewActions.replaceText("sd!5sda"))
        Thread.sleep(200) // room to improvement - remove sleep

        // check both post gone
        onView(withText("title1!")).check(doesNotExist())
        onView(withText("title2!")).check(doesNotExist())

        // clear search input text
        onView(withId(R.id.search_post_et)).perform(ViewActions.replaceText(""))
        Thread.sleep(200) // room to improvement - remove sleep

        // check both posts are visible
        onView(withText("title1!")).check(matches(isDisplayed()))
        onView(withText("title2!")).check(matches(isDisplayed()))

    }

}