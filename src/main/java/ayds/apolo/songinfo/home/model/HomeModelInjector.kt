package ayds.apolo.songinfo.home.model

import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.home.model.repository.SongRepository
import ayds.apolo.songinfo.home.model.repository.SongRepositoryImpl
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyModule
import ayds.apolo.songinfo.home.model.repository.local.Cache.Cache
import ayds.apolo.songinfo.home.model.repository.local.Cache.CacheImpl
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.ResultSetToSpotifySongMapperImpl
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.SpotifySqlDBImpl
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.SpotifySqlQueriesImpl

object HomeModelInjector {


  internal val internalSpotifiyDataBase = SpotifySqlDBImpl(
    SpotifySqlQueriesImpl(), ResultSetToSpotifySongMapperImpl()
  )
  val spotifyTrackService = SpotifyModule.spotifyTrackService

  val theCache = mutableMapOf<String, SpotifySong>()
  val internalCache : Cache = CacheImpl(theCache)

  private val repository: SongRepository = SongRepositoryImpl(internalSpotifiyDataBase, spotifyTrackService, internalCache)

  val homeModel: HomeModel = HomeModelImpl(repository)
}