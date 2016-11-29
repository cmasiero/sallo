package it.cristiano.sallo.util

import java.io.File
import java.nio.file.{Files, Path, Paths}
import java.security.{KeyPair, KeyPairGenerator}

import it.cristiano.sallo.common.BaseTest
import org.scalatest.{BeforeAndAfter, FlatSpec}

/**
  * Created by cristiano on 11/22/16.
  */
class CryptTest extends BaseTest with BeforeAndAfter{


  "RSA" must "have its test" in {
    val keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
    val pair = keyGen.generateKeyPair();
    println("private " + pair.getPrivate)
    println("public  " + pair.getPublic)
    assert(true)
  }

  "CryptoUtils" must "create the default file.csv.enc" in {
    CryptoUtils.encrypt(DEFAULT_PASS,DECRYPT_FILE_PATH)
    assert(Files.exists(Paths.get(ENCRYPT_FILE_DEFAULT_NAME)))
    new File(ENCRYPT_FILE_DEFAULT_NAME).delete()
  }

  "CryptoUtils" must "create a file named customname.enc" in {
    CryptoUtils.encrypt(DEFAULT_PASS,DECRYPT_FILE_PATH,ENCRYPT_FILE_CUSTOM_NAME)
    assert(Files.exists(Paths.get(ENCRYPT_FILE_CUSTOM_NAME)))
    new File(ENCRYPT_FILE_CUSTOM_NAME).delete()
  }

  "CryptoUtils, create the default file.csv.enc from Array of String it" must "contain 2 lines" in {
    val list = List (
      "attr01=value1,attr02=value2" + System.getProperty("line.separator"),
      "attr03=value3,attr03=value3" + System.getProperty("line.separator")
    )
    CryptoUtils.encryptList(DEFAULT_PASS,list,ENCRYPT_FILE_DEFAULT_NAME)
    val result = CryptoUtils.decrypt(DEFAULT_PASS,ENCRYPT_FILE_DEFAULT_NAME)
    assert(result.size == 2)
    new File(ENCRYPT_FILE_DEFAULT_NAME).delete()
  }

  "CryptoUtils" must "decrypt a file named file.csv.enc" in {
    CryptoUtils.encrypt(DEFAULT_PASS,DECRYPT_FILE_PATH)
    val dc = CryptoUtils.decrypt(DEFAULT_PASS,ENCRYPT_FILE_DEFAULT_NAME)
    assert(dc.size == 4)
    new File(ENCRYPT_FILE_DEFAULT_NAME).delete()
  }

}
