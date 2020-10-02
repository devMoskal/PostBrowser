package com.dev.moskal.postbrowser

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.dev.moskal.postbrowser.data.db.PostBrowserDatabase
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class DatabaseTestRule : TestWatcher() {

    lateinit var database: PostBrowserDatabase
    private val testDispatcher = TestCoroutineDispatcher()
    val testScope = TestCoroutineScope(testDispatcher)

    override fun starting(description: Description?) {
        super.starting(description)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, PostBrowserDatabase::class.java)
            .setTransactionExecutor(testDispatcher.asExecutor())
            .setQueryExecutor(testDispatcher.asExecutor())
            .build()

    }

    override fun finished(description: Description?) {
        super.finished(description)
        database.close()
        testDispatcher.cleanupTestCoroutines()
        testScope.cleanupTestCoroutines()

    }
}
