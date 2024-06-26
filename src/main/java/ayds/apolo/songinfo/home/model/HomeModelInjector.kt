package ayds.apolo.songinfo.home.model

import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.home.model.repository.SongRepository
import ayds.apolo.songinfo.home.model.repository.SongRepositoryImpl
import ayds.apolo.songinfo.home.model.repository.broker.SongBroker
import ayds.apolo.songinfo.home.model.repository.broker.SongBrokerImpl
import ayds.apolo.songinfo.home.model.repository.broker.WikipediaProxy
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

  val broker : SongBroker = SongBrokerImpl(spotifyTrackService, WikipediaProxy())

  val theCache = mutableMapOf<String, SpotifySong>()
  val internalCache : Cache = CacheImpl(theCache)

  private val repository: SongRepository = SongRepositoryImpl(internalSpotifiyDataBase, broker, internalCache)

  val homeModel: HomeModel = HomeModelImpl(repository)
}