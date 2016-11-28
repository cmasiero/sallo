package it.cristiano.sallo.dao

import java.io.{File, FileNotFoundException}

import it.cristiano.sallo.common.BaseTest
import it.cristiano.sallo.util.CryptoUtils
import org.scalatest.FlatSpec

/**
  * Created by cristiano on 11/21/16.
  */
class FileDaoTest extends BaseTest{

  CryptoUtils.encrypt(DEFAULT_PASS,DECRYPT_FILE_PATH,ENCRYPT_FILE_NAME_FILE_DAO_TEST)
  val fdao = new FileDao(DEFAULT_PASS,ENCRYPT_FILE_NAME_FILE_DAO_TEST)

  /**
    * Check if file exists.
    */
  it must "produce FileNotFoundException when file does not exits!" in {
    assertThrows[FileNotFoundException] {
      new FileDao(DEFAULT_PASS,"./src/test/resources/noFile.csv")
    }
  }

  /**
    * Check FileDao.count method
    */
  "File " + ENCRYPT_FILE_NAME_FILE_DAO_TEST must "have 4 records" in {
    assert(fdao.count == 4)
  }

  /**
    * Check FileDao.getAll method
    */
  "File " + ENCRYPT_FILE_NAME_FILE_DAO_TEST must "have 4 String records" in {
    assert(fdao.getAll.size == 4)
  }

  /**
    * When there is no match getByMatch it get a 0 size List.
    */
  "File " + ENCRYPT_FILE_NAME_FILE_DAO_TEST must " not contain records with pattern 'no_match'" in {
    assert(fdao.getByMatch("no_match").size == 0)
  }

  /**
    * Count by pattern
    */
  "File " + ENCRYPT_FILE_NAME_FILE_DAO_TEST must " contain 2 records with pattern : " + DEFAULT_PASS in {
    assert(fdao.getByMatch(DEFAULT_PASS).size == 2)
  }

  /**
    * Adds two records
    */
  "File " + ENCRYPT_FILE_NAME_FILE_DAO_TEST must " contain 2 records added" in {
    assert(DaoReturnMessage.INSERTED ==
      fdao.addLine("entity=irenAdd1,topic=vpn,hostname=vpn.cristiano.it,IPaddress=00.00.00.01,user=e-masieroc,pass=passwordAdd1"))
    assert(DaoReturnMessage.INSERTED ==
      fdao.addLine("entity=irenAdd2,topic=vpn,hostname=vpn.cristiano.it,IPaddress=00.00.00.01,user=e-masieroc,pass=passwordAdd2"))
    assert(fdao.getAll.size == 6)
  }



}
