package io.github.serdeliverance.utils

import java.security.MessageDigest
import java.util
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

trait EncryptionUtils {

  def encrypt(key: String, value: String): String = {
    val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, keyToSpec(key))
    Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes("UTF-8")))
  }

  def keyToSpec(key: String): SecretKeySpec = {
    var keyBytes: Array[Byte] = (SALT + key).getBytes("UTF-8")
    val sha: MessageDigest    = MessageDigest.getInstance("SHA-1")
    keyBytes = sha.digest(keyBytes)
    keyBytes = util.Arrays.copyOf(keyBytes, 16)
    new SecretKeySpec(keyBytes, "AES")
  }

  private val SALT: String =
    "jMhKlOuJnM34G6NHkqo9V010GhLAqOpF0BePojHgh1HgNg8^72k"
}
