package com.freesundance

import com.beust.klaxon.Klaxon
import org.junit.Test
import org.assertj.core.api.Assertions.*

class TestXertToken {
    @Test
    fun `given XertToken when Serialize then get Json string`() {
        val unit = XertToken("access_token", 1000, "token_type", "scope", "refresh_token")
        val result = Klaxon().toJsonString(unit)

        assertThat(result).isEqualTo(
                """{"access_token" : "access_token", "expires_in" : 1000, "refresh_token" : "refresh_token", "scope" : "scope", "token_type" : "token_type"}"""
        )
    }

    @Test
    fun `give Json string when parse then get XertToken`() {
        val xertToken = Klaxon().parse<XertToken>("""{"access_token" : "access_token", "expires_in" : 1000, "refresh_token" : "refresh_token", "scope" : "scope", "token_type" : "token_type"}""")
        assertThat(xertToken).isNotNull
        assertThat(xertToken.toString()).isEqualTo("accessToken=access_token expiresIn=1000 tokenType=token_type scope=scope refreshToken=refresh_token")
    }
}