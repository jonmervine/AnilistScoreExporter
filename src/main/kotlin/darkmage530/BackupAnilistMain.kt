package darkmage530

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.time.LocalDate
import java.time.LocalTime

fun main() {
    BackupAnilist().start()
}

class BackupAnilist {
    private val config = Config()

    fun start() {
        val (accessToken, _, userId, anilistUrls, appInfo) = config
        val query = getQuery("GetAnimeList.txt")
        val content = callGraphQl(accessToken, userId, anilistUrls.baseUrl, query)
        outputJson(content)
    }

    private fun getQuery(filename: String = "Test.txt"): String {
        val queryFile = File("src/main/resources/graphql", filename)
        return FileInputStream(queryFile).bufferedReader().use(BufferedReader::readText)
    }

    private fun callGraphQl(accessToken: String, userId: String, baseUrl: String, query: String): String {
        val client = OkHttpClient()
        val body = FormBody.Builder()
            .add("query", query)
            .add("variables", "{\"userId\":$userId}")
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
        val responseBody = response.body?.string() ?: ""

        response.close()
        return responseBody
    }

    private fun outputJson(jsonBody: String) {
        val date = LocalDate.now().toString()
        val time = LocalTime.now()
        val timestamp = date + "T" + time.hour + "-" + time.minute
        File("src/main/resources/backup", "backup-$timestamp.txt").writeText(jsonBody, Charsets.UTF_8)
    }
}