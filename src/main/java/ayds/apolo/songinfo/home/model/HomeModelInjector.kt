package ayds.apolo.songinfo.home.model

import ayds.apolo.songinfo.home.model.entities.SpotifySong
import ayds.apolo.songinfo.home.model.repository.SongRepository
import ayds.apolo.songinfo.home.model.repository.SongRepositoryImpl
import ayds.apolo.songinfo.home.model.repository.broker.SongBroker
import ayds.apolo.songinfo.home.model.repository.broker.SongBrokerImpl
import ayds.apolo.songinfo.home.model.repository.broker.WikipediaProxy
import ayds.apolo.songinfo.home.model.repository.external.spotify.SpotifyModule
import ayds.apolo.songinfo.home.model.repository.external.wikipedia.WikipediaAPI
import ayds.apolo.songinfo.home.model.repository.external.wikipedia.WikipediaService
import ayds.apolo.songinfo.home.model.repository.external.wikipedia.WikipediaServiceImpl
import ayds.apolo.songinfo.home.model.repository.local.Cache.Cache
import ayds.apolo.songinfo.home.model.repository.local.Cache.CacheImpl
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.ResultSetToSpotifySongMapperImpl
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.SpotifySqlDBImpl
import ayds.apolo.songinfo.home.model.repository.local.spotify.sqldb.SpotifySqlQueriesImpl
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object HomeModelInjector {


  internal val internalSpotifiyDataBase = SpotifySqlDBImpl(
    SpotifySqlQueriesImpl(), ResultSetToSpotifySongMapperImpl()
  )

  val spotifyTrackService = SpotifyModule.spotifyTrackService

  var retrofit: Retrofit? = Retrofit.Builder()
    .baseUrl("https://en.wikipedia.org/w/")
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()
  var wikipediaAPI = retrofit!!.create(WikipediaAPI::class.java)

  val wikipedia : WikipediaService = WikipediaServiceImpl(wikipediaAPI)

  val broker : SongBroker = SongBrokerImpl(spotifyTrackService, wikipedia)

  val theCache = mutableMapOf<String, SpotifySong>()
  val internalCache : Cache = CacheImpl(theCache)

  private val repository: SongRepository = SongRepositoryImpl(internalSpotifiyDataBase, broker, internalCache)

  val homeModel: HomeModel = HomeModelImpl(repository)
}