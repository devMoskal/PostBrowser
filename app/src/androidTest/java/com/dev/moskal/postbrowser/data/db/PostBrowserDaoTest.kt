package com.dev.moskal.postbrowser.data.db

import com.dev.moskal.postbrowser.DatabaseTestRule
import com.dev.moskal.postbrowser.test
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
    fun testGetPostWithUserRelationship() = runDbTest {
        // when
        val postWithUser = dao.getPostWithUser().first()

        // then
        assertThat(postWithUser).hasSize(testPosts.size)
        assertThat(postWithUser[0]).isEqualTo(DbPostWithUser(testPosts[0], testUsers[0]))
        assertThat(postWithUser[1]).isEqualTo(DbPostWithUser(testPosts[1], testUsers[0]))
        assertThat(postWithUser[2]).isEqualTo(DbPostWithUser(testPosts[2], testUsers[1]))
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
        val postWithUser = dao.getPostWithUser().first()

        // then
        assertThat(deletedPost).isNull()
        assertThat(postWithUser).hasSize(testPosts.size - 1)
        assertThat(postWithUser[0]).isEqualTo(DbPostWithUser(testPosts[0], testUsers[0]))
        assertThat(postWithUser[1]).isEqualTo(DbPostWithUser(testPosts[1], testUsers[0]))
        assertThat(postWithUser).doesNotContain(DbPostWithUser(testPosts[2], testUsers[1]))
    }

    @Test
    fun testGetPostInfoReturnsNewDataWhenDbIsUpdated() = runDbTest {
        //given
        val newPost = DbPost(4, testUsers[1].userId, "test4", "test4b")
        val initialList = mutableListOf(
            DbPostWithUser(testPosts[0], testUsers[0]),
            DbPostWithUser(testPosts[1], testUsers[0]),
            DbPostWithUser(testPosts[2], testUsers[1]),
        )

        // when
        val testObserver = dao.getPostWithUser().test(databaseRule.testScope)
        dao.insertPosts(listOf(newPost))

        // then
        testObserver.assertValues(
            initialList,
            initialList.plus(DbPostWithUser(newPost, testUsers[1]))
        )
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