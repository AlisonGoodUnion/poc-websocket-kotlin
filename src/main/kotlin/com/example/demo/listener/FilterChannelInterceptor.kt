package com.example.demo.listener

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.UrlJwkProvider
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import org.springframework.http.HttpStatus
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.web.client.HttpClientErrorException
import java.net.URL
import java.security.interfaces.RSAPublicKey

class FilterChannelInterceptor : ChannelInterceptor {
    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val headerAccessor = StompHeaderAccessor.wrap(message)

        if (StompCommand.CONNECT == headerAccessor.command) {
            val nativesHeaders = listOf(headerAccessor?.messageHeaders?.get("nativeHeaders"))
            val nativeHeaders = nativesHeaders[0] as Map<String, List<String>>
            val token = nativeHeaders["Authorization"]?.get(0)

            val decodeJWT = obtemDecodeJWT(token)

            val provider: JwkProvider =
                UrlJwkProvider(URL("https://www.google.com.br"))
            val jwk = provider[decodeJWT.keyId]
            val algorithm = Algorithm.RSA256(jwk.publicKey as RSAPublicKey, null)

            try {
                algorithm.verify(decodeJWT)
                return message
            } catch (erro: SignatureVerificationException) {
                throw HttpClientErrorException(HttpStatus.UNAUTHORIZED)
            }
        }

        return message
    }

    private fun obtemDecodeJWT(token: String?): DecodedJWT {
        try {
            return JWT.decode(token)
        } catch (erro: JWTDecodeException) {
            throw HttpClientErrorException(HttpStatus.UNAUTHORIZED)
        }
    }
}