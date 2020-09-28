package com.dev.moskal.postbrowser.data.db

import com.dev.moskal.postbrowser.domain.model.PostInfo
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/**
 * test of each db mapper would be very similar
 *
 * as I would not show any new technology/design other db mapper tests
 * were omitted in sample application due to time constrain
 */
internal class PostAndUserMapperDbEntityToDomainModelTest {

    @ParameterizedTest(name = "{0} should be map to {1}")
    @MethodSource("provideTestData")
    fun `when map db entity then proper domain model should be returned`(
        input: List<DbPostWithUser>,
        expected: List<PostInfo>
    ) {
        //when
        val result = input.mapPostAndUserDbEntityToDomainModel()
        //then
        assertThat(result).isEqualTo(expected)
    }


    companion object {
        @JvmStatic
        fun provideTestData(): Stream<Arguments?>? {
            return Stream.of(
                Arguments.of(emptyList<DbPostWithUser>(), emptyList<PostInfo>()),

                Arguments.of(
                    listOf(DbPostWithUser(DbPost(1, 11, "test1", "body1"), DbUser(101, "email1"))),
                    listOf(PostInfo(1, "test1", "email1"))

                ),

                Arguments.of(
                    listOf(DbPostWithUser(DbPost(1, 11, "test1", "body1"), null)),
                    listOf(PostInfo(1, "test1", ""))
                ),


                Arguments.of(
                    listOf(
                        DbPostWithUser(DbPost(1, 11, "test1", "body1"), DbUser(101, "email1")),
                        DbPostWithUser(DbPost(2, 12, "test2", "body2"), DbUser(102, "email2")),
                        DbPostWithUser(DbPost(3, 13, "test3", "body3"), DbUser(103, "email3")),

                        ),
                    listOf(
                        PostInfo(1, "test1", "email1"),
                        PostInfo(2, "test2", "email2"),
                        PostInfo(3, "test3", "email3")
                    )
                ),
            )
        }
    }
}