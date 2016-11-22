package it.cristiano.sallo.util

import java.io.File
import java.security.{KeyPair, KeyPairGenerator}
import java.security.interfaces.RSAPublicKey

import org.scalatest.FlatSpec

/**
  * Created by cristiano on 11/22/16.
  */
class CryptTest extends FlatSpec{


  "RSA" must "have its test" in {

    val keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
    val pair = keyGen.generateKeyPair();

    println("private " + pair.getPrivate)
    println("public  " + pair.getPublic)

    assert(true)

  }


  "CryptoUtils" must "have its test" in {

    val encrypt = CryptoUtils.encrypt("pippo","./src/test/resources/file.csv")
    println("Encrypted File  : " + encrypt)
    val decrypt = CryptoUtils.decrypt("pippi",encrypt)
    println("Dencrypted File : " + decrypt)

    assert(true)
  }


  

}
