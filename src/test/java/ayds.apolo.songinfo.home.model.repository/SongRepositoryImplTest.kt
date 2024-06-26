package ayds.apolo.songinfo.home.model.repository

import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.home.model.repository.broker.SongBroker
import ayds.apolo.songinfo.home.model.repository.local.Cache.Cache
import ayds.apolo.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage
import io.mockk.every
import io.mockk.mockk

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

private val internalSpotifiyDataBase: SpotifyLocalStorage = mockk(relaxUnitFun = true)
private val broker: SongBroker = mockk(relaxUnitFun = true)
private val internalCache: Cache = mockk(relaxUnitFun = true)
private val repository : SongRepository = SongRepositoryImpl(internalSpotifiyDataBase, broker, internalCache)

class SongRepositoryImplTest {

    val term = "duki"
    @Test
    fun `when getSongByTerm return a song from cache`() {
        val mockSong = SpotifySong(
            id = "id",
            songName = "songName",
            artistName = term,
            albumName = "albunName",
            releaseDate = "releaseDate",
            spotifyUrl = "spotifyUrl",
            imageUrl = "imageUrl",
            isLocallyStored =  false,
            isCacheStored =  false
        )
        every { internalCache.getResultFromCache(term) } returns mockSong

        val result = repository.getSongByTerm(term)
        assertEquals(result,mockSong)
        assertTrue(mockSong.isCacheStored)
    }
}