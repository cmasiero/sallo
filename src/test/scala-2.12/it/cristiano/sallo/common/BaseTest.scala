package it.cristiano.sallo.common

import it.cristiano.sallo.util.CryptoUtils
import org.scalatest.FlatSpec

/**
  * Created by cristiano on 11/23/16.
  */
abstract class BaseTest extends FlatSpec{

  val DEFAULT_PASS                    = "DEFAULT_PASS"

  val DECRYPT_FILE_PATH               = "./src/test/resources/file.csv"
  val DECRYPT_FILE_DRAFT_PATH         = "./src/test/resources/file-draft.csv"
  val ENCRYPT_FILE_DEFAULT_NAME       = "./src/test/resources/file.csv.enc"
  val ENCRYPT_FILE_CUSTOM_NAME        = "./src/test/resources/customname.enc"
  val ENCRYPT_FILE_NAME_FILE_DAO_TEST = "./src/test/resources/file-dao-test.enc"
  val ENCRYPT_FILE_DRAFT_PATH         = "./src/test/resources/file-draft.csv.enc"

}
