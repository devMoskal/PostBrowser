package com.dev.moskal.postbrowser.data.db

import com.dev.moskal.postbrowser.domain.model.Album
import com.dev.moskal.postbrowser.domain.model.Photo
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/**
 * test of each db mapper would be very similar
 *
 * as I would not show any new technology/design other db mapper tests
 * were omitted in sample application due to time constrains
 */
internal class AlbumWithPhotosDbEntityToDomainModelTest {

    @ParameterizedTest(name = "{0} should be map to {1}")
    @MethodSource("provideTestData")
    fun `when map db entity then proper domain model should be returned`(
        input: List<DbAlbumWithPhotos>,
        expected: List<Album>
    ) {
        //when
        val result = input.mapAlbumWithPhotosDbEntityToDomainModel()
        //then
        assertThat(result).isEqualTo(expected)
    }


    companion object {
        @JvmStatic
        fun provideTestData(): Stream<Arguments?>? {
            return Stream.of(
                Arguments.of(emptyList<DbAlbumWithPhotos>(), emptyList<Album>()),

                Arguments.of(
                    listOf(
                        DbAlbumWithPhotos(
                            DbAlbum(1, 11, "test1"),
                            listOf(DbPhoto(101, 1, "title", "url"))
                        )
                    ),
                    listOf(Album(1, "test1", listOf(Photo(101, "title", "url"))))
                ),

                Arguments.of(
                    listOf(DbAlbumWithPhotos(DbAlbum(1, 11, "test1"), emptyList())),
                    listOf(Album(1, "test1", emptyList()))
                ),

                Arguments.of(
                    listOf(
                        DbAlbumWithPhotos(DbAlbum(1, 11, "test1"), null)
                    ),
                    listOf(Album(1, "test1", emptyList()))
                ),

                Arguments.of(
                    listOf(
                        DbAlbumWithPhotos(
                            DbAlbum(1, 11, "test1"), listOf(DbPhoto(101, 1, "title", "url"))
                        ),

                        DbAlbumWithPhotos(
                            DbAlbum(2, 12, "test2"), listOf(DbPhoto(102, 2, "title", "url"))
                        ),
                        DbAlbumWithPhotos(
                            DbAlbum(3, 13, "test3"), listOf(DbPhoto(103, 3, "title", "url")),
                        )
                    ),
                    listOf(
                        Album(1, "test1", listOf(Photo(101, "title", "url"))),
                        Album(2, "test2", listOf(Photo(102, "title", "url"))),
                        Album(3, "test3", listOf(Photo(103, "title", "url"))),
                    )
                ),
            )
        }
    }
}