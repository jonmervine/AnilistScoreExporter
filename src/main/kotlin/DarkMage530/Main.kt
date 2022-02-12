import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import schema.anilist.TestGetQuery
import kotlin.system.exitProcess

val baseUrl = "https://graphql.anilist.co/"

suspend fun main(args: Array<String>) {
//    val token = ""
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain: Interceptor.Chain ->
            val original: Request = chain.request()
            val builder: Request.Builder = original.newBuilder().method(original.method(), original.body())
//            builder.header("Authorization", "bearer $token")
            chain.proceed(builder.build())
        }
        .build()

    val apolloClient: ApolloClient = ApolloClient.builder()
        .serverUrl(baseUrl)
        .okHttpClient(okHttpClient)
        .build()

    val user = apolloClient.query(TestGetQuery()).await()
    val mlg = user.data?.mediaListCollection?.lists?.map { melg ->
        MediaListGroup(melg?.name ?: "default", melg?.entries?.map { entry ->
            MediaList(entry?.progress ?: 0,
                Media(entry?.media?.title?.english ?: "cant", entry?.media?.episodes ?: 0)
            )
        }?.toList() ?: emptyList()
        )
    }
    println(mlg)
    exitProcess(0)
}
data class MediaListGroup(val name: String, val entries: List<MediaList>)
data class MediaList(val progress: Int, val media:  Media)
data class Media(val title: String, val episodes: Int)
/*

query {
	MediaListCollection(userId: 120727, status: CURRENT, type:ANIME) {
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