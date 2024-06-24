package ayds.apolo.songinfo.home.model.repository

import ayds.apolo.songinfo.home.model.entities.EmptySong
import ayds.apolo.songinfo.home.model.entities.SearchResult
import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.home.model.repository.broker.SongBroker
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.apolo.songinfo.home.model.repository.local.Cache.Cache
import ayds.apolo.songinfo.home.model.repository.local.spotify.SpotifyLocalStorage
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException

interface SongRepository{
    fun getSongByTerm(term: String): SearchResult
}

internal class SongRepositoryImpl(
    private val internalSpotifiyDataBase: SpotifyLocalStorage,
    private val broker: SongBroker,
    private val internalCache: Cache
) : SongRepository{


    override fun getSongByTerm(term: String): SearchResult {
        var song = internalCache.getResultFromCache(term) as SpotifySong?
        if (song != null) {
            song.markIsCacheStore()
            return song
        }else{
            song = internalSpotifiyDataBase.getSongByTerm(term)
            if (song != null) {
                song.markisLocallyStored()
                internalCache.updateInternalCache(term,song)
                return song
            }else {
                song = broker.getSong(term)
                if (song != null) {
                    this.internalSpotifiyDataBase.insertSong(term, song)
                    return song
                }
            }
        }
        return EmptySong
    }

    private fun SpotifySong.markisLocallyStored(){
        isLocallyStored = true
    }

    private fun SpotifySong.markIsCacheStore(){
        isCacheStored = true
    }

}

