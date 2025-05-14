package com.marketsmasher.util

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object HmacSHA256 {
    fun calc(data: String, secret: String): String {
        val hmac = Mac.getInstance("HmacSHA256")
        hmac.init(SecretKeySpec(secret.toByteArray(), "HmacSHA256"))
        return hmac.doFinal(data.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}