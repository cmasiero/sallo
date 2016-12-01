package it.cristiano.sallo.util

import java.io.{BufferedWriter, File, FileWriter, PrintWriter}
import java.nio.file.{Files, Paths}
import java.security.MessageDigest
import java.util
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

import org.apache.commons.codec.binary.Base64

import scala.collection.mutable.ListBuffer
import scala.io.Source


/**
  * Created by cristiano on 11/22/16.
  */
object CryptoUtils {

  def encrypt(key: String, filename: String, filenameEnc: String = "not_declared"): Unit = {
    val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, keyToSpec(key))
    val byteArray = Files.readAllBytes(Paths.get(filename))
    val fileContent = Base64.encodeBase64String(cipher.doFinal(byteArray))

    val file = if (filenameEnc.equals("not_declared"))
      new File(filename + ".enc")
    else
      new File(filenameEnc)

    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(fileContent)
    bw.close()
  }

  def encryptList(key: String, listString: List[String], filenameEnc: String = "not_declared"): Unit = {
    val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, keyToSpec(key))

    val listSep = listString.map(l => l.concat(System.getProperty("line.separator")))
    val byteArray = listSep.flatMap(s => s.toList).map(c => c.toByte)
    val fileContent = Base64.encodeBase64String(cipher.doFinal(byteArray.toArray))

    val bw = new BufferedWriter(new FileWriter(new File(filenameEnc)))
    bw.write(fileContent)
    bw.close()
  }

  def decrypt(key: String, encryptedFilename: String): List[String] = {
    val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING")
    cipher.init(Cipher.DECRYPT_MODE, keyToSpec(key))
    val byteArray = Files.readAllBytes(Paths.get(encryptedFilename))

    val bufferedSource = io.Source.fromBytes(cipher.doFinal(Base64.decodeBase64(byteArray)))
    var result = new ListBuffer[String]()
    for (line <- bufferedSource.getLines)
      result += line
    bufferedSource.close()

    result.toList
  }

  private def keyToSpec(key: String): SecretKeySpec = {
    var keyBytes: Array[Byte] = (SALT + key).getBytes("UTF-8")
    val sha: MessageDigest = MessageDigest.getInstance("SHA-1")
    keyBytes = sha.digest(keyBytes)
    keyBytes = util.Arrays.copyOf(keyBytes, 16)
    new SecretKeySpec(keyBytes, "AES")
  }

  private val SALT: String =
    "jMhKlOuJnM34G6NHkqo9V010GhLAqOpF0BePojHgh1HgNg8^72k"

}
