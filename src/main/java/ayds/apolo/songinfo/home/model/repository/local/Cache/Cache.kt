package ayds.apolo.songinfo.home.model.repository.local.Cache

import ayds.apolo.songinfo.home.model.entities.SearchResult
import ayds.apolo.songinfo.home.model.entities.SpotifySong

interface Cache{
    fun getResultFromCache(term: String): SearchResult?
    fun updateInternalCache(term: String, song: SearchResult)
}

class CacheImpl(
    private val internalCache: MutableMap<String, SearchResult>
): Cache {
    override fun getResultFromCache(term: String): SearchResult? {
        return internalCache[term]
    }

    override fun updateInternalCache(term: String, song: SearchResult) {
        internalCache[term] = song
    }

}