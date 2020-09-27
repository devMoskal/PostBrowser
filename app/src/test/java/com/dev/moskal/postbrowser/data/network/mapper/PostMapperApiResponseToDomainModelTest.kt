package com.dev.moskal.postbrowser.data.network.mapper

import com.dev.moskal.postbrowser.data.network.response.PostApiResponse
import com.dev.moskal.postbrowser.domain.model.Post
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class PostMapperApiResponseToDomainModelTest {

    @ParameterizedTest(name = "{0} should be map to {1}")
    @MethodSource("provideTestData")
    fun `when map api response then proper domain model should be returned`(
        input: List<PostApiResponse>,
        expected: List<Post>
    ) {
        //when
        val result = input.mapPostApiResponseToDomainModel()
        //then
        assertThat(result).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun provideTestData(): Stream<Arguments?>? {
            return Stream.of(
                Arguments.of(emptyList<PostApiResponse>(), emptyList<Post>()),

                Arguments.of(
                    listOf(PostApiResponse(1, 1, "test1", "body1")),
                    listOf(Post(1, 1, "test1", "body1"))
                ),

                Arguments.of(
                    listOf(PostApiResponse(1, null, null, null)),
                    listOf(Post(1, -1, "", ""))
                ),

                Arguments.of(
                    listOf(
                        PostApiResponse(1, 1, "test1", "body1"),
                        PostApiResponse(2, 2, "test2", "body2"),
                        PostApiResponse(3, 3, "test3", "body3")
                    ),
                    listOf(
                        Post(1, 1, "test1", "body1"),
                        Post(2, 2, "test2", "body2"),
                        Post(3, 3, "test3", "body3")
                    )
                ),
            )
        }
    }

}