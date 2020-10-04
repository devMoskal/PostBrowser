package com.dev.moskal.postbrowser.data.db

import com.dev.moskal.postbrowser.DatabaseTestRule
import com.dev.moskal.postbrowser.testPosts
import com.dev.moskal.postbrowser.testUsers
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * There should be a lot more tests of dao, for each query and transaction.
 * For sample app I've provided just a few sample one.
 */
class PostBrowserDaoTest {
    private lateinit var dao: PostBrowserDao

    @get:Rule
    var databaseRule = DatabaseTestRule()

    @Before
    fun createDb() = runBlocking {

        dao = databaseRule.database.postBrowserDao()

        dao.insertUsers(testUsers)
        dao.insertPosts(testPosts)
    }

    @Test
    fun testGetPost() = runDbTest {
        //given
        val postId = testPosts[0].postId

        // when
        val post = dao.getPost(postId).first()

        // then
        assertThat(post).isEqualTo(testPosts[0])
    }

    @Test
    fun testInsertPostReplaceOnConflict() = runDbTest {
        //given
        val conflictingId = 0
        val newPost = DbPost(conflictingId, 1, "other", "changed")
        // when
        dao.insertPosts(listOf(newPost))
        val postInDb = dao.getPost(conflictingId).first()

        // then
        assertThat(postInDb).isEqualTo(newPost)
    }

    @Test
    fun testDeletePost() = runDbTest {
        //given
        val idToDelete = 2
        // when
        dao.deletePost(idToDelete)
        val deletedPost = dao.getPost(idToDelete).first()

        // then
        assertThat(deletedPost).isNull()
    }

    @Test
    fun testCascadeDeletePostAfterDeleteUser() = runDbTest {
        //given
        val userIdToDelete = 0
        // when
        dao.deleteUser(userIdToDelete)
        val deletedPost = dao.getPost(0).first()
        val anotherDeletedPost = dao.getPost(1).first()
        val remainingPost = dao.getPost(2).first()

        // then
        assertThat(deletedPost).isNull()
        assertThat(anotherDeletedPost).isNull()
        assertThat(remainingPost).isEqualTo(testPosts[2])
    }


    private fun <T : Any> runDbTest(
        block: suspend () -> T
    ) = databaseRule.testScope.runBlockingTest {
        block()
    }

}