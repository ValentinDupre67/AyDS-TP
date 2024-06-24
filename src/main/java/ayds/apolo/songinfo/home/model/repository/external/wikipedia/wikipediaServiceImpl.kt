package ayds.apolo.songinfo.home.model.repository.external.wikipedia

import ayds.apolo.songinfo.home.model.entities.SearchResult
import ayds.apolo.songinfo.home.model.entities.SpotifySong
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Response
import java.io.IOException

interface WikipediaService{
    fun getSong(term: String): SpotifySong?
}

internal class WikipediaServiceImpl(
    private val wikipediaAPI : WikipediaAPI
) : WikipediaService{
    override fun getSong(term : String): SpotifySong? {
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
        return null
    }
}