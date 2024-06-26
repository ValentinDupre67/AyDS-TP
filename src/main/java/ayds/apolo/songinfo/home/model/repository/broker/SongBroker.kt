package ayds.apolo.songinfo.home.model.repository.broker


import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyTrackService

interface SongBroker{
    fun getSong(term : String) : SpotifySong?
}

internal class SongBrokerImpl(
    private val spotifyTrackService: SpotifyTrackService,
    private val wikipedia : WikipediaProxy
) : SongBroker{
    override fun getSong(term : String) : SpotifySong?{
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