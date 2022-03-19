package darkmage530

import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

fun main() {
    SetupProject().start()
}

/**
 * The Access Token has a life of 365 days
 * If you need to initially set your AccessToken or Update run this main after the following steps
 *
 * 1. In your browser go to:
 *     https://anilist.co/api/v2/oauth/authorize?client_id={client_id}&response_type=code
 * and make sure you are logged in
 * Then accept the authorization for your anilist account.
 *
 * 2. The URL should now contain a ?code=  with a long string
 * Copy and paste that string into the code= property in your local.properties
 *
 * 3. Run this Main, it will use the code to get an access_token and write it to your local.properties
 * You won't have to do this again for another year
 */
class SetupProject {

    private val config = Config()

    fun start() {
        val (_, code, anilistUrls, appInfo) = config

        val accessToken = getAccessToken(appInfo, code, anilistUrls.tokenUrl)
        config.updateAccessToken(accessToken.accessToken)
    }

    data class AccessTokens(val accessToken: String, val refreshToken: String)

    /**
     * given a Code use it to get an accessToken and Refresh Token
     */
    private fun getAccessToken(appInfo: Config.AppInfo, code: String, tokenUrl: String): AccessTokens {
        val client = OkHttpClient()

        val body = FormBody.Builder()
            .add("grant_type", "authorization_code")
            .add("client_id", appInfo.clientId)
            .add("client_secret", appInfo.secret)
            .add("redirect_uri", appInfo.redirectUrl)
            .add("code", code)
            .build()
        val request = Request.Builder()
            .url(tokenUrl)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .post(body)
            .build()

        val response = client.newCall(request).execute()

        val bodyMap = Gson().fromJson(response.body?.string(), Map::class.java)

        response.body?.close()
        return AccessTokens(bodyMap["access_token"] as String, bodyMap["refresh_token"] as String)
    }
    /**
     * still need to figure out how to do this without a browser
     */
    private fun getCode(anilistUrls: Config.AnilistUrls, appInfo: Config.AppInfo) {
        val client = OkHttpClient().newBuilder().followRedirects(true).build()

        val request = Request.Builder()
            .get()
            .url("${anilistUrls.authorizeUrl}?client_id=${appInfo.clientId}&response_type=code")
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()

        val response = client.newCall(request).execute()
        println(response.code)
        println(response.body?.string())
        println(response.headers)
        println(response.message)
        println(response.isRedirect)
    }
}
