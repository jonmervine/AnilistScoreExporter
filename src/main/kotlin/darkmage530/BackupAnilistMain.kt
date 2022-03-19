package darkmage530
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

fun main() {
    BackupAnilist().start()
}
/*
 medialistcollection params
        userId
//        Status_in = [current, completed, dropped, paused, repeating]
        media type = anime
   hasNextChunk - paging
   lists MediaListGroup (if we care about completed list split between like tv show/movie, or if is custom list, and what status)
   entries [MediaList]
       id
       mediaId? <- might be the actual id and the other is the id in my list?
       status
       score(score format) POINT_10 current
       progress
       repeat
       ????? priority "priority of planning"
       private
       notes
       hiddenFromStatusLists
       customLists
       advancedScores
       startedAt
       completedAt
       updatedAt
       createdAt

 media
    id

 */

/*suspend fun callGraphQl() {
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val original: Request = chain.request()
            val builder: Request.Builder = original.newBuilder()
//                .method(original.method(), original.body())
//            .addHeader("Authorization", "Bearer xxx")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
            chain.proceed(builder.build())
        }
        .build()

    println("here")
    val apolloClient: ApolloClient = ApolloClient.builder()
        .serverUrl("baseurl")
        .okHttpClient(okHttpClient)
        .build()

    println("there")
    val user = apolloClient.query(TestGetQuery()).await()
//    val user = apolloClient.query(GetAnimeListQuery()).await()

    println("how about now")
    withContext(Dispatchers.IO) {
        FileOutputStream(File("getAnimeList.txt."), true)
    }.bufferedWriter().use { writer ->
        println("in here")
        user.data?.mediaListCollection?.lists?.map { melg ->
            writer.write(melg.toString())

//        MediaListGroup(melg?.name ?: "default", melg?.entries?.map { entry ->
//            MediaList(entry?.progress ?: 0,
//                Media(entry?.media)//entry?.media?.title?.english ?: "cant", entry?.media?.episodes ?: 0)
//            )
//        }?.toList() ?: emptyList()
        }
    }
}*/



class BackupAnilist {
    private val config = Config()

    fun start() {
        val (accessToken, _, anilistUrls, appInfo) = config
        callGraphQl(accessToken, anilistUrls.baseUrl)
    }

    private fun callGraphQl(accessToken: String, baseUrl: String) {
        val client = OkHttpClient()
        val body = FormBody.Builder()
            .add("query", "query getAnime(\$userId: Int!) {\r\n\tMediaListCollection(userId: \$userId, status: PAUSED, type:ANIME) {\r\n    lists {\r\n      name\r\n      entries {\r\n        progress\r\n        media {\r\n          id\r\n          title {\r\n            userPreferred\r\n          }\r\n          episodes\r\n        }\r\n      }\r\n    }\r\n  }\r\n}")
            .add("variables", "{\"userId\":xxx}")
            .build()

        val request = Request.Builder()
            .url(baseUrl)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .addHeader(
                "Authorization",
                "Bearer $accessToken"
            )
            .post(body)
            .build()

        val response = client.newCall(request).execute()
        println(response.code)
        println(response.headers)
        println(response.body?.string())

        response.close()
    }
}


//data class MediaListGroup(val name: String, val entries: List<MediaList>)
//data class MediaList(val progress: Int, val media:  Media)
//data class Media(val title: String, val episodes: Int)
/*

query {
	MediaListCollection(userId: xxx, status: CURRENT, type:ANIME) {
    lists {
      name
      entries {
        progress
        media {
          title {
            romaji
            english
            native
            userPreferred
          }
          episodes
        }
      }
    }
  }
}

 */