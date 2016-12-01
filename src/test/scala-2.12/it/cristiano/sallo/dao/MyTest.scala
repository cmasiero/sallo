package it.cristiano.sallo.dao

import it.cristiano.sallo.common.BaseTest
import it.cristiano.sallo.util.CryptoUtils

/**
  * Created by cristiano on 11/30/16.
  */
class MyTest extends BaseTest {

  //  CryptoUtils.encrypt(DEFAULT_PASS, DECRYPT_FILE_DRAFT_PATH, ENCRYPT_FILE_DRAFT_PATH)
  //  val fdao = new FileDao(DEFAULT_PASS,ENCRYPT_FILE_DRAFT_PATH)
  //
  //  "test " must " draft" in {
  //    for ( line <- fdao.getAll ){
  //      println("line after init " + line)
  //    }
  //
  //    fdao.addLine("add=one")
  //
  //    fdao.fixIndex
  //
  //    val listFromDecrypt = CryptoUtils.decrypt(DEFAULT_PASS,ENCRYPT_FILE_DRAFT_PATH)
  //    for (line <- listFromDecrypt)
  //      println("line after decrypt: " + line)
  //
  //  }


  "test 2" must " draft" in {

    val list = List("at1=val1,at2=val2", "at10=val10,at20=val20")
    CryptoUtils.encryptList(DEFAULT_PASS, list, ENCRYPT_FILE_DRAFT_PATH)

    val listFromDecrypt = CryptoUtils.decrypt(DEFAULT_PASS, ENCRYPT_FILE_DRAFT_PATH)
    for (line <- listFromDecrypt)
      println("line after decrypt: " + line)


  }


}
