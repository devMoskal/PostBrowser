package com.dev.moskal.postbrowser.data.network

import com.dev.moskal.postbrowser.data.db.DbPost
import com.dev.moskal.postbrowser.data.network.response.PostApiResponse
import com.google.common.truth.Truth
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


/**
 * test of each api mapper would be very similar
 *
 * as I would not show any new technology/design other api mapper tests
 * were omitted in sample application due to time constrains
 */
internal class PostMapperApiResponseToDbEntityTest {

    @ParameterizedTest(name = "{0} should be map to {1}")
    @MethodSource("provideTestData")
    fun `when map api response then proper db entity should be returned`(
        input: List<PostApiResponse>,
        expected: List<DbPost>
    ) {
        //when
        val result = input.mapPostApiResponseToDbEntity()
        //then
        Truth.assertThat(result).isEqualTo(expected)
    }


    companion object {
        @JvmStatic
        fun provideTestData(): Stream<Arguments?>? {
            return Stream.of(
                Arguments.of(emptyList<PostApiResponse>(), emptyList<DbPost>()),

                Arguments.of(
                    listOf(PostApiResponse(1, 11, "test1", "body1")),
                    listOf(DbPost(1, 11, "test1", "body1"))
                ),

                Arguments.of(
                    listOf(PostApiResponse(1, 2, null, null)),
                    listOf(DbPost(1, 2, "", ""))
                ),

                Arguments.of(
                    listOf(
                        PostApiResponse(1, 11, "test1", "body1"),
                        PostApiResponse(2, 12, "test2", "body2"),
                        PostApiResponse(3, 13, "test3", "body3"),

                        ),
                    listOf(
                        DbPost(1, 11, "test1", "body1"),
                        DbPost(2, 12, "test2", "body2"),
                        DbPost(3, 13, "test3", "body3")
                    )
                ),
            )
        }
    }
}