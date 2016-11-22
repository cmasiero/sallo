package it.cristiano.sallo.util

import java.security._
import java.security.spec.X509EncodedKeySpec
import javax.crypto._
import org.apache.commons.codec.binary.Base64

/**
  * Created by cristiano on 11/22/16.
  */
object RSA {

  def decodePublicKey(encodedKey: String): Option[PublicKey] = {
    this.decodePublicKey(
      (new Base64()).decode(encodedKey)
    )
  }

  def decodePublicKey(encodedKey: Array[Byte]): Option[PublicKey] = {
    scala.util.control.Exception.allCatch.opt {
      val spec = new X509EncodedKeySpec(encodedKey)
      val factory = KeyFactory.getInstance("RSA")
      factory.generatePublic(spec)
    }
  }

  def encrypt(key: PublicKey, data: Array[Byte]): Array[Byte] = {
    val cipher = Cipher.getInstance("RSA")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    cipher.doFinal(data)
  }

  def encryptB64(key: PublicKey, data: Array[Byte]): String = {
    (new Base64()).encodeAsString(this.encrypt(key, data))
  }

  def encryptB64(key: PublicKey, data: String): String = {
    this.encryptB64(key, data.getBytes)
  }

}
