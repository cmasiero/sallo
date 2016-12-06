package it.cristiano.sallo.dao

import java.io.{File, FileNotFoundException}

import it.cristiano.sallo.common.BaseTest
import it.cristiano.sallo.dao.message.DaoReturnMessage
import it.cristiano.sallo.util.CryptoUtils

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
      new FileDao(DEFAULT_PASS, "./src/test/resources/noFile.csv")
    }
  }

  /**
    * Check for wrong password
    */
  it must "have the right password!" in {
    assertThrows[java.security.GeneralSecurityException] {
      new FileDao("wrongPassword", ENCRYPT_FILE_NAME_FILE_DAO_TEST)
    }
  }

  /**
    * Check FileDao.count
    */
  "File " + ENCRYPT_FILE_NAME_FILE_DAO_TEST must "have 4 records" in {
    assert(fdao.count == 4)
  }

  /**
    * Check FileDao.getAll
    */
  "File " + ENCRYPT_FILE_NAME_FILE_DAO_TEST must "have 4 String records" in {
    assert(fdao.getAll.size == 4)
  }

  /**
    * Check FileDao.getByMatch
    * When there is no match getByMatch it get a 0 size List.
    */
  "File " + ENCRYPT_FILE_NAME_FILE_DAO_TEST must " not contain records with pattern 'no_match'" in {
    assert(fdao.getByMatch("no_match").size == 0)
  }

  /**
    * Check FileDao.getByMatch
    * Count by pattern
    */
  "File " + ENCRYPT_FILE_NAME_FILE_DAO_TEST must " contain 2 records with pattern : " + DEFAULT_PASS in {
    assert(fdao.getByMatch(DEFAULT_PASS).size == 2)
  }

  /**
    * Check FileDao.addLine
    * Adds two records
    */
  "File " + ENCRYPT_FILE_NAME_FILE_DAO_TEST must " contain 2 records added" in {
    assert(DaoReturnMessage.INSERTED ==
      fdao.addLine("name=massimo,surname=marino,user=m-marino,pass=password1"))
    assert(DaoReturnMessage.INSERTED ==
      fdao.addLine("name=nicola,surname=nuzzo,user=n-nuzzo,pass=password2"))
    assert(fdao.getAll.size == 6)
  }

  /**
    * Check FileDao.getLine
    * Get line 4
    */
  "File " + ENCRYPT_FILE_NAME_FILE_DAO_TEST must " contain record 4 but record 32" in {
    //fdao.fixIndex
    assert(None == fdao.getLine("32"))
    val check = "index=4,name=massimo,surname=marino,user=m-marino,pass=password1"
    assert(check == fdao.getLine("4").get)
  }

  /**
    * Check FileDao.removeLine
    * remove line 4
    */
  "File " + ENCRYPT_FILE_NAME_FILE_DAO_TEST must " not contain record 4 because it has been removed" in {
    //fdao.fixIndex
    assert(DaoReturnMessage.FAIL == fdao.removeLine("32"))

    val check = "name=massimo,surname=marino,user=m-marino,pass=password1"
    assert(DaoReturnMessage.DELETED == fdao.removeLine("4"))

  }

  /**
    * Check FileDao.addFile without input file
    */
  "File no_file.csv" must " not be added to " + DECRYPT_FILE_PATH in {
    assert(fdao.addFile("no_file.csv") == DaoReturnMessage.FILE_NOT_EXIST)
  }

  /**
    * Check FileDao.addFile
    */
  "File " + DECRYPT_FILE_DRAFT_PATH must " be added to " in {
    fdao.addFile(DECRYPT_FILE_DRAFT_PATH)
    assert(fdao.getAll.size == 7)
  }

  /**
    * Check FileDao.insertAttribute
    */
  "File" + ENCRYPT_FILE_NAME_FILE_DAO_TEST  must " contain record 4 changed in attribute, an error for non-existent record 100" in{
    assert(fdao.insertAttribute("100","newattribute","newvalue") == DaoReturnMessage.NO_LINE)
    assert(fdao.insertAttribute("4","newattribute","newvalue") == DaoReturnMessage.INSERTED)
    assert(fdao.getLine("4").get == "index=4,name=nicola,surname=nuzzo,user=n-nuzzo,pass=password2,newattribute=newvalue")
  }

  /**
    * Check FileDao.updateAttribute
    *
    */
  "File" + ENCRYPT_FILE_NAME_FILE_DAO_TEST  must " contain index=4 with attribute name=nicola changed in NickChanged" in{
    assert(fdao.updateAttribute("10000","name","NickChanged")==DaoReturnMessage.NO_LINE)
    assert(fdao.updateAttribute("4","no_valid_attr","NickChanged")==DaoReturnMessage.NO_ATTRIBUTE_CHANGED)
    assert(fdao.updateAttribute("4","name","NickChanged")==DaoReturnMessage.UPDATED)
    assert(fdao.getLine("4").get=="index=4,name=NickChanged,surname=nuzzo,user=n-nuzzo,pass=password2,newattribute=newvalue")
  }

  /**
    * Check FileDao.removeAttribute
    *
    */
  "File" + ENCRYPT_FILE_NAME_FILE_DAO_TEST  must " NOT contain index=4 with attribute newattribute=newvalue" in{
    assert(fdao.removeAttribute("10000","newattribute")==DaoReturnMessage.NO_LINE)
    assert(fdao.removeAttribute("4","no_valid_attr")==DaoReturnMessage.NO_ATTRIBUTE_CHANGED)
    assert(fdao.removeAttribute("4","newattribute")==DaoReturnMessage.DELETED)
    assert(fdao.getLine("4").get=="index=4,name=NickChanged,surname=nuzzo,user=n-nuzzo,pass=password2")
  }

}
