package com.dev.moskal.postbrowser

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import com.dev.moskal.postbrowser.app.MainActivity
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase
import com.dev.moskal.postbrowser.idilingresources.DataBindingIdlingResource
import com.dev.moskal.postbrowser.idilingresources.EspressoIdlingResource
import com.dev.moskal.postbrowser.idilingresources.monitorActivity
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

abstract class BaseHiltTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    private var activityScenario: ActivityScenario<MainActivity>? = null

    @Inject
    lateinit var db: PostBrowserDatabase

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Before
    fun startActivity() {
        activityScenario = ActivityScenario.launch(MainActivity::class.java)?.also {
            dataBindingIdlingResource.monitorActivity(it)
        }
        // Unfortunately due to delays in work with db and paging lib screen is not ready instantly.
        // Precise tailored idlingResource for flows should solve a problem
        // yet we it would be extremely time consuming to write.
        Thread.sleep(500)
    }

    @After
    fun finishActivity() {
        activityScenario?.close()
        db.clearAllTables()
    }

}