package ayds.apolo.songinfo.home.model.repository.broker


import ayds.apolo.songinfo.home.model.entities.SearchResult
import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyTrackService
import ayds.apolo.songinfo.home.model.repository.external.wikipedia.WikipediaService

interface SongBroker{
    fun getSong(term : String) : SearchResult?
}

internal class SongBrokerImpl(
    private val spotifyTrackService: SpotifyTrackService,
    private val wikipedia : WikipediaService
) : SongBroker{
    override fun getSong(term : String) : SearchResult?{
        var song = spotifyTrackService.getSong(term)
        if(song != null){
            return song
        }else{
            song = wikipedia.getSong(term)
            if (song !=  null){
                return song
            }
        }
        return null
    }

}