package ayds.apolo.songinfo.home.model.repository

import ayds.apolo.songinfo.home.model.entities.EmptySong
import ayds.apolo.songinfo.home.model.entities.SearchResult
import ayds.apolo.songinfo.home.model.entities.SpotifySong
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

class SongRepositoryImpl(
    private val internalSpotifiyDataBase: SpotifyLocalStorage,
    private val spotifyTrackService: SpotifyTrackService,
    private val internalCache: Cache
) : SongRepository{

    val theCache = mutableMapOf<String, SpotifySong>()

    ///// Wiki
    var retrofit: Retrofit? = Retrofit.Builder()
        .baseUrl("https://en.wikipedia.org/w/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()

    var wikipediaAPI = retrofit!!.create(WikipediaAPI::class.java)
    //// end wiki

    override fun getSongByTerm(term: String): SearchResult {
        var song: SpotifySong?

        // check in the cache
        song = internalCache.getResultFromCache(term) as SpotifySong?
        if (song != null) {
            song.isCacheStored = true
            return song
        }

        // check in the DB
        song = this.internalSpotifiyDataBase.getSongByTerm(term)
        if (song != null) {
            song.isLocallyStored = true
            // update the cache
            theCache[term] = song
            return song
        }

        // the service
        song = this.spotifyTrackService.getSong(term)
        if (song != null) {
            this.internalSpotifiyDataBase.insertSong(term, song)
            return song
        }

        /////// Last chance, get anything from the wiki
        val callResponse: Response<String>
        try {
            callResponse = wikipediaAPI.getInfo(term).execute()
            System.out.println("JSON " + callResponse.body())
            val gson = Gson()
            val jobj: JsonObject = gson.fromJson(callResponse.body(), JsonObject::class.java)
            val query = jobj["query"].asJsonObject
            val snippetObj = query["search"].asJsonArray.firstOrNull()
            if (snippetObj != null) {
                val snippet = snippetObj.asJsonObject["snippet"]
                return SpotifySong("", snippet.asString, " - ", " - ", " - ", "", "")
            }
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return EmptySong
    }

    private fun getInternalCacheSong(term: String): SpotifySong? {
        val song = internalCache.getResultFromCache(term)
        if (song != null) {
            song.markisLocallyStored()
            return song
        }
        return null
    }

    private fun SpotifySong.markisLocallyStored(){
        isLocallyStored = true
    }


}

