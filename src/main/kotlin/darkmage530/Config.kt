package darkmage530

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.*

class Config private constructor (
    private val accessToken: String,
    private val code: String,
    private val anilistUrls: AnilistUrls,
    private val appInfo: AppInfo
) {

    data class AnilistUrls(
        val baseUrl: String,
        val authorizeUrl: String,
        val tokenUrl: String
    )

    data class AppInfo(val clientId: String,
                val secret: String,
                val appName: String,
                val redirectUrl: String
                )

    /**
     * Statically allows the construction and reading of properties
     * via overloading the Config() constructor
     */
    companion object {
        private val propFile = File("src/main/resources", "local.properties")

        operator fun invoke(): Config {
            val prop = Properties().apply {
                load(FileInputStream(propFile))
            }
            return Config(
                prop["accessToken"].toString(),
                prop["code"].toString(),
                AnilistUrls(
                    prop["baseUrl"].toString(),
                    prop["authorizeUrl"].toString(),
                    prop["tokenUrl"].toString(),
                ),
                AppInfo(
                    prop["clientId"].toString(),
                    prop["secret"].toString(),
                    prop["appName"].toString(),
                    prop["redirectUrl"].toString()
                )
            )
        }
    }

    fun updateAccessToken(accessToken: String) {
        val prop = Properties().apply {
            load(FileInputStream(propFile))
        }
        prop.setProperty("accessToken", accessToken)
        val output = FileOutputStream(propFile)
        prop.store(output, "Updating Access Token")
    }

    /**
     * Used for Destructuring Config
     */
    operator fun component1() = accessToken
    operator fun component2() = code
    operator fun component3() = anilistUrls
    operator fun component4() = appInfo
}

