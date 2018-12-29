package com.freesundance

import com.beust.klaxon.Json
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.DataPart
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Method
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.gson.responseObject
import mu.KotlinLogging
import java.io.File
import java.net.InetSocketAddress
import java.net.Proxy


private val logger = KotlinLogging.logger {}
private val fuelManager = FuelManager.instance

// set the truststore using -Djavax.net.ssl.trustStore=owasp-zap-truststore -Djavax.net.ssl.trustStorePassword=password
//
fun proxy() {
    logger.info("http.proxyHost=${System.getProperties().get("http.proxyHost")} http.proxyPort=${System.getProperties().get("http.proxyPort")}")
    if (System.getProperties().getProperty("http.proxyHost")?.isNotBlank()!!)
        fuelManager.proxy = Proxy(Proxy.Type.HTTP,
                InetSocketAddress(System.getProperties().getProperty("http.proxyHost"),
                        Integer.valueOf(System.getProperties().getProperty("http.proxyPort"))))

}

fun authenticate(username :String, password : String) {
    // curl -u xert_public:xert_public -POST "https://www.xertonline.com/oauth/token" -d 'grant_type=password' -d 'username=AzureDiamond' -d 'password=Hunter2'
    fuelManager.basePath = "https://www.xertonline.com"

    proxy()

    var(request1, response1, result1) = Fuel.post("/oauth/token",
            listOf("grant_type" to "password", "username" to username, "password" to password))
            .authenticate("xert_public", "xert_public")
            .responseObject(XertToken.Deserializer())

    var(xertToken, error) = result1
    logger.info("xertToken=$xertToken error=$error")

    fuelManager.baseHeaders = mapOf("Authorization" to "${xertToken?.tokenType} ${xertToken?.accessToken}")

}

fun workouts() {
    // curl -X GET "https://www.xertonline.com/oauth/workouts"
    var (request, response, result) = Fuel.get( path = "/oauth/workouts").responseString()
    var(payload, error) = result
    logger.info("payload=$payload error=$error")
}

fun upload(filename : String) {
// curl -POST "https://www.xertonline.com/oauth/upload" -H "Authorization: Bearer 7d3fc2e8cb80caffd881f08e764e1b507168cce4" -F 'file=@workout.fit' -F 'name=Morning Ride'
    var(request, response, result) = Fuel.upload(path = "/oauth/upload", method = Method.POST, parameters = listOf("data_type" to "tcx"))
            .dataParts{ request, url ->
                listOf(DataPart(file = File(filename), name = filename, type = "application/xml"))
            }.responseString()

    var(payload, error) = result
    logger.info("payload=$payload error=$error")
}

// https://www.xertonline.com/API.html

fun main(args: Array<String>) {
    val username = args[0]
    val password = args[1]
    val filename = args[2]

    authenticate(username, password)
    workouts()
    upload(filename)
}


class XertToken(@Json(name = "access_token")
                val accessToken: String,
                @Json(name = "expires_in")
                val expiresIn: Long,
                @Json(name = "token_type")
                val tokenType: String,
                @Json(name = "scope")
                val scope: String,
                @Json(name = "refresh_token")
                val refreshToken: String) {
    class Deserializer : ResponseDeserializable<XertToken> {
        override fun deserialize(content: String) =
                Klaxon().parse<XertToken>(content)
    }

    override fun toString(): String {
        return "accessToken=$accessToken expiresIn=$expiresIn tokenType=$tokenType scope=$scope refreshToken=$refreshToken"
    }
}



