package ayds.apolo.songinfo.home.model.repository.local.Cache

import ayds.apolo.songinfo.home.model.entities.SearchResult
import ayds.apolo.songinfo.home.model.entities.SpotifySong

interface Cache{
    fun getResultFromCache(term: String): SpotifySong?
    fun updateInternalCache(term: String, song: SpotifySong)
}

class CacheImpl(
    private val internalCache: MutableMap<String, SpotifySong>
): Cache {
    override fun getResultFromCache(term: String): SpotifySong? {
        return internalCache[term]
    }

    override fun updateInternalCache(term: String, song: SpotifySong) {
        internalCache[term] = song
    }

}